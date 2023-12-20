def sonarChecks() {
    stage('Sonar Checks') {
        echo "****** Starting Static Code Analysis for ${COMPONENT} ******"    
        //sh '''sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}'''
        echo "****** Static Code Analysis is Completed for ${COMPONENT} ******"
    }
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
// below given all 3 ways works for parallel execution in jenkins scripted pipeline

// def testCases() {
//     stage('Testing') {
//         parallel(
//             'Unit_Testing': {
//                 echo "****** Unit Testing is Started for ${COMPONENT} ******"
//                 // sh "npm test"
//                 echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Unit Testing is Completed for ${COMPONENT} ******"
//             },
//             'Integration_Testing': {
//                 echo "****** Integration Testing is Started for ${COMPONENT} ******"
//                 // sh "npm verify"
//                 echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Integration Testing is Completed for ${COMPONENT} ******"
//             },
//             'Functional_Testing': {
//                 echo "****** Functional Testing is Started for ${COMPONENT} ******"
//                 // sh "npm function"
//                 echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Functional Testing is Completed for ${COMPONENT} ******"
//             }
//         )
//     }
// }

// def testCases() {
//     stage('Testing') {
//         parallel([
//             Unit_Testing: {
//                 echo "****** Unit Testing is Started for ${COMPONENT} ******"
//                 // sh "npm test"
//                 echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Unit Testing is Completed for ${COMPONENT} ******"
//             },
//             Integration_Testing: {
//                 echo "****** Integration Testing is Started for ${COMPONENT} ******"
//                 // sh "npm verify"
//                 echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Integration Testing is Completed for ${COMPONENT} ******"
//             },
//             Functional_Testing: {
//                 echo "****** Functional Testing is Started for ${COMPONENT} ******"
//                 // sh "npm function"
//                 echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
//                 echo "****** Functional Testing is Completed for ${COMPONENT} ******"
//             }
//         ])
//     }
// }

def testCases(){
    stage('Testing') {
        def stages = [:]

        stages['Unit_Testing'] = {
            echo "****** Unit Testing is Started for ${COMPONENT} ******"
            // sh "npm test"
            echo "****** Unit Testing is InProgress for ${COMPONENT} ******"
            echo "****** Unit Testing is Completed for ${COMPONENT} ******"
            }
        
        stages['Integration_Testing'] = {
            echo "****** Integration Testing is Started for ${COMPONENT} ******"
            // sh "npm verify"
            echo "****** Integration Testing is InProgress for ${COMPONENT} ******"
            echo "****** Integration Testing is Completed for ${COMPONENT} ******" 
        }

        stages['Functional_Testing'] = {
            echo "****** Functional Testing is Started for ${COMPONENT} ******"
            // sh "npm function"
            echo "****** Functional Testing is InProgress for ${COMPONENT} ******"
            echo "****** Functional Testing is Completed for ${COMPONENT} ******"
        }

        parallel(stages)
    }
}

def generatingArtifacts() {
    stage('Checking Artifacts Availability on Nexus'){
        env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print UPLOAD_STATUS
    }
    if(env.UPLOAD_STATUS == "" || env.UPLOAD_STATUS == null){
        stage('Generating the Artifacts') {
            echo "Generating the ${COMPONENT} Artifacts"

            if(env.APP_TYPE == "node") {
                sh "npm install"
                sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
            }
            else if(env.APP_TYPE == "java"){
                sh "mvn clean package"
                sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
            }
            else if(env.APP_TYPE == "python"){
                sh "ls -ltr"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py  *.ini requirements.txt"
                sh "ls -ltr"
            }
            else if(env.APP_TYPE == "angular"){
                sh '''
                    cd static/
                    zip -r  ../${COMPONENT}-${TAG_NAME}.zip *
                    ls -lrth
                '''
            }
        }

        stage('Uploading the Artifacts to Nexus') {
            withCredentials([usernamePassword(credentialsId: 'NEXUS_CRED', passwordVariable: 'NEXUS_CRED_PSW', usernameVariable: 'NEXUS_CRED_USR')]) {
                sh "echo Uploading the ${COMPONENT} Artifacts to Nexus"
                sh "curl -f -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip  http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                sh "echo Upload of ${COMPONENT} Artifacts to Nexus Completed"
            }
        }
    
    }
}