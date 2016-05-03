====
       Copyright 2006-2016 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
====

===============================================================================
Overview
--------
MyBatis Generator (MBG) is a code generator for the MyBatis (and iBATIS) SQL
mapping framework.  MBG will introspect database tables (through JDBC
DatabaseMetaData) and generate SQL Map XML files, Java model object (POJOs)
that match the table, and (optionally) Java client classes that use the other
generated objects.

For full documentation, please refer to the user's manual at
docs/index.html in this distribution.

Dependencies
------------
There are no dependencies beyond the JRE.  JRE 5.0 or above is required.
Also required is a JDBC driver that implements the DatabaseMetaData interface,
especially the "getColumns" and "getPrimaryKeys" methods.

Support
-------
Support is provided through the user mailing list.  Mail
questions or bug reports to:

  mybatis-user@googlegroups.com
