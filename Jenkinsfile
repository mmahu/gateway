def name=""
def port=""
def registry=""
def buildNumber=""

pipeline {
    agent any
    stages {
        stage('init') {
            steps {
                script {
                    name = "e-gateway"
                    port = "9090:9090"
                    registry = "192.168.0.19:5000"
                    buildNumber = "1.0.$BUILD_NUMBER"
                }
            }
        }
        stage('fetch') {
            steps {
                git url: 'https://github.com/mmahu/gateway.git', branch: 'master'
            }
        }
        stage('build') {
            steps {
                sh 'chmod +x gradlew'
                sh "echo ${buildNumber}"
                sh "./gradlew clean assemble -PbuildNumber=${buildNumber} -Dorg.gradle.java.home=/usr/local/jdk-11.0.2"
            }
        }
        stage('imaging') {
            steps {
                sh "docker buildx build --platform=linux/arm64/v8 . -t ${registry}/${name}:${buildNumber} --load"
                sh "docker push ${registry}/${name}"
            }
        }
        stage('deploy') {
            steps {
                sh "docker service rm ${name} || true"
                sh "docker service create \
                    --name ${name} \
                    --publish ${port} \
                    ${registry}/${name}:${buildNumber}"
            }
        }
        stage('clean up') {
            steps {
                sh "docker image prune --all -f"
            }
        }
    }
}