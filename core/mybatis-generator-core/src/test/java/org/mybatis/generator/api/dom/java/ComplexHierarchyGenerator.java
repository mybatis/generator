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

import java.util.ArrayList;
import java.util.List;

/**
 * This test verifies the ability of the generator to use fully qualified names
 * in code generation when the type is not explicitly imported.
 *
 * <p>The test generates a hierarchy with multiple classes that have the same name in
 * different packages.
 */
public class ComplexHierarchyGenerator {

    private static final String BASE_PACKAGE = "mbg.domtest.generators.fieldtype1.output";

    public static List<CompilationUnit> generateTestClasses() {
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType(BASE_PACKAGE + ".SomeClass");

        List<CompilationUnit> answer = new ArrayList<>();
        TopLevelClass tlcMain = generateFieldTypeMain();
        TopLevelClass tlcSub1 = generateFieldTypeSub1();
        TopLevelClass tlcTcSub1 = generateTestClassSub1();
        TopLevelClass tlcSub2 = generateFieldTypeSub2();

        answer.add(tlcMain);
        answer.add(tlcSub1);
        answer.add(tlcTcSub1);
        answer.add(tlcSub2);

        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.setVisibility(JavaVisibility.PUBLIC);
        topLvlClass.addImportedType(tlcTcSub1.getType());

        Field field = new Field("main", tlcMain.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        field = new Field("tcSub1", tlcTcSub1.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        field = new Field("sub1", tlcSub1.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        field = new Field("sub2", tlcSub2.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        Method m = new Method("executeMain");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("main.mainMethod();");
        topLvlClass.addMethod(m);

        m = new Method("setMain");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addParameter(new Parameter(tlcMain.getType(), "main"));
        m.addBodyLine("this.main = main;");
        topLvlClass.addMethod(m);

        m = new Method("getMain");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setReturnType(tlcMain.getType());
        m.addBodyLine("return main;");
        topLvlClass.addMethod(m);

        m = new Method("executeSub1");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("sub1.sub1Method();");
        topLvlClass.addMethod(m);

        m = new Method("setSub1");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addParameter(new Parameter(tlcSub1.getType(), "sub1"));
        m.addBodyLine("this.sub1 = sub1;");
        topLvlClass.addMethod(m);

        m = new Method("getSub1");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setReturnType(tlcSub1.getType());
        m.addBodyLine("return sub1;");
        topLvlClass.addMethod(m);

        m = new Method("executeSub2");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("sub2.sub2Method();");
        topLvlClass.addMethod(m);

        m = new Method("setSub2");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addParameter(new Parameter(tlcSub2.getType(), "sub2"));
        m.addBodyLine("this.sub2 = sub2;");
        topLvlClass.addMethod(m);

        m = new Method("getSub2");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setReturnType(tlcSub2.getType());
        m.addBodyLine("return sub2;");
        topLvlClass.addMethod(m);

        answer.add(topLvlClass);

        return answer;
    }

    private static TopLevelClass generateFieldTypeMain() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        Method m = new Method("mainMethod");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("System.out.println(\"main method\");");
        tlc.addMethod(m);

        return tlc;
    }

    private static TopLevelClass generateFieldTypeSub1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub1.FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        Method m = new Method("sub1Method");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("System.out.println(\"sub1 method\");");
        tlc.addMethod(m);

        return tlc;
    }

    private static TopLevelClass generateTestClassSub1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub1.SomeClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        Method m = new Method("testClassMethod");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("System.out.println(\"testClass sub1 method\");");
        tlc.addMethod(m);

        return tlc;
    }

    private static TopLevelClass generateFieldTypeSub2() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub2.FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        Method m = new Method("sub2Method");
        m.setVisibility(JavaVisibility.PUBLIC);
        m.addBodyLine("System.out.println(\"sub2 method\");");
        tlc.addMethod(m);

        return tlc;
    }
}
