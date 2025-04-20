pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/OletiSatishKumar/Spring-boot-Application.git'
        BRANCH = 'master'
        IMAGE_BACKEND = 'satishosk/springboot-backend'
        IMAGE_FRONTEND = 'satishosk/react-frontend'
    }

    stages {
        // 1. Checkout Code
        stage('Checkout Code') {
            steps {
                script {
                    try {
                        checkout scm
                        echo '✅ Code cloned successfully from GitHub.'
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        error "❌ Code cloning failed: ${e.getMessage()}"
                    }
                }
            }
        }

        // 2. Verify Clone Structure
        stage('Verify Code Clone') {
            steps {
                script {
                    def isBackendValid = fileExists('backend/pom.xml')
                    def isFrontendValid = fileExists('frontend/src/index.js')
                    if (isBackendValid && isFrontendValid) {
                        echo '✅ Project structure is valid. Both backend and frontend found.'
                    } else {
                        error '❌ Check project structure. Missing backend or frontend.'
                    }
                }
            }
        }

        // 3. Build Spring Boot Backend
        stage('Build Backend') {
            steps {
                script {
                    bat 'cd backend && mvn clean install'
                }
            }
        }

        // 4. Code Quality Analysis using SonarQube
        stage('Code Quality - SonarQube') {
            steps {
                withSonarQubeEnv('MySonarQubeServer') {
                    script {
                        bat '''
                            cd backend
                            mvn sonar:sonar ^
                                -Dsonar.projectKey=springboot-backend ^
                                -Dsonar.projectName=SpringBootBackend
                        '''

                        withEnv(["PATH+SONAR=${tool 'SonarScanner'}/bin"]) {
                            bat '''
                                cd frontend
                                sonar-scanner ^
                                    -Dsonar.projectKey=react-frontend ^
                                    -Dsonar.projectName=ReactFrontend ^
                                    -Dsonar.sources=src ^
                                    -Dsonar.sourceEncoding=UTF-8
                            '''
                        }
                    }
                }
            }
        }

        // 5. Quality Gate Check
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // 6. Build Docker Images
        stage('Build Docker Images') {
            steps {
                script {
                    bat """
                        docker build -t ${IMAGE_BACKEND}:latest backend
                        docker build -t ${IMAGE_FRONTEND}:latest frontend
                    """
                }
            }
        }

        // 7. Docker Hub Login & Push
        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'Docker_Cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        bat """
                            echo Logging in to Docker Hub...
                            echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin

                            docker push ${IMAGE_BACKEND}:latest
                            docker push ${IMAGE_FRONTEND}:latest
                        """
                    }
                }
            }
        }

        // 8. Deploy with Docker Compose
        stage('Deploy with Docker Compose') {
            steps {
                script {
                    bat 'docker-compose down || echo "No containers to stop"'
                    bat 'docker-compose pull' // In case the latest image is pushed
                    bat 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }

        success {
            echo '🎉 Pipeline completed successfully!'
        }

        failure {
            echo '🚨 Pipeline failed. Please check the logs above.'
        }
    }
}
