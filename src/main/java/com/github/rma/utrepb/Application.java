package com.github.rma.utrepb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
public class Application {

    public static final String BASIC_AUTH_USERNAME = "user";
    public static final String BASIC_AUTH_PASSWORD = "password";

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web -> web.ignoring().mvcMatchers("/unsecure", "/unsecure-error"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(Environment environment, HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(withDefaults());

        if (environment.getRequiredProperty("authorize-http-requests", Boolean.class)) {
            httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        } else {
            httpSecurity.authorizeRequests().anyRequest().authenticated().and();
        }

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(BASIC_AUTH_USERNAME)
                .password(BASIC_AUTH_PASSWORD)
                .authorities(Collections.emptyList())
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
