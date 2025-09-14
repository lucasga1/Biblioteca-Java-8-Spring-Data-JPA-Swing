package com.alpha7.api.biblioteca;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.api.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Classe que implementa a BibliotecaApi, onde chama a camada de servico.
 * Dessa forma, quando precisarmos mudar aluma regra de negocio, fica mais simples as implementacoes, sem precisar mexer nos endpoints.
 * @author Lucas Gouvea Araujo
 * */
@RestController
public class BibliotecaApiImpl implements BibliotecaApi {

    private final LivroService service;

    public BibliotecaApiImpl(LivroService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<LivroDTO> buscarPorId(@RequestParam("id") Long id) {
        LivroDTO livros = service.buscarPorId(id);
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body(livros);
    }

    @Override
    public ResponseEntity<LivroDTO> buscarLivroOpenLibrary(@RequestParam("isbn") String isbn) {
        LivroDTO livro = service.buscarLivroOpenLibrary(isbn);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(livro);
    }

    @Override
    public ResponseEntity<Page<LivroDTO>> buscarTodos(Pageable pagina) {
        Page<LivroDTO> livros = service.buscarTodos(pagina);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(livros);
    }

    @Override
    public ResponseEntity<Page<LivroDTO>> buscarPorParametro(@RequestParam(value = "id", required = false) Long id,
                                                             @RequestParam(value = "titulo", required = false) String titulo,
                                                             @RequestParam(value = "autor", required = false) String autor,
                                                             @RequestParam(value = "dataPublicacao", required = false) String dataPublicacao,
                                                             @RequestParam(value = "isbn", required = false) String isbn,
                                                             @RequestParam(value = "editora", required = false) String editora,
                                                             @RequestParam(value = "categoria", required = false) String categoria,
                                                             @RequestParam(value = "isEditarLivro") boolean isEditarLivro,
                                                             Pageable pageable) {
        Page<LivroDTO> livro = service.buscarPorParametro(id, titulo, autor, dataPublicacao, isbn, editora, categoria, isEditarLivro, pageable);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(livro);
    }

    @Override
    public ResponseEntity<LivroDTO> cadastrar(@RequestBody LivroDTO dto) {
        LivroDTO livroCadastrado = service.cadastrarLivro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroCadastrado);
    }

    @Override
    public ResponseEntity<Void> cadastrarLivrosArquivoCSV(@RequestParam("file") MultipartFile arquivoCsv) {
        service.cadastrarLivrosArquivoCSV(arquivoCsv);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<LivroDTO> atualizarLivro(@PathParam("id") Long id, @RequestBody LivroDTO dto) {
        LivroDTO livroAtualizado = service.atualizarLivro(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroAtualizado);
    }

    @Override
    public ResponseEntity<Void> deletarLivro(@RequestBody List<LivroDTO> livros) {
        service.deletarLivro(livros);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}