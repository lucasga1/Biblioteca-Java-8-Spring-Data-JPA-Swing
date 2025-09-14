package com.alpha7.view.inclusaolivros;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.client.ApiClient;
import com.alpha7.common.dto.Resposta;
import com.alpha7.common.exception.CadastroLivroException;
import com.alpha7.common.exception.CadastroLivroViaCSVException;
import com.alpha7.common.exception.CriacaoDTOException;
import com.alpha7.common.exception.OpenLibraryException;
import com.alpha7.view.util.AtualizarTabelaUtil;
import com.alpha7.view.util.CapturarMensagemErro;
import com.alpha7.view.util.CriaTabelaLivros;
import com.alpha7.view.util.ValidarLivroDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Tela de cadastro de livros.
 *
 * Esta tela permite ao usuário:
 * - Cadastrar um novo livro, preenchendo todos os dados necessários;
 * - Pesquisar um livro por ISBN utilizando a API externa OpenLibrary.org;
 * - Validar os dados do livro antes do cadastro;
 * - Atualizar automaticamente a tabela de livros na tela principal após o cadastro.
 *
 * Regras principais:
 * - Para cadastrar, pelo menos um campo deve estar preenchido;
 * - O ISBN deve ser único;
 * - Erros na criação de DTO ou no cadastro são tratados com exceções específicas.
 *
 * @author Lucas Gouvea Araujo
 * @version 1.0
 */
public class InclusaoLivros extends JFrame {

	private static final long serialVersionUID = 1L;

    private final JTextField autorInput;
    private final JTextField tituloInput;
	private final JFormattedTextField dataPublicacaoInput;
	private final JTextField isbnInput;
	private final JTextField editoraInput;
	private final JTextField categoriaInput;
	private final JFrame caixaAlerta;

