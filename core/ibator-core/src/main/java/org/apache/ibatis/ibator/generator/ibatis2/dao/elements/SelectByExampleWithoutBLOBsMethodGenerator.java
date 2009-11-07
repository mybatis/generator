/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import java.util.Set;
import java.util.TreeSet;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * 
 * @author Jeff Butler
 *
 */
public class SelectByExampleWithoutBLOBsMethodGenerator extends
        AbstractDAOElementGenerator {

    private boolean generateForJava5;

    public SelectByExampleWithoutBLOBsMethodGenerator(boolean generateForJava5) {
        super();
        this.generateForJava5 = generateForJava5;
    }

    @Override
    public void addImplementationElements(TopLevelClass topLevelClass) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = getMethodShell(importedTypes);

        if (generateForJava5) {
            method.addSuppressTypeWarningsAnnotation();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getShortName());
        sb.append(" list = "); //$NON-NLS-1$
        sb.append(daoTemplate.getQueryForListMethod(introspectedTable.getIbatis2SqlMapNamespace(),
                introspectedTable.getSelectByExampleStatementId(), "example")); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return list;"); //$NON-NLS-1$

        if (ibatorContext.getPlugins().daoSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        if (getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
            Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
            Method method = getMethodShell(importedTypes);
            
            if (ibatorContext.getPlugins().daoSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                interfaze.addImportedTypes(importedTypes);
                interfaze.addMethod(method);
            }
        }
    }

    private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(getExampleMethodVisibility());

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType
                .getNewListInstance();
        ;
        if (generateForJava5) {
            FullyQualifiedJavaType fqjt;
            if (introspectedTable.getRules().generateBaseRecordClass()) {
                fqjt = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            } else if (introspectedTable.getRules().generatePrimaryKeyClass()) {
                fqjt = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            } else {
                throw new RuntimeException(Messages
                        .getString("RuntimeError.12")); //$NON-NLS-1$
            }

            importedTypes.add(fqjt);
            returnType.addTypeArgument(fqjt);
        }

        method.setReturnType(returnType);

        if (ibatorContext.getSuppressTypeWarnings(introspectedTable)) {
            method.addSuppressTypeWarningsAnnotation();
        }
        
        method.setName(getDAOMethodNameCalculator()
                .getSelectByExampleWithoutBLOBsMethodName(introspectedTable));
        method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$

        for (FullyQualifiedJavaType fqjt : daoTemplate.getCheckedExceptions()) {
            method.addException(fqjt);
            importedTypes.add(fqjt);
        }

        ibatorContext.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        return method;
    }
}
