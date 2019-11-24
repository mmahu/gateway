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
                sh './gradlew clean assemble -PbuildNumber=1.0.$BUILD_NUMBER'
            }
        }
        stage('imaging') {
            steps {
                sh 'docker build . -t mmahu-main:5000/mmahu-gateway:1.0.$BUILD_NUMBER'
                sh 'docker push mmahu-main:5000/mmahu-gateway'
            }
        }
        stage('deploy') {
            steps {
                sh 'docker service rm mmahu-gateway || true'
                sh 'docker service create --limit-memory 512M --hostname mmahu-gateway --no-resolve-image --name mmahu-gateway --p 7070:7070 mmahu-main:5000/mmahu-gateway:1.0.$BUILD_NUMBER'
            }
        }
    }
}