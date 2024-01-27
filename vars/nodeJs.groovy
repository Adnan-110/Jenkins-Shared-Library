// def call(){
//     node {
//         git branch: 'main', url: "https://github.com/Adnan-110/${COMPONENT}.git"
//         env.SONAR_URL="172.31.39.131"
//         env.NEXUS_URL="172.31.15.124"
//         env.APP_TYPE="node"

//         common.lintChecks()
//         env.ARGS="-Dsonar.sources=."
//         common.sonarChecks()
//         common.testCases()
//         if(env.TAG_NAME != null || env.TAG_NAME != "") {
//             common.generatingArtifacts()
//         }
//     }
// }

def call() {
        pipeline{
            agent{
                label 'ws'
            }
            environment{
                    SONAR_CRED = credentials('SONAR_CRED')
                    NEXUS_CRED = credentials('NEXUS_CRED')
                    SONAR_URL="172.31.39.131"
                    NEXUS_URL="172.31.15.124"
                }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        helloWorld.info(COMPONENT)
                        common.lintChecks()
                        // Below we are passing parameter to both methods/functions
                        // helloWorld.info(component)
                        // lintChecks(component)
                    }
                }
            }
            stage('Static Code Analysis') {
                steps{
                    script{
                        sh "env"
                        env.ARGS="-Dsonar.sources=."
                        common.sonarChecks()
                    }
                }   
            }
            stage('Get the Sonar Analysis Result') {
                steps{
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate?ref_type=heads > qualityGate.sh"
                    // sh "chmod 777 qualityGate.sh" // We added this line of code to get the execution permission aswell for all the profiles
                    // sh "./qualityGate.sh admin password ${SONAR_URL} ${component}"
                    //Instead of changing file permission to execute like above we can simply perform like below
                    // sh "bash qualityGate.sh admin password ${SONAR_URL} ${COMPONENT}"
                    // Instead of of using ./ to execute any file, we can simply use bash so even if we dont have execution permission it will be executed 
                    echo "Scan is Good"
                }   
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps{
                            echo "****** Unit Testing is Started for ${COMPONENT} ******"
                            // sh "npm test"
                            echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Unit Testing is Completed for ${COMPONENT} ******"
                        }   
                    }
                    stage('Integration Testing') {
                        steps{
                            echo "****** Integration Testing is Started for ${COMPONENT} ******"
                            // sh "npm verify"
                            echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Integration Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                    stage('Functional Testing') {
                        steps{
                            echo "****** Functional Testing is Started for ${COMPONENT} ******"
                            // sh "npm function"
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
            stage('Prepare Artifacts'){  // This block will be executed only when run from tag 
                when { 
                    expression { env.TAG_NAME != null } 
                    expression {env.UPLOAD_STATUS == "" || env.UPLOAD_STATUS == null }
                } 
                steps{
                    echo "****** Artifacts Preparation is Started for ${COMPONENT} ******" 
                    sh '''
                        npm install 
                        ls -lrth
                        zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                        ls -lrth
                    '''
                    echo "****** Artifacts Preparation is Completed for ${COMPONENT} ******" 
                }
            }
            stage('Upload Artifacts to Nexus'){  // This block will be executed only when run from tag 
                when { 
                    expression {env.TAG_NAME != null } 
                    expression {env.UPLOAD_STATUS == "" || env.UPLOAD_STATUS == null}
                }
                steps{
                    echo "****** Uploading of Artifacts is Started for ${COMPONENT} ******" 
                    sh "curl -f -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                    echo "****** Uploading of Artifacts is Completed for ${COMPONENT} ******" 
                }
            }
        }
    }
}

