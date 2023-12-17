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
            stage('Unit Testing') {
                steps{
                    echo "****** Unit Testing is Started for ${COMPONENT} ******"
                    echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
                    echo "****** Unit Testing is Completed for ${COMPONENT} ******"
                }   
            }
        }
    }
}