package com.example.unittestwithcontainer;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.example.unittestwithcontainer.api.SimpleEntityApi;
import com.example.unittestwithcontainer.config.database.TestDataSourceConfig;
import com.example.unittestwithcontainer.config.rest.TestRestConfiguration;
import com.example.unittestwithcontainer.dto.SimpleEntity;
import com.example.unittestwithcontainer.request.SimpleEntityCreateRequest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class, TestDataSourceConfig.class, TestRestConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApiTestWithRetrofit {

    Retrofit retrofit;

    private int port;
    private String baseUrl;

    @Autowired
    WebApplicationContext context;

    SimpleEntityApi simpleEntityApi;

    @Before
    public void setup() {
        port = Integer.parseInt(context.getEnvironment().getProperty("local.server.port"));
        baseUrl = String.format("http://localhost:%d", port);
        System.out.println("baseUrl: " + baseUrl);

        retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

        simpleEntityApi = retrofit.create(SimpleEntityApi.class);
    }

    @Test
    public void testGetSimpleEntity() throws Exception {
        String name = "retrofit test";
        SimpleEntityCreateRequest request = new SimpleEntityCreateRequest(name);
        Call<SimpleEntity> saveSimpleEntityCall = simpleEntityApi.saveSimpleEntity(request);
        Response<SimpleEntity> savedSimpleEntityRes = saveSimpleEntityCall.execute();
        SimpleEntity savedSimpleEntity = savedSimpleEntityRes.body();

        assertThat("Saving simple entity is not null", savedSimpleEntity, is(notNullValue()));
        assertThat("Saved simple entity has same name as requested", savedSimpleEntity.getName(), is(name));

        Call<List<SimpleEntity>> listCall = simpleEntityApi.simpleEntityList();
        Response<List<SimpleEntity>> listResponse = listCall.execute();
        List<SimpleEntity> simpleEntities = listResponse.body();
        assertThat("Saved simple entity has same name as requested",
                   simpleEntities
                           .stream()
                           .anyMatch(s -> s.getName().equals(name)),
                   is(true));
    }
}
