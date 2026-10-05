```groovy
pipeline {

    agent {
        label 'spring'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/samikshabhoir43/jenkins-demo-app.git'
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "JAVA_HOME=$JAVA_HOME"
                    ls -ld "$JAVA_HOME"
                    "$JAVA_HOME/bin/java" -version
                    mvn -version
                '''
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t jenkins-demo-app:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {

                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

                        docker tag jenkins-demo-app:${BUILD_NUMBER} \
                            $DOCKER_USERNAME/jenkins-demo-app:${BUILD_NUMBER}

                        docker push $DOCKER_USERNAME/jenkins-demo-app:${BUILD_NUMBER}

                        docker logout
                    '''
                }
            }
        }
    }
}
```
