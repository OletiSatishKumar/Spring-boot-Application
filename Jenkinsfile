pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/OletiSatishKumar/Spring-boot-Application.git'
        BRANCH = 'master'
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
                        // Backend - Java + Maven
                        bat '''
                            cd backend
                            mvn sonar:sonar ^
                                -Dsonar.projectKey=springboot-backend ^
                                -Dsonar.projectName=SpringBootBackend
                        '''

                        // Frontend - React using CLI tool
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

        // 5. Quality Gate Check (optional but recommended)
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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
