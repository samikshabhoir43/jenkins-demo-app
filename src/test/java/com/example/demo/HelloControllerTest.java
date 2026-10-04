package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloControllerTest {

    @Test
    void helloTest() {

        HelloController controller = new HelloController();

        String response = controller.hello();

        assertEquals("Hello from Jenkins CI/CD!", response);
    }
}
