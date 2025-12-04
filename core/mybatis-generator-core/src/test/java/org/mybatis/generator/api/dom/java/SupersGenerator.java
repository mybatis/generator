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
 * This test verifies that implementations can be in different packages from their super classes.
 */
public class SupersGenerator {

    private static final String BASE_PACKAGE = "mbg.domtest.generators.supers";

    public static List<CompilationUnit> generateTestClasses() {
        List<CompilationUnit> answer = new ArrayList<>();

        TopLevelClass baseClass = getBaseClass();
        TopLevelClass superClass = getSuperClass();
        baseClass.setSuperClass(superClass.getType());

        Interface baseInterface = getBaseInterface();
        Interface superInterface = getSuperInterface();
        baseInterface.addSuperInterface(superInterface.getType());
        baseClass.addSuperInterface(superInterface.getType());

        answer.add(baseClass);
        answer.add(superClass);
        answer.add(baseInterface);
        answer.add(superInterface);

        return answer;
    }

    private static TopLevelClass getSuperClass() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub.SuperClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        return tlc;
    }

    private static Interface getSuperInterface() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub.SuperInterface");
        Interface ifc = new Interface(fqjt);
        ifc.setVisibility(JavaVisibility.PUBLIC);

        return ifc;
    }

    private static TopLevelClass getBaseClass() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".BaseClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);

        return tlc;
    }

    private static Interface getBaseInterface() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".BaseInterface");
        Interface ifc = new Interface(fqjt);
        ifc.setVisibility(JavaVisibility.PUBLIC);

        return ifc;
    }
}
