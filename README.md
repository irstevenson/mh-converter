mh-converter
============

Simple maidenhead converter library with command line interface

# Introduction

The library is intended to provide a Groovy example of converting to and from
[Maidenhead](en.wikipedia.org/wiki/Maidenhead_Locator_System) references. And while providing a
library for use in other software, it does also provide a basic command line interface.

# Building
The system uses gradle for its building, and to date (2 July 2014) is know to build with gradle
version 1.12 and java 1.8.0_05.

Included though is the gradle wrapper, so you should only need to have the JDK installed.

So to build, I'd suggest a simple:
```
$ gradlew jar
```

This will place the resultant JAR in `build/libs`.

## Documentation
If you'd like to build the documentation:
```
$ gradlew groovydoc
```
The results can then be found in `build/docs/groovydoc`.

# Usage
## Command Line

Assuming you're in the root of the project and you've done the above `gradle jar` command, you will
then be able to do:
```
java -jar build/libs/mh-converter-0.1.jar
```

That will in turn show you the usage information and be enough to get you going.

## As a library

There are three key places you can see how to use this as a library:
* By generating and reading the GroovyDoc
 * Details above on how to do this. Note: a bit of work is still required on the GroovyDoc, but there should be enough there to get you going - especially when combined with the below.
* By reading the tests in `src/test/groovy/ConverterSpec.groovy`
* By reading the command line interface source at `src/main/groovy/vk7is/mhconvert/CLI.groovy`
