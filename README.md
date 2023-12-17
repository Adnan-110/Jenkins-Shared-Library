# jenkins-shared-library

This is a repository to host all the common patterns that arise during the CI/CD of roboshop components development.

```
This jenkins-shared-libary vars/ is going to host all the common stages and we are going to import this library inturn the functions to keep the code DRY.
```

Ref : https://www.jenkins.io/doc/book/pipeline/shared-libraries/


### Why Shared Libraries In Jenkins ?

```
    As Pipeline is adopted for more and more projects in an organization, common patterns are likely to emerge. Oftentimes it is useful to share parts of Pipelines between various projects to reduce redundancies and keep code "DRY" [1].

    Pipeline has support for creating "Shared Libraries" which can be defined in external source control repositories and loaded into existing Pipelines.

```

### Shared Library Structure

Directory structure
The directory structure of a Shared Library repository is as follows:

```
    (root)
    +- src                     # Groovy source files
    |   +- org
    |       +- foo
    |           +- Bar.groovy  # for org.foo.Bar class
    +- vars
    |   +- foo.groovy          # for global 'foo' variable
    |   +- foo.txt             # help for 'foo' variable
    +- resources               # resource files (external libraries only)
    |   +- org
    |       +- foo
    |           +- bar.json    # static helper data for org.foo.Bar
```

>>> The src directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.

>>> The vars directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline. So if you had a file called vars/log.groovy with a function like def info(message)…​ in it, you can access this function like log.info "hello world" in the Pipeline. You can put as many functions as you like inside this file. Read on below for more examples and options.


### Security Scans 
```

    Scans are of 2 types :
        1) SAST   ( Analysing the code )
        2) DAST   ( Analysing the application through the endPoint / pen testing )

```
### Why do I need Static Code Analysis ( SAST : SonarQube )

```
    1) Determinsed the overall code quality.
    2) Identifies the hotSpots in the code ( Hotspot : Any sensitivie information in the code ).
    3) Code Standards and the package versions to be used can be controlled.
    4) Identifes the duplicate patterns in the code.
    5) Overall Code Quality Standard.
```

### SonarQube can be utilized in any of the 2 ways 

```
    1) Create your own server and set-up sonarQube on the top of that server ( Paid tool : 1 month : In free version, you'd get embedded Portgress DB ) 

    2) You can use SAS offering ( You don't have to host anything locally )
```


### Maven Goals

A Maven phase represents a stage in the Maven build lifecycle. Each phase is responsible for a specific task.

Here are some of the most important phases in the default build lifecycle:


```
    validate: check if all information necessary for the build is available
    compile: compile the source code
    test-compile: compile the test source code
    test: run unit tests
    package: package compiled source code into the distributable format (jar, war, …)
    integration-test: process and deploy the package if needed to run integration tests
    install: install the package to a local repository
    deploy: copy the package to the remote repository
```


### How to do Unit Testing & Ingeration Testing

```
Both these test cases are typically placed in the code where you applications is hosted. And can be called by the same build tool using test or verify.

If you're node based project :
    ex : npm test    [ Unit Testing ]
         npm verify  [ Integration Testing]

```


### How to add your Jenkins Job The Ability To Run the Job From a particular branch or from Tag ?

```

```


### What is the versioning strategy we are going through ???/

```
    We are going with Git Semantic Versioning
```


### How to create a Git Tag ?

```
 Tags are typically created against MAIN Branch only.

    $ git tag 0.0.0
    $ git push --tags
```



### When Artifacts has to be prepared and when they are supposed to be uploaded ??? 

```
    1) Only then the TAG_NAME or verison of the artifact that you're trying to upload is not available on Nexus.
    2) That means, even before you prepare the ARTIFACT, let's have a stage to validate against NEXUS on the availability of that particular verison.
    3) We need to ensure that BUILD and UPLOAD of artifacts will happen in the event of unavailability of that verison on NEXUS.
```