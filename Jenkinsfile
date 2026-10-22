pipeline {
    agent any

    environment {
        GIT_URL = 'https://github.com/RajendraMutyala/Jenkins-projects.git'
        GIT_BRANCH = 'main'
        DOCKER_IMAGE = 'jenkins-projects'
    }

    stages {
        stage('Git Clone') {
            steps {
                echo 'Cloning source code from GitHub...'

                git branch: "${GIT_BRANCH}",
                    url: "${GIT_URL}"

                sh 'git log -1 --oneline'
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building application with Maven...'

                sh '''
                    set -eu
                    test -f pom.xml
                    mvn -B clean package
                    test -s target/jenkins-projects-1.0.0.jar
                '''

                archiveArtifacts artifacts: 'target/*.jar'
            }
        }

        stage('Docker Image') {
            steps {
                echo 'Building Docker image locally...'

                sh '''
                    set -eu
                    docker --version
                    test -f Dockerfile

                    docker build \
                        -t "$DOCKER_IMAGE:$BUILD_NUMBER" .

                    docker tag \
                        "$DOCKER_IMAGE:$BUILD_NUMBER" \
                        "$DOCKER_IMAGE:latest"

                    docker images jenkins-projects
                '''
            }
        }

        stage('Deployment') {
            steps {
                echo 'Local Docker image created.'
                echo "Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}"
                echo 'No remote deployment configured.'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
