Copyright 2013-2014 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


Created by: Mahiar Mody

This is the ReadMe file for the MyBatis Generator Jaxb annotations Plugin version 1.0.0

Contents:

1. THE JAXB ANNOTATIONS PLUGIN JAR FILE NAME AND LOCATION IN THIS DIRECTORY HIERARCHY
2. DEPENDENCIES
3. PLUGIN USAGE
4. STRUCTURE OF THE JAXB ANNOTATIONS PLUGIN PROJECT
5. BUILDING THE JAXB ANNOTATIONS PLUGIN JAR FROM SOURCE
6. HIERARCHY OF THE JAXB ANNOTATIONS PLUGIN PROJECT



1. THE JAXB ANNOTATIONS PLUGIN JAR FILE NAME AND LOCATION IN THIS DIRECTORY HIERARCHY
======================================================================================
JaxbAnnotations-1.0.0.jar is the only file needed to add JAXB annotation
support to the MyBatis Generator Tool. Copy it from this directory hierarchy and
add it to the classpath of your project, along with the MyBatis Generator's JAR file.
The location of this JAR file in this directory hierarchy is:
jaxbAnnotationsBase/dist/JaxbAnnotations-1.0.0.jar
Note that the "-1.0.0" suffix in the JAR file name represents this plugin's version.



2. DEPENDENCIES
================
There are no dependencies beyond the JRE and the MyBatis Generator Tool itself.
JRE 5.0 or above is required to use this plugin. JRE 6.0 and above is required
to run the test cases that are used to test this plugin.




3. PLUGIN USAGE
================
Please refer to the plugin's Java Doc API for a detailed description on how to
use this plugin. The Java Doc API can be found in the
jaxbAnnotationsBase/javaDocs directory.
Details on the plugin usage can be found at: jaxbAnnotationsBase/javaDocs/index.html
Add JaxbAnnotations-1.0.0.jar to the classpath of your project, along with the
MyBatis Generator's JAR file, in order to add JAXB annotations to the generated
model classes.




4. STRUCTURE OF THE JAXB ANNOTATIONS PLUGIN PROJECT
=====================================================

Project install directory
-------------------------
The entire project is rooted at (installed in) the directory named jaxbAnnotationsBase.


Jaxb Annotations Plugin JAR file
---------------------------------
JaxbAnnotations-1.0.0.jar is the Jaxb annotations plugin JAR file
responsible for adding JAXB annotations to the Mybatis Generator generated
model classes. It is located in the jaxbAnnotationsBase/dist directory.

The directory jaxbAnnotationsBase/src/test/resources/plugin contains a copy of
the jaxbAnnotationsBase/dist/JaxbAnnotations-1.0.0.jar file. This copied
file is used to test this plugin.

Since both jaxbAnnotationsBase/src/test/resources/plugin/JaxbAnnotations-1.0.0.jar
and
jaxbAnnotationsBase/dist/JaxbAnnotations-1.0.0.jar are identical, either
JAR file can be added to the classpath of your project, along with the MyBatis
Generator's JAR file, in order to add JAXB annotations to the generated
model classes.


Jaxb Annotations Plugin Java Doc API files
-------------------------------------------
The Jaxb Annotations Plugin Java Doc API files are located under the
jaxbAnnotationsBase/javaDocs directory. The Java Doc API also details
how this plugin is to be used.


Jaxb Annotations Plugin Source code files
------------------------------------------
The Jaxb Annotations Plugin Source code files are located under the
jaxbAnnotationsBase/src/main directory.


Jaxb Annotations Plugin JUnit Test source code files
-----------------------------------------------------
The Jaxb Annotations Plugin JUnit test case files are located under the
jaxbAnnotationsBase/src/test directory.


Jaxb Annotations Plugin Resources subdirectories
-------------------------------------------------
There are two resources subdirectories. They are located at:
jaxbAnnotationsBase/src/test/resources
and
jaxbAnnotationsBase/classes/test/resources
Both resources subdirectories are used for testing this plugin.

