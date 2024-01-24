def call() {
    properties([
        parameters{[
            choice(choices: ['dev\nprod'], description: 'Select the Environment', name:"ENV"),
            choice(choices: ['apply\destroy'], description: 'Select the Actionn to be Performed', name: "ACTION")
        ]}
    ])
    node{
        ansiColor('xterm') {
            git branch: 'main', url: "https://github.com/Adnan-110/${REPONAME}.git"
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
                    terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                '''
            }
            stage('Terraform Action') {
                sh '''
                    cd mutable-infra
                    terraform ${ACTION} -auto-approve -var-file=env-${ENV}/${ENV}.tfvars
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