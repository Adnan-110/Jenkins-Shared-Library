def lintChecks() {
    sh "echo ****** Starting Style Checks ****** "
    sh "npm install jslint"
    sh "/home/centos/node_modules/jslint/bin/jslint.js server.js || true"
                sh "echo ****** Style Check are Completed ******"
}