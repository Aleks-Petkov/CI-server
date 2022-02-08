# CI-server

Work in progress...

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