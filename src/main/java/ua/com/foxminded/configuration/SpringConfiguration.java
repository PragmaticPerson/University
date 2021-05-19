package ua.com.foxminded.configuration;

import java.util.Arrays;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@SpringBootConfiguration
@EntityScan("ua.com.foxminded.service.models")
@EnableJpaRepositories("ua.com.foxminded.dao.repository")
public class SpringConfiguration {
    @Bean
    public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("University API").version("1.0.0").description("Application to study new things")
                        .license(new License().name("Foxminded").url("https://foxminded.com.ua/"))
                        .contact(new Contact().name("Alex Chereshnev").email("aleksejceresnev43@gmail.com")))
                .servers(Arrays.asList(new Server().url("http://localhost:8080").description("Working server")));
    }
}
