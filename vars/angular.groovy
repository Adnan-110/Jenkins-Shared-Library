def lintChecks() {
    sh """echo ****** Starting Style Checks for ${COMPONENT} ****** """
    //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
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
        environment{
            SONAR_CRED = credentials('SONAR_CRED')
        }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        helloWorld.info(COMPONENT)
                        lintChecks()
                        // Below we are passing parameter to both methods/functions
                        // helloWorld.info(component)
                        // lintChecks(component)
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
                    sh "bash qualityGate.sh admin password ${SONAR_URL} ${COMPONENT}"
                }   
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps{
                            sh "env"
                            echo "****** Unit Testing is Started for ${COMPONENT} ******"
                            // sh "mvn test" dont know cmd for Unit testing in javscript 
                            echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Unit Testing is Completed for ${COMPONENT} ******"
                        }   
                    }
                    stage('Integration Testing') {
                        steps{
                            echo "****** Integration Testing is Started for ${COMPONENT} ******"
                            // sh "mvn verify" dont know cmd for Integration testing in javscript
                            echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Integration Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                    stage('Functional Testing') {
                        steps{
                            echo "****** Functional Testing is Started for ${COMPONENT} ******"
                            // sh "mvn function" dont know cmd for Functional testing in javscript
                            echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
                            echo "****** Functional Testing is Completed for ${COMPONENT} ******"
                        }
                    }
                }
            }
            stage('Prepare Artifacts for ${COMPONENT}'){  //Runs only when u run this job from tag and from branches it should not run
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