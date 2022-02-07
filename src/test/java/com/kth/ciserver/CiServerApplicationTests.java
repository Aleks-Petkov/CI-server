package com.kth.ciserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CiServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void failTest() {
        boolean hello = true;
        assertTrue(hello);
    }
}
