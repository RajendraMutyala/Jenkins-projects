pipeline {
    agent any

    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        GIT_URL = 'https://github.com/RajendraMutyala/Jenkins-projects.git'
        GIT_BRANCH = 'main'
        DOCKER_IMAGE = 'jenkins-projects'
        CONTAINER_NAME = 'jenkins-projects-app'
        APP_PORT = '8081'
    }

    stages {
        stage('Git Clone') {
            steps {
                echo 'Cloning source code from GitHub...'

                git branch: "${GIT_BRANCH}",
                    url: "${GIT_URL}"

                sh '''
                    set -eu
                    echo "Latest checked-out commit:"
                    git log -1 --oneline

                    echo "Checking Java source file..."
                    test -f src/main/java/com/example/App.java
                '''
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building Java application with Maven...'

                sh '''
                    set -eu
                    test -f pom.xml
                    mvn -B clean package
                    test -s target/jenkins-projects-1.0.0.jar
                '''

                archiveArtifacts artifacts: 'target/*.jar',
                    fingerprint: true
            }
        }

        stage('Docker Image') {
            steps {
                echo 'Building the latest Docker image...'

                sh '''
                    set -eu
                    docker --version
                    test -f Dockerfile

                    docker build \
                        --pull \
                        --no-cache \
                        -t "$DOCKER_IMAGE:$BUILD_NUMBER" .

                    docker tag \
                        "$DOCKER_IMAGE:$BUILD_NUMBER" \
                        "$DOCKER_IMAGE:latest"

                    docker images "$DOCKER_IMAGE"
                '''
            }
        }

        stage('Deployment') {
            steps {
                echo 'Deploying the latest application...'

                sh '''
                    set -eu

                    echo "Removing previous application container..."
                    docker rm -f "$CONTAINER_NAME" 2>/dev/null || true

                    echo "Starting the new application container..."
                    docker run -d \
                        --name "$CONTAINER_NAME" \
                        --restart unless-stopped \
                        -p "$APP_PORT:8080" \
                        "$DOCKER_IMAGE:$BUILD_NUMBER"

                    echo "Checking container status..."
                    docker ps --filter "name=$CONTAINER_NAME"

                    echo "Waiting for application startup..."
                    sleep 3

                    echo "Checking HTTP response..."
                    curl --fail --retry 5 --retry-delay 2 \
                        http://127.0.0.1:$APP_PORT/

                    echo "Deployment completed successfully."
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
            echo 'Application URL: http://YOUR_SERVER_PUBLIC_IP:8081'
        }

        failure {
            echo 'Pipeline failed. Check Console Output for the error.'
        }

        always {
            echo 'Jenkins pipeline execution finished.'
        }
    }
}
