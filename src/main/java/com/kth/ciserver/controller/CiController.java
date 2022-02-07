package com.kth.ciserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    }
}
