/*
 *    Copyright 2006-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;

public class AnnotatedSelectByPrimaryKeyMethodGenerator extends SelectByPrimaryKeyMethodGenerator {

    private final boolean useResultMapIfAvailable;

    public AnnotatedSelectByPrimaryKeyMethodGenerator(boolean useResultMapIfAvailable, boolean isSimple) {
        super(isSimple);
        this.useResultMapIfAvailable = useResultMapIfAvailable;
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        buildInitialSelectAnnotationStrings().forEach(method::addAnnotation);

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        buildByPrimaryKeyWhereClause().forEach(method::addAnnotation);

        method.addAnnotation("})"); //$NON-NLS-1$

        if (useResultMapIfAvailable) {
            if (introspectedTable.getRules().generateBaseResultMap()
                    || introspectedTable.getRules().generateResultMapWithBLOBs()) {
                addResultMapAnnotation(method);
            } else {
                addAnnotatedResults(interfaze, method, introspectedTable.getNonPrimaryKeyColumns());
            }
        } else {
            addAnnotatedResults(interfaze, method, introspectedTable.getNonPrimaryKeyColumns());
        }
    }

    private void addResultMapAnnotation(Method method) {

        String annotation = String.format("@ResultMap(\"%s.%s\")", //$NON-NLS-1$
                introspectedTable.getMyBatis3SqlMapNamespace(),
                introspectedTable.getRules().generateResultMapWithBLOBs()
                    ? introspectedTable.getResultMapWithBLOBsId() : introspectedTable.getBaseResultMapId());
        method.addAnnotation(annotation);
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        if (useResultMapIfAvailable) {
            if (introspectedTable.getRules().generateBaseResultMap()
                    || introspectedTable.getRules().generateResultMapWithBLOBs()) {
                interfaze.addImportedType(
                        new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap")); //$NON-NLS-1$
            } else {
                addAnnotationImports(interfaze);
            }
        } else {
            addAnnotationImports(interfaze);
        }
    }

    private void addAnnotationImports(Interface interfaze) {
        addAnnotatedSelectImports(interfaze);
    }
}
