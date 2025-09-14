package com.alpha7.view.telaprincipal;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.client.ApiClient;
import com.alpha7.common.exception.BuscarLivroException;
import com.alpha7.common.exception.CadastroLivroViaCSVException;
import com.alpha7.common.exception.DeletarLivroException;
import com.alpha7.common.exception.ParametrosBuscaException;
import com.alpha7.view.atualizarlivros.AtualizarLivros;
import com.alpha7.view.inclusaolivros.InclusaoLivros;
import com.alpha7.view.util.AtualizarTabelaUtil;
import com.alpha7.view.util.CapturarMensagemErro;
import com.alpha7.view.util.CriaTabelaLivros;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Tela principal da aplicação de gerenciamento de livros.
 *
 * Esta tela exibe todos os livros cadastrados em uma tabela paginada e permite:
 *  Buscar livros filtrando por título, autor, data de publicação, ISBN, editora ou categoria;
 *  Incluir livros via upload de arquivo CSV;
 *  Excluir livros selecionados;
 *  Editar informações de livros selecionados;
 *  Visualizar todos os livros cadastrados.
 *
 * A interface é construída usando JFrame e componentes Swing.
 *
 * @author Lucas Gouvea Araujo
 * @version 1.0
 */
public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField tituloInput;
    private final JTextField autorInput;
    private final JFormattedTextField dataPublicacaoInput;
    private final JTextField isbnInput;
    private final JTextField editoraInput;
    private final JTextField categoriaInput;
    private final JFrame caixaAlerta;
    private static final String baseUrl = "http://localhost:8080/biblioteca/livros";
    private static final ApiClient client = new ApiClient(new RestTemplate(), baseUrl);
    private static final CapturarMensagemErro capturarMensagemErro = new CapturarMensagemErro();
    private static final CriaTabelaLivros tabelaPanel = new CriaTabelaLivros();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TelaPrincipal frame = new TelaPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                System.out.println("Erro ao iniciar a aplicacao: " + e.getMessage());
            }
        });
    }

    // Criacao do Frame
    public TelaPrincipal() {

        //Configuracao do Frame
        setTitle("Alpha 7 Software");
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(40, 100, 1600, 768);
        JLabel lblCabecalhoTabela = new JLabel("LISTA DE LIVRO");
        lblCabecalhoTabela.setFont(new Font("Arial", Font.BOLD, 14));
        lblCabecalhoTabela.setBounds(768, 0, 107, 42);
        getContentPane().add(lblCabecalhoTabela);

        // Conteiners/JPanels
        JPanel inferiorContent = new JPanel();
        inferiorContent.setLayout(null);
        inferiorContent.setBounds(442, 459, 720, 259);
        getContentPane().add(inferiorContent);

        JPanel camposBuscaContent = new JPanel();
        camposBuscaContent.setBorder(new LineBorder(new Color(0, 0, 0)));
        camposBuscaContent.setLayout(null);
        camposBuscaContent.setBounds(10, 11, 479, 237);
        inferiorContent.add(camposBuscaContent);

        JPanel csvContent = new JPanel();
        csvContent.setBorder(new LineBorder(new Color(0, 0, 0)));
        csvContent.setBounds(510, 11, 199, 96);
        csvContent.setLayout(null);
        inferiorContent.add(csvContent);

        JPanel excluirContent = new JPanel();
        excluirContent.setBorder(new LineBorder(new Color(0, 0, 0)));
        excluirContent.setBounds(510, 118, 199, 130);
        excluirContent.setLayout(null);
        inferiorContent.add(excluirContent);
        
        JPanel alphaPanel = new JPanel();
        excluirContent.setLayout(null);
        alphaPanel.setBounds(1273, 496, 210, 170);
        getContentPane().add(alphaPanel);
        
        JPanel logoPanel = new JPanel();
        excluirContent.setLayout(null);
        logoPanel.setBounds(116, 496, 210, 170);
                

        // Caixas de Alerta
        caixaAlerta = new JFrame("Exemplo de Alerta");
        caixaAlerta.setSize(300, 200);
        caixaAlerta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        caixaAlerta.getContentPane().setLayout(null);
        

        // JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        File diretorioPadrao = new File("C:/Projetos/arquivos");
        fileChooser.setCurrentDirectory(diretorioPadrao);


        // JLabels
        JLabel lblTitulo = new JLabel("Titulo:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setBounds(10, 41, 46, 14);
        camposBuscaContent.add(lblTitulo);

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setFont(new Font("Arial", Font.BOLD, 12));
        lblAutor.setBounds(10, 66, 46, 14);
        camposBuscaContent.add(lblAutor);

        JLabel lblDataDePublicao = new JLabel("Data de Publicação:");
        lblDataDePublicao.setFont(new Font("Arial", Font.BOLD, 12));
        lblDataDePublicao.setBounds(10, 91, 118, 14);
        camposBuscaContent.add(lblDataDePublicao);

        JLabel lblIsbn = new JLabel("ISBN:");
        lblIsbn.setFont(new Font("Arial", Font.BOLD, 12));
        lblIsbn.setBounds(10, 116, 46, 14);
        camposBuscaContent.add(lblIsbn);

        JLabel lblEditora = new JLabel("Editora:");
        lblEditora.setFont(new Font("Arial", Font.BOLD, 12));
        lblEditora.setBounds(10, 141, 46, 14);
        camposBuscaContent.add(lblEditora);

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 12));
        lblCategoria.setBounds(10, 166, 67, 14);
        camposBuscaContent.add(lblCategoria);

        JLabel lblCabecalhoCsv = new JLabel("CADASTRAR ARQUIVO");
        lblCabecalhoCsv.setHorizontalAlignment(SwingConstants.CENTER);
        lblCabecalhoCsv.setFont(new Font("Arial", Font.BOLD, 12));
        lblCabecalhoCsv.setBounds(10, 11, 179, 14);
        csvContent.add(lblCabecalhoCsv);

        JLabel lblCabecalhoExcluir = new JLabel("EXCLUIR LIVRO");
        lblCabecalhoExcluir.setHorizontalAlignment(SwingConstants.CENTER);
        lblCabecalhoExcluir.setFont(new Font("Arial", Font.BOLD, 12));
        lblCabecalhoExcluir.setBounds(10, 11, 179, 14);
        excluirContent.add(lblCabecalhoExcluir);

        JLabel lblCabecalhoFiltros = new JLabel("BUSQUE USANDO UM DOS FILTROS");
        lblCabecalhoFiltros.setHorizontalAlignment(SwingConstants.CENTER);
        lblCabecalhoFiltros.setFont(new Font("Arial", Font.BOLD, 12));
        lblCabecalhoFiltros.setBounds(10, 11, 459, 14);
        camposBuscaContent.add(lblCabecalhoFiltros);
        
        JLabel lblInformacaoExcluir1 = new JLabel("SELECIONE ATÉ 20 LIVROS");
        lblInformacaoExcluir1.setHorizontalAlignment(SwingConstants.CENTER);
        lblInformacaoExcluir1.setFont(new Font("Arial", Font.BOLD, 12));
        lblInformacaoExcluir1.setBounds(10, 36, 179, 23);
        excluirContent.add(lblInformacaoExcluir1);
        
        JLabel lblInformacaoExcluir2 = new JLabel("PARA EXCLUIR");
        lblInformacaoExcluir2.setHorizontalAlignment(SwingConstants.CENTER);
        lblInformacaoExcluir2.setFont(new Font("Arial", Font.BOLD, 12));
        lblInformacaoExcluir2.setBounds(10, 54, 179, 23);
        excluirContent.add(lblInformacaoExcluir2);
                
        
        // Imagem
        ImageIcon alpha = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/alpha7software_logo.jpeg")));
        Image imgAplha = alpha.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
        JLabel lblImagemAlpha = new JLabel(new ImageIcon(imgAplha));
        alphaPanel.add(lblImagemAlpha);
        
        // JTextFields
        tituloInput = new JTextField();
        tituloInput.setBounds(66, 36, 403, 20);
        camposBuscaContent.add(tituloInput);

        autorInput = new JTextField();
        autorInput.setBounds(66, 63, 403, 20);
        camposBuscaContent.add(autorInput);

        // FormattedTextField para Data de Publicacao
        MaskFormatter mascaraData = null;
        try {
            mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            System.out.println("Erro no MaskFormatter: " + e.getMessage());
        }
        dataPublicacaoInput = new JFormattedTextField(mascaraData);
        dataPublicacaoInput.setBounds(133, 88, 336, 20);
        camposBuscaContent.add(dataPublicacaoInput);

        isbnInput = new JTextField();
        isbnInput.setBounds(53, 113, 416, 20);
        camposBuscaContent.add(isbnInput);

        editoraInput = new JTextField();
        editoraInput.setBounds(66, 138, 403, 20);
        camposBuscaContent.add(editoraInput);

        categoriaInput = new JTextField();
        categoriaInput.setBounds(87, 163, 382, 20);
        camposBuscaContent.add(categoriaInput);


        // Buttons
        JButton cadastroButton = new JButton("Cadastrar Livro");
        cadastroButton.setBounds(581, 436, 135, 23);
        getContentPane().add(cadastroButton);

        JButton buscaFiltroButton = new JButton("Buscar");
        buscaFiltroButton.setBounds(143, 203, 89, 23);
        camposBuscaContent.add(buscaFiltroButton);
        
        JButton limparInputsButton = new JButton("Limpar");        
        limparInputsButton.setBounds(256, 203, 89, 23);
        camposBuscaContent.add(limparInputsButton);

        JButton csvButton = new JButton("Importar CSV");
        csvButton.setBounds(10, 48, 179, 23);
        csvContent.add(csvButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBounds(10, 88, 179, 23);
        excluirContent.add(excluirButton);      
        
        JButton carregarTodosButton = new JButton("Carregar Todos");
        carregarTodosButton.setBounds(738, 436, 150, 23);
        getContentPane().add(carregarTodosButton);

        JButton editarButton = new JButton("Editar Livro");
        editarButton.setBounds(910, 436, 150, 23);
        getContentPane().add(editarButton);

        // Cria JPanel com lista de livros populada
        tabelaPanel.carregarDados(false, false);
        getContentPane().add(tabelaPanel);

        // ActionListeners
        limparInputsButton.addActionListener(e -> limparImputs());
        
        cadastroButton.addActionListener(e -> {
            InclusaoLivros inclusaoLivro = new InclusaoLivros();
            inclusaoLivro.setVisible(true);
        });

        editarButton.addActionListener(e -> {
            try {
                List<LivroDTO> livrosSelecionados = livrosSelecionados();
                AtualizarLivros atualizarLivros = new AtualizarLivros();
                atualizarLivros.getLivroSelecionado(livrosSelecionados);
                atualizarLivros.setVisible(true);
            } catch (Exception err ) {
                JOptionPane.showMessageDialog(caixaAlerta, "Selecione um livro.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                throw new BuscarLivroException("Erro ao buscar livro para atualizar.");
            }
        });

        carregarTodosButton.addActionListener(e -> {
            try {
                AtualizarTabelaUtil.atualizar(tabelaPanel, false, false, null);
            } catch (Exception err) {
                try {
                    String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                    JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (JsonProcessingException ex) {
                    throw new BuscarLivroException(err.getMessage());
                }
                throw new BuscarLivroException("Erro ao buscar livros");
            }
        });

        buscaFiltroButton.addActionListener(e -> {
            try {
                List<String> parametrosDeBusca = getParametrosBuscaFiltro();
                AtualizarTabelaUtil.atualizar(tabelaPanel, false, true, parametrosDeBusca);
            } catch (Exception err) {
                try {
                    String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                    JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (JsonProcessingException ex) {
                    throw new BuscarLivroException(err.getMessage());
                }
                throw new BuscarLivroException("Erro ao buscar livros.");
            }
        });

        csvButton.addActionListener(e -> {
            try {
                int resultado = fileChooser.showOpenDialog(null);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                client.cadastrarArquivo(arquivo);
                AtualizarTabelaUtil.atualizar(tabelaPanel, false, true, null);
                JOptionPane.showMessageDialog(caixaAlerta,
                        "Arquivo " + arquivo.getName() + " carregado com sucesso.", "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception err) {
                try {
                    String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                    JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (JsonProcessingException ex) {
                    throw new CadastroLivroViaCSVException(err.getMessage());
                }
                throw new CadastroLivroViaCSVException("Erro no arquivo");
            }
        });

        excluirButton.addActionListener(e -> {
            try {
                List<LivroDTO> listaExcluir = livrosSelecionados();
                int resposta = optionPaneExcluir(listaExcluir);
                if (resposta == JOptionPane.YES_OPTION) {
                    client.deletar(listaExcluir);
                    tabelaPanel.getParametrosBusca(null);
                    int totalPaginas = tabelaPanel.getTotalPaginas();
                    if (tabelaPanel.getPaginaAtual() >= totalPaginas && totalPaginas > 0) {
                        tabelaPanel.setPaginaAtual(totalPaginas - 1);
                    }
                    AtualizarTabelaUtil.atualizar(tabelaPanel, false, false, null);
                    JOptionPane.showMessageDialog(caixaAlerta, "Livro excluído com sucesso!", "Informação",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception err) {
                System.out.println("err " + err.getMessage());
                JOptionPane.showMessageDialog(caixaAlerta, "Livro não excluído.", "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
                throw new DeletarLivroException(err.getMessage());
            }
        });
    }

    public List<LivroDTO> livrosSelecionados() {
        return tabelaPanel.listarLivrosSelecionados();
    }

    public int optionPaneExcluir(List<LivroDTO> listaExcluir) {
        if (listaExcluir.isEmpty()) {
            JOptionPane.showMessageDialog(caixaAlerta, "Selecione um livro.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return JOptionPane.DEFAULT_OPTION;
        } else {
            StringBuilder mensagemExclusao = new StringBuilder("Deseja realmente excluir os seguintes livros?\n");
            for (LivroDTO livro : listaExcluir) {
                mensagemExclusao.append("- ").append(livro.getTitulo()).append("\n");
            }
            return JOptionPane.showConfirmDialog(
                    caixaAlerta, mensagemExclusao.toString(), "Confirmar Exclusão", JOptionPane.YES_NO_OPTION );
        }
    }

    private void limparImputs() {
        tituloInput.setText(null);
        autorInput.setText(null);
        dataPublicacaoInput.setText(null);
        isbnInput.setText(null);
        editoraInput.setText(null);
        categoriaInput.setText(null);
    }
    
    private List<String> getParametrosBuscaFiltro() {
        List<String> params = new ArrayList<>();
        String parametro;
        String value;
        try {
            if (!tituloInput.getText().trim().isEmpty()) {
                parametro = "titulo";
                value = tituloInput.getText().trim();
                params.add(parametro);
                params.add(value);
            } else if (!autorInput.getText().trim().isEmpty()) {
                parametro = "autor";
                value = autorInput.getText().trim();
                params.add(parametro);
                params.add(value);
            } else if (!isDataFormatada(dataPublicacaoInput)) {
                parametro = "dataPublicacao";
                value = dataPublicacaoInput.getText().trim();
                params.add(parametro);
                params.add(value);
            } else if (!isbnInput.getText().trim().isEmpty()) {
                parametro = "isbn";
                value = isbnInput.getText().trim();
                params.add(parametro);
                params.add(value);
            } else if (!editoraInput.getText().trim().isEmpty()) {
                parametro = "editora";
                value = editoraInput.getText().trim();
                params.add(parametro);
                params.add(value);
            } else if (!categoriaInput.getText().trim().isEmpty()) {
                parametro = "categoria";
                value = categoriaInput.getText().trim();
                params.add(parametro);
                params.add(value);
            }
            return params;
        } catch (Exception err) {
            throw new ParametrosBuscaException(err.getMessage());
        }
    }

    private boolean isDataFormatada(JFormattedTextField campo) {
        String texto = campo.getText().trim();
        // verifica se todos os caracteres são "_" (ou espaços)
        return texto.replace("/", "").replace("_", "").isEmpty();
    }
}
