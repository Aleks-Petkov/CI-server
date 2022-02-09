package com.kth.ciserver.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GithubWebhookRequest {
    RepositoryObject repository;

    HeadCommit headCommit;

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class RepositoryObject {
        String htmlUrl;
        String statusesUrl;

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String html_url) {
            this.htmlUrl = html_url;
        }

        public String getStatusesUrl() {
            return statusesUrl;
        }

        public void setStatusesUrl(String statuses_url) {
            this.statusesUrl = statuses_url;
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class HeadCommit {
        String id;
        String timestamp;
        Committer committer;

        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public class Committer {
            String name;
            String email;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Committer getCommitter() {
            return committer;
        }

        public void setCommitter(Committer committer) {
            this.committer = committer;
        }
    }

    public HeadCommit getHeadCommit() {
        return headCommit;
    }

    public void setHeadCommit(HeadCommit head_commit) {
        this.headCommit = head_commit;
    }

    public RepositoryObject getRepository() {
        return repository;
    }

    public void setRepository(RepositoryObject repository) {
        this.repository = repository;
    }
  
    public String toString() {
        StringBuilder builder= new StringBuilder();
        builder.append("\nURL: " + this.getRepository().getHtml_url());
        builder.append("\nCommit id: " + this.getHead_commit().getId());
        builder.append("\nTimestamp: " + this.getHead_commit().getTimestamp());
        builder.append("\nAuthor name: " + this.getHead_commit().getCommitter().getName());
        builder.append("\nAuthor email: " + this.getHead_commit().getCommitter().getEmail()+"\n");
        return builder.toString();

    }
}
