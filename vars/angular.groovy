def call(){
    node {
        env.SONAR_URL="172.31.39.131"
        env.NEXUS_URL="172.31.86.85"
        env.APP_TYPE="angular"

        common.lintChecks()
        env.ARGS="-Dsonar.sources=."
        common.sonarChecks()
    }
}




// def call() {
//     pipeline{
//         agent{
//             label 'ws'
//         }
//         environment{
//             SONAR_CRED = credentials('SONAR_CRED')
//             NEXUS_CRED = credentials('NEXUS_CRED')
//             SONAR_URL="172.31.39.131"
//             NEXUS_URL="172.31.86.85"
//         }
//         stages{
//             stage('Lint Checks') {
//                 steps {
//                     script{
//                         helloWorld.info(COMPONENT)
//                         common.lintChecks()
//                         // Below we are passing parameter to both methods/functions
//                         // helloWorld.info(component)
//                         // lintChecks(component)
//                     }
//                 }
//             }
//             stage('Static Code Analysis') {
//                 steps{
//                     script{
//                         env.ARGS="-Dsonar.sources=."
//                         common.sonarChecks()
//                     }
//                 }   
//             }
//             stage('Get the Sonar Analysis Result') {
//                 steps{
//                     sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate?ref_type=heads > qualityGate.sh"
//                     // sh "bash qualityGate.sh admin password ${SONAR_URL} ${COMPONENT}"
//                     echo "Scan is Good"
//                 }   
//             }
//             stage('Test Cases') {
//                 parallel {
//                     stage('Unit Testing') {
//                         steps{
//                             sh "env"
//                             echo "****** Unit Testing is Started for ${COMPONENT} ******"
//                             // sh "mvn test" dont know cmd for Unit testing in javscript 
//                             echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
//                             echo "****** Unit Testing is Completed for ${COMPONENT} ******"
//                         }   
//                     }
//                     stage('Integration Testing') {
//                         steps{
//                             echo "****** Integration Testing is Started for ${COMPONENT} ******"
//                             // sh "mvn verify" dont know cmd for Integration testing in javscript
//                             echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
//                             echo "****** Integration Testing is Completed for ${COMPONENT} ******"
//                         }
//                     }
//                     stage('Functional Testing') {
//                         steps{
//                             echo "****** Functional Testing is Started for ${COMPONENT} ******"
//                             // sh "mvn function" dont know cmd for Functional testing in javscript
//                             echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
//                             echo "****** Functional Testing is Completed for ${COMPONENT} ******"
//                         }
//                     }
//                 }
//             }
//             stage('Checking Artifacts Availability on Nexus'){  // This block will be executed only when run from tag 
//                 when { expression { env.TAG_NAME != null } } 
//                 steps{
//                     echo "****** ${COMPONENT} Artifacts Availabilty checking is Started ******"
//                     script{
//                         env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                     }
//                     echo UPLOAD_STATUS
//                     echo "****** ${COMPONENT} Artifacts Availabilty checking is Completed ******"
//                 }
//             }
//             stage('Prepare Artifacts for ${COMPONENT}'){  // This block will be executed only when run from tag
//                 when { 
//                     expression { env.TAG_NAME != null } 
//                     expression {env.UPLOAD_STATUS == "" || env.UPLOAD_STATUS == null}
//                 } 
//                 steps{
//                     echo "****** Artifacts Preparation is Started for ${COMPONENT} ******" 
//                     echo "****** Artifacts Preparation is InProgress for ${COMPONENT} ******" 
//                     echo "****** Artifacts Preparation is Completed for ${COMPONENT} ******" 
//                 }
//             }
//             stage('Uploading Artifacts for ${COMPONENT}'){ // This block will be executed only when run from tag
//                 when { 
//                     expression { env.TAG_NAME != null } 
//                     expression {env.UPLOAD_STATUS == "" || env.UPLOAD_STATUS == null}
//                 } 
//                 steps{
//                     echo "****** Uploading of Artifacts is Started for ${COMPONENT} ******" 
//                     echo "****** Uploading of Artifacts is InProgress for ${COMPONENT} ******" 
//                     echo "****** Uploading of Artifacts is Completed for ${COMPONENT} ******" 
//                 }
//             }
//         }
//     }
// }