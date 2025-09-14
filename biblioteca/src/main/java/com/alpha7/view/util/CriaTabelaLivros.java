package com.alpha7.view.util;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.client.ApiClient;
import com.alpha7.common.dto.Resposta;
import com.alpha7.common.exception.BuscarLivroException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por controlar a criacao das tabelas que apresentam os livros na pagina principal.
 * Atraves dessa classe, podemos criar TabelaGeral (apresentada quando iniciamos a aplicacao e quando realizamos uma busca por todos os registros)
 * e TabelaFiltro (que traz somente os registros pelos campos que foram filtrados).
 * @author Lucas Gouvea Araujo
 * */
public class CriaTabelaLivros extends JPanel {

    private static final long serialVersionUID = 1L;

    private int paginaAtual = 0;
    private boolean isButtonAtualizar = false;
    private boolean isButtonFiltro = false;
    private JTable tabelaListagemLivros = new JTable();
    private Resposta<PageResponse<LivroDTO>> pageResponse = new Resposta<>();
    private final List<String> parametrosBusca = new ArrayList<>();
    private final JButton proximaButton;
    private final JButton anteriorButton;
    private final JLabel lblPagina;
    private final JScrollPane scrollPane;
    private final JFrame caixaAlerta;

    private static final String baseUrl = "http://localhost:8080/biblioteca/livros";
    private static final ApiClient client = new ApiClient(new RestTemplate(), baseUrl);
    private static final CapturarMensagemErro capturarMensagemErro = new CapturarMensagemErro();
    private static final LivroTableModel livroTableModel = new LivroTableModel(new ArrayList<>());

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CriaTabelaLivros panel = new CriaTabelaLivros();
                panel.setVisible(true);
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        });
    }

    // Criacao do Frame
    public CriaTabelaLivros() {

        //Configuracao do Frame
        setLayout(null);
        setBounds(10, 37, 1560, 380);

        caixaAlerta = new JFrame("Exemplo de Alerta");
        caixaAlerta.setSize(300, 200);
        caixaAlerta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        caixaAlerta.getContentPane().setLayout(null);

        proximaButton = new JButton("Próxima");
        proximaButton.setBounds(834, 357, 89, 23);
        add(proximaButton);

        anteriorButton = new JButton("Anterior");
        anteriorButton.setBounds(679, 357, 89, 23);
        add(anteriorButton);

        lblPagina = new JLabel();
        lblPagina.setHorizontalAlignment(SwingConstants.CENTER);
        lblPagina.setBounds(778, 357, 46, 14);
        add(lblPagina);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 1560, 343);
        add(scrollPane);

        carregarDados(isButtonAtualizar, isButtonFiltro);
        scrollPane.setViewportView(tabelaListagemLivros);

        proximaButton.addActionListener(e -> irParaProximaPagina());

        anteriorButton.addActionListener(e -> irParaPaginaAnterior());

    }

    private JTable criarTabela(boolean isButtonAtualizar, boolean isButtonFiltro) {
        DefaultTableModel tabelaPadrao = new DefaultTableModel(new Object[][]{}, new String[]{"Código do Livro", "Título", "Autor", "Data de Publicação", "ISBN", "Editora", "Categoria"});
        try {
            int tamanhoPagina = 20;
            if (isButtonFiltro && parametrosBusca != null && !parametrosBusca.isEmpty()) {
                String parametro = parametrosBusca.get(0);
                String value = parametrosBusca.get(1);
                pageResponse = client.buscarPorFiltro(paginaAtual, tamanhoPagina, parametro, value, isButtonAtualizar);
            } else {
                pageResponse = client.buscarTodos(paginaAtual, tamanhoPagina);
            }
            if (pageResponse.isOk() && pageResponse.getData() != null) {
                List<LivroDTO> listaLivros = pageResponse.getData().getContent();
                livroTableModel.setLivros(listaLivros);
            } else {
                livroTableModel.setLivros(new ArrayList<>());
            }

            JTable tabelaPreenchida = new JTable(livroTableModel);
            atualizarEstadoBotoes();
            return tabelaPreenchida;
        } catch (Exception err) {
            try {
                System.out.println("erro " + err.getMessage());
                String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
            } catch (JsonProcessingException ex) {
                throw new BuscarLivroException(err.getMessage());
            }
            throw new BuscarLivroException("Erro ao buscar livros.");
        }
    }

    private void irParaProximaPagina() {
        try {
            if (pageResponse != null && pageResponse.getData() != null) {
                int totalPaginas = pageResponse.getData().getTotalPages();

                if (paginaAtual < totalPaginas - 1) {
                    paginaAtual++;
                    carregarDados(isButtonAtualizar, isButtonFiltro);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void irParaPaginaAnterior() {
        try {
            if (paginaAtual > 0) {
                paginaAtual--;
                carregarDados(isButtonAtualizar, isButtonFiltro);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void atualizarEstadoBotoes() {
        if (pageResponse != null && pageResponse.getData() != null) {
            int totalPaginas = pageResponse.getData().getTotalPages();

            anteriorButton.setEnabled(paginaAtual > 0);
            proximaButton.setEnabled(paginaAtual < totalPaginas - 1);

            lblPagina.setText((paginaAtual + 1) + " / " + totalPaginas);
        } else {
            anteriorButton.setEnabled(false);
            proximaButton.setEnabled(false);
            lblPagina.setText("0 / 0");
        }
    }

    public void getParametrosBusca(List<String> params) {
        parametrosBusca.clear();
        if (params != null && !params.isEmpty()) {
            parametrosBusca.add(params.get(0));
            parametrosBusca.add(params.get(1));
        }
    }

    public void carregarDados(boolean isAtualizar, boolean isFiltro) {
        isButtonAtualizar = isAtualizar;
        isButtonFiltro = isFiltro;
        try {
            tabelaListagemLivros = criarTabela(isButtonAtualizar, isButtonFiltro);
            scrollPane.setViewportView(tabelaListagemLivros);
            atualizarEstadoBotoes();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void voltarParaPaginacao() {
        paginaAtual = 0;
        carregarDados(isButtonAtualizar, isButtonFiltro);
    }

    public List<LivroDTO> listarLivrosSelecionados() {
        int[] linhas = tabelaListagemLivros.getSelectedRows();
        if (linhas != null && linhas.length > 20) {
            JOptionPane.showMessageDialog(caixaAlerta, "Permitido selecionar até 20 livros por vez.",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            return new ArrayList<>();
        }
        if (linhas != null && linhas.length > 0) {
            List<LivroDTO> livrosSelecionados = new ArrayList<>();
            for (int linha : linhas) {
                LivroDTO livro = livroTableModel.getLivroAt(linha);
                livrosSelecionados.add(livro);
            }
            return livrosSelecionados;
        }
        return new ArrayList<>();
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int pagina) {
        this.paginaAtual = pagina;
    }

    public int getTotalPaginas() {
        if (pageResponse != null && pageResponse.getData() != null) {
            return pageResponse.getData().getTotalPages();
        }
        return 0;
    }
}