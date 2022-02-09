package com.kth.ciserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.ArrayList;

@RestController
public class CiController {

    @GetMapping()
    public String handleHomepage() {
        return "Welcome to our CI server!";
    }

    @PostMapping("/ci")
    public String handleGithubWebhook(@RequestBody GithubWebhookRequest request) throws IOException {
        System.out.println("Received post request!");
        System.out.println(request.toString());
        pullAndBuildApplication(request);
        return "Ok!";
    }

    private void pullAndBuildApplication(GithubWebhookRequest request) throws IOException {
        executeAndPrintCommand("git pull");
        executeAndPrintCommand(String.format("git checkout %s", request.getHead_commit().getId()));
        Boolean testsSuccessful;
        try{
            testsSuccessful = executeAndPrintCommand("./gradlew clean build");
        }
        catch(IOException e){
            testsSuccessful = executeAndPrintCommand("gradlew.bat clean build");
        }
        executeAndPrintCommand("git checkout main");
        System.out.println("Tests successful: " + testsSuccessful); 
        writeToFile(testsSuccessful, request);
    }

    private void writeToFile(Boolean success, GithubWebhookRequest request){
        try {
            FileWriter writer = new FileWriter("BuildHistory.txt", true);
            writer.write(request.toString());
            if (success)
                writer.write("TESTS SUCCESSFUL\n");
            else
                writer.write("TESTS FAILED\n");
            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
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
