package hello;

import com.sun.org.apache.xpath.internal.operations.Quo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final String url = "http://gturnquist-quoters.cfapps.io/api/random";

    public static void main(String[] args) {
         SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws  Exception {
        return args -> {
            Quote quote = restTemplate.getForObject(url, Quote.class);
            log.info(quote.toString());
        };
    }

    @Bean
    public  CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("==========lets inspect the beans proviced by Spring boot");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);

            Arrays.stream(beanNames).forEach(n -> System.out.println(n));

            System.out.println("==========lets inspect the beans proviced by Spring boot ends=========");


        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/greeting-javaconfig").allowedOrigins("http://localhost:9000");
            }
        };
    }

}
