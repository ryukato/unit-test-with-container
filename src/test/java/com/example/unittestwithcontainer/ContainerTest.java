package com.example.unittestwithcontainer;

import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import javax.naming.ldap.PagedResultsControl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.example.unittestwithcontainer.config.database.TestDataSourceConfig;
import com.example.unittestwithcontainer.config.rest.TestRestConfiguration;
import com.example.unittestwithcontainer.dto.SimpleEntity;
import com.example.unittestwithcontainer.request.SimpleEntityCreateRequest;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, TestDataSourceConfig.class, TestRestConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContainerTest {

    private RequestSpecification requestSpecification;

    private int port;
    private String baseUrl;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setUp() {
        System.out.println("port: " + context.getEnvironment().getProperty("local.server.port"));
        port = Integer.parseInt(context.getEnvironment().getProperty("local.server.port"));
        baseUrl = String.format("http://localhost:%d", port);
        context.getEnvironment().getProperty("server.ports");
        requestSpecification = RestAssured.given()
                                          .log()
                                          .all()
                                          .when()
                                          .header("Content-Type", "application/json");;
    }



    @Test
    public void testSaveSimpleEntity() throws Exception {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("name", "test");

        SimpleEntityCreateRequest simpleEntityCreateRequest = new SimpleEntityCreateRequest("test");

        requestSpecification
//                .body(reqParams)
                .body(simpleEntityCreateRequest)
                .post(baseUrl+"/simple-entity")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("test")).body("id", is(1));

        requestSpecification.get(baseUrl+"/simple-entity")
                            .then()
                            .log().all()
                            .body("[0].id", is(1))
                            .body("[0].name", is("test"))
                            .statusCode(200);
    }
}
