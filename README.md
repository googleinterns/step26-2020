# GrowPod

by Kayla Harwell, Stephanie Galvan, and Cody Rivera

This is a community garden management web app that targets Google Cloud's
App Engine.

To run a local server, execute this command in Cloud Shell:

```bash
mvn package appengine:run
```

To deploy, set the deploy.projectId field in pom.xml
and execute this command in Cloud Shell:

```bash
mvn package appengine:deploy
```
