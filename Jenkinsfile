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
                sh 'docker service create --limit-memory 512M --network dev --hostname mmahu-gateway --no-resolve-image --replicas 1 --name mmahu-gateway --publish target=8002,published=8002 mmahu-main:5000/mmahu-gateway:1.0.$BUILD_NUMBER'
            }
        }
    }
}