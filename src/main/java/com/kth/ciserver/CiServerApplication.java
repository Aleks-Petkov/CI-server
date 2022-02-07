package com.kth.ciserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiServerApplication.class, args);
    }

    @GetMapping("/ping")
    public String hello() {
        return "Pong!";
    }

    @PostMapping("/ci")
    public String handleGithubWebhook(@RequestBody GithubWebhookRequest request) {
        System.out.println(request.getRepository().getHtml_url());
        return "Ok!";
    }
}
