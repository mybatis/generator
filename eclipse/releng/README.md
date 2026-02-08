# Release Engineering Support

This project includes target platform definition for Eclipse to support running and debugging
the feature and plugins in Eclipse. The target platform is used to make sure we maintain backwards compatibility with
a few prior Eclipse releases, and it also manages the dependencies we need from Maven.

When updating the plugin, it is a good idea to update the supported eclipse version in the target file.
Find p2 Repository sites for the various releases here: https://github.com/eclipse-simrel/.github/blob/main/wiki/Simultaneous_Release.md

## How To Update Dependencies for Eclipse

Dependency management for this project takes some inspiration from Eclipse's Orbit project
here: https://github.com/eclipse-orbit/orbit-simrel/tree/main/maven-bnd

Unfortunately, dependencies are in several locations. Updating dependencies starts with selecting an
Eclipse version to target. We normally try to support several prior versions of Eclipse, so the use
of a platform definition file is essential.

### Parent POM

The parent POM has some build dependencies. Updates to this file should be handled automatically
by the GitHub renovate bot.

The parent pom also has a link to the target platform file for the build.

### Tycho Build Extension

The Tycho build extension for Maven is used to enable POM-less builds for many of the projects.
Update the version in .mvn/extensions.xml to match the Tycho version in the parent pom.

### Plugin Dependencies

Once the target platform has been updated, update the dependencies in the MANIFEST.MF files in the
following projects:

- org.mybatis.generator.core
- org.mybatis.generator.eclipse.ui
- org.mybatis.generator.eclipse.ui.tests

### Runtime Dependencies

Runtime dependencies for the core generator are made available through Eclipse bnd. Entries in
[MyBatisGenerator.target](./MyBatisGenerator.target) generate a new feature for every runtime dependency.
They should be upgraded to match the versions used in the core project. If you update a version,
make sure to update the MANIFEST.MF file in the related project.

We also redistribute these runtime dependencies on our update site. Entries in the site project's
`category.xml` file list the items we want to redistribute. If you add a new dependency, make
sure to update that file accordingly.

We only bundle and redistribute dependencies that are not available on the Eclipse update sites.

Some runtime dependencies (like Ant) are optional, and also available from Eclipse. So we can reference
them directly in the MANIFST.MF.

### Test Dependencies

Some dependencies (like AssertJ and JUnit 6) may not be available in the target platform, but only needed
during the build phase. For these dependencies, we can reference them in
[MyBatisGenerator.target](./MyBatisGenerator.target) directly
from Maven central. This makes them available for development and build.

JUnit 6 will eventually be made available directly from Eclipse when we upgrade to a newer platform, so we can
remove that Maven reference in the future.

### Update Java Execution Environment

Eclipse may throw warnings about the plugin execution environment being lower than a plugin dependency.
For example, when updating to target environment 2021-06, JDK 11 was a minimum for several plugins.
If that happens, update the execution environment in the MANIFEST.MF files in the
following projects:

- org.mybatis.generator.core
- org.mybatis.generator.eclipse.ui
- org.mybatis.generator.eclipse.ui.tests

You will also need to update the project definition files (.classpath and org.eclipse.jdt.core.prefs -
this can be done with a quick fix helper in the MANIFEST.MF file).

Also update the source and compile versions in the parent pom to match, and the toolchain definition.

## How To Find Platform Features
It can be very frustrating to try and find the features that bundle the plugins you need in a particular
target runtime. Here's how to do it using the OSGi console:

1. Open an OSGi Console from the console view
2. Enter this command to find the console number:
   ```
   ss p2.console
   ```

3. Start the console:
   ```
   start <<consoleNumber>>
   ```
   
4. The following command will look for `org.apache.commons.commons-logging` in the update site
   http://download.eclipse.org/releases/2024-09:
   ```
   provlquery http://download.eclipse.org/releases/2024-09 "select(parent | parent.properties['org.eclipse.equinox.p2.type.group'] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == 'org.apache.commons.commons-logging' && iu ~= rc)))" true
   ```
   
5. The query shows that commons-logging is bundled in the following features
   - org.eclipse.ecf.remoteservice.rest.feature.feature.group 1.0.303.v20240709-0844
   - org.eclipse.net4j.util.feature.group 4.25.0.v20240904-1115

   These features can be added directly to a target platform definition file.

The [P2 Console User's Guide](https://wiki.eclipse.org/Equinox_p2_Console_Users_Guide) may also be helpful.
