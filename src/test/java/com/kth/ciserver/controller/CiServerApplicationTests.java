package com.kth.ciserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CiController.class)
class CiServerApplicationTests {

    private static final String COMMIT_ID = "111112222233333";
    private static final String STATUSES_URL = "www.githubstatuses.com";

    CiController ciController = new CiController();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiController mockCiC;

    @Mock
    private RestTemplate mockRestTemplate;

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

    @Test
    void handleGithubWebhookCorrectlyHandlesPostRequest() throws Exception {
        // the function expects a POST requests sent to "/ci", containing a json object
        // that partially reflects the structure of the GithubWebhookRequest class.
        ObjectMapper objectMapper = new ObjectMapper();
        GithubWebhookRequest request = createTestRequest();

        doReturn(true).when(mockCiC).pullAndBuildApplication(request);
        doNothing().when(mockCiC).updateGithubCommitStatus(true, request.getHeadCommit().getId(), request.getRepository().getStatusesUrl());

        mockMvc.perform(post("/ci")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void test_handleGithubWebhookCorrectlyHandlesGetRequest() throws Exception {
        // the function expects a POST requests sent to "/ci", not GET requests
        mockMvc.perform(get("/ci"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void postRequestHttpStatusOK() {
        CiController ciControllerNew = Mockito.spy(ciController);

        doReturn(new ResponseEntity<>("response", HttpStatus.OK)).when(mockRestTemplate).postForEntity(anyString(), any(), any());
        Mockito.doReturn(HttpStatus.OK).when(ciControllerNew).postRequest(any(), any());

        assertEquals(ciControllerNew.postRequest("https://", "success"), HttpStatus.OK);
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