The jaxbAnnotationsBase/src/test/resources subdirectory contains the resources
necessary for the MyBatis Generator to generate the MyBatis artefacts.

The jaxbAnnotationsBase/classes/test/resources subdirectory contains the
MyBatis Generator generated artefacts, i.e. Model classes, Sql maps, and
client classes, all of which are used to test this plugin. Additionally,
the mybatis-config.xml file necessary to run MyBatis, is also located in this
subdirectory.


The ant build files
--------------------
The ant build files are located in the jaxbAnnotationsBase directory.


Jaxb Annotations Plugin class files
------------------------------------
The Jaxb Annotations Plugin compiled class files are located under the
jaxbAnnotationsBase/classes/main directory.


Jaxb Annotations Plugin JUnit Test class files
-----------------------------------------------
The Jaxb Annotations Plugin JUnit test class files are located under the
jaxbAnnotationsBase/classes/test directory.





5. TO BUILD THE PLUGIN FROM SOURCE CODE
========================================

Copy the jaxbAnnotationsPlugin directory recursively to any
directory of your choice.
Next, move into the copied jaxbAnnotationsBase directory.

From command line
-----------------
To compile:
jaxbAnnotationsBase$ javac -sourcepath src/main -d classes/main -source 1.5 -target 1.5 -bootclasspath </path/to/your/jre1.5/lib/rt.jar> -cp </path/to/your/mybatis3/generator/jar/file/mybatis-generator-core-1.3.2.jar> src/main/org/mybatis/generator/plugins/annotations/*.java

To create the Jaxb annotations Jar file:
jaxbAnnotationsBase$ jar -cvf dist/JaxbAnnotations-1.0.0.jar -C classes/main/ .

To create JavaDoc API:
jaxbAnnotationsBase$ javadoc -sourcepath src/main -d javaDocs -source 1.5 -bootclasspath </path/to/your/jre1.5/lib/rt.jar> -classpath </path/to/your/mybatis3/generator/jar/file/mybatis-generator-core-1.3.2.jar>  src/main/org/mybatis/generator/plugins/annotations/*.java 

To run JUnit tests:
Too cumbersome to run from the command line. Please use ant.


From Ant
=========
Before running the ant tasks, please change the following properties in the build.properties file
to match your computer specific locations for the various JAR files:
mybatis.generator.tool.jar.path
mybatis.jar.path
boot.class.path.base.5
boot.class.path.base.6
junit.lib.path


To clean (delete all class files and generted Jaxb Annotations JAR archive):
jaxbAnnotationsBase$ ant clean

To compile:
jaxbAnnotationsBase$ ant compile

To create the Jaxb annotations Jar file:
jaxbAnnotationsBase$ ant dist

To clean java docs directory
jaxbAnnotationsBase$ ant cleanJavaDoc

To create JavaDoc API:
jaxbAnnotationsBase$ ant javaDoc

To run JUnit tests:
jaxbAnnotationsBase$ ant runJUnitTests

Please note that to run the JUnit Tests, you'll need Java 6 or above.





6. HIERARCHY OF THE JAXB ANNOTATIONS PLUGIN PROJECT
=====================================================

jaxbAnnotationsBase
|-- build.properties
|-- build.xml
|-- classes
|   |-- main
|   |   `-- org
|   |       `-- mybatis
|   |           `-- generator
|   |               `-- plugins
|   |                   `-- annotations
|   |                       |-- FieldTypeJaxbAnnotations.class
|   |                       |-- JaxbAnnotations.class
|   |                       `-- PropertyTypeJaxbAnnotations.class
|   `-- test
|       |-- org
|       |   `-- mybatis
|       |       `-- generator
|       |           `-- plugins
|       |               `-- annotations
|       |                   |-- FieldTypeJaxbAnnotationsPresenceNoLobsTest.class
|       |                   |-- FieldTypeJaxbAnnotationsPresenceWithLobsTest.class
|       |                   |-- FieldTypeJaxbAnnotationsXmlNoLobsTest.class
|       |                   |-- FieldTypeJaxbAnnotationsXmlWithLobsTest.class
|       |                   |-- PropertyTypeJaxbAnnotationsPresenceNoLobsTest.class
|       |                   |-- PropertyTypeJaxbAnnotationsPresenceWithLobsTest.class
|       |                   |-- PropertyTypeJaxbAnnotationsXmlNoLobsTest.class
|       |                   `-- PropertyTypeJaxbAnnotationsXmlWithLobsTest.class
|       `-- resources
|           |-- generatedFiles
|           |   |-- classes
|           |   |   |-- fieldType
|           |   |   |   |-- noLobs
|           |   |   |   |   |-- clientDao
|           |   |   |   |   |   |-- UserBlogMapper.class
|           |   |   |   |   |   |-- UserPhotosMapper.class
|           |   |   |   |   |   |-- UserSkillsMapper.class
|           |   |   |   |   |   |-- UsersMapper.class
|           |   |   |   |   |   |-- UsersToSkillsMapper.class
|           |   |   |   |   |   `-- UserTutorialMapper.class
|           |   |   |   |   `-- modelDto
|           |   |   |   |       |-- UserBlog.class
|           |   |   |   |       |-- UserBlogExample.class
|           |   |   |   |       |-- UserBlogExample$Criteria.class
|           |   |   |   |       |-- UserBlogExample$Criterion.class
|           |   |   |   |       |-- UserBlogExample$GeneratedCriteria.class
|           |   |   |   |       |-- UserPhotos.class
|           |   |   |   |       |-- UserPhotosExample.class
|           |   |   |   |       |-- UserPhotosExample$Criteria.class
|           |   |   |   |       |-- UserPhotosExample$Criterion.class
|           |   |   |   |       |-- UserPhotosExample$GeneratedCriteria.class
|           |   |   |   |       |-- Users.class
|           |   |   |   |       |-- UsersExample.class
|           |   |   |   |       |-- UsersExample$Criteria.class
|           |   |   |   |       |-- UsersExample$Criterion.class
|           |   |   |   |       |-- UsersExample$GeneratedCriteria.class
|           |   |   |   |       |-- UserSkills.class
|           |   |   |   |       |-- UserSkillsExample.class
|           |   |   |   |       |-- UserSkillsExample$Criteria.class
|           |   |   |   |       |-- UserSkillsExample$Criterion.class
|           |   |   |   |       |-- UserSkillsExample$GeneratedCriteria.class
|           |   |   |   |       |-- UsersToSkillsExample.class
|           |   |   |   |       |-- UsersToSkillsExample$Criteria.class
|           |   |   |   |       |-- UsersToSkillsExample$Criterion.class
|           |   |   |   |       |-- UsersToSkillsExample$GeneratedCriteria.class
|           |   |   |   |       |-- UsersToSkillsKey.class
|           |   |   |   |       |-- UserTutorial.class
|           |   |   |   |       |-- UserTutorialExample.class
|           |   |   |   |       |-- UserTutorialExample$Criteria.class
|           |   |   |   |       |-- UserTutorialExample$Criterion.class
|           |   |   |   |       |-- UserTutorialExample$GeneratedCriteria.class
|           |   |   |   |       `-- UserTutorialWithBLOBs.class
|           |   |   |   `-- withLobs
|           |   |   |       |-- clientDao
|           |   |   |       |   |-- UserBlogMapper.class
|           |   |   |       |   |-- UserPhotosMapper.class
|           |   |   |       |   |-- UserSkillsMapper.class
|           |   |   |       |   |-- UsersMapper.class
|           |   |   |       |   |-- UsersToSkillsMapper.class
|           |   |   |       |   `-- UserTutorialMapper.class
|           |   |   |       `-- modelDto
|           |   |   |           |-- UserBlog.class
|           |   |   |           |-- UserBlogExample.class
|           |   |   |           |-- UserBlogExample$Criteria.class
|           |   |   |           |-- UserBlogExample$Criterion.class
|           |   |   |           |-- UserBlogExample$GeneratedCriteria.class
|           |   |   |           |-- UserPhotos.class
|           |   |   |           |-- UserPhotosExample.class
|           |   |   |           |-- UserPhotosExample$Criteria.class
|           |   |   |           |-- UserPhotosExample$Criterion.class
|           |   |   |           |-- UserPhotosExample$GeneratedCriteria.class
|           |   |   |           |-- Users.class
|           |   |   |           |-- UsersExample.class
|           |   |   |           |-- UsersExample$Criteria.class
|           |   |   |           |-- UsersExample$Criterion.class
|           |   |   |           |-- UsersExample$GeneratedCriteria.class
|           |   |   |           |-- UserSkills.class
|           |   |   |           |-- UserSkillsExample.class
|           |   |   |           |-- UserSkillsExample$Criteria.class
|           |   |   |           |-- UserSkillsExample$Criterion.class
|           |   |   |           |-- UserSkillsExample$GeneratedCriteria.class
|           |   |   |           |-- UsersToSkillsExample.class
|           |   |   |           |-- UsersToSkillsExample$Criteria.class
|           |   |   |           |-- UsersToSkillsExample$Criterion.class
|           |   |   |           |-- UsersToSkillsExample$GeneratedCriteria.class
|           |   |   |           |-- UsersToSkillsKey.class
|           |   |   |           |-- UserTutorial.class
|           |   |   |           |-- UserTutorialExample.class
|           |   |   |           |-- UserTutorialExample$Criteria.class
|           |   |   |           |-- UserTutorialExample$Criterion.class
|           |   |   |           |-- UserTutorialExample$GeneratedCriteria.class
|           |   |   |           `-- UserTutorialWithBLOBs.class
|           |   |   `-- propertyType
|           |   |       |-- noLobs
|           |   |       |   |-- clientDao
|           |   |       |   |   |-- UserBlogMapper.class
|           |   |       |   |   |-- UserPhotosMapper.class
|           |   |       |   |   |-- UserSkillsMapper.class
|           |   |       |   |   |-- UsersMapper.class
|           |   |       |   |   |-- UsersToSkillsMapper.class
|           |   |       |   |   `-- UserTutorialMapper.class
|           |   |       |   `-- modelDto
|           |   |       |       |-- UserBlog.class
|           |   |       |       |-- UserBlogExample.class
|           |   |       |       |-- UserBlogExample$Criteria.class
|           |   |       |       |-- UserBlogExample$Criterion.class
|           |   |       |       |-- UserBlogExample$GeneratedCriteria.class
|           |   |       |       |-- UserPhotos.class
|           |   |       |       |-- UserPhotosExample.class
|           |   |       |       |-- UserPhotosExample$Criteria.class
|           |   |       |       |-- UserPhotosExample$Criterion.class
|           |   |       |       |-- UserPhotosExample$GeneratedCriteria.class
|           |   |       |       |-- Users.class
|           |   |       |       |-- UsersExample.class
|           |   |       |       |-- UsersExample$Criteria.class
|           |   |       |       |-- UsersExample$Criterion.class
|           |   |       |       |-- UsersExample$GeneratedCriteria.class
|           |   |       |       |-- UserSkills.class
|           |   |       |       |-- UserSkillsExample.class
|           |   |       |       |-- UserSkillsExample$Criteria.class
|           |   |       |       |-- UserSkillsExample$Criterion.class
|           |   |       |       |-- UserSkillsExample$GeneratedCriteria.class
|           |   |       |       |-- UsersToSkillsExample.class
|           |   |       |       |-- UsersToSkillsExample$Criteria.class
|           |   |       |       |-- UsersToSkillsExample$Criterion.class
|           |   |       |       |-- UsersToSkillsExample$GeneratedCriteria.class
|           |   |       |       |-- UsersToSkillsKey.class
|           |   |       |       |-- UserTutorial.class
|           |   |       |       |-- UserTutorialExample.class
|           |   |       |       |-- UserTutorialExample$Criteria.class
|           |   |       |       |-- UserTutorialExample$Criterion.class
|           |   |       |       |-- UserTutorialExample$GeneratedCriteria.class
|           |   |       |       `-- UserTutorialWithBLOBs.class
|           |   |       `-- withLobs
|           |   |           |-- clientDao
|           |   |           |   |-- UserBlogMapper.class
|           |   |           |   |-- UserPhotosMapper.class
|           |   |           |   |-- UserSkillsMapper.class
|           |   |           |   |-- UsersMapper.class
|           |   |           |   |-- UsersToSkillsMapper.class
|           |   |           |   `-- UserTutorialMapper.class
|           |   |           `-- modelDto
|           |   |               |-- UserBlog.class
|           |   |               |-- UserBlogExample.class
|           |   |               |-- UserBlogExample$Criteria.class
|           |   |               |-- UserBlogExample$Criterion.class
|           |   |               |-- UserBlogExample$GeneratedCriteria.class
|           |   |               |-- UserPhotos.class
|           |   |               |-- UserPhotosExample.class
|           |   |               |-- UserPhotosExample$Criteria.class
|           |   |               |-- UserPhotosExample$Criterion.class
|           |   |               |-- UserPhotosExample$GeneratedCriteria.class
|           |   |               |-- Users.class
|           |   |               |-- UsersExample.class
|           |   |               |-- UsersExample$Criteria.class
|           |   |               |-- UsersExample$Criterion.class
|           |   |               |-- UsersExample$GeneratedCriteria.class
|           |   |               |-- UserSkills.class
|           |   |               |-- UserSkillsExample.class
|           |   |               |-- UserSkillsExample$Criteria.class
|           |   |               |-- UserSkillsExample$Criterion.class
|           |   |               |-- UserSkillsExample$GeneratedCriteria.class
|           |   |               |-- UsersToSkillsExample.class
|           |   |               |-- UsersToSkillsExample$Criteria.class
|           |   |               |-- UsersToSkillsExample$Criterion.class
|           |   |               |-- UsersToSkillsExample$GeneratedCriteria.class
|           |   |               |-- UsersToSkillsKey.class
|           |   |               |-- UserTutorial.class
|           |   |               |-- UserTutorialExample.class
|           |   |               |-- UserTutorialExample$Criteria.class
|           |   |               |-- UserTutorialExample$Criterion.class
|           |   |               |-- UserTutorialExample$GeneratedCriteria.class
|           |   |               `-- UserTutorialWithBLOBs.class
|           |   `-- src
|           |       |-- fieldType
|           |       |   |-- noLobs
|           |       |   |   |-- clientDao
|           |       |   |   |   |-- UserBlogMapper.java
|           |       |   |   |   |-- UserPhotosMapper.java
|           |       |   |   |   |-- UserSkillsMapper.java
|           |       |   |   |   |-- UsersMapper.java
|           |       |   |   |   |-- UsersToSkillsMapper.java
|           |       |   |   |   `-- UserTutorialMapper.java
|           |       |   |   |-- modelDto
|           |       |   |   |   |-- UserBlogExample.java
|           |       |   |   |   |-- UserBlog.java
|           |       |   |   |   |-- UserPhotosExample.java
|           |       |   |   |   |-- UserPhotos.java
|           |       |   |   |   |-- UsersExample.java
|           |       |   |   |   |-- Users.java
|           |       |   |   |   |-- UserSkillsExample.java
|           |       |   |   |   |-- UserSkills.java
|           |       |   |   |   |-- UsersToSkillsExample.java
|           |       |   |   |   |-- UsersToSkillsKey.java
|           |       |   |   |   |-- UserTutorialExample.java
|           |       |   |   |   |-- UserTutorial.java
|           |       |   |   |   `-- UserTutorialWithBLOBs.java
|           |       |   |   `-- sqlMaps
|           |       |   |       |-- UserBlogMapper.xml
|           |       |   |       |-- UserPhotosMapper.xml
|           |       |   |       |-- UserSkillsMapper.xml
|           |       |   |       |-- UsersMapper.xml
|           |       |   |       |-- UsersToSkillsMapper.xml
|           |       |   |       `-- UserTutorialMapper.xml
|           |       |   `-- withLobs
|           |       |       |-- clientDao
|           |       |       |   |-- UserBlogMapper.java
|           |       |       |   |-- UserPhotosMapper.java
|           |       |       |   |-- UserSkillsMapper.java
|           |       |       |   |-- UsersMapper.java
|           |       |       |   |-- UsersToSkillsMapper.java
|           |       |       |   `-- UserTutorialMapper.java
|           |       |       |-- modelDto
|           |       |       |   |-- UserBlogExample.java
|           |       |       |   |-- UserBlog.java
|           |       |       |   |-- UserPhotosExample.java
|           |       |       |   |-- UserPhotos.java
|           |       |       |   |-- UsersExample.java
|           |       |       |   |-- Users.java
|           |       |       |   |-- UserSkillsExample.java
|           |       |       |   |-- UserSkills.java
|           |       |       |   |-- UsersToSkillsExample.java
|           |       |       |   |-- UsersToSkillsKey.java
|           |       |       |   |-- UserTutorialExample.java
|           |       |       |   |-- UserTutorial.java
|           |       |       |   `-- UserTutorialWithBLOBs.java
|           |       |       `-- sqlMaps
|           |       |           |-- UserBlogMapper.xml
|           |       |           |-- UserPhotosMapper.xml
|           |       |           |-- UserSkillsMapper.xml
|           |       |           |-- UsersMapper.xml
|           |       |           |-- UsersToSkillsMapper.xml
|           |       |           `-- UserTutorialMapper.xml
|           |       `-- propertyType
|           |           |-- noLobs
|           |           |   |-- clientDao
|           |           |   |   |-- UserBlogMapper.java
|           |           |   |   |-- UserPhotosMapper.java
|           |           |   |   |-- UserSkillsMapper.java
|           |           |   |   |-- UsersMapper.java
|           |           |   |   |-- UsersToSkillsMapper.java
|           |           |   |   `-- UserTutorialMapper.java
|           |           |   |-- modelDto
|           |           |   |   |-- UserBlogExample.java
|           |           |   |   |-- UserBlog.java
|           |           |   |   |-- UserPhotosExample.java
|           |           |   |   |-- UserPhotos.java
|           |           |   |   |-- UsersExample.java
|           |           |   |   |-- Users.java
|           |           |   |   |-- UserSkillsExample.java
|           |           |   |   |-- UserSkills.java
|           |           |   |   |-- UsersToSkillsExample.java
|           |           |   |   |-- UsersToSkillsKey.java
|           |           |   |   |-- UserTutorialExample.java
|           |           |   |   |-- UserTutorial.java
|           |           |   |   `-- UserTutorialWithBLOBs.java
|           |           |   `-- sqlMaps
|           |           |       |-- UserBlogMapper.xml
|           |           |       |-- UserPhotosMapper.xml
|           |           |       |-- UserSkillsMapper.xml
|           |           |       |-- UsersMapper.xml
|           |           |       |-- UsersToSkillsMapper.xml
|           |           |       `-- UserTutorialMapper.xml
|           |           `-- withLobs
|           |               |-- clientDao
|           |               |   |-- UserBlogMapper.java
|           |               |   |-- UserPhotosMapper.java
|           |               |   |-- UserSkillsMapper.java
|           |               |   |-- UsersMapper.java
|           |               |   |-- UsersToSkillsMapper.java
|           |               |   `-- UserTutorialMapper.java
|           |               |-- modelDto
|           |               |   |-- UserBlogExample.java
|           |               |   |-- UserBlog.java
|           |               |   |-- UserPhotosExample.java
|           |               |   |-- UserPhotos.java
|           |               |   |-- UsersExample.java
|           |               |   |-- Users.java
|           |               |   |-- UserSkillsExample.java
|           |               |   |-- UserSkills.java
|           |               |   |-- UsersToSkillsExample.java
|           |               |   |-- UsersToSkillsKey.java
|           |               |   |-- UserTutorialExample.java
|           |               |   |-- UserTutorial.java
|           |               |   `-- UserTutorialWithBLOBs.java
|           |               `-- sqlMaps
|           |                   |-- UserBlogMapper.xml
|           |                   |-- UserPhotosMapper.xml
|           |                   |-- UserSkillsMapper.xml
|           |                   |-- UsersMapper.xml
|           |                   |-- UsersToSkillsMapper.xml
|           |                   `-- UserTutorialMapper.xml
|           `-- mybatis-config.xml
|-- dist
|   `-- JaxbAnnotations-1.0.0.jar
|-- javaDocs
|   |-- allclasses-frame.html
|   |-- allclasses-noframe.html
|   |-- constant-values.html
|   |-- deprecated-list.html
|   |-- help-doc.html
|   |-- index-all.html
|   |-- index.html
|   |-- org
|   |   `-- mybatis
|   |       `-- generator
|   |           `-- plugins
|   |               `-- annotations
|   |                   |-- FieldTypeJaxbAnnotations.html
|   |                   |-- JaxbAnnotations.html
|   |                   |-- package-frame.html
|   |                   |-- package-summary.html
|   |                   |-- package-tree.html
|   |                   `-- PropertyTypeJaxbAnnotations.html
|   |-- overview-tree.html
|   |-- package-list
|   |-- resources
|   |   |-- background.gif
|   |   |-- tab.gif
|   |   |-- titlebar_end.gif
|   |   `-- titlebar.gif
|   `-- stylesheet.css
`-- src
    |-- main
    |   |-- MANIFEST.MF
    |   `-- org
    |       `-- mybatis
    |           `-- generator
    |               `-- plugins
    |                   `-- annotations
    |                       |-- FieldTypeJaxbAnnotations.java
    |                       |-- JaxbAnnotations.java
    |                       `-- PropertyTypeJaxbAnnotations.java
    `-- test
        |-- org
        |   `-- mybatis
        |       `-- generator
        |           `-- plugins
        |               `-- annotations
        |                   |-- FieldTypeJaxbAnnotationsPresenceNoLobsTest.java
        |                   |-- FieldTypeJaxbAnnotationsPresenceWithLobsTest.java
        |                   |-- FieldTypeJaxbAnnotationsXmlNoLobsTest.java
        |                   |-- FieldTypeJaxbAnnotationsXmlWithLobsTest.java
        |                   |-- PropertyTypeJaxbAnnotationsPresenceNoLobsTest.java
        |                   |-- PropertyTypeJaxbAnnotationsPresenceWithLobsTest.java
        |                   |-- PropertyTypeJaxbAnnotationsXmlNoLobsTest.java
        |                   `-- PropertyTypeJaxbAnnotationsXmlWithLobsTest.java
        `-- resources
            |-- jdbcDrivers
            |   `-- hsqldb.jar
            |-- mbgConfig
            |   |-- MyBatisGeneratorConfig.properties
            |   `-- MyBatisGeneratorConfig.xml
            |-- plugin
            |   `-- JaxbAnnotations-1.0.0.jar
            `-- sqlScript
                `-- SetupDbTestScripts.sql

75 directories, 300 files

