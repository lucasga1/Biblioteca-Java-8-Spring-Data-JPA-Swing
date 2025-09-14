package com.alpha7.api.biblioteca.dto;

import com.alpha7.api.model.Livro;

/**
 * LivroDTO e nossa camada de entrada e saida da API.
 * @author Lucas Gouvea Araujo
 * */
public class LivroDTO {

    private Long codigoLivro;
    private String titulo;
    private String autor;
    private String dataPublicacao;
    private String isbn;
    private String editora;
    private String categoria;

    public LivroDTO() {
    }

    public LivroDTO(Livro livro) {
        this.codigoLivro = livro.getId();
        this.titulo = livro.getTitulo();
        this.autor = livro.getAutor();
        this.dataPublicacao = livro.getDataPublicacao();
        this.isbn = livro.getIsbn();
        this.editora = livro.getEditora();
        this.categoria = livro.getCategoria();
    }

    public Long getCodigoLivro() { return codigoLivro; }

    public void setCodigoLivro(Long codigoLivro) { this.codigoLivro = codigoLivro; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "LivroResponse{" +
                "codigoLivro=" + codigoLivro +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", dataPublicacao='" + dataPublicacao + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editora='" + editora + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
