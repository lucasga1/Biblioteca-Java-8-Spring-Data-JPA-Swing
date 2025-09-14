package com.alpha7.api.service.impl;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.api.model.Livro;
import com.alpha7.api.repository.LivroRepository;
import com.alpha7.api.service.LivroService;
import com.alpha7.api.util.ProcessarArquivoCSV;
import com.alpha7.common.client.OpenLibraryClient;
import com.alpha7.common.exception.AtualizarLivroException;
import com.alpha7.common.exception.BuscarLivroException;
import com.alpha7.common.exception.CadastroLivroException;
import com.alpha7.common.exception.DeletarLivroException;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alpha7.api.repository.specification.LivroEspecificacao.*;

/**
 * LivroServiceImpl. Classe implements da LivroService.
 * Responsavel por implementar as regras de negocio e organizar as chamadas para o repository.
 * @author Lucas Gouvea Araujo
 * */
@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository repository;
    private final OpenLibraryClient openLibraryClient;
    private final ProcessarArquivoCSV processarArquivoCSV;

    private static final Logger LOG = Logger.getLogger(LivroServiceImpl.class);

    public LivroServiceImpl(OpenLibraryClient openLibraryClient, ProcessarArquivoCSV processarArquivoCSV, LivroRepository repository) {
        this.openLibraryClient = openLibraryClient;
        this.processarArquivoCSV = processarArquivoCSV;
        this.repository = repository;
    }

    @Override
    public Page<LivroDTO> buscarTodos(Pageable pageable) {
        Page<LivroDTO> response = repository.findAll(pageable).map(LivroDTO::new);
        if (response.isEmpty()) {
            LOG.debug("Nenhum registro encontrado!");
            throw new BuscarLivroException("Nenhum registro encontrado na base");
        }
        return response;
    }

    @Override
    public LivroDTO buscarPorId(Long id) {
        LivroDTO response = repository.buscarPorId(id);
        if (response == null) {
            LOG.debug("Livro nao encontrado.");
            throw new BuscarLivroException("Livro com o ID: " + id + " não encontrado.");
        }
        return response;
    }

    @Override
    public Page<LivroDTO> buscarPorParametro(Long id,
                                             String titulo,
                                             String autor,
                                             String dataPublicacao,
                                             String isbn,
                                             String editora,
                                             String categoria,
                                             boolean isEditarLivro,
                                             Pageable pageable) {
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());
        if (!isEditarLivro) {
            if (titulo != null) {  specs = specs.and(tituloContains(titulo)); }
            if (autor != null) { specs = specs.and(autorContains(autor)); }
            if (dataPublicacao != null) { specs = specs.and(dataPublicacaoEquals(dataPublicacao)); }
            if (isbn != null) { specs = specs.and(isbnEquals(isbn)); }
            if (editora != null) { specs = specs.and(editoraContains(editora)); }
            if (categoria != null) { specs = specs.and(categoriaContains(categoria)); }
        } else {
            if (id != null) { specs = specs.and(idEquals(id)); }
            if (titulo != null) { specs = specs.and(tituloEquals(titulo)); }
            if (isbn != null) { specs = specs.and(isbnEquals(isbn)); }
        }
        Page<LivroDTO> response = repository.findAll(specs, pageable).map(LivroDTO::new);
        if (response.isEmpty()) {
            LOG.info("Livro nao encontrado.");
            throw new BuscarLivroException("Livro não encontrado");
        } else {
            return response;
        }
    }

    @Override
    public LivroDTO cadastrarLivro(LivroDTO dto) {
        if (dto.getIsbn() == null) {
            throw new CadastroLivroException("Inserir ISBN para cadastrar um livro.");
        }
        if (repository.existsByIsbn(dto.getIsbn())) {
            LOG.debug("ISBN ja cadastrado no banco de dados.");
            throw new CadastroLivroException("Este ISBN já está cadastrado.");
        }
        return mapearLivroToDto(repository.save(mapearDtoToLivro(dto)));
    }

    @Override
    public LivroDTO buscarLivroOpenLibrary(String isbn) {
        return openLibraryClient.getLivro(isbn);
    }

    @Override
    @Transactional
    public void cadastrarLivrosArquivoCSV(MultipartFile arquivoMultipart) {
        processarArquivoCSV.cadastrarLivrosArquivoCSV(arquivoMultipart);
    }

    @Override
    @Transactional
    public LivroDTO atualizarLivro(Long id, LivroDTO dto) {
        if (id == null) {
            throw new AtualizarLivroException("Código do livro deve ser informado para atualização.");
        }
        if (dto == null) {
            throw new AtualizarLivroException("Informe os dados do livro que deseja atualizar.");
        }
        Optional<Livro> response = repository.findById(id);
        if (!response.isPresent()) {
            LOG.debug("Livro nao encontrado.");
            throw new BuscarLivroException("Livro não encontrado.");
        }
        return atualizar(response, dto);
    }

    @Override
    @Transactional(rollbackFor = DeletarLivroException.class)
    public void deletarLivro(List<LivroDTO> dtos) {
        if (dtos.isEmpty()) {
            throw new DeletarLivroException("Selecione o/os livro(s) para excluir.");
        }
        List<Livro> livros = mapearDtoToListLivro(dtos);
        for (Livro livro : livros) {
            Optional<Livro> pesquisa = repository.findById(livro.getId());
            if (pesquisa.isPresent()) {
                repository.delete(livro);
            } else {
                throw new DeletarLivroException("Livro com ID: " + livro.getId() + " não encontrado.");
            }
        }
    }

    private LivroDTO atualizar(Optional<Livro> livros, LivroDTO dto) {
        Livro livroAtualizado = mapearDtoToLivro(dto);
        try {
            return livros
                    .map(livro -> {
                        repository.save(atualizarCampos(livro, livroAtualizado));
                        return mapearLivroToDto(livro);
                    })
                    .orElseThrow(() -> new AtualizarLivroException("Livro não encontrado para atualizar."));
        } catch (Exception e) {
            LOG.debug("Erro ao atualizar livro.", e);
            throw new AtualizarLivroException("Erro ao atualizar livro.");
        }
    }

    private Livro mapearDtoToLivro(LivroDTO dto) {
        return Optional.ofNullable(dto).map(d -> {
            Livro livro = new Livro();
            livro.setTitulo(d.getTitulo());
            livro.setAutor(d.getAutor());
            livro.setDataPublicacao(d.getDataPublicacao());
            livro.setIsbn(d.getIsbn());
            livro.setEditora(d.getEditora());
            livro.setCategoria(d.getCategoria());
            livro.setDataCadastro(LocalDate.now());
            return livro;
        }).orElse(null);
    }

    private LivroDTO mapearLivroToDto(Livro livro) {
        return Optional.ofNullable(livro).map(l -> {
            LivroDTO dto = new LivroDTO();
            dto.setCodigoLivro(l.getId());
            dto.setTitulo(l.getTitulo());
            dto.setAutor(l.getAutor());
            dto.setDataPublicacao(l.getDataPublicacao());
            dto.setIsbn(l.getIsbn());
            dto.setEditora(l.getEditora());
            dto.setCategoria(l.getCategoria());
            return dto;
        }).orElse(null);
    }

    private Livro atualizarCampos(Livro livro, Livro livroAtualizado) {
        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setAutor(livroAtualizado.getAutor());
        livro.setDataPublicacao(livroAtualizado.getDataPublicacao());
        livro.setIsbn(livroAtualizado.getIsbn());
        livro.setEditora(livroAtualizado.getEditora());
        livro.setCategoria(livroAtualizado.getCategoria());
        return livro;
    }

    private List<Livro> mapearDtoToListLivro(List<LivroDTO> livrosDTO) {
        List<Livro> livros = new ArrayList<>();
        if (livrosDTO != null && !livrosDTO.isEmpty()) {
            for (LivroDTO dto : livrosDTO) {
                Livro livro = new Livro();
                livro.setId(dto.getCodigoLivro());
                livro.setTitulo(dto.getTitulo());
                livro.setAutor(dto.getAutor());
                livro.setDataPublicacao(dto.getDataPublicacao());
                livro.setIsbn(dto.getIsbn());
                livro.setEditora(dto.getEditora());
                livro.setCategoria(dto.getCategoria());
                livros.add(livro);
            }
        }
        return livros;
    }
}
