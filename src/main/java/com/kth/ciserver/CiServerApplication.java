package com.kth.ciserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is used to bootstrap and launch a Spring application for our CI-server
 *
 * @author Louis Cameron Booth
 * @author Aleks Petkov
 * @author Daniel Tsada Yosief
 * @author Linnea Hagman
 * @author Shotaro Ishii
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class CiServerApplication {

    /**
     * Main function for our CI-server
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(CiServerApplication.class, args);
    }
}
