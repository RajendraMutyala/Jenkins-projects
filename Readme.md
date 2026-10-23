# CI/CD Pipeline Automation Using Jenkins, Maven, and Docker

## 1. Project Introduction

This project demonstrates how to automatically build and deploy a Java application using DevOps tools such as Jenkins, GitHub, Maven, and Docker.

Normally, when a developer changes an application, someone needs to download the latest code, build the application, prepare it for deployment, and start it on a server. Doing these steps manually takes time and can lead to mistakes.

In this project, I automated these steps using a Jenkins CI/CD pipeline.

Whenever Jenkins detects a new change in the GitHub repository, it can automatically download the code, build the application, create a Docker image, deploy the application in a container, and check whether it is responding correctly.

**The main goal of this project is to make application building and deployment faster, easier, and more reliable.**

## 2. Tools and Technologies Used

| Tool | What it does in this project |
|---|---|
| Git | Tracks changes made to the project code |
| GitHub | Stores the project code online |
| Jenkins | Automatically runs the build and deployment steps |
| Java 17 | The programming language and runtime used by the application |
| Maven | Builds the Java application and packages it into a JAR file |
| Docker | Packages the application so it can run inside a container |
| Linux Shell Commands | Execute the commands required for building and deployment |
| cURL | Checks whether the deployed application responds to an HTTP request |

## 3. Understanding the Project Workflow

The complete process follows these steps:

```text
Developer Updates Code
         |
         v
Code Stored in GitHub
         |
         v
Jenkins Detects Changes
         |
         v
Jenkins Downloads the Code
         |
         v
Maven Builds the Application
         |
         v
Executable JAR File Created
         |
         v
Docker Image Created
         |
         v
Application Runs in a Container
         |
         v
Jenkins Checks the Application
```

### Step 1: Store the Code in GitHub

GitHub is the online location where I store my project files.

These files include the Java source code, Maven configuration (`pom.xml`), Docker configuration (`Dockerfile`), and Jenkins pipeline script (`Jenkinsfile`).

Git helps track changes to the code, while GitHub makes the repository accessible to Jenkins.

### Step 2: Jenkins Checks for Changes

Jenkins is the automation tool responsible for running the pipeline.

In this project, I configured Jenkins to check the GitHub repository approximately every two minutes.

When Jenkins detects a new commit, it can start a new pipeline run automatically.

This avoids the need to manually start a build every time the source code changes.

### Step 3: Jenkins Downloads the Code

Jenkins connects to the GitHub repository and downloads the code from the `main` branch.

It also checks whether the expected Java source file exists before moving to the next stage.

This ensures that the basic files required for the build are present.

### Step 4: Maven Builds the Application

Maven is a tool that helps turn Java source code into an application that can be executed.

Jenkins runs the following command:

```bash
mvn -B clean package
```

This command removes the previous build output, compiles the Java code, and packages the application into a JAR file.

The generated file is:

```text
target/jenkins-projects-1.0.0.jar
```

A JAR file is a package that contains compiled Java code and other required application resources.

Jenkins also saves the generated JAR as a build artifact, making it available in the Jenkins build records.

### Step 5: Docker Creates an Image

Docker helps package an application together with the runtime environment needed to start it.

In this project, the `Dockerfile` tells Docker to use a Java 17 runtime, copy the JAR file into the image, and run the application when the container starts.

Jenkins creates a Docker image using the following command pattern:

```bash
docker build -t jenkins-projects:BUILD_NUMBER .
```

The actual pipeline uses the current Jenkins build number in place of `BUILD_NUMBER`. It also tags the image as `jenkins-projects:latest`.

For example, a build numbered 15 creates the image:

```text
jenkins-projects:15
```

Think of a Docker image as a prepared package from which an application container can be started.

### Step 6: Docker Runs the Application

After creating the image, Jenkins deploys the application inside a Docker container.

A container is an isolated environment in which an application runs.

The pipeline removes the previous application container, if present, and starts a new one using the image from the current build.

The container is configured with the following settings:

- **Container name:** `jenkins-projects-app`
- **Application port inside the container:** `8080`
- **Port used to access the application on the server:** `8081`
- **Restart policy:** `unless-stopped`

The port mapping means that requests sent to port `8081` on the server are forwarded to port `8080` inside the container.

