package com.example.web1.config;

import com.example.web1.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers("/login", "/css/**").permitAll()
          .anyRequest().authenticated()
      )
      .formLogin(formLogin ->
        formLogin
          .loginPage("/login")
          .defaultSuccessUrl("/menu", true)
          .permitAll()
      )
      .logout(logout ->
        logout
          .logoutUrl("/logout")
          .logoutSuccessUrl("/login")
          .permitAll()
      );
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
