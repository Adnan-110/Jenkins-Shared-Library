def lintChecks(String component) {
    // sh """echo ****** Starting Style Checks for ${component} ****** """
     sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
    // sh """echo ****** Style Check are Completed for ${component} ******"""
     sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
    // We have used environment variable directly
}
def call() {
// Above we are not catching the parameter value
// def call(String component) {
        pipeline{
            agent{
                label 'ws'
        }
        stages{
            stage('Lint Checks') {
                steps {
                    script{
                        // helloWorld.info(component)
                        // lintChecks(component)
                        helloWorld.info()
                        lintChecks()
                    }
                }
            }
            stage('Static Code Analysis') {
                steps{
                    sh "echo ****** Starting Static Code Analysis ******"
                }   
            }
        }
    }
}