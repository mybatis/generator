# What is this Project?
This project is used to test the MyBatis Generator Java code generator classes.
The basic idea is that we want to use the generator DOM classes to generate Java
code and then try to compile that code as a part of the build.  If the code is
generated incorrectly, then the build should fail with compile errors.

We may or may not write additional test classes to exercise the code that was generated.

The build will automatically discover code generators that implement the `CompilationUnitGenerator`
interface as long as they are in a sub package of `mbg.domtest.generators`.

# How to Add a New Test

1. Add a class in src/main/java. The class should be in a sub-package of `mbg.domtest.generators`.
2. The class must implement `mbg.domtest.CompilationUnitGenerator`
3. During plugin execution, the `generate()` method in your class will be called and in that method
   you should use the Java DOM classes to generate any Java code you are interested in.
4. You can use the `mbg.domtest.IgnoreDomTest` annotation to ignore any code generator class (useful
   if you are doing TDD and don't want to break the build)
   
# How Does this Project Work?
The project hooks a code generation step into the standard Maven lifecycle.  We use the exec-maven plugin to hook into the maven build life cycle. We use the build-helper-maven-plugin to add generated source to the test source folders.  The basic order of execution is as follows:

* [compile phase] Your generator classes are compiled along with all the other support classes
* [generate-test-sources phase] Your generator classes are executed and the output is added to the test
  source path
* [test-compile phase] The output of your generator is compiled.  If it fails, it will break the build.  This is the primary test of generated code - that it compiles.  Any other tests you add to src/test/java are also compiled
* [test phase] Any tests you create are executed.  You can write tests to execute your generated code if you so desire