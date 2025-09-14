package com.alpha7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BibliotecaApplicationTests {

    @Test
    void dummyTest() {
        // Teste simples sem carregar Spring Boot
        int soma = 2 + 2;
        assert soma == 4;
    }
}