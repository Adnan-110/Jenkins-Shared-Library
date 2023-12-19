def call() {
    pipeline{
        agent{
            label 'ws'
        }
        environment{
            SONAR_CRED = credentials('SONAR_CRED')
            NEXUS_CRED = credentials('NEXUS_CRED')
            SONAR_URL="172.31.39.131"
                    NEXUS_URL="172.31.86.85"
        }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        helloWorld.info(COMPONENT)
                        common.lintChecks()
                    }
                }
            }
            stage('Static Code Analysis') {
                steps{
                   script{
                    env.ARGS="-Dsonar.sources=."
                    common.sonarChecks()
                   }
                }
            }
            stage('Get the Sonar Analysis Result') {
                steps{
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate?ref_type=heads > qualityGate.sh"
                    // sh "bash qualityGate.sh admin password 172.31.39.131 ${COMPONENT}"
                    echo "Scan is Good"
                }
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps{
                            sh "env"
                            echo "****** Unit Testing is Started for ${COMPONENT} ******"
                            // sh "python -m unittest"
                            echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Unit Testing is Completed for ${COMPONENT} ******"
                        }   
                    }
                    stage('Integration Testing') {
                        steps{
                            echo "****** Integration Testing is Started for ${COMPONENT} ******"
                            // sh "python -m unittest"   dont know cmd for integration testing in python
                            echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Integration Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                    stage('Functional Testing') {
                        steps{
                            echo "****** Functional Testing is Started for ${COMPONENT} ******"
                            // sh "python -m unittest"   dont know cmd for Functional testing in python
                            echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Functional Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                }
            }
            stage('Checking Artifacts Availability on Nexus'){  // This block will be executed only when run from tag 
                when { expression { env.TAG_NAME != null } } 
                steps{
                    echo "****** ${COMPONENT} Artifacts Availabilty checking is Started ******"
                    script{
                        env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
                    }
                    echo UPLOAD_STATUS
                    echo "****** ${COMPONENT} Artifacts Availabilty checking is Completed ******"
                }
            }
            stage('Prepare Artifacts for ${COMPONENT}'){ // This block will be executed only when run from tag
                when { 
                    expression { env.TAG_NAME != null } 
                    expression {env.ARTIFACTS_AVAILABILITY == "" }
                } 
                steps{
                    echo "****** Artifacts Preparation is Started for ${COMPONENT} ******" 
                    echo "****** Artifacts Preparation is InProgress for ${COMPONENT} ******" 
                    echo "****** Artifacts Preparation is Completed for ${COMPONENT} ******" 
                }
            }
            stage('Uploading Artifacts for ${COMPONENT}'){ // This block will be executed only when run from tag
                when { 
                    expression { env.TAG_NAME != null } 
                    expression {env.ARTIFACTS_AVAILABILITY == "" }
                } 
                steps{
                    echo "****** Uploading of Artifacts is Started for ${COMPONENT} ******" 
                    echo "****** Uploading of Artifacts is InProgress for ${COMPONENT} ******" 
                    echo "****** Uploading of Artifacts is Completed for ${COMPONENT} ******" 
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
