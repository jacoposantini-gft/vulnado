package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VulnadoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() {
        // Arrange
        String expectedMessage = Cowsay.run("I love Linux!");

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay", String.class);

        // Assert
        assertEquals("Default input should return the default message", expectedMessage, response.getBody());
    }

    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() {
        // Arrange
        String customInput = "Hello, World!";
        String expectedMessage = Cowsay.run(customInput);

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=" + customInput, String.class);

        // Assert
        assertEquals("Custom input should return the custom message", expectedMessage, response.getBody());
    }
}
