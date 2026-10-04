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
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t jenkins-demo-app:${BUILD_NUMBER} .'
            }
        }
    }
}
