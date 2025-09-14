package com.alpha7.view.atualizarlivros;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.client.ApiClient;
import com.alpha7.common.dto.Resposta;
import com.alpha7.common.exception.BuscarLivroException;
import com.alpha7.common.exception.CriacaoDTOException;
import com.alpha7.common.exception.ParametrosBuscaException;
import com.alpha7.view.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela de atualização de livros da biblioteca.
 * Esta classe permite atualizar os dados de um livro existente na base de dados.
 * A atualização pode ser realizada de duas formas:
 * Selecionando um livro já cadastrado a partir da lista, preenchendo automaticamente os inputs;
 * Após a edição, o usuário confirma as alterações clicando no botão "Editar" e a tela é fechada.
 *
 * Campos disponíveis:
 * ID do livro
 * Título
 * Autor
 * Data de Publicação
 * ISBN
 * Editora
 * Categoria
 * Todos os campos podem ser habilitados ou desabilitados dependendo da ação do usuário.
 * A tela exibe mensagens de alerta em caso de erro ou sucesso.
 *
 * @author Lucas Gouvêa Araujo
 */
public class AtualizarLivros extends JFrame {

	private static final long serialVersionUID = 1L;

    private final JTextField idInput;
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
                AtualizarLivros frame = new AtualizarLivros();
                frame.setVisible(true);
            } catch (Exception e) {
                System.out.println("Erro ao iniciar a aplicação: " + e.getMessage());
            }
		});
	}

    // Criacao do Frame
	public AtualizarLivros() {

        //Configuracao do Frame
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(490, 555, 720, 300);
		
        JLabel lblCabecalhoFrame = new JLabel("ATUALIZAR LIVROS");
		lblCabecalhoFrame.setBounds(41, 0, 624, 30);
		lblCabecalhoFrame.setHorizontalAlignment(SwingConstants.CENTER);
		lblCabecalhoFrame.setFont(new Font("Arial", Font.BOLD, 12));
		getContentPane().add(lblCabecalhoFrame);

        // Conteiners/JPanels
        JPanel atualizarLivroContent = new JPanel();
		atualizarLivroContent.setLayout(null);
		atualizarLivroContent.setBounds(10, 29, 684, 232);
		getContentPane().add(atualizarLivroContent);

        CriaTabelaLivros tabelaPanel = new CriaTabelaLivros();

        // Caixas de Alerta
		caixaAlerta = new JFrame("Exemplo de Alerta");
		caixaAlerta.setSize(300, 200);
		caixaAlerta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		caixaAlerta.getContentPane().setLayout(null);

        // JLabels
        JLabel lblId = new JLabel("Código do Livro:");
		lblId.setFont(new Font("Arial", Font.BOLD, 12));
		lblId.setBounds(10, 20, 100, 14);
		atualizarLivroContent.add(lblId);

        JLabel lblTitulo = new JLabel("Titulo:");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
		lblTitulo.setBounds(10, 45, 46, 14);
		atualizarLivroContent.add(lblTitulo);

        JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setFont(new Font("Arial", Font.BOLD, 12));
		lblAutor.setBounds(10, 70, 46, 14);
		atualizarLivroContent.add(lblAutor);

        JLabel lblDataDePublicao = new JLabel("Data de Publicação:");
		lblDataDePublicao.setFont(new Font("Arial", Font.BOLD, 12));
		lblDataDePublicao.setBounds(10, 95, 118, 14);
		atualizarLivroContent.add(lblDataDePublicao);

        JLabel lblIsbn = new JLabel("ISBN:");
		lblIsbn.setFont(new Font("Arial", Font.BOLD, 12));
		lblIsbn.setBounds(10, 120, 46, 14);
		atualizarLivroContent.add(lblIsbn);

        JLabel lblEditora = new JLabel("Editora:");
		lblEditora.setFont(new Font("Arial", Font.BOLD, 12));
		lblEditora.setBounds(10, 145, 46, 14);
		atualizarLivroContent.add(lblEditora);

        JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setFont(new Font("Arial", Font.BOLD, 12));
		lblCategoria.setBounds(10, 170, 66, 14);
		atualizarLivroContent.add(lblCategoria);

        // JTextFields
		idInput = new JTextField();
		idInput.setBounds(113, 17, 561, 20);
		atualizarLivroContent.add(idInput);
		idInput.setColumns(10);
		
		tituloInput = new JTextField();
		tituloInput.setBounds(66, 42, 608, 20);
		atualizarLivroContent.add(tituloInput);

		autorInput = new JTextField();
		autorInput.setBounds(60, 67, 614, 20);
        atualizarLivroContent.add(autorInput);

        // FormattedTextField para Data de Publicacao
        MaskFormatter mascaraData = null;
        try {
            mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            System.out.println("Erro no MaskFormatter: " + e.getMessage());
        }
		dataPublicacaoInput = new JFormattedTextField(mascaraData);
		dataPublicacaoInput.setBounds(132, 92, 542, 20);
		atualizarLivroContent.add(dataPublicacaoInput);

		isbnInput = new JTextField();
		isbnInput.setBounds(52, 117, 622, 20);
		atualizarLivroContent.add(isbnInput);

		editoraInput = new JTextField();
		editoraInput.setBounds(66, 142, 608, 20);
		atualizarLivroContent.add(editoraInput);
		
		categoriaInput = new JTextField();
		categoriaInput.setBounds(76, 167, 598, 20);
		atualizarLivroContent.add(categoriaInput);

        desabilitarInputs();

        // Buttons
        JButton atualizarButton = new JButton("Editar");
		atualizarButton.setBounds(150, 198, 100, 23);
		atualizarLivroContent.add(atualizarButton);

        JButton voltarButton = new JButton("Voltar");
		voltarButton.setBounds(585, 198, 89, 23);
		atualizarLivroContent.add(voltarButton);

        JButton buscarDadosButton = new JButton("Buscar dados");
		buscarDadosButton.setBounds(10, 198, 130, 23);
		atualizarLivroContent.add(buscarDadosButton);

        // ActionListeners
        voltarButton.addActionListener(e -> dispose());

        buscarDadosButton.addActionListener(e -> {
            try {
                LivroDTO dto = buscarDadosDoLivro();
                preencherInputs(dto);
            } catch (Exception err) {
                try {
                    String mensagemErro = capturarMensagemErro.capturarMensagemErro(err.getMessage());
                    JOptionPane.showMessageDialog(caixaAlerta, mensagemErro, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } catch (JsonProcessingException ex) {
                    throw new BuscarLivroException(err.getMessage());
                }
                throw new BuscarLivroException("Erro ao buscar dados do livro.");
            }
		});

		atualizarButton.addActionListener(e -> {
                LivroDTO dto = criarDTO();
                Resposta<LivroDTO> resposta = client.atualizarLivro(idInput.getText().isEmpty() ? null : Long.parseLong(idInput.getText().trim()), dto);
                if (resposta.isOk()) {
                    JOptionPane.showMessageDialog(caixaAlerta, "Livro: " + resposta.getData().getTitulo() + " atualizado com sucesso.",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                    AtualizarTabelaUtil.atualizar(tabelaPanel, true, false, null);
                    limparImputs();
                    dispose();
                } else {
                    System.out.println(resposta.getErro());
                    JOptionPane.showMessageDialog(caixaAlerta, "Erro ao atualizar livro.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
		});
	}

    public void getLivroSelecionado(List<LivroDTO> listaLivros) {
        if (listaLivros.size() > 1) {
            JOptionPane.showMessageDialog(caixaAlerta, "Selecione apenas um livro ou busque por um.",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!listaLivros.isEmpty()) {
            preencherInputs(listaLivros.stream().findFirst().orElse(null));
            habilitarInputs();
        }
    }

    private LivroDTO buscarDadosDoLivro() {
        List<String> params = parametrosFiltro();
        if (params == null || params.isEmpty()) {
            throw new BuscarLivroException("Passe ao menos um parametro.");
        }
        String parametro = params.get(0);
        String value = params.get(1);
        boolean isEditarLivro = true;
        int tamanhoPagina = 20;
        int paginaAtual = 0;
        Resposta<PageResponse<LivroDTO>> resposta = client.buscarPorFiltro(paginaAtual, tamanhoPagina, parametro, value, isEditarLivro);
        if (resposta.getErro() != null) {
            throw new BuscarLivroException("Livro não encontrado");
        }
        return getLivroDTO(resposta);
    }

    private static LivroDTO getLivroDTO(Resposta<PageResponse<LivroDTO>> resposta) {
        List<LivroDTO> listaLivros = resposta.getData().getContent();
        LivroDTO livro = new LivroDTO();
        for (LivroDTO livroLista : listaLivros) {
            livro.setCodigoLivro(livroLista.getCodigoLivro());
            livro.setTitulo(livroLista.getTitulo());
            livro.setAutor(livroLista.getAutor());
            livro.setDataPublicacao(livroLista.getDataPublicacao());
            livro.setIsbn(livroLista.getIsbn());
            livro.setEditora(livroLista.getEditora());
            livro.setCategoria(livroLista.getCategoria());
        }
        return livro;
    }

    private LivroDTO criarDTO() {
        try {
            LivroDTO dto = new LivroDTO();
            dto.setTitulo(tituloInput.getText().trim());
            dto.setAutor(autorInput.getText().trim());
            dto.setDataPublicacao(dataPublicacaoInput.getText().trim());
            dto.setIsbn(isbnInput.getText().trim());
            dto.setEditora(editoraInput.getText().trim());
            dto.setCategoria(categoriaInput.getText().trim());
            return validarDto.hasData(dto) ? dto : null;
        } catch (Exception err) {
            throw new CriacaoDTOException(err.getMessage());
        }
    }

    private List<String> parametrosFiltro() {
        List<String> params = new ArrayList<>();
        String parametro;
        String value;
        habilitarInputs();
        try {
            if (!idInput.getText().trim().isEmpty()) {
                parametro = "id";
                value = idInput.getText();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!tituloInput.getText().trim().isEmpty()) {
                parametro = "titulo";
                value = tituloInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!autorInput.getText().trim().isEmpty()) {
                parametro = "autor";
                value = autorInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!dataPublicacaoInput.getText().trim().isEmpty()) {
                parametro = "dataPublicacao";
                value = dataPublicacaoInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!isbnInput.getText().trim().isEmpty()) {
                parametro = "isbn";
                value = isbnInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!editoraInput.getText().trim().isEmpty()) {
                parametro = "editora";
                value = editoraInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            if (!categoriaInput.getText().trim().isEmpty()) {
                parametro = "categoria";
                value = categoriaInput.getText().trim();
                params.add(parametro);
                params.add(value);
                return params;
            }
            return params;
        } catch (Exception err) {
            throw new ParametrosBuscaException(err.getMessage());
        }
    }
    private void habilitarInputs() {
        autorInput.setEnabled(true);
        dataPublicacaoInput.setEnabled(true);
        editoraInput.setEnabled(true);
        categoriaInput.setEnabled(true);
    }

    private void desabilitarInputs() {
        autorInput.setEnabled(false);
        dataPublicacaoInput.setEnabled(false);
        editoraInput.setEnabled(false);
        categoriaInput.setEnabled(false);
    }
    private void preencherInputs(LivroDTO dto) {
        idInput.setText(dto.getCodigoLivro().toString());
        tituloInput.setText(dto.getTitulo());
        autorInput.setText(dto.getAutor());
        dataPublicacaoInput.setText(dto.getDataPublicacao());
        isbnInput.setText(dto.getIsbn());
        editoraInput.setText(dto.getEditora());
        categoriaInput.setText(dto.getCategoria());
    }

    private void limparImputs() {
        idInput.setText(null);
        tituloInput.setText(null);
        autorInput.setText(null);
        dataPublicacaoInput.setText(null);
        isbnInput.setText(null);
        editoraInput.setText(null);
        categoriaInput.setText(null);
    }
}
