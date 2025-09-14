package com.alpha7.common.exception;

import com.alpha7.common.dto.ErroCampo;
import com.alpha7.common.dto.ErroResposta;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controle daa excessoes personalizadas do sistema.
 * @author Lucas Gouvea Araujo
 * */
@RestControllerAdvice //capturar exceptions
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors(); //pega os campos que deram erros na validacao
        List<ErroCampo> listaErros = fieldErrors //mapea cada campo de acordo com o dto criado
                .stream()
                .map(fieldError -> new ErroCampo(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação dos campos",
                listaErros);
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        return ErroResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(BuscarLivroException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleBuscarLivroException(BuscarLivroException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(CadastroLivroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleCadastroLivroException(CadastroLivroException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(ConversaoInputsException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ErroResposta handleConversaoInputsException(ConversaoInputsException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(OpenLibraryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleOpenLibraryException(OpenLibraryException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(AtualizarLivroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleAtualizarLivroException(AtualizarLivroException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(DeletarLivroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleDeletarLivroException(DeletarLivroException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(CriacaoDTOException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ErroResposta handleCriacaoDTOException(CriacaoDTOException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(ParametrosBuscaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleParametrosBuscaException(ParametrosBuscaException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }

    @ExceptionHandler(CadastroLivroViaCSVException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleCadastroLivroViaCSVException(CadastroLivroViaCSVException e) {
        return ErroResposta.respostaPadrao((e.getMessage()));
    }
}
