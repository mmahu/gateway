pipeline {
    agent any
    stages {
        stage('fetch') {
            git url: 'https://github.com/mmahu/gateway.git'
        }
        stage('build') {
            def rtGradle = Artifactory.newGradleBuild()
            rtGradle.tool = "Gradle-2.4"
            rtGradle.deployer repo:'ext-release-local', server: server
            rtGradle.resolver repo:'remote-repos', server: server
            rtGradle.run rootDir: "mmahu-gateway", buildFile: 'build.gradle', tasks: 'clean build'
        }
    }
}