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
        stage('Maven Install') {
          agent {
            docker {
              image 'maven:3.5.0'
            }
          }
          steps {
            sh 'mvn clean install'
          }
        stage('imaging') {
            steps {
                sh 'docker build -t Dockerfile'
            }
        }
    }
}