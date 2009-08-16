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

package org.apache.ibatis.abator.internal.java.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.abator.api.DAOGenerator;
import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.IntrospectedTable;
import org.apache.ibatis.abator.api.dom.java.CompilationUnit;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.java.InnerClass;
import org.apache.ibatis.abator.api.dom.java.JavaVisibility;
import org.apache.ibatis.abator.api.dom.java.Method;
import org.apache.ibatis.abator.api.dom.java.Parameter;
import org.apache.ibatis.abator.api.dom.java.TopLevelClass;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.sqlmap.ExampleClause;
import org.apache.ibatis.abator.internal.util.JavaBeansUtil;

/**
 * This class overrides the base DAO generator class to provide the legacy
 * implementarion of the "by example" methods.
 * 
 * This class can be removed when we remove the Legacy generator set.
 * 
 * @author Jeff Butler
 *
 */
public class BaseLegacyDAOGenerator extends BaseDAOGenerator implements DAOGenerator {
    
    /**
     * 
     */
    public BaseLegacyDAOGenerator(AbstractDAOTemplate daoTemplate) {
        super(daoTemplate, false);
    }

    protected List getSelectByExampleWithoutBLOBsMethods(
            IntrospectedTable introspectedTable,
            boolean interfaceMethod,
            CompilationUnit compilationUnit) {

        if (interfaceMethod && exampleMethodVisibility != JavaVisibility.PUBLIC) {
            return null;
        }

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = javaModelGenerator.getExampleType(table);
        compilationUnit.addImportedType(type);
        compilationUnit.addImportedType(FullyQualifiedJavaType.getNewListInstance());

        Method method1 = new Method();
        method1.setVisibility(exampleMethodVisibility);
        method1.setReturnType(FullyQualifiedJavaType.getNewListInstance());
        method1.setName(methodNameCalculator.getSelectByExampleWithoutBLOBsMethodName(introspectedTable));
        method1.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        method1.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(),
                "orderByClause")); //$NON-NLS-1$
        
        Method method2 = new Method();
        method2.setVisibility(JavaVisibility.PUBLIC);
        method2.setReturnType(FullyQualifiedJavaType.getNewListInstance());
        method2.setName(methodNameCalculator.getSelectByExampleWithoutBLOBsMethodName(introspectedTable));
        method2.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        
        Iterator iter = daoTemplate.getCheckedExceptions().iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
            method1.addException(fqjt);
            method2.addException(fqjt);
            compilationUnit.addImportedType(fqjt);
        }

        abatorContext.getCommentGenerator().addGeneralMethodComment(method1, table);
        abatorContext.getCommentGenerator().addGeneralMethodComment(method2, table);
        
        if (!interfaceMethod) {
            // generate the implementation method
            compilationUnit.addImportedType(FullyQualifiedJavaType.getNewMapInstance());
            
            StringBuffer sb = new StringBuffer();

            if (abatorContext.getSuppressTypeWarnings()) {
                method1.addSuppressTypeWarningsAnnotation();
            }
            method1.addBodyLine("Map parms = getExampleParms(example);"); //$NON-NLS-1$
            method1.addBodyLine("if (orderByClause != null) {"); //$NON-NLS-1$
            method1.addBodyLine("parms.put(\"ABATOR_ORDER_BY_CLAUSE\", orderByClause);"); //$NON-NLS-1$
            method1.addBodyLine("}"); //$NON-NLS-1$

            sb.append("List list = "); //$NON-NLS-1$
            sb.append(daoTemplate.getQueryForListMethod(sqlMapGenerator.getSqlMapNamespace(table),
                    sqlMapGenerator.getSelectByExampleStatementId(),
                    "parms")); //$NON-NLS-1$
            method1.addBodyLine(sb.toString());
            method1.addBodyLine("return list;"); //$NON-NLS-1$


            sb.setLength(0);
            sb.append("return "); //$NON-NLS-1$
            sb.append(methodNameCalculator.getSelectByExampleWithoutBLOBsMethodName(introspectedTable));
            sb.append("(example, null);"); //$NON-NLS-1$
            method2.addBodyLine(sb.toString());
        }

        ArrayList answer = new ArrayList();
        answer.add(method1);
        answer.add(method2);

        return answer;
    }

    protected List getSelectByExampleWithBLOBsMethods(
            IntrospectedTable introspectedTable,
            boolean interfaceMethod,
            CompilationUnit compilationUnit) {

        if (interfaceMethod && exampleMethodVisibility != JavaVisibility.PUBLIC) {
            return null;
        }
        
        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = javaModelGenerator.getExampleType(table);
        compilationUnit.addImportedType(type);
        compilationUnit.addImportedType(FullyQualifiedJavaType.getNewListInstance());

        Method method1 = new Method();
        method1.setVisibility(exampleMethodVisibility);
        method1.setReturnType(FullyQualifiedJavaType.getNewListInstance());
        method1.setName(methodNameCalculator.getSelectByExampleWithBLOBsMethodName(introspectedTable));
        method1.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        method1.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(),
                "orderByClause")); //$NON-NLS-1$
        
        Method method2 = new Method();
        method2.setVisibility(JavaVisibility.PUBLIC);
        method2.setReturnType(FullyQualifiedJavaType.getNewListInstance());
        method2.setName(methodNameCalculator.getSelectByExampleWithBLOBsMethodName(introspectedTable));
        method2.addParameter(new Parameter(type, "example")); //$NON-NLS-1$

        Iterator iter = daoTemplate.getCheckedExceptions().iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
            method1.addException(fqjt);
            method2.addException(fqjt);
            compilationUnit.addImportedType(fqjt);
        }

        abatorContext.getCommentGenerator().addGeneralMethodComment(method1, table);
        abatorContext.getCommentGenerator().addGeneralMethodComment(method2, table);
        
        if (!interfaceMethod) {
            // generate the implementation method
            compilationUnit.addImportedType(FullyQualifiedJavaType.getNewMapInstance());

            StringBuffer sb = new StringBuffer();

            if (abatorContext.getSuppressTypeWarnings()) {
                method1.addSuppressTypeWarningsAnnotation();
            }
            method1.addBodyLine("Map parms = getExampleParms(example);"); //$NON-NLS-1$
            method1.addBodyLine("if (orderByClause != null) {"); //$NON-NLS-1$
            method1.addBodyLine("parms.put(\"ABATOR_ORDER_BY_CLAUSE\", orderByClause);"); //$NON-NLS-1$
            method1.addBodyLine("}"); //$NON-NLS-1$

            sb.append("List list = "); //$NON-NLS-1$
            sb.append(daoTemplate.getQueryForListMethod(sqlMapGenerator.getSqlMapNamespace(table),
                    sqlMapGenerator.getSelectByExampleWithBLOBsStatementId(),
                    "parms")); //$NON-NLS-1$
            method1.addBodyLine(sb.toString());
            method1.addBodyLine("return list;"); //$NON-NLS-1$

            sb.setLength(0);
            sb.append("return "); //$NON-NLS-1$
            sb.append(methodNameCalculator.getSelectByExampleWithBLOBsMethodName(introspectedTable));
            sb.append("(example, null);"); //$NON-NLS-1$
            method2.addBodyLine(sb.toString());
        }

        ArrayList answer = new ArrayList();
        answer.add(method1);
        answer.add(method2);

        return answer;
    }

    protected List getDeleteByExampleMethods(
            IntrospectedTable introspectedTable,
            boolean interfaceMethod,
            CompilationUnit compilationUnit) {

        if (interfaceMethod && exampleMethodVisibility != JavaVisibility.PUBLIC) {
            return null;
        }
        
        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = javaModelGenerator.getExampleType(table);
        compilationUnit.addImportedType(type);

        Method method = new Method();
        method.setVisibility(exampleMethodVisibility);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(methodNameCalculator.getDeleteByExampleMethodName(introspectedTable));
        method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        
        Iterator iter = daoTemplate.getCheckedExceptions().iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
            method.addException(fqjt);
            compilationUnit.addImportedType(fqjt);
        }

        abatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

        if (!interfaceMethod) {
            // generate the implementation method
            StringBuffer sb = new StringBuffer();

            sb.append("int rows = "); //$NON-NLS-1$
            sb.append(daoTemplate.getDeleteMethod(sqlMapGenerator.getSqlMapNamespace(table),
                    sqlMapGenerator.getDeleteByExampleStatementId(),
                    "getExampleParms(example)")); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
            
            method.addBodyLine("return rows;"); //$NON-NLS-1$
        }

        ArrayList answer = new ArrayList();
        answer.add(method);

        return answer;
    }

    protected List getCountByExampleMethods(
            IntrospectedTable introspectedTable,
            boolean interfaceMethod,
            CompilationUnit compilationUnit) {

        if (interfaceMethod && exampleMethodVisibility != JavaVisibility.PUBLIC) {
            return null;
        }
        
        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = javaModelGenerator.getExampleType(table);
        compilationUnit.addImportedType(type);

        Method method = new Method();
        method.setVisibility(exampleMethodVisibility);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(methodNameCalculator.getCountByExampleMethodName(introspectedTable));
        method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        
        Iterator iter = daoTemplate.getCheckedExceptions().iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
            method.addException(fqjt);
            compilationUnit.addImportedType(fqjt);
        }

        abatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

        if (!interfaceMethod) {
            // generate the implementation method
            StringBuffer sb = new StringBuffer();

            sb.append("Integer count = (Integer) "); //$NON-NLS-1$
            sb.append(daoTemplate.getQueryForObjectMethod(sqlMapGenerator.getSqlMapNamespace(table),
                    sqlMapGenerator.getCountByExampleStatementId(),
                    "getExampleParms(example)")); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
            
            method.addBodyLine("return count.intValue();"); //$NON-NLS-1$
        }

        ArrayList answer = new ArrayList();
        answer.add(method);

        return answer;
    }

    protected void afterImplementationGenerationHook(
            IntrospectedTable introspectedTable,
            TopLevelClass generatedClass) {

        if (!introspectedTable.getRules().generateExampleClass()) {
            return;
        }

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType type = javaModelGenerator.getExampleType(table);
        
        generatedClass.addImportedType(FullyQualifiedJavaType.getNewMapInstance());
        generatedClass.addImportedType(FullyQualifiedJavaType.getNewHashMapInstance());
        generatedClass.addImportedType(type);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        if (abatorContext.getSuppressTypeWarnings()) {
            method.addSuppressTypeWarningsAnnotation();
        }
        method.setReturnType(FullyQualifiedJavaType.getNewMapInstance());
        method.setName("getExampleParms"); //$NON-NLS-1$
        method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
        
        abatorContext.getCommentGenerator().addGeneralMethodComment(method, table);
        
        method.addBodyLine("Map parms = new HashMap();"); //$NON-NLS-1$

        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            StringBuffer sb = new StringBuffer();

            Method method1 = getExampleParmsMethod(cd, table);
            if (method1 != null) {
                generatedClass.addMethod(method1);

                sb.setLength(0);
                sb.append("parms.putAll("); //$NON-NLS-1$
                sb.append(method1.getName());
                sb.append("(example));"); //$NON-NLS-1$
                method.addBodyLine(sb.toString());
            }
        }

        method.addBodyLine("return parms;"); //$NON-NLS-1$

        generatedClass.addMethod(method);
    }

    /**
     * This method returns a properly formatted method that sets up example
     * parms for an individual column. In the generated DAO, the method will be
     * called by the <code>getExampleParms</code> method. The expectation is
     * that there will be one column based method for each column in the table
     * (except BLOB columns). We do it this way to avoid generating one huge
     * method - which in some cases can actually be too large to compile. The
     * generated method should have this signature:
     * 
     * <pre>
     * 
     *     protected Map getXXXXExampleParms(YYYY example)
     *  
     * </pre>
     * 
     * Where XXXX is the column name and YYYY is the example class
     * 
     * @param cd
     *            the column for which the method should be generated
     * @param table
     *            the table in which the column exists
     * @return the method
     */
    protected Method getExampleParmsMethod(ColumnDefinition cd,
            FullyQualifiedTable table) {
        
        StringBuffer sb = new StringBuffer();

        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        if (abatorContext.getSuppressTypeWarnings()) {
            method.addSuppressTypeWarningsAnnotation();
        }
        method.setReturnType(FullyQualifiedJavaType.getNewMapInstance());
        sb.append(JavaBeansUtil.getGetterMethodName(cd.getJavaProperty(), method.getReturnType()));
        sb.append("ExampleParms"); //$NON-NLS-1$
        method.setName(sb.toString());

        method.addParameter(new Parameter(javaModelGenerator.getExampleType(table),
                "example")); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addGeneralMethodComment(method, table);
        
        method.addBodyLine("Map parms = new HashMap();"); //$NON-NLS-1$

        sb.setLength(0);
        sb.append("switch (example."); //$NON-NLS-1$
        String property = cd.getJavaProperty() + "_Indicator"; //$NON-NLS-1$
        sb.append(JavaBeansUtil.getGetterMethodName(property, FullyQualifiedJavaType.getIntInstance()));
        sb.append("()) {"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());

        Iterator clauseIterator = ExampleClause.getAllExampleClauses();
        while (clauseIterator.hasNext()) {
            ExampleClause clause = (ExampleClause) clauseIterator.next();

            if (clause.isCharacterOnly() && !cd.isJdbcCharacterColumn()) {
                continue;
            }

            sb.setLength(0);
            sb.append("case "); //$NON-NLS-1$
            sb.append(javaModelGenerator.getExampleType(table)
                    .getShortName());
            sb.append('.');
            sb.append(clause.getExamplePropertyName());
            sb.append(':');
            method.addBodyLine(sb.toString());
            
            method.addBodyLine("if (example.isCombineTypeOr()) {"); //$NON-NLS-1$

            sb.setLength(0);
            sb.append("parms.put(\""); //$NON-NLS-1$
            sb.append(clause.getSelectorOrProperty(cd, true));
            sb.append("\", \"Y\");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());

            method.addBodyLine("} else {"); //$NON-NLS-1$

            sb.setLength(0);
            sb.append("parms.put(\""); //$NON-NLS-1$
            sb.append(clause.getSelectorAndProperty(cd, true));
            sb.append("\", \"Y\");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());

            method.addBodyLine("}"); //$NON-NLS-1$

            if (clause.isPropertyInMapRequired()) {
                String exampleProperty = cd.getJavaProperty();
                
                sb.setLength(0);
                sb.append("parms.put(\""); //$NON-NLS-1$
                sb.append(exampleProperty);
                sb.append("\", "); //$NON-NLS-1$
                FullyQualifiedJavaType fqjt = cd.getResolvedJavaType()
                        .getFullyQualifiedJavaType();
                if (fqjt.isPrimitive()) {
                    sb.append("new "); //$NON-NLS-1$
                    sb.append(fqjt.getPrimitiveTypeWrapper().getShortName());
                    sb.append('(');
                    sb.append("example."); //$NON-NLS-1$
                    sb.append(JavaBeansUtil
                            .getGetterMethodName(exampleProperty, cd.getResolvedJavaType().getFullyQualifiedJavaType()));
                    sb.append("()));"); //$NON-NLS-1$
                } else {
                    sb.append("example."); //$NON-NLS-1$
                    sb.append(JavaBeansUtil
                            .getGetterMethodName(exampleProperty, cd.getResolvedJavaType().getFullyQualifiedJavaType()));
                    sb.append("());"); //$NON-NLS-1$
                }
                method.addBodyLine(sb.toString());
            }

            method.addBodyLine("break;"); //$NON-NLS-1$
        }

        method.addBodyLine("}"); //$NON-NLS-1$

        method.addBodyLine("return parms;"); //$NON-NLS-1$

        return method;
    }

    protected List getUpdateByExampleSelectiveMethods(IntrospectedTable introspectedTable, boolean interfaceMethod, CompilationUnit compilationUnit) {
        // this method is not supported in the legacy generator set
        return null;
    }

    protected List getUpdateByExampleWithBLOBsMethods(IntrospectedTable introspectedTable, boolean interfaceMethod, CompilationUnit compilationUnit) {
        // this method is not supported in the legacy generator set
        return null;
    }

    protected List getUpdateByExampleWithoutBLOBsMethods(IntrospectedTable introspectedTable, boolean interfaceMethod, CompilationUnit compilationUnit) {
        // this method is not supported in the legacy generator set
        return null;
    }

    protected InnerClass getUpdateByExampleParms(IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        // this method is not supported in the legacy generator set
        return null;
    }
}
