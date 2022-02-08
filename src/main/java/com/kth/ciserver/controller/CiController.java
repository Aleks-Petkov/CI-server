package com.kth.ciserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.ArrayList;

@RestController
public class CiController {
    //File buildHistory;


    @GetMapping()
    public String handleHomepage() {
        return "Welcome to our CI server!";
    }

    @PostMapping("/ci")
    public String handleGithubWebhook(@RequestBody GithubWebhookRequest request) {
        System.out.println("Received post request!");
        request.printRequest();
        pullAndBuildApplication(request);
        return "Ok!";
    }

    private void pullAndBuildApplication(GithubWebhookRequest request) {
        executeAndPrintCommand("git pull");
        executeAndPrintCommand(String.format("git checkout %s", request.getHead_commit().getId()));
        Boolean testsSuccessful = executeAndPrintCommand("./gradlew clean build");
        System.out.println("Tests successful: " + testsSuccessful); // this should instead be printed to file for consistent storage
    }

    
    /** 
     * @param cmd Command to be executed and printed
     * @return True if the second word in the second to last line of the standard output 
     * is "SUCCESSFUL" (indicating a successful test run), else false.
     */
    private Boolean executeAndPrintCommand(String cmd) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec(cmd);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (lines.size() < 2 || lines.get(lines.size()-2).split(" ").length < 2) 
            return false;
        return lines.get(lines.size()-2).split(" ")[1].equals("SUCCESSFUL");
    }
}
