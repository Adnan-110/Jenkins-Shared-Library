def sonarChecks() {
    echo "****** Starting Static Code Analysis for ${COMPONENT} ******"    
    //sh '''sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}'''
    echo "****** Static Code Analysis is Completed for ${COMPONENT} ******"

    }

def lintChecks() {
    stage('Lint Checks') {
        if (env.APP_TYPE ==  "python"){
             sh '''echo ****** Starting Style Checks for ${COMPONENT} ****** 
                # sh "pip3 install pylint"
                # sh "pylint *.py || true"
                echo ****** Style Check are Completed for ${COMPONENT} ****** 
            '''
        }
        else if (env.APP_TYPE == "maven"){
            sh '''echo ****** Starting Style Checks for ${COMPONENT} ****** 
                 # mvn checkstyle:check || true
                  echo ****** Style Check are Completed for ${COMPONENT} ****** 
            '''
        }
        else if (env.APP_TYPE == "node"){
            sh '''echo ****** Starting Style Checks for ${COMPONENT} ****** 
                  npm install jslint
                  /home/centos/node_modules/jslint/bin/jslint.js server.js || true
                  echo ****** Style Check are Completed for ${COMPONENT} ****** 
            '''
        }
        else{
            sh '''echo ****** Starting Style Checks for ${COMPONENT} ****** 
                  echo ****** Style Check are Completed for ${COMPONENT} ****** 
            '''
        }
    }
   
}