package com.alpha7.api.biblioteca;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Classe responsavel por expor os endpoints que temos na API.
 * @author Lucas Gouvea Araujo
 * */
@RequestMapping("/livros")
public interface BibliotecaApi {

    @GetMapping("/filtro")
    ResponseEntity<Page<LivroDTO>> buscarPorParametro(@RequestParam(value = "id", required = false) Long id,
                                                      @RequestParam(value = "titulo", required = false) String titulo,
                                                      @RequestParam(value = "autor", required = false) String autor,
                                                      @RequestParam(value = "dataPublicacao", required = false) String dataPublicacao,
                                                      @RequestParam(value = "isbn", required = false) String isbn,
                                                      @RequestParam(value = "editora", required = false) String editora,
                                                      @RequestParam(value = "categoria", required = false) String categoria,
                                                      @RequestParam(value = "isEditarLivro") boolean isEditarLivro,
                                                      Pageable pageable);

    @GetMapping("/busca")
    ResponseEntity<Page<LivroDTO>> buscarTodos(Pageable pagina);

    @GetMapping("/id")
    ResponseEntity<LivroDTO> buscarPorId(@RequestParam("id") Long id);

    @GetMapping("/openapi")
    ResponseEntity<LivroDTO> buscarLivroOpenLibrary(@RequestParam("isbn") String isbn);

    @PostMapping("/cadastrar")
    ResponseEntity<LivroDTO> cadastrar(@RequestBody LivroDTO dto);

    @PostMapping("/upload/arquivo")
    ResponseEntity<Void> cadastrarLivrosArquivoCSV(@RequestParam("file") MultipartFile arquivoCsv);

    @PutMapping("/editar")
    ResponseEntity<LivroDTO> atualizarLivro(@PathParam("id") Long id, @RequestBody LivroDTO dto);

    @DeleteMapping("/deletar")
    ResponseEntity<Void> deletarLivro(@RequestBody List<LivroDTO> livros);
}