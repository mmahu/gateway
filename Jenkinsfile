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
                sh 'docker service create --no-resolve-image --replicas 2 --name mmahu-gateway -p 8888:8888 mmahu-main:5000/mmahu-gateway:1.0.$BUILD_NUMBER'
            }
        }
    }
}