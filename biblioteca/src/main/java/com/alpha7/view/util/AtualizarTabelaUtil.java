package com.alpha7.view.util;

import java.util.List;

public class AtualizarTabelaUtil {

    /**
     * Atualiza a tabela de livros com os parâmetros informados.
     *
     * @param tabelaPanel painel da tabela a ser atualizado
     * @param atualizar   indica se o botão atualizar foi pressionado
     * @param filtro      indica se é uma busca filtrada
     * @param parametrosBusca lista de parâmetros de busca (pode ser null)
     * @author Lucas Gouvea Araujo
     */
    public static void atualizar(CriaTabelaLivros tabelaPanel, boolean atualizar, boolean filtro, List<String> parametrosBusca) {
        tabelaPanel.setVisible(true);
        tabelaPanel.getParametrosBusca(parametrosBusca);
        tabelaPanel.carregarDados(atualizar, filtro);
        tabelaPanel.voltarParaPaginacao();
    }
}