### Step 7: Jenkins Checks Whether the Application Is Running

Starting a container does not necessarily mean that the application is ready to use.

To check the deployment, Jenkins first checks the container status, waits briefly for startup, and then sends an HTTP request using cURL.

The pipeline checks this address from the server:

```text
http://127.0.0.1:8081/
```

If the application responds successfully, the deployment verification passes. If the check fails after the configured retries, the pipeline reports a failure.

This final step helps identify problems after deployment instead of simply assuming that the application started correctly.

## 4. Understanding the Project Files

This project uses three important configuration files.

### Jenkinsfile

The `Jenkinsfile` is the instruction manual for Jenkins.

It defines the order in which the pipeline runs:

1. Download the code from GitHub.
2. Build the application using Maven.
3. Create a Docker image.
4. Deploy the application in a container.
5. Verify the application response.

It also defines what Jenkins should report when the pipeline succeeds or fails.

### pom.xml

The `pom.xml` file is the instruction manual for Maven.

It specifies the project details, Java version, build plugins, and main application class.

In this project, Maven uses Java 17 and generates the JAR file named `jenkins-projects-1.0.0.jar`.

### Dockerfile

The `Dockerfile` is the instruction manual for Docker.

It specifies the Java runtime environment, where the JAR should be copied, and the command used to start the application.

Together, these three files automate the process of building and deploying the application.

## 5. Project Configuration

| Setting | Value |
|---|---|
| GitHub Repository | `https://github.com/RajendraMutyala/Jenkins-projects.git` |
| Git Branch | `main` |
| Java Version | 17 |
| Maven Artifact | `jenkins-projects-1.0.0.jar` |
| Docker Image Name | `jenkins-projects` |
| Docker Container Name | `jenkins-projects-app` |
| Server Port | `8081` |
| Container Port | `8080` |

## 6. Requirements to Run the Project

Before running the pipeline, the Jenkins execution environment needs:

- Jenkins with Pipeline and Git support
- Access to the GitHub repository
- Java 17 and Maven
- Docker installed and running
- Permission for Jenkins to execute Docker commands
- cURL installed
- Network access to download required dependencies and Docker images

The Java application must listen on port `8080` inside the container for the configured deployment and verification steps to work.

## 7. How to Run the Application Manually

The same build and deployment process can be performed manually for testing.

### Build the application

```bash
mvn clean package
```

### Create the Docker image

```bash
docker build -t jenkins-projects:local .
```

### Start the container

```bash
docker run -d \
  --name jenkins-projects-app \
  -p 8081:8080 \
  jenkins-projects:local
```

### Check the application response

```bash
curl -i http://localhost:8081/
```

### Check the running container

```bash
docker ps
```

These commands require the appropriate tools to be installed and the application to be configured to respond on the expected port and endpoint.

## 8. What Is CI/CD in This Project?

**Continuous Integration (CI)** means automatically integrating and building code changes so that build problems can be identified quickly.

In this project, Jenkins retrieves the updated code and uses Maven to compile and package the Java application.

**Continuous Deployment (CD)** means automatically deploying a successfully built application to its target environment.

In this project, Jenkins creates a Docker image, starts the application in a container, and checks its HTTP response.

Together, these processes reduce the amount of manual work needed to move code from a GitHub repository to a running application.

## 9. Error Handling and Verification

The pipeline includes checks to help identify problems during execution:

- Verifies that required source files exist.
- Checks that Maven produces the expected JAR file.
- Checks that Docker is available and the Dockerfile exists.
- Stops shell execution when a command fails or a required variable is undefined.
- Archives the JAR file in Jenkins.
- Checks the deployed container status.
- Retries the HTTP request if the application does not respond successfully.
- Reports the pipeline result in the Jenkins Console Output.

These checks help identify failures during the build and deployment process.

## 10. Project Outcome

By completing this project, I implemented an automated workflow connecting GitHub, Jenkins, Maven, Java, and Docker.

The pipeline retrieves the application code, builds it, packages it into a JAR file, creates a Docker image, deploys the application in a container, and verifies its HTTP response.

This project demonstrates my practical understanding of CI/CD fundamentals, Jenkins pipeline configuration, Maven builds, Docker containerization, and automated application deployment.

It provides a foundation for understanding how DevOps tools work together to deliver software with less manual intervention.
