def lintChecks(String component) {
    sh """echo ****** Starting Style Checks for ${component} ****** """
    //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
    sh """echo ****** Style Check are Completed for ${component} ******"""
    //  sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
    // We have used environment variable directly
}
// def call() {
// Above we are not catching the parameter value

def sonarChecks(String component) {
    echo "****** Starting Static Code Analysis for ${component} ******"    
    sh """
    sonar-scanner -Dsonar.host.url=http://172.31.39.131:9000 -Dsonar.sources=. -Dsonar.projectKey=${component} -Dsonar.login=admin -Dsonar.password=password
    """
    echo "****** Static Code Analysis is Completed for ${component} ******"

    }
def call(String component) {
        pipeline{
            agent{
                label 'ws'
        }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        helloWorld.info(component)
                        lintChecks(component)
                        // Below we are not passing any parameter to both methods/functions
                        // helloWorld.info()
                        // lintChecks()
                    }
                }
            }
            stage('Static Code Analysis') {
                steps{
                    script{
                        sonarChecks(component)
                    }
                }   
            }
            stage('Get the Sonar Analysis Result') {
                steps{
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate?ref_type=heads > qualityGate.sh"
                    sh "./qualityGate.sh admin password ${SONAR_URL} ${component}"
                }   
            }
            stage('Unit Testing') {
                steps{
                    echo "****** Unit Testing is Started for ${component} ******"
                    echo "****** Unit Testing is InProgress for ${component} ******"
                    echo "****** Unit Testing is Completed for ${component} ******"
                }   
            }
        }
    }
}