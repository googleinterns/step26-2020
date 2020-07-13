# GrowPod

by Kayla Harwell, Stephanie Galvan, and Cody Rivera

This is a community garden management web app that targets Google Cloud's
App Engine.

* [Beta Website](https://beta26-step-2020.uc.r.appspot.com)
* [Production Website](https://prod26-step-2020.uc.r.appspot.com)

## Build Instructions

Before the build, execute this command in Cloud Shell to install
all needed depencencies:

```bash
make install
```

To run a development server, execute this command in Cloud Shell:

```bash
make dev
```

Changes to the frontend will be immediately reflected, while changes
to the backend require the development server be restarted.

To run a production server, execute this command in Cloud Shell:

```bash
make prod
```

To deploy, set the `deploy.projectId` field in `pom.xml`
and execute this command in Cloud Shell:

```bash
make deploy
```
