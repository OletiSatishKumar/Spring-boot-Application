// pipeline {
//     agent any

//     environment {
//         DOCKER_HUB_CREDENTIALS = credentials('Docker_Cred') // 🔐 Docker Hub Credentials
//         IMAGE_NAME = "satishosk/fullstack-app"              // 🐳 Base Docker Image Name
//     }

//     stages {

//         stage('📥 Clone Code from GitHub') {
//             steps {
//                 git 'https://github.com/OletiSatish/Spring-boot-Application.git'
//             }
//         }

//         stage('🛠 Build Spring Boot Backend') {
//             steps {
//                 dir('backend') {
//                     bat 'mvn clean package -DskipTests'
//                 }
//             }
//         }

//         stage('⚙️ Build React Frontend') {
//             steps {
//                 dir('frontend') {
//                     bat 'npm install'
//                     bat 'npm run build'
//                 }
//             }
//         }

//         stage('🐳 Build Docker Images') {
//             steps {
//                 bat 'docker-compose build'
//             }
//         }

//         stage('🔐 Docker Login to Hub') {
//             steps {
//                 bat """
//                     echo %DOCKER_HUB_CREDENTIALS_PSW% | docker login -u %DOCKER_HUB_CREDENTIALS_USR% --password-stdin
//                 """
//             }
//         }

//         stage('📦 Push Docker Images to Hub') {
//             steps {
//                 // Just push the images directly since they are already tagged in docker-compose.yml
//                 bat "docker images"
//                 bat "docker push %IMAGE_NAME%-backend"
//                 bat "docker push %IMAGE_NAME%-frontend"
//             }
//         }

//         stage('🚀 Deploy with Docker Compose') {
//             steps {
//                 bat 'docker-compose down'
//                 bat 'docker-compose up -d'
//             }
//         }
//     }

//     post {
//         success {
//             echo '✅ Build and Deployment Successful!'
//         }
//         failure {
//             echo '❌ Build Failed! Please Check the Logs.'
//         }
//     }
// }


pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/OletiSatish/Spring-boot-Application.git'  // Change to your GitHub repo
        BRANCH = 'master'  // You can specify your branch here
    }

    stages {
        // 1. Checkout Code from GitHub
        stage('Checkout Code') {
            steps {
                script {
                    // Clone the GitHub repo
                    try {
                        checkout scm  // This will checkout the code using the GitHub webhook triggering the pipeline
                        echo "Code cloned successfully."
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'  // If cloning fails, mark the build as failed
                        error "Code cloning failed: ${e.getMessage()}"
                    }
                }
            }
        }

        // 2. Verify the Code was Cloned
        stage('Verify Code Clone') {
            steps {
                script {
                    // Checking if the repository is correctly cloned
                    def isRepoCloned = fileExists('pom.xml')  // Check if the pom.xml exists (for Maven-based Spring Boot)
                    if (isRepoCloned) {
                        echo "Code is successfully cloned and pom.xml exists."
                    } else {
                        error "Code was not cloned properly, pom.xml is missing."
                    }
                }
            }
        }

        // 3. Build the Project (Maven or any other build tool)
        stage('Build Project') {
            steps {
                script {
                    // Run Maven Build or another build tool (optional stage to add)
                    bat 'mvn clean install'  // Use bat instead of sh for Windows environments
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean workspace after the build, optional
        }

        success {
            echo "Pipeline completed successfully!"
        }

        failure {
            echo "Pipeline failed. Please check the logs."
        }
    }
}