    private static final String baseUrl = "http://localhost:8080/biblioteca/livros";
    private static final ApiClient client = new ApiClient(new RestTemplate(), baseUrl);
    private static final ValidarLivroDTO validarDto = new ValidarLivroDTO();
    private static final CapturarMensagemErro capturarMensagemErro = new CapturarMensagemErro();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                InclusaoLivros frame = new InclusaoLivros();
                frame.setVisible(true);
            } catch (Exception e) {
                System.out.println("Erro ao iniciar a aplicação: " + e.getMessage());
            }
        });
	}

	// Criacao do Frame
	public InclusaoLivros() {

        //Configuracao do Frame
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(490, 555, 720, 300);
        JLabel lblCabecalhoFrame = new JLabel("CADASTRO DE LIVROS");
		lblCabecalhoFrame.setBounds(10, 11, 624, 25);
		lblCabecalhoFrame.setHorizontalAlignment(SwingConstants.CENTER);
		lblCabecalhoFrame.setFont(new Font("Arial", Font.BOLD, 12));
		getContentPane().add(lblCabecalhoFrame);

        // Conteiners/JPanels
        JPanel cadastroLivroContent = new JPanel();
		cadastroLivroContent.setLayout(null);
		cadastroLivroContent.setBounds(10, 38, 684, 212);
		getContentPane().add(cadastroLivroContent);

        CriaTabelaLivros tabelaPanel = new CriaTabelaLivros();

        // Caixas de Alerta
		caixaAlerta = new JFrame("Exemplo de Alerta");
		caixaAlerta.setSize(300, 200);
		caixaAlerta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		caixaAlerta.getContentPane().setLayout(null);

        // JLabels
        JLabel lblTitulo = new JLabel("Titulo:");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
		lblTitulo.setBounds(10, 14, 46, 14);
		cadastroLivroContent.add(lblTitulo);

        JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setFont(new Font("Arial", Font.BOLD, 12));
		lblAutor.setBounds(10, 39, 46, 14);
		cadastroLivroContent.add(lblAutor);

        JLabel lblDataDePublicao = new JLabel("Data de Publicação:");
		lblDataDePublicao.setFont(new Font("Arial", Font.BOLD, 12));
		lblDataDePublicao.setBounds(10, 64, 118, 14);
		cadastroLivroContent.add(lblDataDePublicao);

        JLabel lblIsbn = new JLabel("ISBN:");
		lblIsbn.setFont(new Font("Arial", Font.BOLD, 12));
		lblIsbn.setBounds(10, 89, 46, 14);
		cadastroLivroContent.add(lblIsbn);

        JLabel lblEditora = new JLabel("Editora:");
		lblEditora.setFont(new Font("Arial", Font.BOLD, 12));
		lblEditora.setBounds(10, 114, 46, 14);
		cadastroLivroContent.add(lblEditora);

        JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setFont(new Font("Arial", Font.BOLD, 12));
		lblCategoria.setBounds(10, 139, 66, 14);
		cadastroLivroContent.add(lblCategoria);

        // JTextFields
		tituloInput = new JTextField();
		tituloInput.setBounds(66, 11, 608, 20);
		cadastroLivroContent.add(tituloInput);

		autorInput = new JTextField();
		autorInput.setBounds(60, 36, 614, 20);
		cadastroLivroContent.add(autorInput);

        // FormattedTextField para Data de Publicacao
        MaskFormatter mascaraData = null;
        try {
            mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            System.out.println("Erro no MaskFormatter: " + e.getMessage());
        }
        dataPublicacaoInput = new JFormattedTextField(mascaraData);
        dataPublicacaoInput.setBounds(132, 61, 542, 20);
		cadastroLivroContent.add(dataPublicacaoInput);

		isbnInput = new JTextField();
		isbnInput.setBounds(52, 86, 622, 20);
		cadastroLivroContent.add(isbnInput);

		editoraInput = new JTextField();
		editoraInput.setBounds(66, 111, 608, 20);
		cadastroLivroContent.add(editoraInput);
		
		categoriaInput = new JTextField();
		categoriaInput.setBounds(76, 136, 598, 20);
		cadastroLivroContent.add(categoriaInput);
		categoriaInput.setColumns(10);

        // Buttons
        JButton cadastrarButton = new JButton("Cadastrar");
		cadastrarButton.setBounds(180, 167, 100, 23);
		cadastroLivroContent.add(cadastrarButton);

        JButton isbnButton = new JButton("Buscar por ISBN");
		isbnButton.setBounds(10, 167, 150, 23);
		cadastroLivroContent.add(isbnButton);

        JButton voltarButton = new JButton("Voltar");
		voltarButton.setBounds(585, 167, 89, 23);
		cadastroLivroContent.add(voltarButton);

        // ActionListeners
		voltarButton.addActionListener(e -> dispose());

		cadastrarButton.addActionListener(e -> {
            try {
                LivroDTO dto = criarDTO();
                ResponseEntity<Void> response = client.cadastrarLivro(dto);
                if (HttpStatus.CREATED.equals(response.getStatusCode())) {
                    JOptionPane.showMessageDialog(caixaAlerta, "Livro cadastrado com sucesso.", "Informação",
                            JOptionPane.INFORMATION_MESSAGE);
                    AtualizarTabelaUtil.atualizar(tabelaPanel, false, false, null);
                    limparImputs();
                    dispose();
                }
            } catch (CadastroLivroException err) {
                try {
                    String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                    JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (JsonProcessingException ex) {
                    throw new CadastroLivroViaCSVException(err.getMessage());
                }
            }
		});

		isbnButton.addActionListener(e -> {
            try {
                Resposta<LivroDTO> response = client.getLivroOpenLibrary(isbnInput.getText().trim());
                if (response.getErro() != null) {
                    try {
                        String mensagemErro = capturarMensagemErro.capturarMensagemErro(response.getErro());
                        JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                    } catch (JsonProcessingException ex) {
                        throw new CadastroLivroViaCSVException(response.getErro());
                    }
                }
                if (response.isOk()) {
                    LivroDTO livro = response.getData();
                    preencherInputs(livro);
                }
            } catch (Exception err) {
                throw new OpenLibraryException("Erro ao buscar livro.");
            }
		});
	}

    private LivroDTO criarDTO() {
        try {
            LivroDTO dto = new LivroDTO();
            dto.setTitulo(!tituloInput.getText().trim().isEmpty() ? tituloInput.getText().trim() : null);
            dto.setAutor(!autorInput.getText().trim().isEmpty() ? autorInput.getText().trim() : null);
            dto.setDataPublicacao(!dataPublicacaoInput.getText().trim().isEmpty() ? dataPublicacaoInput.getText().trim() : null);
            dto.setIsbn(!isbnInput.getText().trim().isEmpty() ? isbnInput.getText().trim() : null);
            dto.setEditora(!editoraInput.getText().trim().isEmpty() ? editoraInput.getText().trim() : null);
            dto.setCategoria(!categoriaInput.getText().trim().isEmpty() ? categoriaInput.getText().trim() : null);
            return validarDto.hasData(dto) ? dto : null;
        } catch (Exception err) {
            throw new CriacaoDTOException(err.getMessage());
        }
    }

    private void preencherInputs(LivroDTO dto) {
        tituloInput.setText(dto.getTitulo());
        autorInput.setText(dto.getAutor());
        dataPublicacaoInput.setText(dto.getDataPublicacao());
        editoraInput.setText(dto.getEditora());
        categoriaInput.setText(dto.getCategoria());
    }

    private void limparImputs() {
        tituloInput.setText(null);
        autorInput.setText(null);
        dataPublicacaoInput.setText(null);
        isbnInput.setText(null);
        editoraInput.setText(null);
        categoriaInput.setText(null);
    }
}
