package com.alpha7.api.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entidade Livros da aplicacao.
 * @author Lucas Gouvea Araujo
 * */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "autor")
    private String autor;

    @Column(name = "data_publicacao")
    private String dataPublicacao;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "editora")
    private String editora;
    
    @Column(name = "categoria")
    private String categoria;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    public Livro() {
        this.dataCadastro = LocalDate.now();
    }

    public Livro(String titulo, String autor, String dataPublicacao, String isbn, String editora, String categoria, LocalDate dataCadastro) {
        this.titulo = titulo;
        this.autor = autor;
        this.dataPublicacao = dataPublicacao;
        this.isbn = isbn;
        this.editora = editora;
        this.categoria = categoria;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", dataPublicacao='" + dataPublicacao + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editora='" + editora + '\'' +
                ", categoria='" + categoria + '\'' +
                ", dataCadastro='" + dataCadastro + '\'' +
                '}';
    }
}