package com.alpha7.api.service;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Camada de Servicos, onde implementamos os metodos que serao usados nas regras de negocio.
 * @author Lucas Gouvea Araujo
 * */
public interface LivroService {

    Page<LivroDTO> buscarPorParametro(Long id, String titulo, String autor, String dataPublicacao, String isbn, String editora,
                                             String categoria, boolean isEditarLivro, Pageable pageable);

    Page<LivroDTO> buscarTodos(Pageable pageable);

    LivroDTO buscarPorId(Long id);

    LivroDTO buscarLivroOpenLibrary(String isbn);

    LivroDTO cadastrarLivro(LivroDTO dto);

    void cadastrarLivrosArquivoCSV(MultipartFile arquivoMultipart);

    LivroDTO atualizarLivro(Long id, LivroDTO dto);

    void deletarLivro(List<LivroDTO> livros);
}
