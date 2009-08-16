===============================================================================
Overview
--------
Abator is a code generator for the iBATIS SQL mapping framefork.
Abator will introspect database tables (through JDBC DatabaseMetaData) and
generate SQL Map XML files, Java model object (POJOs) tha match the table,
and (optionally) DAO classes that use the other generated objects.

For full documentation, please refer to the user's manual at doc/index.html
in this distribution.

Dependencies
------------
Abator has no dependencies beyond the JRE.  Abator does require JRE 1.4 or
above.  Abator also requires that the JDBC driver implements the
DatabaseMetaData interface, especially the "getColumns" and "getPrimaryKeys"
methods.

Support
-------
Support for Abator is provided through the iBATIS user mailing list.  Mail
questions or bug reports to:

  user-java@ibatis.apache.org
