package com.alpha7.common.client;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.exception.OpenLibraryException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Classe responsavel por realizar as buscas na OpenLibraryAPI.
 * @author Lucas Gouvea Araujo
 * */
@Component
public class OpenLibraryClient {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private static final Logger LOG = Logger.getLogger(OpenLibraryClient.class);

    public OpenLibraryClient(RestTemplate restTemplate) {
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(new java.text.SimpleDateFormat("dd-MM-yyyy"));
        this.restTemplate = restTemplate;
    }

    /**
     * Busca o livro de acordo com o ISBN.
     */
    public LivroDTO getLivro(String isbn) {
        try {
            String url = "https://openlibrary.org/isbn/" + isbn + ".json";
            String response = restTemplate.getForObject(url, String.class);
            return criarLivroDeJson(response);
        } catch (HttpClientErrorException.NotFound e) {
            LOG.debugf("Livro nao encontrado na OpenLibraryAPI");
            throw new OpenLibraryException("Livro não encontrado na OpenLibraryAPI.");
        }
    }

    /**
     * Busca o nome do autor, caso o livro encontrado tenha o codigo do autor.
     * */
    private String getAutor(String authorKey) {
        String url = "https://openlibrary.org" + authorKey + ".json";
        JsonNode node;
        try {
            String response = restTemplate.getForObject(url, String.class);
            node = mapper.readTree(response);
            return node.path("name").asText("Nome do autor não encontrado");
        } catch (HttpClientErrorException.NotFound | JsonProcessingException e) {
            LOG.debugf("Autor não encontrado na OpenLibraryAPI");
            return "Autor não encontrado.";
        }
    }

    /**
     * Cria o Json com o livro encontrado para retornar na ApiCLient.
     * */
    private LivroDTO criarLivroDeJson(String jsonResponse) {
        try {
            JsonNode root = mapper.readTree(jsonResponse);
            LivroDTO livro = new LivroDTO();

            livro.setTitulo(root.path("title").asText(null));
            livro.setDataPublicacao(padronizarData(root.path("publish_date").asText(null)));
            livro.setIsbn(root.path("isbn_13").findValue("0") != null ?
                    root.path("isbn_13").get(0).asText() :
                    root.path("isbn_10").get(0).asText(null));
            livro.setEditora(root.path("publishers").get(0).asText(null));

            if (root.has("authors") && root.get("authors").isArray() && !root.get("authors").isEmpty()) {
                String authorKey = root.get("authors").get(0).path("key").asText();
                livro.setAutor(getAutor(authorKey));
            }
            return livro;
        } catch (Exception e) {
            LOG.debugf("Erro ao criar Livro a partir do JSON");
            throw new RuntimeException("Erro ao criar Livro a partir do JSON");
        }
    }

    private String padronizarData(String dataOriginal) {
        System.out.println("Data original: " + dataOriginal);
        if (dataOriginal == null || dataOriginal.isEmpty()) {
            return null;
        }
        String[] formatosPossiveis = {
                "dd/MM/yyyy",
                "d/M/yyyy",
                "yyyy-MM-dd",
                "MMMM yyyy",
                "yyyy",
                "MMMM d, yyyy"
        };
        for (String formato : formatosPossiveis) {
            try {
                DateTimeFormatter parser = DateTimeFormatter.ofPattern(formato, Locale.ENGLISH);
                LocalDate data;
                if (formato.equals("yyyy")) {
                    data = LocalDate.of(Integer.parseInt(dataOriginal), 1, 1);
                } else if (formato.equals("MMMM yyyy")) {
                    data = LocalDate.parse(dataOriginal, parser).withDayOfMonth(1);
                } else {
                    data = LocalDate.parse(dataOriginal, parser);
                }
                return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
            }
        }
        System.out.println("Não foi possível padronizar a data: " + dataOriginal);
        return dataOriginal;
    }
}
