MyBatis Generator (MBG)
=======================

[![Build Status](https://github.com/mybatis/generator/actions/workflows/ci.yaml/badge.svg)](https://github.com/mybatis/generator/actions/workflows/ci.yaml)
[![Coverage](https://coveralls.io/repos/github/mybatis/generator/badge.svg?branch=master)](https://coveralls.io/github/mybatis/generator?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/org.mybatis.generator/mybatis-generator)](https://central.sonatype.com/artifact/org.mybatis.generator/mybatis-generator)
[![License](https://img.shields.io/github/license/mybatis/generator)](https://www.apache.org/licenses/LICENSE-2.0)
[![Docs](https://img.shields.io/badge/docs-mybatis.org-blue?logo=github)](https://mybatis.org/generator/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mybatis_generator&metric=alert_status)](https://sonarcloud.io/dashboard?id=mybatis_generator)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mybatis_generator&metric=security_rating)](https://sonarcloud.io/dashboard?id=mybatis_generator)

![mybatis-generator](https://mybatis.org/images/mybatis-logo.png)

This is a code generator for MyBatis.

This library will generate code for use with MyBatis. It will introspect a database table (or many tables) and will generate artifacts that can be used to access the table(s). This lessens the initial nuisance of setting up objects and configuration files to interact with database tables. MBG seeks to make a major impact on the large percentage of database operations that are simple CRUD (Create, Retrieve, Update, Delete).

MBG can generate code in multiple styles (or "runtimes"). MBG can generate code for Java based projects, or for Kotlin based projects.

MBG can be run in multiple ways - from the command line, with an Ant task, as a Maven plugin, etc.  See this page for details:
[Running MBG](https://mybatis.org/generator/running/running.html)

## Eclipse

There is an Eclipse feature for MBG as well. Here's how to install it:

### Eclipse Update Site (Marketplace)
The easiest way to install the Eclipse feature is from the Eclipse Marketplace at this address: https://marketplace.eclipse.org/content/mybatis-generator

### Eclipse Update Site (Direct)
You can also manually configure an Eclipse update site for the generator. The update site is here: https://jeffgbutler.github.io/mybatis-generator-update-site/

### From the Release Bundle
You can manually install the plugin by downloading a zip file containing the update site from the GitHub release page here: https://github.com/mybatis/generator/releases
