package com.kth.ciserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.*;

import com.kth.ciserver.controller.CiController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


@SpringBootTest
class CiServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void succeedingTest() {
        assertTrue(true);
    }

    @Test
    void writeToFileTestSuccessfull() throws IOException{
        File testFile = new File("testFile.txt");
        Path fileName = Path.of("testFile.txt");
        String testString = "This is a test ";
        CiController.writeToFile(true, testString, testFile);
        assertTrue (Files.readString(fileName).contains( "TESTS SUCCESSFUL"));
        PrintWriter printWriter = new PrintWriter("testFile.txt");
        printWriter.close();
    }

    @Test
    void writeToFileTest_Fail() throws IOException{
        File testFile = new File("testFile.txt");
        Path fileName = Path.of("testFile.txt");
        String testString = "This is a test ";
        CiController.writeToFile(false, testString, testFile);
        assertTrue (Files.readString(fileName).contains("TESTS FAILED"));
        PrintWriter printWriter = new PrintWriter("testFile.txt");
        printWriter.close();
    }

}

