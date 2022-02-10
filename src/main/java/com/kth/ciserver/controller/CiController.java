package com.kth.ciserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CiController {
  
    public static final String COMMIT_STATE_SUCCESS = "success";
    public static final String COMMIT_STATE_FAILURE = "failure";
    public static final String BUILD_HISTORY_FILE_PATH = "BuildHistory.html";

    File buildHistory = new File(BUILD_HISTORY_FILE_PATH);

    @Value("${github.username}")
    private String GITHUB_USERNAME;

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    private final RestTemplate restTemplate;

    public CiController() {
        restTemplate = new RestTemplate();
    }

    @GetMapping()
    public String handleHomepage() throws IOException {
        Path fileName = Path.of(BUILD_HISTORY_FILE_PATH);
        return Files.readString(fileName);
    }

    @PostMapping("/ci")
    public String handleGithubWebhook(@RequestBody GithubWebhookRequest request) throws IOException {
        System.out.println("Received post request!");
        System.out.println(request.toString());
        boolean buildSuccessful = pullAndBuildApplication(request);
        writeToFile(buildSuccessful, request.toHtml(), buildHistory);
        updateGithubCommitStatus(buildSuccessful,
                request.getHeadCommit().getId(),
                request.getRepository().getStatusesUrl());
        return "Ok!";
    }

    boolean pullAndBuildApplication(GithubWebhookRequest request) throws IOException {
        executeAndPrintCommand("git pull");
        executeAndPrintCommand(String.format("git checkout %s", request.getHeadCommit().getId()));
        boolean testsSuccessful;
        try {
            testsSuccessful = executeAndPrintCommand("./gradlew clean build");
        }
        catch(IOException e){
            testsSuccessful = executeAndPrintCommand("gradlew.bat clean build");
        }
        executeAndPrintCommand("git checkout main");
        System.out.println("Tests successful: " + testsSuccessful); 
        return testsSuccessful;
    }

    public void writeToFile(boolean success, String requestString, File file){
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(requestString);
            if (success)
                writer.write("<h3 style='color:green'> TESTS SUCCESSFUL </h3> <br>");
            else
                writer.write("<h3 style='color:red'> TESTS FAILED </h3> <br>");
            writer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }

    void updateGithubCommitStatus(boolean testsSuccessful, String commitId, String statusesUrl) {
        String commitState;

        if (testsSuccessful)
            commitState = COMMIT_STATE_SUCCESS;
        else
            commitState = COMMIT_STATE_FAILURE;

        String url = statusesUrl.replace("{sha}", commitId);

        HttpStatus httpStatus = postRequest(url, commitState);
        System.out.println("Http status for post to Github " + httpStatus);
    }

    HttpStatus postRequest(String url, String commitState) {
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
    private boolean executeAndPrintCommand(String cmd) throws IOException {
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
