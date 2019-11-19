pipeline {
    agent any
    stages {
        stage('fetch') {
            steps {
                git url: 'https://github.com/mmahu/gateway.git' branch: 'master'
            }
        }
        stage('build') {
            steps {
                def rtGradle = Artifactory.newGradleBuild()
                rtGradle.tool = "Gradle-2.4"
                rtGradle.deployer repo:'ext-release-local', server: server
                rtGradle.resolver repo:'remote-repos', server: server
                rtGradle.run rootDir: "mmahu-gateway", buildFile: 'build.gradle', tasks: 'clean build'
            }
        }
    }
}