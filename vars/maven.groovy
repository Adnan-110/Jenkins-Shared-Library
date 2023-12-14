    def lintChecks(String component) {
        sh """echo ****** Starting Style Checks for ${component} ****** """
        //  sh "echo ****** Starting Style Checks for ${COMPONENT} ******"
        sh "mvn checkstyle:check || true"
        sh """echo ****** Style Check are Completed for ${component} ******"""
        //  sh "echo ****** Style Check are Completed for ${COMPONENT} ******"
        // We have used environment variable directly
    }
    // def call() {
    // Above we are not catching the parameter value
    def call(String component) {
            pipeline{
                agent{
                    label 'ws'
            }
            tools{  // This option will make build tools available only for this single run and will not install permanently.
            maven 'maven-390' // In This way we configured in Jenkins->mangage->Tools section that whenever maven-390 is passed make maven-3.9.0 version available
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
                        sh "echo ****** Starting Static Code Analysis ******"
                    }   
                }
            }
        }
    }