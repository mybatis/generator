# Release Engineering Support

This project includes target platform definition files for Eclipse to support running and debugging
the feature and plugins in Eclipse. These files are used to make sure we maintain backwards compatibility.

| Target File | Our Version | P2 Repository                                 | Notes                                                  |
|-------------|-------------|-----------------------------------------------|--------------------------------------------------------|
| 2018-12     | 1.4.1       | http://download.eclipse.org/releases/2018-12/ | First Eclipse version that supports Java 11 in the AST |
| 2021-06     | 1.4.2       | http://download.eclipse.org/releases/2021-06/ | First Eclipse version that supports M1 Mac             |
| 2022-09     | 1.4.3       | http://download.eclipse.org/releases/2022-09/ | First Eclipse version that requires Java 17            |

Find p2 Repository sites for the various releases here: https://github.com/eclipse-simrel/.github/blob/main/wiki/Simultaneous_Release.md

## How To Update Dependencies for Eclipse

Unfortunately, dependencies are in several locations. Updating dependencies starts with selecting an
Eclipse version to target. We normally try to support several prior versions of Eclipse, so the use
of a platform definition file is essential.

### Parent POM

The parent POM has some build dependencies. Updates to this file should be handled automatically
by the GitHub renovate bot.

The parent pom also has a link to the target file for the build - this should be manually
updated to match the target platform selected (change the configuration of the target-platform-configuration
plugin).

### Tycho Build Extension

The Tycho build extension for Maven is used to enable POM-less builds for many of the projects.
Update the version in .mvn/extensions.xml to match the Tycho version in the parent pom.

### Plugin Dependencies

Once a target platform has been selected, update the dependencies in the MANIFEST.MF files in the
following projects:

- org.mybatis.generator.core
- org.mybatis.generator.eclipse.core
- org.mybatis.generator.eclipse.core.tests
- org.mybatis.generator.eclipse.test.utilities
- org.mybatis.generator.eclipse.test.utilities.tests
- org.mybatis.generator.eclipse.ui
- org.mybatis.generator.eclipse.ui.tests

### Libraries

Optional dependencies are in the "lib" directory of the org.mybatis.generator.core project. They should be upgraded
periodically.

We use this strategy rather then relying on Eclipse's distributions of these libraries for the following reasons:

- Eclipse doesn't distribute every library we need
- The Eclipse versions are often very down level
- We don't want the packages declared as dependencies for the final artifact


### Update Java Execution Environment

Eclipse may throw warnings about the plugin execution environment being lower than a plugin dependency.
For example, when updating to target environment 2021-06, JDK 11 was a minimum for several plugins.
If that happens, update the execution environment in the MANIFEST.MF files in the
following projects:

- org.mybatis.generator.core
- org.mybatis.generator.eclipse.core
- org.mybatis.generator.eclipse.core.tests
- org.mybatis.generator.eclipse.test.utilities
- org.mybatis.generator.eclipse.test.utilities.tests
- org.mybatis.generator.eclipse.ui
- org.mybatis.generator.eclipse.ui.tests

You will also need to update the project definition files (.classpath and org.eclipse.jdt.core.prefs -
this can be done with a quick fix helper in the MANIFEST.MF file).

Also update the source and compile versions in the parent pom to match, and the toolchain definition.


