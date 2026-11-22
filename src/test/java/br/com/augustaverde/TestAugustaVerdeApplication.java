package br.com.augustaverde;

import org.springframework.boot.SpringApplication;

/**
 * Sobe o site com o banco H2 em memória e o admin de teste (senha "senha-de-teste"),
 * para ver as telas sem instalar o PostgreSQL: ./mvnw spring-boot:test-run
 */
public class TestAugustaVerdeApplication {

    public static void main(String[] args) {
        SpringApplication.from(AugustaVerdeApplication::main).run(args);
    }
}
