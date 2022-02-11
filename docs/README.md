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