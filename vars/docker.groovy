def call() {
    node {
        git branch: 'main', url: "https://github.com/Adnan-110/${COMPONENT}.git"
        common.lintChecks()
        if(env.TAG_NAME != null) {
            stage('Generating & Pushing Artifacts') {
                if(env.APP_TYPE == "node") {
                    sh "echo Generating Artifacts For ${COMPONENT}"
                    sh "npm install"
                }    
                else if(env.APP_TYPE == "python") {
                    sh "echo Generating Artifacts For ${COMPONENT}"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py  *.ini requirements.txt"
                } 
                else if(env.APP_TYPE == "java") {
                    sh "echo Generating Artifacts For ${COMPONENT}"
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                } 
                else if(env.APP_TYPE == "angular") {
                    sh "echo Generating Artifacts For ${COMPONENT}"
                    sh "cd static/"       
                    sh "zip -r ../${COMPONENT}-${TAG_NAME}.zip *"
                } 
                else {
                    sh "Selected Component Doesn't exist"
                    }
                }
                sh "wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
                sh "docker build -t 093842890430.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."
                sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 093842890430.dkr.ecr.us-east-1.amazonaws.com"
                sh "docker push 093842890430.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"                
            }
        }
    }