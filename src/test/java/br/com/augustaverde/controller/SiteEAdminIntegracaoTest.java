package br.com.augustaverde.controller;

import br.com.augustaverde.repository.ContadorArvoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SiteEAdminIntegracaoTest {

    private static final String EMAIL_ADMIN = "sardenhacarlos@gmail.com";
    private static final String SENHA_DE_TESTE = "senha-de-teste";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ContadorArvoresRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void paginaInicialAbreSemLoginComAsQuatroSecoesNaOrdem() throws Exception {
        String html = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("nós plantamos árvores na região do Morro Branco")))
                .andExpect(content().string(containsString("https://chat.whatsapp.com/GnUGIHcwc33FY6ubSVK7zw")))
                .andReturn().getResponse().getContentAsString();

        int apresentacao = html.indexOf("id=\"apresentacao\"");
        int beneficios = html.indexOf("id=\"beneficios\"");
        int resultados = html.indexOf("id=\"resultados\"");
        int participe = html.indexOf("id=\"participe\"");
        org.assertj.core.api.Assertions.assertThat(apresentacao).isPositive();
        org.assertj.core.api.Assertions.assertThat(apresentacao).isLessThan(beneficios);
        org.assertj.core.api.Assertions.assertThat(beneficios).isLessThan(resultados);
        org.assertj.core.api.Assertions.assertThat(resultados).isLessThan(participe);
    }

    @Test
    void painelSemLoginMandaParaOLogin() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }

    @Test
    void atualizarSemLoginNaoMudaNada() throws Exception {
        mvc.perform(post("/admin/arvores").with(csrf()).param("quantidade", "50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));

        org.assertj.core.api.Assertions.assertThat(repository.count()).isZero();
    }

    @Test
    void loginComSenhaCertaEntra() throws Exception {
        mvc.perform(formLogin("/admin/login").userParameter("email").passwordParam("senha")
                        .user(EMAIL_ADMIN).password(SENHA_DE_TESTE))
                .andExpect(authenticated().withRoles("ADMIN"))
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    void loginComSenhaErradaEhRecusado() throws Exception {
        mvc.perform(formLogin("/admin/login").userParameter("email").passwordParam("senha")
                        .user(EMAIL_ADMIN).password("senha-errada"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/admin/login?erro"));
    }

    @Test
    void loginComOutroEmailEhRecusado() throws Exception {
        mvc.perform(formLogin("/admin/login").userParameter("email").passwordParam("senha")
                        .user("outra.pessoa@gmail.com").password(SENHA_DE_TESTE))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = EMAIL_ADMIN, roles = "ADMIN")
    void adminAtualizaENumeroECarbonoAparecemNoSite() throws Exception {
        mvc.perform(post("/admin/arvores").with(csrf()).param("quantidade", "1000"))
                .andExpect(redirectedUrl("/admin?salvo"));

        mvc.perform(get("/"))
                .andExpect(content().string(containsString("id=\"total-arvores\">1.000<")))
                .andExpect(content().string(containsString("id=\"co2-20-anos\">163,1<")))
                .andExpect(content().string(containsString("id=\"co2-por-ano\">8.157<")));
    }

    @Test
    @WithMockUser(username = EMAIL_ADMIN, roles = "ADMIN")
    void numeroNegativoEhRecusado() throws Exception {
        mvc.perform(post("/admin/arvores").with(csrf()).param("quantidade", "-5"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("não pode ser negativo")));

        org.assertj.core.api.Assertions.assertThat(repository.count()).isZero();
    }

    @Test
    void naoExisteRotaDeCadastroDeAdmin() throws Exception {
        mvc.perform(get("/admin/cadastro")).andExpect(status().is3xxRedirection());
        mvc.perform(post("/admin/usuarios").with(csrf())).andExpect(status().is3xxRedirection());
    }
}
