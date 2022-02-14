# CI-server

## Statement of contributions

- Daniel:
    - Helped extract relevant data from the JSON object sent from Github
    - Created the unit tests for almost all our functions
- Linnea:
    - Helped extract relevant data from the JSON object sent from Github
    - Implemented consistent storage for the build history by writing 
    the writeToFile() function and its corresponding unit tests
    - Documented our TEAM with regards to ESSENCE
- Aleks:
    - Helped extract relevant data from the JSON object sent from Github
    - Set up the GitHub Webhook for the server
    - Set up the endpoint to receive GitHub webhoost requests
    - Implemented the building and testing of new code pushed to the repo
    - Output the build file to web page as HTML with Louis
- Shotaro:
    - Helped extract relevant data from the JSON object sent from Github
    - Set the commit status on the repository together with Louis
    - Wrote JavaDocs documentation for all public classes and methods
- Louis:
    - Created skeleton code for the Project using Spring Boot
    - Set the commit status on the repository together with Shotaro
    - Wrote the building instructions in the README 
    - Output the build file to web page as HTML with Aleks
    
## Requirements

- Java 17

You need to set basic auth varibles in order to supply Github with the status of the commit. To do this set these two environment variables:

- `GITHUB_USERNAME`
- `GITHUB_TOKEN`

## How to build?

`./gradlew build`

## How to run?

`./gradlew bootRun`

## How to test?

`./gradlew test`

## Using Ngrok and GitHub webhooks
To make the server visible to the internet, install [ngrok](https://ngrok.com/) and run ```./ngrok http 8080``` when the server is running. This will yield a forwarding address such as ```http://d118-217-208-62-96.ngrok.io``` but the server will however not receive POST requests from GitHub unless the CI-server repository is configured with a webhook to the payload URL ```http://d118-217-208-62-96.ngrok.io/ci``` with the content type set to ```application/json```.

### Step-by-step Guide

* First clone the repo.

* The server need basic authentication in order to supply Github with the state of the head commit after building and testing the commit. For this, create a authentication token found under `Setting / Developer settings / Personal access tokens`.

* To access the this token and the corresponding username, the server uses the environment variables `GITHUB_USERNAME`, `GITHUB_TOKEN`. These variables can be set by running the commands;

    `export GITHUB_USERNAME=your_username`

    `export GITHUB_TOKEN=your_token`

    in the current runtime environment, where  *your_username* and *your_token* are changed receptively to your own username and the created token.

* Then build and start the server by running the commands:

    `./gradlew build`

    `./gradlew bootRun`

* To make the server visible to the internet, install [ngrok](https://ngrok.com/) and run;

    `./ngrok http 8080`

    when the server is running. This will yield a forwarding address such as *http://d118-217-208-62-96.ngrok.io*

* In order for the server to receive POST requests from GitHub whenever a commit has been pushed, a Github webhook for the repository, found under `Setting / Webhooks`, must be created and configured by setting the following field's:
    * **Payload URL** to the URL produced by ngrok with */ci* added at the end. For example: `http://d118-217-208-62-96.ngrok.io/ci`
    * **content type** to `application/json`
    * **Which events would you like to trigger this webhook?** to `Just the push event`
    * And make sure to check the check box for **Active**

* Now everything is set up, and the commit history will be displayed on the URL produced by ngrok.
