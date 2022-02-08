package com.kth.ciserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;

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
        executeAndPrintCommand("git branch");
        executeAndPrintCommand("git checkout main");
        executeAndPrintCommand("git branch");
    }

    private void executeAndPrintCommand(String cmd) {
        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec(cmd);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
