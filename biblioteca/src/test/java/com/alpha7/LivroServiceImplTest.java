package com.alpha7;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.api.model.Livro;
import com.alpha7.api.repository.LivroRepository;
import com.alpha7.api.service.impl.LivroServiceImpl;
import com.alpha7.api.util.ProcessarArquivoCSV;
import com.alpha7.common.client.OpenLibraryClient;
import com.alpha7.common.exception.BuscarLivroException;
import com.alpha7.common.exception.CadastroLivroException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link LivroServiceImpl}.
 * Esta classe verifica os principais cenários do serviço de livros
 * Os mocks incluem o {@link LivroRepository}, {@link OpenLibraryClient}.</p> *
 * @author Lucas Gouvea Araujo
 */
class LivroServiceImplTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroServiceImpl livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscar_todos_retornar_livros() {
        Livro livro = criarLivro();
        List<Livro> livros = new java.util.ArrayList<>();
        livros.add(livro);
        Page<Livro> page = new PageImpl<>(livros);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<LivroDTO> result = livroService.buscarTodos(PageRequest.of(0, 10));
        LivroDTO dto = mapearResponse(result);
        assertFalse(result.isEmpty());
        assertEquals(livro.getAutor(), dto.getAutor());
    }

    @Test
    void buscar_todos_quando_vazio_deve_lancar_excecao() {
        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        assertThrows(BuscarLivroException.class, () ->
                livroService.buscarTodos(PageRequest.of(0, 10)));
    }

    @Test
   void buscar_por_id_deve_retornar_livro() {
        Livro livro = criarLivro();
        when(repository.buscarPorId(livro.getId())).thenReturn(new LivroDTO(livro));
        LivroDTO result = livroService.buscarPorId(livro.getId());
        assertNotNull(result);
        assertEquals("Dom Quixote", result.getTitulo());
        assertEquals("Miguel de Cervantes", result.getAutor());
    }

    @Test
    void buscar_por_id_deve_lancar_exception() {
        when(repository.buscarPorId(1L)).thenReturn(null);//
        assertThrows(BuscarLivroException.class, () -> livroService.buscarPorId(1L));
    }

    @Test
    void cadastrar_livro_deve_salvar_livro() {
        LivroDTO dto = new LivroDTO(criarLivro());
        dto.setCodigoLivro(null);
        when(repository.existsByIsbn(dto.getIsbn())).thenReturn(false);
        when(repository.save(any(Livro.class))).thenAnswer(invocation -> {
            Livro livroSalvo = invocation.getArgument(0);
            livroSalvo.setId(1L); // Simula a atribuição do ID pelo banco de dados
            return livroSalvo;
        });
        LivroDTO response = livroService.cadastrarLivro(dto);
        verify(repository, times(1)).save(any(Livro.class));
        assertEquals(dto.getTitulo(), response.getTitulo());
    }

    @Test
    void cadastrar_livro_quando_isbn_null_deve_lancar_exception() {
        LivroDTO dto = criarLivroDTO();
        dto.setIsbn(null);
        assertThrows(CadastroLivroException.class, () -> livroService.cadastrarLivro(dto));
    }

    @Test
    void cadastrar_livro_quando_isbn_existir_deve_lancar_exception() {
        LivroDTO dto = criarLivroDTO();
        when(repository.existsByIsbn(dto.getIsbn())).thenReturn(true);
        assertThrows(CadastroLivroException.class, () -> livroService.cadastrarLivro(dto));
    }

    @Test
    void atualizar_livro_deve_atualizar_corretamente() {
        Livro livro = criarLivro();
        Optional<Livro> optionalLivro = Optional.of(livro);
        LivroDTO dto = criarLivroDTO();
        dto.setTitulo("Atualizado");
        when(repository.findById(livro.getId())).thenReturn(optionalLivro);
        livroService.atualizarLivro(livro.getId(), dto);
        verify(repository, times(1)).save(any(Livro.class));
        assertEquals("Atualizado", livro.getTitulo());
    }

    @Test
    void atualizar_livro_quando_nao_encontrado_deve_lancar_exception() {
        Livro livro = criarLivro();
        when(repository.findById(livro.getId())).thenReturn(Optional.empty());
        LivroDTO dto = criarLivroDTO();

        assertThrows(BuscarLivroException.class, () -> livroService.atualizarLivro(103L, dto));
    }

    private Livro criarLivro() {
        Livro livro = new Livro();
        livro.setId(103L);
        livro.setTitulo("Dom Quixote");
        livro.setAutor("Miguel de Cervantes");
        livro.setDataPublicacao("16-01-1605");
        livro.setIsbn("978-8535911969");
        livro.setEditora("L&PM Pocket");
        livro.setCategoria("Literatura Clássica");
        livro.setDataCadastro(LocalDate.now());
        return livro;
    }

    private LivroDTO criarLivroDTO() {
        LivroDTO dto = new LivroDTO();
        dto.setTitulo("Dom Quixote");
        dto.setAutor("Miguel de Cervantes");
        dto.setDataPublicacao("16-01-1605"); // Usando LocalDate
        dto.setIsbn("978-8535911969");
        dto.setEditora("L&PM Pocket");
        dto.setCategoria("Literatura Clássica");
        dto.setCodigoLivro(1L); // exemplo de ID
        return dto;
    }

    private LivroDTO mapearResponse(Page<LivroDTO> pageResponse) {
        List<LivroDTO> listaLivros = pageResponse.getContent();
        LivroDTO dto = new LivroDTO();
        for (LivroDTO livro : listaLivros) {
            dto.setCodigoLivro(livro.getCodigoLivro());
            dto.setTitulo(livro.getTitulo());
            dto.setAutor(livro.getAutor());
            dto.setDataPublicacao(livro.getDataPublicacao());
            dto.setIsbn(livro.getIsbn());
            dto.setEditora(livro.getEditora());
            dto.setCategoria(livro.getCategoria());
        }
        return dto;
    }
//
////    @Test
////    void cadastrarLivrosArquivoCSV_deveChamarProcessamento() throws IOException {
////        FileInputStream fis = new FileInputStream("biblioteca/src/test/java/com/alpha7/arquivo/Lista com 50 livros.csv");
////        MultipartFile arquivo = new MockMultipartFile("arquivo", "Lista com 50 livros.csv", "text/csv", fis);
////
////        livroService.cadastrarLivrosArquivoCSV(arquivo);
////
////        verify(processarArquivoCSV, times(1)).cadastrarLivrosArquivoCSV(arquivo);
////    }
}
