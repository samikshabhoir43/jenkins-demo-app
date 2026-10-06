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

                        docker push \
                        $DOCKER_USERNAME/jenkins-demo-app:${BUILD_NUMBER}

                        docker logout
                    '''
                }
            }
        }

        stage('Kubernetes Deploy') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh '''
                        export KUBECONFIG=/var/lib/jenkins/.kube/config

                        kubectl apply -f k8s/deployment.yaml
                        kubectl apply -f k8s/service.yaml

                        kubectl set image deployment/jenkins-demo-app \
                        jenkins-demo-app=$DOCKER_USERNAME/jenkins-demo-app:${BUILD_NUMBER}

                        kubectl rollout status deployment/jenkins-demo-app \
                        --timeout=120s
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    export KUBECONFIG=/var/lib/jenkins/.kube/config

                    echo "Checking Pod status..."
                    kubectl get pods -l app=jenkins-demo-app

                    echo "Checking Service..."
                    kubectl get svc jenkins-demo-app

                    echo "Waiting for new Pod to become Ready..."
                    kubectl wait \
                        --for=condition=Ready pod \
                        -l app=jenkins-demo-app \
                        --timeout=120s

                    echo "Checking Deployment rollout..."
                    kubectl rollout status \
                        deployment/jenkins-demo-app \
                        --timeout=120s

                    echo "Health Check Completed Successfully"
                '''
            }
        }
    }
}