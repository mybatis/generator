/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.codegen.ibatis2.dao.elements;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.DAOMethodNameCalculator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Generates the Insert method.
 * 
 * @author Jeff Butler
 * 
 */
public class InsertMethodGenerator extends AbstractDAOElementGenerator {

    private boolean generateForJava5;

    public InsertMethodGenerator(boolean generateForJava5) {
        super();
        this.generateForJava5 = generateForJava5;
    }

    @Override
    public void addImplementationElements(TopLevelClass topLevelClass) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = getMethodShell(importedTypes);
        if (generateForJava5) {
            method.addAnnotation("@Override"); //$NON-NLS-1$
        }

        FullyQualifiedJavaType returnType = method.getReturnType();

        StringBuilder sb = new StringBuilder();

        if (returnType != null) {
            sb.append("Object newKey = "); //$NON-NLS-1$
        }

        sb.append(daoTemplate.getInsertMethod(introspectedTable
                .getIbatis2SqlMapNamespace(), introspectedTable
                .getInsertStatementId(), "record")); //$NON-NLS-1$
        method.addBodyLine(sb.toString());

        if (returnType != null) {
            if ("Object".equals(returnType.getShortName())) { //$NON-NLS-1$
                // no need to cast if the return type is Object
                method.addBodyLine("return newKey;"); //$NON-NLS-1$
            } else {
                sb.setLength(0);

                if (returnType.isPrimitive()) {
                    PrimitiveTypeWrapper ptw = returnType
                            .getPrimitiveTypeWrapper();
                    sb.append("return (("); //$NON-NLS-1$
                    sb.append(ptw.getShortName());
                    sb.append(") newKey"); //$NON-NLS-1$
                    sb.append(")."); //$NON-NLS-1$
                    sb.append(ptw.getToPrimitiveMethod());
                    sb.append(';');
                } else {
                    sb.append("return ("); //$NON-NLS-1$
                    sb.append(returnType.getShortName());
                    sb.append(") newKey;"); //$NON-NLS-1$
                }

                method.addBodyLine(sb.toString());
            }
        }

        if (context.getPlugins().clientInsertMethodGenerated(method,
                topLevelClass, introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = getMethodShell(importedTypes);

        if (context.getPlugins().clientInsertMethodGenerated(method,
                interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
        Method method = new Method();

        FullyQualifiedJavaType returnType;
        if (introspectedTable.getGeneratedKey() != null) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(introspectedTable.getGeneratedKey().getColumn());
            if (introspectedColumn == null) {
                // the specified column doesn't exist, so don't do the generated
                // key
                // (the warning has already been reported)
                returnType = null;
            } else {
                returnType = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(returnType);
            }
        } else {
            returnType = null;
        }

        method.setReturnType(returnType);
        method.setVisibility(JavaVisibility.PUBLIC);
        DAOMethodNameCalculator methodNameCalculator = getDAOMethodNameCalculator();
        method.setName(methodNameCalculator
                .getInsertMethodName(introspectedTable));

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        for (FullyQualifiedJavaType fqjt : daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        return method;
    }
}
