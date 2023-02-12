pipeline {
    agent none
    environment {
        CONTAINER_REGISTRY = '253520709108.dkr.ecr.us-west-2.amazonaws.com'
        CONTAINER_REPO = 'mcc-code-school-recipe-services'
        BRANCH_NAME = 'develop'
    }      
    stages {
        stage('Build App') {
            agent { 
                docker { 
                    image 'gradle:jdk11'
                    args '-u root:sudo' 
                }
            }
            steps {
                sh 'chmod 755 gradlew'
                sh './gradlew build'
            }
        }
        stage('Build and Publish Docker Container') {
            agent any
            steps {
                script {
                    docker.withRegistry("https://${env.CONTAINER_REGISTRY}", "ecr:us-west-2:jenkins-ecr") {
                        def image = docker.build("${env.CONTAINER_REPO}:${env.BUILD_ID}", "--build-arg branch=${env.BRANCH_NAME} .")
                        image.push()
                    }
                }
            }
        }
        stage('Deploy') {
            agent any
            steps {
                sshagent(credentials: ['AppServer']) {
                    sh """scp deploy.sh ${env.BRANCH_NAME}.env ubuntu@172.31.49.124:recipe/services"""
                    sh """ssh -t -o StrictHostKeyChecking=no ubuntu@172.31.49.124 'cd recipe/services && chmod 755 deploy.sh && ./deploy.sh ${env.BRANCH_NAME} ${CONTAINER_REGISTRY} ${env.CONTAINER_REPO}:${env.BUILD_ID}'"""
                }
            }
        }
    }
}
