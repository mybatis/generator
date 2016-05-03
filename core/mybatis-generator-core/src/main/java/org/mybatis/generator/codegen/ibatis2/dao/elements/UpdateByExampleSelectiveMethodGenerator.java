/**
 *    Copyright 2006-2016 the original author or authors.
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

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateByExampleSelectiveMethodGenerator extends
        AbstractDAOElementGenerator {

    @Override
    public void addImplementationElements(TopLevelClass topLevelClass) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = getMethodShell(importedTypes);

        method
                .addBodyLine("UpdateByExampleParms parms = new UpdateByExampleParms(record, example);"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();

        sb.append("int rows = "); //$NON-NLS-1$

        sb.append(daoTemplate.getUpdateMethod(introspectedTable
                .getIbatis2SqlMapNamespace(), introspectedTable
                .getUpdateByExampleSelectiveStatementId(), "parms")); //$NON-NLS-1$
        method.addBodyLine(sb.toString());

        method.addBodyLine("return rows;"); //$NON-NLS-1$

        if (context.getPlugins()
                .clientUpdateByExampleSelectiveMethodGenerated(method,
                        topLevelClass, introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        if (getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
            Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
            Method method = getMethodShell(importedTypes);

            if (context.getPlugins()
                    .clientUpdateByExampleSelectiveMethodGenerated(method,
                            interfaze, introspectedTable)) {
                interfaze.addImportedTypes(importedTypes);
                interfaze.addMethod(method);
            }
        }
    }

    private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else if (introspectedTable.getRules().generateBaseRecordClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getPrimaryKeyType());
        }

        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(getExampleMethodVisibility());
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(getDAOMethodNameCalculator()
                .getUpdateByExampleSelectiveMethodName(introspectedTable));
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType(
                introspectedTable.getExampleType()), "example")); //$NON-NLS-1$

        for (FullyQualifiedJavaType fqjt : daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        return method;
    }
}
