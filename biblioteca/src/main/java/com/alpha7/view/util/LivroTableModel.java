package com.alpha7.view.util;

import com.alpha7.api.biblioteca.dto.LivroDTO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Modelo de tabela para exibir informações de livros em um JTable.
 * Cada linha representa um LivroDTO e cada coluna representa um atributo do livro.
 *
 * Colunas:
 * - Código do Livro
 * - Título
 * - Autor
 * - Data Publicação
 * - ISBN
 * - Editora
 * - Categoria
 *
 * Autor: Lucas Gouvea Araujo
 */
public class LivroTableModel extends AbstractTableModel {

    private final String[] colunas = {
            "Código do Livro", "Título", "Autor", "Data Publicação", "ISBN", "Editora", "Categoria"
    };

    private List<LivroDTO> livros;

    public LivroTableModel(List<LivroDTO> livros) {
        this.livros = livros;
    }

    @Override
    public int getRowCount() {
        return livros.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LivroDTO livro = livros.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return livro.getCodigoLivro();
            case 1:
                return livro.getTitulo();
            case 2:
                return livro.getAutor();
            case 3:
                return livro.getDataPublicacao();
            case 4:
                return livro.getIsbn();
            case 5:
                return livro.getEditora();
            case 6:
                return livro.getCategoria();
            default:
                return null;
        }
    }

    public LivroDTO getLivroAt(int rowIndex) {
        if (livros == null || livros.isEmpty()) {
            return null;
        }
        return livros.get(rowIndex);
    }

    public void setLivros(List<LivroDTO> livros) {
        this.livros = livros;
        fireTableDataChanged();
    }
}
