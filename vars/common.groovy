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