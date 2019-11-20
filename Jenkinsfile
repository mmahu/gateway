pipeline {
    agent any
    stages {
        stage('fetch') {
            steps {
                git url: 'https://github.com/mmahu/gateway.git', branch: 'master'
            }
        }
        stage('build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
            }
        }
        stage('imaging') {
            steps {
                sh 'docker build -t mmahu-gateway:lates .'
            }
        }
        stage('deploy') {
            steps {
                sh 'docker run --name="mmahu-gateway" -p 8001:8080 mmahu-gateway:lates'
            }
        }
    }
}