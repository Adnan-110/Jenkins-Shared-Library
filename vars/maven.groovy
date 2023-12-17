def lintChecks() {
    sh """echo ****** Starting Style Checks for ${COMPONENT} ****** """
    //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
    sh "mvn checkstyle:check || true"
    sh """echo ****** Style Check are Completed for ${COMPONENT} ******"""
    //  sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
    // We have used environment variable directly
}

def call() {
    pipeline{
        agent{
            label 'ws'
        }
        tools{  // This option will make build tools available only for this single run and will not install permanently.
            maven 'maven-390' // In This way we configured in Jenkins->mangage->Tools section that whenever maven-390 is passed make maven-3.9.0 version available
        }
        environment{
            SONAR_CRED = credentials('SONAR_CRED')
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
            stage('Compiling Java Code'){
                steps{
                    sh "mvn clean compile"
                    sh "ls -lrth target/"
                }
            }
            stage('Static Code Analysis') {
                steps{
                   script{
                    env.ARGS="-Dsonar.java.binaries=./target/"
                    common.sonarChecks()
                   }
                }
            }
            stage('Get the Sonar Analysis Result') {
                steps{
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate?ref_type=heads > qualityGate.sh"
                    sh "bash qualityGate.sh admin password ${SONAR_URL} ${COMPONENT}"
                }
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps{
                            sh "env"
                            echo "****** Unit Testing is Started for ${COMPONENT} ******"
                            // sh "mvn test"
                            echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Unit Testing is Completed for ${COMPONENT} ******"
                        }   
                    }
                    stage('Integration Testing') {
                        steps{
                            echo "****** Integration Testing is Started for ${COMPONENT} ******"
                            // sh "mvn verify"
                            echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Integration Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                    stage('Functional Testing') {
                        steps{
                            echo "****** Functional Testing is Started for ${COMPONENT} ******"
                            // sh "mvn function"
                            echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Functional Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                }
            }
            stage('Prepare Artifacts for ${COMPONENT}'){
                steps{
                    echo "****** Artifacts Preparation is Started for ${COMPONENT} ******" 
                    echo "****** Artifacts Preparation is InProgress for ${COMPONENT} ******" 
                    echo "****** Artifacts Preparation is Completed for ${COMPONENT} ******" 
                }
            }
            stage('Uploading Artifacts for ${COMPONENT}'){
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
//         sh """echo ****** Starting Style Checks for ${COMPONENT} ****** """
//         //  sh "echo ****** Starting Style Checks for ${component} ******"
//         sh "mvn checkstyle:check || true"
//         sh """echo ****** Style Check are Completed for ${COMPONENT} ******"""
//         //  sh "echo ****** Style Check are Completed for ${component} ******"
//         // We have used environment variable directly
//     }
//     // Above we are not catching the parameter value
   
//     def call() {
//             pipeline{
//                 agent{
//                     label 'ws'
//             }
//             environment{
//                 SONAR_CRED = credentials('SONAR_CRED')
//             }
//             tools{  // This option will make build tools available only for this single run and will not install permanently.
//             maven 'maven-390' // In This way we configured in Jenkins->mangage->Tools section that whenever maven-390 is passed make maven-3.9.0 version available
//         }
//             stages{
//                 stage('Lint Checks') {
//                     steps {
//                         script{
//                             helloWorld.info(COMPONENT)
//                             lintChecks()
//                             // Below we are not passing any parameter to both methods/functions
//                             // helloWorld.info()
//                             // lintChecks()
//                         }
//                     }
//                 }
//             stage{
//                 steps {
//                     sh "mvn clean compile"
//                     sh "ls -lrth target/"
//                 }
//             }
//             stage('Static Code Analysis') {
//                 steps{
//                     script{
//                         env.ARGS="-Dsonar.java.binaries=./target/"
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
//                     sh "bash qualityGate.sh admin password ${SONAR_URL} ${component}"
//                     // Instead of of using ./ to execute any file, we can simply euse bash so even if we dont have execution permission it will be executed 
//                 }   
//             }
//             stage('Unit Testing') {
//                 steps{
//                     echo "****** Unit Testing is Started for ${COMPONENT} ******"
//                     echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
//                     echo "****** Unit Testing is Completed for ${COMPONENT} ******"
//                 }   
//             }
//          }
//         }
//     }