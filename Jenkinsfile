pipeline {
    agent any

    stages {
        stage('Git Checkout') {
            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/feature/jenkins_test']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '2057a2c1-f936-4ebc-9467-cb23e5a96baa', url: 'git@github.com:AllenAi007/seeds-java.git']]])
            }
        }
        stage('build & complie'){
            steps{
                echo 'build'
            }
        }
        stage('deploy'){
            steps{
                echo 'deploy project'
            }
        }
    }
}
