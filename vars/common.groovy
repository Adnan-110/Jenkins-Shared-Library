def sonarChecks() {
    echo "****** Starting Static Code Analysis for ${COMPONENT} ******"    
    sh """
    sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password
    """
    echo "****** Static Code Analysis is Completed for ${COMPONENT} ******"

    }