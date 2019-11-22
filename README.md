MyBatis Generator (MBG)
=======================

[![Build Status](https://travis-ci.org/mybatis/generator.svg?branch=master)](https://travis-ci.org/mybatis/generator)
[![Coverage Status](https://coveralls.io/repos/mybatis/generator/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/generator?branch=master)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis.generator/mybatis-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis.generator/mybatis-generator)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.mybatis.generator/mybatis-generator.svg)](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/generator/mybatis-generator/)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

![mybatis-generator](http://mybatis.github.io/images/mybatis-logo.png)

This is a code generator for MyBatis.

This library will generate code for use with MyBatis. It will introspect a database table (or many tables) and will generate artifacts that can be used to access the table(s). This lessens the initial nuisance of setting up objects and configuration files to interact with database tables. MBG seeks to make a major impact on the large percentage of database operations that are simple CRUD (Create, Retrieve, Update, Delete).

MBG can generate code in multiple styles (or "runtimes"). MBG can generate code for Java based projects, or for Kotlin based projects.
