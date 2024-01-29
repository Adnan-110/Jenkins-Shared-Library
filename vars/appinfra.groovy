def call() {
    properties([
        parameters{[
            choice(choices: 'dev\nprod', description: 'Select the Environment', name:"ENV"),
            choice(choices: 'apply\ndestroy', description: 'Select the Action to be Performed', name: "ACTION"),
            string(choices: 'APP_VERSION', description: 'Enter the App Version', name:"APP_VERSION")
        ]}
    ])
    node{
        ansiColor('xterm') {
            git branch: 'main', url: "https://github.com/Adnan-110/${COMPONENT}.git"
            stage('Terraform Init') {
                sh '''
                    cd mutable-infra
                    terrafile -f env-dev/Terrafile
                    terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars
                '''
            }
            stage('Terraform Plan') {
                sh '''
                    cd mutable-infra
                    terraform plan -var-file=env-${ENV}/${ENV}.tfvars -var APP_VERSION=${APP_VERSION}
                '''
            }
            stage('Terraform Action') {
                sh '''
                    cd mutable-infra
                    terraform ${ACTION} -auto-approve -var-file=env-${ENV}/${ENV}.tfvars -var APP_VERSION=${APP_VERSION}
                '''
            }
        }
    }

}

// pipeline{
//     agent{
//         label 'ws'
//     }
//     options {
//         ansiColor('xterm')
//     }
//     parameters {
//         choice(name: 'ENV', choices: ['dev', 'prod'], description: 'Select the Environment')
//         choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Select the Actionn to be Performed')
//     }
//     stages {
//         stage('Terraform init') {
//             steps{
//                 sh "terrafile -f env-dev/Terrafile"
//                 sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
//             }
//         }
//         stage('Terraform plan') {
//             steps{
//                 sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
//             }
//         }
//         stage('Terraform Apply/Destroy') {
//             steps{
//                sh "terraform ${ACTION} -auto-approve -var-file=env-${ENV}/${ENV}.tfvars"
//             }
//         }
//     }
// }