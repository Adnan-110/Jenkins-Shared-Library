def lintChecks() {
    sh """echo ****** Starting Style Checks for ${component} ****** """
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
    sh """echo ****** Style Check are Completed for ${component} ******"""
}
def call() {
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