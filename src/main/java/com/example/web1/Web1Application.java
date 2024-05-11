package com.example.web1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
<<<<<<< HEAD

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})

=======

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
>>>>>>> 63c63fa1a917a034e4d661f70d3397e910c222d7
public class Web1Application {

	public static void main(String[] args) {
		SpringApplication.run(Web1Application.class, args);
	}

}
