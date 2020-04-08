package br.com.sistemavendas;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class VendasApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(VendasApplication.class,args);
    }

}
