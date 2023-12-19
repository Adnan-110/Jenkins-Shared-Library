def sonarChecks() {
    echo "****** Starting Static Code Analysis for ${COMPONENT} ******"    
    //sh '''sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}'''
    echo "****** Static Code Analysis is Completed for ${COMPONENT} ******"

    }

def lintChecks() {
    stage('Lint Checks') {
        if (env.APP_TYPE ==  "python"){
            echo " ****** Starting Style Checks for ${COMPONENT} ****** " 
            //  pip3 install pylint
            //  pylint *.py || true
            echo " ****** Style Check are Completed for ${COMPONENT} ****** " 
             
        }
        else if (env.APP_TYPE == "java"){
            echo " ****** Starting Style Checks for ${COMPONENT} ****** "
            //  mvn checkstyle:check || true
            echo " ****** Style Check are Completed for ${COMPONENT} ****** "
            
        }
        else if (env.APP_TYPE == "node"){
            echo " ****** Starting Style Checks for ${COMPONENT} ****** "
            // npm install jslint
            // /home/centos/node_modules/jslint/bin/jslint.js server.js || true
            echo " ****** Style Check are Completed for ${COMPONENT} ****** " 
        }
        else{
            echo " ****** Starting Style Checks for ${COMPONENT} ****** "
            echo " ****** Style Check are Completed for ${COMPONENT} ******" 
        }
    }
   
}