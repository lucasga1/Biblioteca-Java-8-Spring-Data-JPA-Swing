package com.alpha7.view.util;

import com.alpha7.api.biblioteca.dto.LivroDTO;

/**
 * Responsavel por validar o DTO e garantir que nao seja atualizado ou cadastro um livro vazio.
 * @author Lucas Gouvea Araujo
 * */
public class ValidarLivroDTO {

    public boolean hasData(LivroDTO dto) {
        return (dto.getTitulo() != null && !dto.getTitulo().trim().isEmpty()) ||
                (dto.getAutor() != null && !dto.getAutor().trim().isEmpty()) ||
                (dto.getDataPublicacao() != null && !dto.getDataPublicacao().trim().isEmpty()) ||
                (dto.getIsbn() != null && !dto.getIsbn().trim().isEmpty()) ||
                (dto.getEditora() != null && !dto.getEditora().trim().isEmpty()) ||
                (dto.getCategoria() != null && !dto.getCategoria().trim().isEmpty());
    }

}
