package br.com.augustaverde.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Login do admin. Existe um único admin, fixo, e não há cadastro de outros: ele mora só em memória,
 * com o e-mail e o hash BCrypt da senha vindos do application.properties.
 */
@Configuration
public class SegurancaConfig {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(regras -> regras
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/admin", true)
                        .failureUrl("/admin/login?erro")
                        .permitAll())
                .logout(sair -> sair
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    UserDetailsService admin(@Value("${admin.email}") String email,
                             @Value("${admin.senha-hash}") String senhaHash) {
        return new InMemoryUserDetailsManager(User.withUsername(email)
                .password(senhaHash)
                .roles("ADMIN")
                .build());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
