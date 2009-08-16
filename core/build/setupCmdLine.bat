rem Change the values in this file to the proper directories on your machine

if defined ANT_HOME goto ant_home_defined
set ANT_HOME=C:\JavaTools\apache-ant-1.6.5
set PATH=%PATH%;%ANT_HOME%\bin;

:ant_home_defined
if defined JAVA_HOME goto java_home_defined
set JAVA_HOME=C:\JavaTools\jdk1.5.0_11

:java_home_defined
