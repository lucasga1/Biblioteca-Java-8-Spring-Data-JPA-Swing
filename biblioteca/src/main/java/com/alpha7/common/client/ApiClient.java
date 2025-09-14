package com.alpha7.common.client;

import com.alpha7.api.biblioteca.dto.LivroDTO;
import com.alpha7.common.dto.Resposta;
import com.alpha7.common.exception.CadastroLivroException;
import com.alpha7.common.exception.DeletarLivroException;
import com.alpha7.common.exception.OperacaoNaoPermitidaException;
import com.alpha7.view.util.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * Classe responsável por realizar chamadas à API da biblioteca.
 * <p>
 * Esta classe encapsula operações CRUD e consultas sobre livros, incluindo integração com a API externa OpenLibrary.org.
 * Todas as requisições são feitas via {@link RestTemplate}.
 * <p>
 * Funcionalidades:
 * <ul>
 *     <li>Buscar todos os livros com paginação;</li>
 *     <li>Buscar livros por filtros (ID, título, autor, ISBN, etc.) com paginação caso tenha mais de 20 resultados;</li>
 *     <li>Obter dados de livro na OpenLibrary através do ISBN;</li>
 *     <li>Cadastrar livros;</li>
 *     <li>Atualizar livros;</li>
 *     <li>Deletar livros;</li>
 *     <li>Upload de arquivos CSV para cadastro em massa.</li>
 * </ul>
 *
 * @author Lucas Gouvea Araujo
 */
public class ApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ApiClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Resposta<PageResponse<LivroDTO>> buscarTodos(int pagina, int tamanhoPagina) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/busca")
                    .queryParam("page", pagina)
                    .queryParam("size", tamanhoPagina)
                    .build()
                    .toUri();
            ResponseEntity<PageResponse<LivroDTO>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<LivroDTO>>() {
                    }
            );
            return new Resposta<>(response.getBody());
        } catch (HttpClientErrorException e) {
            return new Resposta<>(e.getMessage());
        }
    }

    public Resposta<PageResponse<LivroDTO>> buscarPorFiltro(int pagina, int tamanhoPagina, String parametro, String value, boolean isEditarLivro) {
        try {
            String url = baseUrl + "/filtro?" +
                    parametro + "=" +
                    value +
                    "&page=" + pagina +
                    "&size=" + tamanhoPagina +
                    "&isEditarLivro=" + isEditarLivro;
            ResponseEntity<PageResponse<LivroDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<PageResponse<LivroDTO>>() {
                    }
            );
            return new Resposta<>(response.getBody());
        } catch (HttpClientErrorException e) {
            return new Resposta<>(e.getMessage());
        }
    }

    public Resposta<LivroDTO> getLivroOpenLibrary(String isbn) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/openapi")
                    .queryParam("isbn", isbn)
                    .build()
                    .toUri();
            ResponseEntity<LivroDTO> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    LivroDTO.class);
            return new Resposta<>(response.getBody());
        } catch (HttpClientErrorException e) {
            return new Resposta<>(e.getMessage());
        }
    }

    public ResponseEntity<Void> cadastrarLivro(LivroDTO livro) {
        if (livro == null) {
            throw new CadastroLivroException("Livro deve estar preenchido.");
        }
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/cadastrar")
                    .build()
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LivroDTO> request = new HttpEntity<>(livro, headers);
            restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    request,
                    Void.class
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            throw new CadastroLivroException(e.getMessage());
        }
    }

    public Resposta<LivroDTO> atualizarLivro(Long id, LivroDTO livro) {
        if (id == null && livro == null) {
            throw new CadastroLivroException("Livro deve estar preenchido.");
        }
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/editar")
                    .queryParam("id=" + id)
                    .build()
                    .toUri();
            ResponseEntity<LivroDTO> response = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    new HttpEntity<>(livro),
                    LivroDTO.class);
            System.out.println("atualizou" + response.getBody());
            return new Resposta<>(response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("deu erro" + e.getMessage());
            return new Resposta<>(e.getMessage());
        }
    }

    public void cadastrarArquivo(File arquivo) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/upload/arquivo")
                    .build()
                    .toUri();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(arquivo));
            restTemplate.postForObject(uri, body, Void.class);
            ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            throw new OperacaoNaoPermitidaException(e.getMessage());
        }
    }

    public void deletar(List<LivroDTO> livros) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/deletar")
                    .build()
                    .toUri();
            HttpEntity<List<LivroDTO>> request = new HttpEntity<>(livros);
            restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    request,
                    Void.class);
        } catch (HttpClientErrorException e) {
            throw new DeletarLivroException(e.getMessage());
        }
    }
}