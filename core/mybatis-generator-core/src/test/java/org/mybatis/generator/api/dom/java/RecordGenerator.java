package org.mybatis.generator.api.dom.java;

import java.io.Serializable;
import java.util.List;

/**
 * Allowed:
 *   super interfaces
 *   inner classes
 *   inner interfaces
 *   inner records
 *   inner enums
 *   methods
 *   constructors
 *   static initialization blocks
 *   static fields
 *   type parameters
 *
 * <p>Not allowed:
 *   super classes
 *   abstract
 *   static (even on inner records)
 *   instance fields
 *
 *
 *
 */
public class RecordGenerator {

    public record Record<T>(int id, String description, T theT) implements Serializable {
        private static final int f;

        static {
            f = 22;
        }

        public Record(int id, String description) {
            this(id, description, null);
        }

        public static int ff() {
            return f;
        }

        public int add(int a, int b) {
            return a + b + f;
        }

        public String sayIt() {
            return id + " " + description;
        }

        public static class Fred implements Flintstone {

        }

        public interface Flintstone {

        }

        public enum MyEnum {
            A, B, C
        }

        public record InnerRecord(int id, String description) {}
    }

    public static List<CompilationUnit> generateTestClasses() {
        return List.of(simpleRecord(), complexRecord());
    }

    private static CompilationUnit simpleRecord() {
        TopLevelRecord topLevelRecord = new TopLevelRecord("mbg.domtest.generators.records.SimpleRecord");
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));

        return topLevelRecord;
    }

    private static CompilationUnit complexRecord() {
        FullyQualifiedJavaType recordType = new FullyQualifiedJavaType("mbg.domtest.generators.records.ComplexRecord");
        TopLevelRecord topLevelRecord = new TopLevelRecord(recordType);
        topLevelRecord.addTypeParameter(new TypeParameter("T"));
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("T"), "theT"));

        Field field = new Field("f", new FullyQualifiedJavaType("int"));
        field.setFinal(true);
        field.setStatic(true);
        topLevelRecord.addField(field);

        InitializationBlock ib = new InitializationBlock(true);
        ib.addBodyLine("f = 22;");
        topLevelRecord.addInitializationBlock(ib);

        Method constructor = new Method("ComplexRecord");
        constructor.setConstructor(true);
        constructor.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        constructor.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));
        constructor.addBodyLine("this(id, description, null);");
        topLevelRecord.addMethod(constructor);

        Method method = new Method("ff");
        method.setStatic(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine("return f;");
        topLevelRecord.addMethod(method);

        method = new Method("add");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.addBodyLine("return a + b + f;");
        topLevelRecord.addMethod(method);

        method = new Method("sayIt");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addBodyLine("return id + \" \" + description;");
        topLevelRecord.addMethod(method);

        InnerClass innerClass = new InnerClass("Fred");
        innerClass.setVisibility(JavaVisibility.PUBLIC);
        innerClass.setStatic(true);
        innerClass.addSuperInterface(new FullyQualifiedJavaType("Flintstone"));
        topLevelRecord.addInnerClass(innerClass);

        InnerInterface innerInterface = new InnerInterface("Flintstone");
        topLevelRecord.addInnerInterface(innerInterface);

        InnerEnum innerEnum = new InnerEnum("MyEnum");
        innerEnum.addEnumConstant("A");
        innerEnum.addEnumConstant("B");
        innerEnum.addEnumConstant("C");
        topLevelRecord.addInnerEnum(innerEnum);

        InnerRecord innerRecord = new InnerRecord("InnerRecord");
        innerRecord.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "id"));
        innerRecord.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "description"));
        topLevelRecord.addInnerRecord(innerRecord);

        return topLevelRecord;
    }
}
