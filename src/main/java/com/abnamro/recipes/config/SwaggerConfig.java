package com.abnamro.recipes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Configuration class for Swagger Implementation
 *
 * @author Ragesh Sharma
 */
@PropertySource("classpath:swagger.yaml")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.abnamro.recipes.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Recipe Service",
                "Documentation of the Recipe service which allows users to manage their favourite recipes.",
                "1.0.0",
                "Terms of service for using Recipe service application",
                ApiInfo.DEFAULT_CONTACT,
                "MIT Licence",
                "http://opensource.org/licences/MIT",
                Collections.emptyList());
    }
}
