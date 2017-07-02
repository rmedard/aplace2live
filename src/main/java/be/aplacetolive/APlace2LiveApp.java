package be.aplacetolive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by Medard on 12/05/2017.
 */

@SpringBootApplication
public class APlace2LiveApp extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(APlace2LiveApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(APlace2LiveApp.class).web(false);
    }
}
