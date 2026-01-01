pipeline{
    agent any
    stages{
        stage('code-pull'){
            steps{
                git branch: 'main', url: 'https://github.com/RajeshGajengi/Flight-Reservation-App-DevOps.git'
            }
        }
        stage('build'){
            steps{
                sh '''
                    cd frontend
                    npm install
                    npm run build
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd frontend
                    aws s3 sync dist/ s3://cbz-frontend-my-project-bux/
                '''
            }
        }
    }
}