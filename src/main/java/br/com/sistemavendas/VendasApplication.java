package br.com.sistemavendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
//@ComponentScan({"sistemavendas"})
public class VendasApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(VendasApplication.class,args);
    }

    @GetMapping("/")
    public String getInitial() {
        return "Servidor executando";
    }
}
