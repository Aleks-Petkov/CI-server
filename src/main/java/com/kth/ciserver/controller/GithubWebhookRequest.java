package com.kth.ciserver.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Class for GitHub webhook request
 *
 * @author Louis Cameron Booth
 * @author Aleks Petkov
 * @author Daniel Tsada Yosief
 * @author Linnea Hagman
 * @author Shotaro Ishii
 * @version 1.0
 * @since 1.0
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GithubWebhookRequest {
    RepositoryObject repository;

    HeadCommit headCommit;

    /**
     * Represents repository
     */
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class RepositoryObject {
        String htmlUrl;
        String statusesUrl;

        /**
         * Gets the url of repository
         *
         * @return url of repository
         */
        public String getHtmlUrl() {
            return htmlUrl;
        }

        /**
         * Sets the url of the repository
         *
         * @param html_url
         */
        public void setHtmlUrl(String html_url) {
            this.htmlUrl = html_url;
        }

        /**
         * Gets statuses url
         *
         * @return statuses url
         */
        public String getStatusesUrl() {
            return statusesUrl;
        }

        /**
         * Sets statuses url
         *
         * @param statuses_url
         */
        public void setStatusesUrl(String statuses_url) {
            this.statusesUrl = statuses_url;
        }
    }

    /**
     * Represents head of commit
     */
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class HeadCommit {
        String id;
        String timestamp;
        Committer committer;

        /**
         * Represents committer
         */
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public class Committer {
            String name;
            String email;

            /**
             * Gets name of committer
             *
             * @return name of committer
             */
            public String getName() {
                return name;
            }

            /**
             * Sets name of committer
             *
             * @param name of committer
             */
            public void setName(String name) {
                this.name = name;
            }

            /**
             * Gets email of committer
             *
             * @return email of committer
             */
            public String getEmail() {
                return email;
            }

            /**
             * Sets email of committer
             *
             * @param email of committer
             */
            public void setEmail(String email) {
                this.email = email;
            }
        }

        /**
         * Gets ID of commit
         *
         * @return id of commit
         */
        public String getId() {
            return id;
        }

        /**
         * Sets ID of commit
         *
         * @param id of commit
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets timestamp of commit
         *
         * @return timestamp of commit
         */
        public String getTimestamp() {
            return timestamp;
        }

        /**
         * Sets timestamp of commit
         *
         * @param timestamp of commit
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * Gets committer
         *
         * @return committer
         */
        public Committer getCommitter() {
            return committer;
        }

        /**
         * Sets committer
         *
         * @param committer
         */
        public void setCommitter(Committer committer) {
            this.committer = committer;
        }
    }

    /**
     * Gets head of commit
     *
     * @return Head of commit
     */
    public HeadCommit getHeadCommit() {
        return headCommit;
    }

    /**
     * Sets head of commit
     *
     * @param head_commit
     */
    public void setHeadCommit(HeadCommit head_commit) {
        this.headCommit = head_commit;
    }

    /**
     * Gets repository
     *
     * @return repository
     */
    public RepositoryObject getRepository() {
        return repository;
    }

    /**
     * Sets repository
     *
     * @param repository
     */
    public void setRepository(RepositoryObject repository) {
        this.repository = repository;
    }

    /**
     * Gets strings of url of repository, ID and timestamp of commit, name and email of committer from webhook request
     *
     * @return Strings of repository and commit information
     */
    public String toString() {
        return "\nURL: " + this.getRepository().getHtmlUrl() +
                "\nCommit id: " + this.getHeadCommit().getId() +
                "\nTimestamp: " + this.getHeadCommit().getTimestamp() +
                "\nAuthor name: " + this.getHeadCommit().getCommitter().getName() +
                "\nAuthor email: " + this.getHeadCommit().getCommitter().getEmail() + "\n";
    }

    public String toHtml() {
        return "<p>" +
                "<h2> New commit </h2>" +
                "URL: " + this.getRepository().getHtmlUrl() + "<br>" +
                "Commit id: " + this.getHeadCommit().getId() + "<br>" +
                "Timestamp: " + this.getHeadCommit().getTimestamp() + "<br>" +
                "Author name: " + this.getHeadCommit().getCommitter().getName() + "<br>" +
                "Author email: " + this.getHeadCommit().getCommitter().getEmail() + "<br>" +
                "</p>";
    }
}
