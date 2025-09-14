package com.alpha7.api.util;

import com.alpha7.api.model.Livro;
import com.alpha7.api.repository.LivroRepository;
import com.alpha7.api.service.impl.LivroServiceImpl;
import com.alpha7.common.exception.CadastroLivroViaCSVException;
import com.alpha7.common.exception.OperacaoNaoPermitidaException;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe responsavel por receber o arquivo CSV, processa-lo e gravar ou atualizar as transacoes no banco de dados.
 * @author Lucas Gouvea Araujo
 * */
@Component
public class ProcessarArquivoCSV {

    private final LivroRepository repository;
    private static final Logger LOG = Logger.getLogger(LivroServiceImpl.class);

    public ProcessarArquivoCSV(LivroRepository repository) {
        this.repository = repository;
    }

    public void cadastrarLivrosArquivoCSV(MultipartFile arquivoMultipart) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(arquivoMultipart.getInputStream(), StandardCharsets.UTF_8))) {
           // Le as linhas e mapeia para o model Livro.
           List<Livro> livrosDoCSV = reader.lines()
                   .skip(1)
                   .map(linha -> linha.split(","))
                   .map(this::criarLivroFromCSV)
                   .collect(Collectors.toList());

           // Busca todos os ISBNs já existentes no banco.
           List<String> isbns = livrosDoCSV.stream()
                   .map(Livro::getIsbn)
                   .collect(Collectors.toList());
           List<Livro> livrosExistentes = repository.findAllByIsbnIn(isbns);

           // Cria um mapa de ISBN existente.
           Map<String, Livro> mapaExistentes = livrosExistentes.stream().collect(Collectors.toMap(Livro::getIsbn, l -> l));

           // Atualiza os existentes, separar novos e coloca todos na List paraSalvar e chama o repository.save apenas uma vez.
           List<Livro> paraSalvar = new ArrayList<>();
           for (Livro livroNovo : livrosDoCSV) {
               if (mapaExistentes.containsKey(livroNovo.getIsbn())) {
                   Livro existente = mapaExistentes.get(livroNovo.getIsbn());
                   existente.setTitulo(livroNovo.getTitulo());
                   existente.setAutor(livroNovo.getAutor());
                   existente.setDataPublicacao(livroNovo.getDataPublicacao());
                   existente.setEditora(livroNovo.getEditora());
                   existente.setCategoria(livroNovo.getCategoria());
                   paraSalvar.add(existente);
               } else {
                   paraSalvar.add(livroNovo);
               }
           }
           repository.saveAll(paraSalvar);
        } catch (OperacaoNaoPermitidaException e) {
           LOG.debug("Erro ao processar o arquivo.");
           throw new OperacaoNaoPermitidaException("Erro ao processar o arquivo.");
        } catch (IOException e) {
           LOG.debug("Erro ao processar o arquivo.");
           throw new RuntimeException(e.getMessage());
        }
    }

    private Livro criarLivroFromCSV(String[] value) {
        try {
            Livro livro = new Livro();
            livro.setTitulo(value[0]);
            livro.setAutor(value[1]);
            livro.setDataPublicacao(value[2]);
            livro.setIsbn(value[3]);
            livro.setEditora(value[4]);
            livro.setCategoria(value[5]);
            return livro;
        } catch (Exception e) {
            LOG.debug("Erro ao criar livro pelas linhas do arquivo. Verifique se o arquivo está correto.");
            throw new CadastroLivroViaCSVException("Erro ao criar livro pelas linhas do arquivo. Verifique se o arquivo está correto.");
        }
    }


}
