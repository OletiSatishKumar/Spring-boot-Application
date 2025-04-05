pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('Docker_Cred') // 🔐 Docker Hub Credentials
        IMAGE_NAME = "satishosk/fullstack-app"              // 🐳 Base Docker Image Name
    }

    stages {

        stage('📥 Clone Code from GitHub') {
            steps {
                git 'https://github.com/OletiSatish/Spring-boot-Application.git'
            }
        }

        stage('🛠 Build Spring Boot Backend') {
            steps {
                dir('backend') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('⚙️ Build React Frontend') {
            steps {
                dir('frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('🐳 Build Docker Images') {
            steps {
                bat 'docker-compose build'
            }
        }

        stage('🔐 Docker Login to Hub') {
            steps {
                bat """
                    echo %DOCKER_HUB_CREDENTIALS_PSW% | docker login -u %DOCKER_HUB_CREDENTIALS_USR% --password-stdin
                """
            }
        }

        stage('📦 Push Docker Images to Hub') {
            steps {
                // Just push the images directly since they are already tagged in docker-compose.yml
                bat "docker images"
                bat "docker push %IMAGE_NAME%-backend"
                bat "docker push %IMAGE_NAME%-frontend"
            }
        }

        stage('🚀 Deploy with Docker Compose') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo '✅ Build and Deployment Successful!'
        }
        failure {
            echo '❌ Build Failed! Please Check the Logs.'
        }
    }
}
