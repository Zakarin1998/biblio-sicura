package com.example.biblioteca_sicura.security;

import com.example.biblioteca_sicura.jwt.JwtAuthenticationFilter;
import com.example.biblioteca_sicura.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura le regole di sicurezza qui
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()  // Permetti l'accesso libero agli endpoint di autenticazione e registrazione
            .antMatchers("/books/**").hasAnyRole("USER", "ADMIN") // Solo utenti autenticati possono vedere i libri
            .antMatchers("/ricerca/**").hasRole("ADMIN")  // Solo gli admin possono aggiungere, modificare o cancellare libri
            .anyRequest().authenticated();

        // Aggiungi il filtro JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Definisci il bean per AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Definisci un PasswordEncoder per la gestione delle password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Definisci UserDetailsService (puoi implementare un servizio personalizzato)
    @Bean
    public UserDetailsService userDetailsService() {
        // Implementa UserDetailsService o utilizza uno già esistente
        return new CustomUserDetailsService();
    }
}
