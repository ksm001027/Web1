package com.example.web1;

import com.example.web1.model.User;
import com.example.web1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Web1Application {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(Web1Application.class, args);
  }

  @Bean
  public CommandLineRunner init() {
    return args -> {
      if (!userRepository.findByUserID("user1").isPresent()) {
        User user = new User();
        user.setUserID("user1");
        user.setPassword(passwordEncoder.encode("0001"));
        userRepository.save(user);
      }
    };
  }
}
