package com.alpha7.api.repository.specification;

import com.alpha7.api.model.Livro;
import org.springframework.data.jpa.domain.Specification;

/**
 * Classe Specification, que diz qual parametro e como sera utilizado na busca filtrada.
 * @author Lucas Gouvea Araujo
 * */
public class LivroEspecificacao {

    public static Specification<Livro> idEquals(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("id"),  id);
    }

    public static Specification<Livro> tituloContains(String titulo) {
        return (root, query, cb) ->
                titulo == null ? null : cb.like(cb.lower(root.get("titulo")),  "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<Livro> tituloEquals(String titulo) {
        return (root, query, cb) ->
                titulo == null ? null : cb.equal(cb.lower(root.get("titulo")),  titulo.toLowerCase());
    }

    public static Specification<Livro> autorContains(String autor) {
        return (root, query, cb) ->
                autor == null ? null : cb.like(cb.lower(root.get("autor")), autor.toLowerCase() + "%");
    }

    public static Specification<Livro> dataPublicacaoEquals(String dataPublicacao) {
        return (root, query, cb) ->
                dataPublicacao == null ? null : cb.like(root.get("dataPublicacao"), "%" + dataPublicacao + "%");
    }

    public static Specification<Livro> isbnEquals(String isbn) {
        return (root, query, cb) ->
                isbn == null ? null : cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> editoraContains(String editora) {
        return (root, query, cb) ->
                editora == null ? null : cb.like(cb.lower(root.get("editora")), "%" + editora.toLowerCase() + "%");
    }

    public static Specification<Livro> categoriaContains(String categoria) {
        return (root, query, cb) ->
                categoria == null ? null : cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%");
    }
}
