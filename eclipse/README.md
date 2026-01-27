# MyBatis Generator Eclipse Feature

This repository contains the source code for the Eclipse feature and plugins for MyBatis Generator.

## Interactive Development
The project is buit using Tycho - which is a Maven extension specifically for Eclpise builds. But these are not "regular" Maven projects.
Tycho is a thin layer over the native Eclipse project definition. Several of the projects are "pomless" meaning that Tycho knows how to build
the project without a `pom.xml`. If there is a `pom.xml` in any project, it is because we are doing something that can't be accomplished
directly with the Eclipse project structure (like build the JavaDoc in the main project, and then copying it into the docs project).

To work on the project in Eclipse, import all the projects in this directory as "existing projects". DO NOT import them as Maven projects.

Once the projects are imported, activate the latest `.target` file in the `releng` directory (open the file and press "set as active target platform").

If you open this directory in VS Code, then VS code will try to setup all the Maven stuff - which will break things.

## Build with Maven
Before you build with Maven, make sure you have a toolchain setup. This is setup by adding a `toolchains.xml` file in your `~/.m2` directory.
Here's an example:

```xml
<?xml version="1.0" encoding="UTF8"?>
<toolchains>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>17</version>
      <vendor>temurin</vendor>
    </provides>
    <configuration>
      <jdkHome>/Users/xxx/.sdkman/candidates/java/17.0.16-tem</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

Use Maven to build a new version of the project for publishing. There are two helpful commands:

Use this command to make sure the build works:
```shell
./mvnw clean integration-test
```

Use this command to build the project and create a new version that can be published:
```shell
./mvnw -Prelease-composite clean integration-test
```

## Helpful Information

Information about updating dependencies is [here](./releng/README.md)

Information about publishing a new version is [here](./org.mybatis.generator.eclipse.doc/html-src/eclipseui/publishing.html)

Manual test scripts to test functionality of the plugin are [here](./org.mybatis.generator.eclipse.doc/html-src/eclipseui/manualTesting.html)
