package com.kth.ciserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CiController {

    public static final String COMMIT_STATE_SUCCESS = "success";
    public static final String COMMIT_STATE_FAILURE = "failure";
    @Value("${github.username}")
    private String GITHUB_USERNAME;

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    private final RestTemplate restTemplate;

    public CiController() {
        restTemplate = new RestTemplate();
    }

    @GetMapping()
    public String handleHomepage() {
        return "Welcome to our CI server!";
    }

    @PostMapping("/ci")
    public String handleGithubWebhook(@RequestBody GithubWebhookRequest request) throws IOException {
        System.out.println("Received post request!");
        request.printRequest();
        pullAndBuildApplication(request);
        return "Ok!";
    }

    private void pullAndBuildApplication(GithubWebhookRequest request) throws IOException {
        executeAndPrintCommand("git pull");
        executeAndPrintCommand(String.format("git checkout %s", request.getHeadCommit().getId()));
        Boolean testsSuccessful = executeAndPrintCommand("./gradlew clean build");
        executeAndPrintCommand("git checkout main");
        System.out.println("Tests successful: " + testsSuccessful); // this should instead be printed to file for consistent storage

        updateGithubCommitStatus(testsSuccessful, request.getHeadCommit().getId(), request.getRepository().getStatusesUrl());
    }

    private void updateGithubCommitStatus(Boolean testsSuccessful, String commitId, String statusesUrl) {
        String commitState;

        if (testsSuccessful)
            commitState = COMMIT_STATE_SUCCESS;
        else
            commitState = COMMIT_STATE_FAILURE;

        String url = statusesUrl.replace("{sha}", commitId);

        HttpStatus httpStatus = postRequest(url, commitState);
        System.out.println("Http status for post to Github " + httpStatus);
    }

    private HttpStatus postRequest(String url, String commitState) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(GITHUB_USERNAME, GITHUB_TOKEN);

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("state", commitState);
        map.put("description", "Result from CI server.");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getStatusCode();
    }

    /** 
     * @param cmd Command to be executed and printed
     * @return True if the second word in the second to last line of the standard output 
     * is "SUCCESSFUL" (indicating a successful test run), else false.
     */
    private Boolean executeAndPrintCommand(String cmd) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(cmd);
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
            lines.add(line);
        }
    
        if (lines.size() < 2 || lines.get(lines.size()-2).split(" ").length < 2) 
            return false;
        return lines.get(lines.size()-2).split(" ")[1].equals("SUCCESSFUL");
    }
}
