package com.example.web1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers("/survey/subjectiveSurveyAnswer/**","/survey/objectiveSurveyAnswer/**",
            "/quiz/subjectiveQuiz/**","/quiz/objectiveQuiz/**", "/download/**").permitAll()  // QR 코드를 통해 접근할 수 있는 URL
          .requestMatchers("/", "/home", "/login", "/css/**", "/js/**").permitAll()  // 공용 페이지 및 리소스에 대한 접근 허용
          .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 요구
      )
      .formLogin(formLogin ->
        formLogin
          .loginPage("/login")  // 사용자 정의 로그인 페이지 설정
          .permitAll()  // 로그인 페이지 접근 허용
      )
      .logout(logout ->
        logout
          .permitAll()  // 로그아웃 URL 접근 허용
      )
      .csrf(csrf -> csrf
        .ignoringRequestMatchers("/survey/subjectiveSurveyAnswer/**","/survey/objectiveSurveyAnswer/**",
          "/quiz/subjectiveQuiz/**","/quiz/objectiveQuiz/**", "/download/**")  // 특정 요청에 대해 CSRF 보호 비활성화
      );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
