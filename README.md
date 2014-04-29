jetty-embedded-template
=======================
This repository provides a basic structure of a web application built around an embedded Jetty. The main build artifact
is a directory structure with all the binaries and resources of the application, including an executable JAR and a startup
bash script.

Enjoy!


Quick Demo Instructions
-----------------------

### Make sure you have the following set on your system:

1. git client - we are on GitHub :)
2. Maven 3.x (installed and in your $PATH)
3. JAVA_HOME set to a Java 7 (or above) JDK
4. *bash* command-line interface is required in order to activate the "integration" profile, which runs integration tests. [GitBash](http://git-scm.com/download/win) is great!


### To build and start the application run the following from the command line:

1. $ git clone https://github.com/sha1n/jetty-embedded-template.git
2. $ cd jetty-embedded-template
3. $ mvn install
4. $ mvn exec:java -f ./launcher/pom.xml

### Use your browser (or any other HTTP client) to GET the following URLs:

#### http://localhost:8080/api/testResource/

* A sample JAX-RS based REST resource implementation
* Java class: org.juitar.web.rest.resources.TestResource

#### http://localhost:8080/

* A sample static HTML page
* File: index.html

#### http://localhost:8080/testServlet

* A sample Servlet 3 servlet implementation
* Java class: org.juitar.servlet.TestServlet
