package com.example.web1;

<<<<<<< HEAD

=======
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Web1Application {
<<<<<<< HEAD
  public static void main(String[] args) {
    SpringApplication.run(Web1Application.class, args);
  }
=======

  public static void main(String[] args) {
    SpringApplication.run(Web1Application.class, args);
  }

>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
}
