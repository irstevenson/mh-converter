mh-converter
============

Simple maidenhead converter library with command line interface

# Introduction

The library is intended to provide a Groovy example of converting to and from [Maidenhead](en.wikipedia.org/wiki/Maidenhead_Locator_System) references. And while providing a library for use in other software, it does also provide a basic command line interface.

# Building
The system uses gradle for its building, and to date (30 June 2014) is know to build with gradle version 1.11 and java 1.8.0.

So to build, I'd suggest a simple:
```
$ gradle jar
```

This will place the resultant JAR in `build/libs`.

# Usage
## Command Line

Assuming you're in the root of the project and you've done the above `gradle jar` command, you will then be able to do:
```
java -jar build/libs/mh-converter-0.1.jar
```

That will in turn show you the usage information and be enough to get you going.

## As a library

There are three key places you can see how to use this as a library:
* By generating and reading the GroovyDoc
* By reading the tests in `src/test/groovy/ConverterSpec.groovy`
* By reading the command line interface source at `src/main/groovy/vk7is/mhconvert/CLI.groovy`
