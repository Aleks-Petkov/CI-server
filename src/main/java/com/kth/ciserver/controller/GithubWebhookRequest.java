package com.kth.ciserver.controller;

public class GithubWebhookRequest {
    RepositoryObject repository;

    HeadCommit head_commit;

    public class RepositoryObject {
        String html_url;

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }
    }

    public class HeadCommit {
        String id;
        String timestamp;
        Committer committer;

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

    public HeadCommit getHead_commit() {
        return head_commit;
    }

    public void setHead_commit(HeadCommit head_commit) {
        this.head_commit = head_commit;
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
