def lintChecks() {
    sh """echo ****** Starting Style Checks for ${COMPONENT} ****** """
    //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
    //sh "pip3 install pylint"
    // sh "pytlint *.py || true"
    sh """echo ****** Style Check are Completed for ${COMPONENT} ******"""
    //  sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
    // We have used environment variable directly
}
// def call() {
// Above we are not catching the parameter value

def call() {
    pipeline{
        agent{
            label 'ws'
        }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        helloWorld.info(COMPONENT)
                        lintChecks()
                    }

                }
            }
            stage('Static Code Analysis') {
                steps{
                   echo "****** Starting Static Code Analysis for ${COMPONENT} ******"
                }
            }
        }
    }
}



// def lintChecks() {
//     sh "echo ****** Starting Style Checks for ${COMPONENT} ****** "
//     //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
//     // sh "pip3 install pylint"
//     // sh "pytlint *.py || true"
//     sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
//     //  sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
//     // We have used environment variable directly
// }
// def call() {
//     pipeline {
//         agent{
//                 label 'ws'
//             }
//         stages{
//             stage('Lint Checks') {
//                 steps{
//                     script{
//                         helloWorld.info(COMPONENT)
//                         lintChecks()
//                         // Below we are passing parameters to both methods/functions
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
//                     // sh "chmod 777 qualityGate.sh" // We added this line of code to get the execution permission aswell for all the profiles
//                     // sh "./qualityGate.sh admin password ${SONAR_URL} ${component}"
//                     //Instead of changing file permission to execute like above we can simply perform like below
//                     sh "bash qualityGate.sh admin password ${SONAR_URL} ${COMPONENT}"
//                     // Instead of of using ./ to execute any file, we can simply use bash so even if we dont have execution permission it will be executed 
//                 }   
//             }
//             stage('Unit Testing') {
//                 steps{
//                     echo "****** Unit Testing is Started for ${COMPONENT} ******"
//                     echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
//                     echo "****** Unit Testing is Completed for ${COMPONENT} ******"
//                 }   
//             }
//         }
//     }
// }
