package com.example.unittestwithcontainer.config.rest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@Configuration
public class TestRestConfiguration {
    @Bean
    public TestRestTemplate testRestTemplate(){
        return new TestRestTemplate();
    }

    @Bean
    RequestSpecification requestSpecification() {
        return RestAssured.given()
                          .when()
                          .header("Content-Type", "application/json");
    }

}
