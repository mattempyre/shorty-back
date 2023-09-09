package com.mattfogz.shortyback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

// Mark this class as the starting point of a Spring Boot application
@SpringBootApplication
// Enable Redis repositories. This annotation will scan for interfaces that extend one of Redis' repositories and register them
@EnableRedisRepositories
public class ShortyBackApplication {

    /**
     * The entry point of the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Launches the Spring application, creating the ApplicationContext and 
        // starting all the services, controllers, repositories, etc.
        SpringApplication.run(ShortyBackApplication.class, args);
    }
}