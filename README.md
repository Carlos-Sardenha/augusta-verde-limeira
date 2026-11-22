# Augusta Verde Limeira

Site institucional do grupo que planta árvores na região do Morro Branco, em Limeira (SP).
Uma página inicial de rolagem única (apresentação, benefícios das árvores, o que já atingimos e convite para o
grupo do WhatsApp) e uma área de admin para atualizar quantas árvores já foram plantadas.

## Stack

A mesma base do back do Labs (`core-api`) da wehandle, só com o que o site usa:

- Java 21 e Spring Boot 3.3, com Maven (wrapper `mvnw` incluso)
- Spring Web + Thymeleaf (MVC com as telas geradas no servidor)
- Bean Validation no formulário do admin
- Spring Data JPA com PostgreSQL (H2 em memória nos testes)
- Spring Security com login por formulário e sessão
- Lombok
- JUnit 5 e ArchUnit (regras de camada do MVC)

## Organização (MVC)

```
br.com.augustaverde
├── model        ContadorArvores (entidade) e ImpactoCarbono
├── repository   ContadorArvoresRepository (JPA)
├── service      ArvoresService e CalculadoraCarbono (regras)
├── controller   SiteController (página pública) e AdminController (área do admin)
└── config       segurança e relógio
templates/       index.html, admin/login.html, admin/painel.html (as views)
```

O `CamadasTest` (ArchUnit) faz o build falhar se um controller acessar o repository direto, ou se o model
depender de alguém.

## Pré-requisitos

- **JDK 21 ou mais novo.** O Maven usa o JDK do `JAVA_HOME`. Se ele apontar para um JDK 17, a compilação
  falha com `release version 21 not supported`. Nesse caso, aponte para um JDK novo só naquele terminal:

  ```powershell
  $env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.4.1"
  ```

- **PostgreSQL** (só para rodar o site de verdade; os testes não precisam).

## Subir o PostgreSQL local

Com o PostgreSQL instalado, crie o usuário e o banco que o projeto espera:

```powershell
psql -U postgres -c "CREATE USER augusta WITH PASSWORD 'augusta';"
psql -U postgres -c "CREATE DATABASE augusta_verde OWNER augusta;"
```

Ou, com Docker:

```powershell
docker run --name augusta-verde-db -e POSTGRES_USER=augusta -e POSTGRES_PASSWORD=augusta -e POSTGRES_DB=augusta_verde -p 5432:5432 -d postgres:16
```

A tabela é criada sozinha na primeira subida (`ddl-auto=update`). Para usar outro banco, passe as variáveis
`DB_URL`, `DB_USER` e `DB_PASSWORD`.

## Rodar o projeto

```powershell
.\mvnw.cmd spring-boot:run
```

Site em http://localhost:8080.

**Sem PostgreSQL**, para só ver as telas: `.\mvnw.cmd spring-boot:test-run`. Esse modo usa o H2 em memória e o
admin de teste (senha `senha-de-teste`). O número de árvores some quando o app para.

## Rodar os testes

```powershell
.\mvnw.cmd test
```

Cobrem o cálculo de carbono, a página pública (as 4 seções na ordem e o link do WhatsApp), o login do admin
(senha certa, senha errada, outro e-mail), o acesso sem login, a validação de número negativo e as regras de
camada do ArchUnit.

## Área do admin

- URL: http://localhost:8080/admin (sem login, manda para `/admin/login`)
- Existe **um único admin, fixo**: `sardenhacarlos@gmail.com`. Não há cadastro de outros admins.
- A senha **não fica no repositório**. O `application.properties` guarda só o hash BCrypt dela (`admin.senha-hash`),
  que não permite descobrir a senha.
- Para trocar a senha, gere o hash BCrypt da senha nova e passe pela variável `ADMIN_SENHA_HASH` (ou troque o valor
  padrão no `application.properties`).

## Cálculo do carbono

Cada árvore nativa da Mata Atlântica absorve em média **163,14 kg de CO2 nos primeiros 20 anos**. Fonte: estudo do
Instituto Totum com a ESALQ/USP e a Fundação SOS Mata Atlântica (2013), feito em plantios de restauração
([clipping da ESALQ](https://www.esalq.usp.br/acom/clipping_semanal/2013/3marco/23_a_29/files/assets/downloads/page0013.pdf)).

O site mostra:

- toneladas de CO2 em 20 anos = árvores × 163,14 ÷ 1000
- kg de CO2 por ano, em média = árvores × 163,14 ÷ 20

É uma estimativa: outros estudos da mesma linha chegam a valores entre 140 e 250 kg por árvore em 20 anos.
O fator fica em `CalculadoraCarbono.KG_CO2_POR_ARVORE_EM_20_ANOS`.
