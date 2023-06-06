package pl.lmaslowski.ddd.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;

@SpringBootApplication
public class SpringBootBankApp {
    
    private static final Logger log = LoggerFactory.getLogger(SpringBootBankApp.class);
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBankApp.class, args);
    }
    
    @Bean
    public CommandLineRunner commandLineRunnerInfo(Environment env) {
        return args -> {
            String protocol = "http";
            
            log.info("\n----------------------------------------------------------\n\t" +
                             "Application: '{}' is running! Access URLs:\n\t" +
                             "Local: \t\t{}://localhost:{}\n\t" +
                             "Swagger:\t{}://localhost:{}/swagger-ui.html\n\t" +
                             "External: \t{}://{}:{}\n\t" +
                             "Profile(s): \t{}\n----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    protocol,
                    env.getProperty("server.port"),
                    protocol,
                    env.getProperty("server.port"),
                    protocol,
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    env.getActiveProfiles());
        };
    }
    
    @Configuration
    @EnableSwagger2
    @Profile("production")
    static public class SwaggerConfiguration {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                           .select()
                           .apis(RequestHandlerSelectors.any())
                           .paths(PathSelectors.any())
                           .build();
        }
    }
}
