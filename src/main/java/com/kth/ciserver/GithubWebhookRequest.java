package com.kth.ciserver;

public class GithubWebhookRequest {
    RepositoryObject repository;

    public class RepositoryObject {
        String html_url;

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }
    }

    public RepositoryObject getRepository() {
        return repository;
    }

    public void setRepository(RepositoryObject repository) {
        this.repository = repository;
    }
}
