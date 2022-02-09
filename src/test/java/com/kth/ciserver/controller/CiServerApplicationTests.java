package com.kth.ciserver.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CiServerApplicationTests {

    private static final String COMMIT_ID = "111112222233333";
    private static final String STATUSES_URL = "www.githubstatuses.com";

    CiController ciController = new CiController();

    @Test
    void handleGithubWebhookOk() throws IOException {
        CiController ciControllerNew = Mockito.spy(ciController);
        GithubWebhookRequest request = createTestRequest();

        Mockito.doReturn(true).when(ciControllerNew).pullAndBuildApplication(request);
        Mockito.doNothing().when(ciControllerNew).updateGithubCommitStatus(true, request.getHeadCommit().getId(), request.getRepository().getStatusesUrl());

        String output = ciControllerNew.handleGithubWebhook(request);

        assertEquals("Ok!", output);
    }

    @Test
    void handleGithubWebhookReturns500WhenErrorThrows() throws IOException {
        CiController ciControllerNew = Mockito.spy(ciController);
        GithubWebhookRequest request = createTestRequest();

        Mockito.doThrow(IOException.class).when(ciControllerNew).pullAndBuildApplication(request);

        assertThrows(IOException.class, () -> {
            ciControllerNew.handleGithubWebhook(request);
        });
    }

    private GithubWebhookRequest createTestRequest() {
        GithubWebhookRequest req = new GithubWebhookRequest();
        GithubWebhookRequest.HeadCommit hc = req.new HeadCommit();
        GithubWebhookRequest.RepositoryObject ro = req.new RepositoryObject();
        GithubWebhookRequest.HeadCommit.Committer c = hc.new Committer();
        c.setEmail("@");
        c.setName("a");
        hc.setId(COMMIT_ID);
        hc.setTimestamp("00:00:00");
        hc.setCommitter(c);
        ro.setHtmlUrl("https://");
        ro.setStatusesUrl(STATUSES_URL);
        req.setHeadCommit(hc);
        req.setRepository(ro);
        return req;
    }
}
