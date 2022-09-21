package com.usermanagement;

import com.usermanagement.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaRepositories
public class UserManagementApplication {

    public static void main(String[] args) {
          final Logger LOGGER = LogManager.getLogger(UserController.class);

          LOGGER.info("***** Service has started *****");

        SpringApplication.run(UserManagementApplication.class, args);

    }

}
