pipeline{
    agent any 
    stages{
        stage('Code-pull'){
            steps{
                git branch: 'main', url: 'https://github.com/RajeshGajengi/Flight-Reservation-App-DevOps.git'
            }
        }
        stage('Code-build'){
            steps{
                sh '''
                cd Backend
                mvn clean package
                '''
            }
        }
        stage('QA-TEST'){
            steps{
                withSonarQubeEnv(installationName:'sonar', credentialsId: 'Sonar-token') {
                    sh '''
                        cd Backend
                        mvn sonar:sonar -Dsonar.projectKey=flight-reservation
                    '''
                }
            }
            
        }
        stage('Docker-build'){
            steps{
                sh '''
                    cd Backend
                    docker build . -t r25gajengi/flightreservation-new:latest
                    docker push r25gajengi/flightreservation-new:latest
                    docker rmi r25gajengi/flightreservation-new:latest
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd Backend
                    kubectl apply -f k8s/
                '''
            }
        }
    }
}