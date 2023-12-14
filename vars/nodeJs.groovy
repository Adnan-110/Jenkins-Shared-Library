def lintChecks(String component) {
    sh """echo ****** Starting Style Checks for ${component} ****** """
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
    sh """echo ****** Style Check are Completed for ${component} ******"""
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
                        helloWorld.info("component")
                        nodeJs.lintChecks("component")
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