package com.alpha7.common.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Classe de configuracao da aplicacao. Responsavel por iniciar os Beans no momento da execucao.
 * @author Lucas Gouvea Araujo
 * */
@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
