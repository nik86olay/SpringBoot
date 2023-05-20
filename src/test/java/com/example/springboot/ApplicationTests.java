package com.example.springboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;
    private static final GenericContainer<?> myapp1 = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);
    private static GenericContainer<?> myapp2 = new GenericContainer<>("devapp")
            .withExposedPorts(8080);
    @BeforeEach
    public void setUp() {
        myapp1.start();
        myapp2.start();
    }
    @Test
    void contextLoads() {
        Integer myapp1Port = myapp1.getMappedPort(8081);
        Integer myapp2Port = myapp2.getMappedPort(8080);
        System.out.println(myapp1Port);
        System.out.println(myapp2Port);

        ResponseEntity<String> forEntity1 = restTemplate.getForEntity("http://localhost:" + myapp1Port + "/profile", String.class);
        ResponseEntity<String> forEntity2 = restTemplate.getForEntity("http://localhost:" + myapp2Port + "/profile", String.class);

        Assertions.assertEquals("Current profile is production", forEntity1.getBody());
        Assertions.assertEquals("Current profile is dev", forEntity2.getBody());
    }
}
