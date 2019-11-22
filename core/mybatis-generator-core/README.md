# Overview

MyBatis Generator (MBG) is a code generator for the MyBatis SQL
mapping framework.  MBG will introspect database tables (through JDBC
DatabaseMetaData) and generate SQL Map XML files, Java model object (POJOs)
that match the table, and (optionally) Java client classes that use the other
generated objects.

MBG can also generate Kotlin code for MyBatis.

For full documentation, please refer to the user's manual at http://www.mybatis.org/generator/

## Dependencies

There are no dependencies beyond the JRE.  Java 8 or above is required.
Also required is a JDBC driver that implements the DatabaseMetaData interface,
especially the "getColumns" and "getPrimaryKeys" methods.

## Support

Support is provided through the user mailing list.  Mail
questions or bug reports to:

  mybatis-user@googlegroups.com
