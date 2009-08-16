/*
 *  Copyright 2006 The Apache Software Foundation
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

package org.apache.ibatis.abator.internal.java.model;

import java.util.Iterator;

import org.apache.ibatis.abator.api.CommentGenerator;
import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.IntrospectedTable;
import org.apache.ibatis.abator.api.JavaModelGenerator;
import org.apache.ibatis.abator.api.dom.java.Field;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.java.JavaVisibility;
import org.apache.ibatis.abator.api.dom.java.Method;
import org.apache.ibatis.abator.api.dom.java.Parameter;
import org.apache.ibatis.abator.api.dom.java.TopLevelClass;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.sqlmap.ExampleClause;
import org.apache.ibatis.abator.internal.util.JavaBeansUtil;

/**
 * This class overrides methods in the Java2 implementation to provide the
 * legacy implementation of the example class.
 * 
 * This class can be removed when we remove the Legacy generator set.
 * 
 * @author Jeff Butler
 *
 */
public class JavaModelGeneratorLegacyImpl extends JavaModelGeneratorJava2Impl implements JavaModelGenerator {

    /**
     * 
     */
    public JavaModelGeneratorLegacyImpl() {
        super();
    }

    protected TopLevelClass getExample(IntrospectedTable introspectedTable) {
        if (!introspectedTable.getRules().generateExampleClass()) {
            return null;
        }
        
        CommentGenerator commentGenerator = abatorContext.getCommentGenerator();

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = getExampleType(table);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        commentGenerator.addJavaFileComment(topLevelClass);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        
        if (introspectedTable.getRules().generateBaseRecordClass()) {
            topLevelClass.setSuperClass(getBaseRecordType(table));
        } else {
            topLevelClass.setSuperClass(getPrimaryKeyType(table));
        }

        StringBuffer sb = new StringBuffer();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setModifierStatic(true);
        field.setModifierFinal(true);
        field.setType(FullyQualifiedJavaType.getIntInstance());
        field.setName("EXAMPLE_IGNORE"); //$NON-NLS-1$
        field.setInitializationString("0"); //$NON-NLS-1$
        commentGenerator.addFieldComment(field, table);
        topLevelClass.addField(field);

        Iterator iter = ExampleClause.getAllExampleClauses();
        while (iter.hasNext()) {
            ExampleClause clause = (ExampleClause) iter.next();
            field = new Field();
            field.setVisibility(JavaVisibility.PUBLIC);
            field.setModifierStatic(true);
            field.setModifierFinal(true);
            field.setType(FullyQualifiedJavaType.getIntInstance());
            field.setName(clause.getExamplePropertyName());
            field.setInitializationString(Integer.toString(clause
                    .getExamplePropertyValue()));
            commentGenerator.addFieldComment(field, table);
            topLevelClass.addField(field);
        }

        field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        field.setName("combineTypeOr"); //$NON-NLS-1$
        commentGenerator.addFieldComment(field, table);
        topLevelClass.addField(field);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setCombineTypeOr"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance(), "combineTypeOr")); //$NON-NLS-1$
        method.addBodyLine("this.combineTypeOr = combineTypeOr;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, table);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance());
        method.setName("isCombineTypeOr"); //$NON-NLS-1$
        method.addBodyLine("return combineTypeOr;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, table);
        topLevelClass.addMethod(method);

        iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            String fieldName = cd.getJavaProperty() + "_Indicator"; //$NON-NLS-1$

            field = new Field();
            field.setVisibility(JavaVisibility.PRIVATE);
            field.setType(FullyQualifiedJavaType.getIntInstance());
            field.setName(fieldName);
            commentGenerator.addFieldComment(field, table);
            topLevelClass.addField(field);

            method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setName(JavaBeansUtil.getGetterMethodName(field.getName(), field.getType()));
            sb.setLength(0);
            sb.append("return "); //$NON-NLS-1$
            sb.append(fieldName);
            sb.append(';');
            method.addBodyLine(sb.toString());
            commentGenerator.addGeneralMethodComment(method, table);
            topLevelClass.addMethod(method);

            method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setName(JavaBeansUtil.getSetterMethodName(fieldName));
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getIntInstance(), fieldName));
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(fieldName);
            sb.append(" = "); //$NON-NLS-1$
            sb.append(fieldName);
            sb.append(';');
            method.addBodyLine(sb.toString());
            commentGenerator.addGeneralMethodComment(method, table);
            topLevelClass.addMethod(method);
        }

        return topLevelClass;
    }
}
