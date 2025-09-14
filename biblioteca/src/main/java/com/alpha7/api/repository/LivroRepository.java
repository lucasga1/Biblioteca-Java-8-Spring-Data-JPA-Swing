package com.alpha7.api.repository;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Classe responsavel pelo acesso ao banco de dados.
 * Onde podemos usar os metodos originarios do Spring Data JPA, mantendo o codigo limpo e organizado, e podemos
 * criar metodos especificos de acordo com as regras de negocio.
 * @author Lucas Gouvea Araujo
 * */
public interface LivroRepository extends JpaRepository<Livro, Long>, JpaSpecificationExecutor<Livro> {

    String QUERY_BUSCA_LIVROS_ID = "select l from Livro as l where l.id = :id";

    @Query(QUERY_BUSCA_LIVROS_ID)
    LivroDTO buscarPorId(Long id);

    boolean existsByIsbn(String isbn);

    List<Livro> findAllByIsbnIn(List<String> isbns);
}
