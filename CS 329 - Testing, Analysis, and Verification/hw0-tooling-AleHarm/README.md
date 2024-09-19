# Intro

The goal of this homework is to install the core tooling that is used throughout the semester and to learn its basic functionality: build (Maven), revision control (Git Feature Branch Workflow), code documentation (JavaDoc), lint-ing (CheckStyle), and logging (SLF4J). Many of the steps require research to complete and lack specificity. Use the TAs, Slack, and peers early rather than spend hours trying to figure something out. Tooling is a pain, but once it works, it is amazing.

At the end of this homework, a student should be able to:

   * Manage code development with Git and the feature branch workflow
   * Configure and use Maven to manage the build lifecycle of a project
   * Write JavaDocs and run JavaDoc via Maven to generate documentation
   * Run CheckStyle via Maven to lint source code
   * Add, via Maven, and use SLF4J logging with the logback binding to the console

# Pre-requisites

This homework requires Git and Maven be available in a command line interface (CLI): `git` and `mvn`. The easiest solution is to use [Docker](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/docker.md). Docker is the preferred solution for the course, and it is what is used in class for lectures and for grading.

That said, it is still mightily convenient to have both `git` and `mvn` installed locally. Git is commonly available by default in Linux and Mac OSX. Windows provides several options. Maven is most easily installed with a package manager (e.g., [Homebrew Cask](https://brew.sh/) for Mac OSX). There is no single preferred shell for the CLI as long as `git` and `mvn` are runnable from the prompt.

The course does not rely on any single platform; the tools are available to Windows, OS X, and Linux. Windows users might consider using [Windows Subsystem for Linux](https://en.wikipedia.org/wiki/Windows_Subsystem_for_Linux) ([Installation guide](https://docs.microsoft.com/en-us/windows/wsl/install-win10), [Using VS Code with WSL](https://code.visualstudio.com/docs/remote/wsl)). OS X and Linux are fairly straightforward with available package managers.

This course is Java based and requires a Java 17 JDK. As with the other tools, the JDK is most easily installed with a package manager. The `javac` and `java` tools should be available from the CLI for `mvn`.

The course is IDE agnostic so that students are free to use any preferred IDE or editor. Maven will manage the build lifecycle for the homework and projects in the CLI as mentioned previously. Students are welcome to use the GUI features in a preferred IDE, but all the grading and support is via CLI with `git` and `mvn`. Students are expected to be proficient with these tools from the CLI. General IDE issues are considered outside the scope of TA and instructor support. Students have been successful using [VS Code](https://code.visualstudio.com/), [IntelliJ](https://www.jetbrains.com/idea/), and [Eclipse](https://www.eclipse.org/downloads/packages/).

# Homework

## Part I: Feature Branch Workflow in Git

**Requirement:** create a feature branch for your homework submission in your local copy of the 'hw0-tooling' repository to later use in a pull request as your homework submission.

Study the [feature branch workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow). The repository created by the invitation request from GitHub Classroom is the central repository. The master branch on the central repository is the latest state of the homework (note: the repo only contains this readme file).

Complete this part of the homework by creating a feature branch for the homework in your local repository. At the end of this homework, the local feature branch will be pushed to the central repository and then used to create a pull request. Be sure branch names reflect their intended purpose.

Along the way, please be mindful of the commit contents and comments. Commits should be self-contained and reflect a set of changes that logically belong together. Commit messages are expected to adhere to these [commit message guidelines](https://gist.github.com/robertpainsi/b632364184e70900af4ab688decf6f53).

## Part II: Build Lifecycle Management with Maven

**Requirement:** 1) grab some Java code (or use the Dijkstra code [here](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/maven-lint-javadoc-git/)) and then 2) create a `pom.xml` file for Maven (details below) such that `mvn compile` followed by [mvn exec:java](http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/) with the appropriate main method class specified runs the Java code as expected using Java 17.

1) Track down some Java code that you wrote from a previous class, or write some Java code for this homework. The amount or size of the code should be no more than a few hundred lines of code and not tens or thousands of lines of code (i.e., the code should be small, but not ridiculously small as in *Hello World*). There should be at least two classes as part of the code. Also consider using the Dijkstra code found in the [BitBucket lecture notes repository](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/maven-lint-javadoc-git/).

2) There are many ways to create the `pom.xml` file. [Your First Maven Project](http://tutorials.jenkov.com/maven/your-first-maven-project.html) tutorial is a good starting point. [Maven Archetypes](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) for Java make it even easier. Any of the following archetypes work for the homework: [maven-archetype-quickstart-java11](https://github.com/EmilianoFraga/maven-archetype-quickstart-java11), [maven-archetype-simple](https://maven.apache.org/archetypes/maven-archetype-simple/), or [maven-archetype-quickstart](https://maven.apache.org/archetypes/maven-archetype-quickstart/) -- just make sure you replace the code file names with *your* code names. Even more archetypes can by explored with `mvn archetype:generate -Dfilter=java`. Try out a few to see what different options are given and how those affect the final `pom.xml` file.

This part of the homework is complete when there exists a `pom.xml` in the same directory as this `README.md` file that is able to build, package, and run the code. Building the code is accomplished with `mvn compile` or `mvn package`. The [mvn exec:java](http://www.vineetmanohar.com/2009/11/3-ways-to-run-java-main-from-maven/) command expects the main class method to be specified as an argument (e.g., `-Dexec.mainClass="edu.byu.cs329.hw0.Main"`). The `pom.xml` file is not required to do anything else. More actions in the build process will be added later. Be sure to use sensible group (e.g., `edu.byu.cs329`) and artifact IDs.

As a note, some Maven archetypes configure plugins in the [Plugin Management](https://maven.apache.org/pom.html#Plugin_Management) section of the `pom.xml` file. Stack Overflow has a nice discussion about [plugin management in Maven](https://stackoverflow.com/questions/10483180/what-is-pluginmanagement-in-mavens-pom-xml) that is worth reading before leaving this section of the homework. In short, the `Plugin Management` section configures plugins for builds that inherit the current `pom.xml` file (e.g., projects in sub-directories). These plugins must be referenced in some element of the current `pom.xml` to be active in the current build.

Some of the plugins in the `pom.xml` file need to be in the `reporting` tag as well as the `build` tag, especially when using the `mvn site` command.

Maven is an intimidating tool and does take some time to learn (which is some of the goal of this homework), so try some experiments and play around with it. As always, be patient and do not be afraid to ask for help!

## Part III: Code Documentation with JavaDoc

**Requirement:** [JavaDoc](https://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) a class in the source code, add the [Apache Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html) to the `pom.xml` file, and use Maven to generate the actual docs.

It is expected that all code is documented via [JavaDoc](https://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) following the Java Software Oracle [style guide](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html). There is a good example at the end of the [style guide](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html) that represents what is expected for the course:

  * All public classes, methods, and fields should have JavaDocs
  * Methods should minimally use the `@param` and `@return` in addition to the overview
  * Classes should minimally indicate `@author` in addition to the overview
  * The first line for any method should be a summary statement similar to a commit message

JavaDoc one of the class files in the project.

The actual docs are generated using the [Apache Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html). Configure the `pom.xml` file to generate the docs in reporting as a part of the `mvn site` build life cycle. Be sure the docs are correctly generated with no warnings.

As a note, the JavaDoc plugin will need to be configured for Java 17, and the [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/) may be required in the `build` section of the `pom.xml` file for the `mvn site` target to complete.

## Part IV: Lint-ing with CheckStyle

**Requirement:** integrate [CheckStyle](https://checkstyle.sourceforge.io/) with the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) into the Mavin build life cycle using the [Maven CheckStyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/) so that the build fails in the validate phase if [CheckStyle](https://checkstyle.sourceforge.io/) does not pass.

Static code analysis is known to reduce code defects. This course uses [CheckStyle](https://checkstyle.sourceforge.io/) with the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) via the [Maven CheckStyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/). [Configure](https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html) the plugin to fail the build in the [validate phase](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) when CheckStyle does not pass. Select the [Google rule set](https://checkstyle.sourceforge.io/google_style.html) by setting the `configlocation` tag in the `configuration` section to **google_checks.xml**.

## Part V: Logging with SLF4J and log

**Requirement:** add logging to the Java code using the [SLF4J](https://www.slf4j.org/) API via the [Log4j2](https://logging.apache.org/log4j/2.x/) binding with a configuration file for [Log4j2](https://logging.apache.org/log4j/2.x/) that outputs to console all logging on *ERROR*, *WARN*, *INFO*, *DEBUG*, and *TRACE*.

Logging is an important part of debugging and fault isolation for removing code defects. All console output related to program state should go through the logger in this course at an appropriate log level:

   * *ERROR*: designates error events that might still allow the application to continue
   * *WARN*: designates potentially harmful situations
   * *INFO*: designates high-level course grained informational messages that highlight progress
   * *DEBUG*: designates fine-grained informational events that are most useful to debugging
   * *TRACE*: designates even finer-grained informational events that the *DEBUG* level

The logger enables the programmer to output fine-grained to coarse-grained program events at appropriate log levels. A simple configuration file turns on/off log levels depending on the intended task or use of the logging.  As such, it is no longer necessary to track down and remove errant console output from operational code relating to debugging efforts---just turn off the logger.

Complete this part of the homework by adding the needed dependencies to the `pom.xml` file to use the [Log4j2](https://logging.apache.org/log4j/2.x/) implementation of [SLF4J](https://www.slf4j.org/). Configure [Log4j2](https://logging.apache.org/log4j/2.x/) to output to console from log-level *TRACE* up to log-level *ERROR* (that should cover all level defined above). Add logging to the Java code to output at all the levels.

## Part VI: .gitignore

**Requirement:** add a `.gitignore` file to excludes any build artifacts and IDE project artifacts from revision control.

Version control is most effective when it only tracks and reports files that are pertinent to the build and deployment of the project. It can be confusing when version control constantly reports information on non-essential files. It is expected that students include an appropriate `.gitignore` file in all project repositories to only track important files and artifacts.

Complete this part of the homework by creating and adding a `.gitignore` file to the project (if it has yet to be added) that ignores build artifacts, IDE project files, and any other files not essential to building the project.

## Part VII: Create a Pull Request

**Requirement:** pushing your local feature branch to the central repository and then create a [pull request](https://help.github.com/en/articles/creating-a-pull-request). Pull requests are expected to roughly follow this [style guide](https://www.braintreepayments.com/blog/effective-pull-requests-a-guide/). Be sure to directly @-reference the instructor for notification.

Upon uploading of your pull request, GitHub will give you a sanity check by running `mvn verify site` on your code.
Note that passing the build *does not* mean that you will get full credit for the assignment.
Please reread this writeup to make sure you have completed all the requirements, and refer to the grading rubric on Canvas for details on grading.
