/*
 *    Copyright 2006-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.java;

import java.util.List;

public class RecordGenerator {

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

    public static CompilationUnit complexRecord() {
        FullyQualifiedJavaType recordType = new FullyQualifiedJavaType("mbg.domtest.generators.records.ComplexRecord");
        TopLevelRecord topLevelRecord = new TopLevelRecord(recordType);
        topLevelRecord.addTypeParameter(new TypeParameter("T"));
        topLevelRecord.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
        topLevelRecord.addImportedType("java.io.Serializable");
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("T"), "theT"));
        topLevelRecord.addFileCommentLine("/**");
        topLevelRecord.addFileCommentLine(" * A complex record");
        topLevelRecord.addFileCommentLine(" */");
        topLevelRecord.addStaticImport("java.lang.Math.PI");

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
