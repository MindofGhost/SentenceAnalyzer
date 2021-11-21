package ru.maistudents.backendsentenceanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@Configuration
public class BackendSentenceAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSentenceAnalyzerApplication.class, args);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(2048));
        factory.setMaxRequestSize(DataSize.ofKilobytes(2048));
        return factory.createMultipartConfig();
    }
}
