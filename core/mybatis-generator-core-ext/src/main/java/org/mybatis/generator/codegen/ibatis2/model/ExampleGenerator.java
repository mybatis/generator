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
package org.mybatis.generator.codegen.ibatis2.model;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.internal.rules.Rules;

/**
 * Generates the example class for iBatis2.
 * 
 * @author Jeff Butler
 * 
 */
public class ExampleGenerator extends AbstractJavaGenerator {

    private boolean generateForJava5;

    public ExampleGenerator(boolean generateForJava5) {
        super();
        this.generateForJava5 = generateForJava5;
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.6", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        // add default constructor
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(type.getShortName());
        if (generateForJava5) {
            method.addBodyLine("oredCriteria = new ArrayList<Criteria>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("oredCriteria = new ArrayList();"); //$NON-NLS-1$
        }

        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add shallow copy constructor if the update by
        // example methods are enabled - because the parameter
        // class for update by example methods will subclass this class
        Rules rules = introspectedTable.getRules();
        if (rules.generateUpdateByExampleSelective()
                || rules.generateUpdateByExampleWithBLOBs()
                || rules.generateUpdateByExampleWithoutBLOBs()) {
            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setConstructor(true);
            method.setName(type.getShortName());
            method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$
            method.addBodyLine("this.orderByClause = example.orderByClause;"); //$NON-NLS-1$
            method.addBodyLine("this.oredCriteria = example.oredCriteria;"); //$NON-NLS-1$
            method.addBodyLine("this.distinct = example.distinct;"); //$NON-NLS-1$
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);
        }

        // add field, getter, setter for orderby clause
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        field.setName("orderByClause"); //$NON-NLS-1$
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setOrderByClause"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "orderByClause")); //$NON-NLS-1$
        method.addBodyLine("this.orderByClause = orderByClause;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getOrderByClause"); //$NON-NLS-1$
        method.addBodyLine("return orderByClause;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add field, getter, setter for distinct
        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        field.setName("distinct"); //$NON-NLS-1$
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setDistinct"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance(), "distinct")); //$NON-NLS-1$
        method.addBodyLine("this.distinct = distinct;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance());
        method.setName("isDistinct"); //$NON-NLS-1$
        method.addBodyLine("return distinct;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // add field and methods for the list of ored criteria
        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);

        FullyQualifiedJavaType fqjt;
        if (generateForJava5) {
            fqjt = new FullyQualifiedJavaType("java.util.List<Criteria>"); //$NON-NLS-1$
        } else {
            fqjt = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }

        field.setType(fqjt);
        field.setName("oredCriteria"); //$NON-NLS-1$
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName("getOredCriteria"); //$NON-NLS-1$
        method.addBodyLine("return oredCriteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("or"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getCriteriaInstance(), "criteria")); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("or"); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = createCriteriaInternal();"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("createCriteria"); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = createCriteriaInternal();"); //$NON-NLS-1$
        method.addBodyLine("if (oredCriteria.size() == 0) {"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("createCriteriaInternal"); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = new Criteria();"); //$NON-NLS-1$
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("clear"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.clear();"); //$NON-NLS-1$
        method.addBodyLine("orderByClause = null;"); //$NON-NLS-1$
        method.addBodyLine("distinct = false;"); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        // now generate the inner class that holds the AND conditions
        topLevelClass
                .addInnerClass(getGeneratedCriteriaInnerClass(topLevelClass));

        topLevelClass.addInnerClass(getCriteriaInnerClass());

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelExampleClassGenerated(
                topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private InnerClass getCriteriaInnerClass() {
        InnerClass answer = new InnerClass(FullyQualifiedJavaType
                .getCriteriaInstance());

        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setStatic(true);
        answer.setSuperClass(FullyQualifiedJavaType
                .getGeneratedCriteriaInstance());

        context.getCommentGenerator().addClassComment(answer,
                introspectedTable, true);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("Criteria"); //$NON-NLS-1$
        method.setConstructor(true);
        method.addBodyLine("super();"); //$NON-NLS-1$
        answer.addMethod(method);

        return answer;
    }

    private InnerClass getGeneratedCriteriaInnerClass(
            TopLevelClass topLevelClass) {
        InnerClass answer = new InnerClass(FullyQualifiedJavaType
                .getGeneratedCriteriaInstance());

        answer.setVisibility(JavaVisibility.PROTECTED);
        answer.setStatic(true);
        answer.setAbstract(true);
        context.getCommentGenerator().addClassComment(answer,
                introspectedTable);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("GeneratedCriteria"); //$NON-NLS-1$
        method.setConstructor(true);
        method.addBodyLine("super();"); //$NON-NLS-1$
        if (generateForJava5) {
            method
                    .addBodyLine("criteriaWithoutValue = new ArrayList<String>();"); //$NON-NLS-1$
            method
                    .addBodyLine("criteriaWithSingleValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$
            method
                    .addBodyLine("criteriaWithListValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$
            method
                    .addBodyLine("criteriaWithBetweenValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$

        } else {
            method.addBodyLine("criteriaWithoutValue = new ArrayList();"); //$NON-NLS-1$
            method.addBodyLine("criteriaWithSingleValue = new ArrayList();"); //$NON-NLS-1$
            method.addBodyLine("criteriaWithListValue = new ArrayList();"); //$NON-NLS-1$
            method.addBodyLine("criteriaWithBetweenValue = new ArrayList();"); //$NON-NLS-1$
        }
        answer.addMethod(method);

        List<String> criteriaLists = new ArrayList<String>();
        criteriaLists.add("criteriaWithoutValue"); //$NON-NLS-1$
        criteriaLists.add("criteriaWithSingleValue"); //$NON-NLS-1$
        criteriaLists.add("criteriaWithListValue"); //$NON-NLS-1$
        criteriaLists.add("criteriaWithBetweenValue"); //$NON-NLS-1$

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonBLOBColumns()) {
            if (stringHasValue(introspectedColumn
                    .getTypeHandler())) {
                criteriaLists.addAll(addtypeHandledObjectsAndMethods(
                        introspectedColumn, method, answer));
            }
        }

        // now generate the isValid method
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("isValid"); //$NON-NLS-1$
        method.setReturnType(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance());
        StringBuilder sb = new StringBuilder();
        Iterator<String> strIter = criteriaLists.iterator();
        sb.append("return "); //$NON-NLS-1$
        sb.append(strIter.next());
        sb.append(".size() > 0"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        while (strIter.hasNext()) {
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append("|| "); //$NON-NLS-1$
            sb.append(strIter.next());
            sb.append(".size() > 0"); //$NON-NLS-1$
            if (!strIter.hasNext()) {
                sb.append(';');
            }
            method.addBodyLine(sb.toString());
        }
        answer.addMethod(method);

        // now we need to generate the methods that will be used in the SqlMap
        // to generate the dynamic where clause
        topLevelClass.addImportedType(FullyQualifiedJavaType
                .getNewMapInstance());
        topLevelClass.addImportedType(FullyQualifiedJavaType
                .getNewListInstance());
        topLevelClass.addImportedType(FullyQualifiedJavaType
                .getNewHashMapInstance());
        topLevelClass.addImportedType(FullyQualifiedJavaType
                .getNewArrayListInstance());

        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        FullyQualifiedJavaType listOfStrings;
        if (generateForJava5) {
            listOfStrings = new FullyQualifiedJavaType(
                    "java.util.List<java.lang.String>"); //$NON-NLS-1$
        } else {
            listOfStrings = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }
        field.setType(listOfStrings);
        field.setName("criteriaWithoutValue"); //$NON-NLS-1$
        answer.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        method.addBodyLine("return criteriaWithoutValue;"); //$NON-NLS-1$
        answer.addMethod(method);

        FullyQualifiedJavaType listOfMaps;
        if (generateForJava5) {
            listOfMaps = new FullyQualifiedJavaType(
                    "java.util.List<java.util.Map<java.lang.String, java.lang.Object>>"); //$NON-NLS-1$
        } else {
            listOfMaps = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }

        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName("criteriaWithSingleValue"); //$NON-NLS-1$
        answer.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        method.addBodyLine("return criteriaWithSingleValue;"); //$NON-NLS-1$
        answer.addMethod(method);

        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName("criteriaWithListValue"); //$NON-NLS-1$
        answer.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        method.addBodyLine("return criteriaWithListValue;"); //$NON-NLS-1$
        answer.addMethod(method);

        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName("criteriaWithBetweenValue"); //$NON-NLS-1$
        answer.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        method.addBodyLine("return criteriaWithBetweenValue;"); //$NON-NLS-1$
        answer.addMethod(method);

        // now add the methods for simplifying the individual field set methods
        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("addCriterion"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "condition")); //$NON-NLS-1$
        method.addBodyLine("if (condition == null) {"); //$NON-NLS-1$
        method
                .addBodyLine("throw new RuntimeException(\"Value for condition cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("criteriaWithoutValue.add(condition);"); //$NON-NLS-1$
        answer.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("addCriterion"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getObjectInstance(), "value")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        if (generateForJava5) {
            method.addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }

        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"value\", value);"); //$NON-NLS-1$
        method.addBodyLine("criteriaWithSingleValue.add(map);"); //$NON-NLS-1$
        answer.addMethod(method);

        FullyQualifiedJavaType listOfObjects;
        if (generateForJava5) {
            listOfObjects = new FullyQualifiedJavaType("java.util.List<? extends java.lang.Object>"); //$NON-NLS-1$
        } else {
            listOfObjects = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("addCriterion"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(listOfObjects, "values")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Value list for \" + property + \" cannot be null or empty\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        if (generateForJava5) {
            method.addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }

        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"values\", values);"); //$NON-NLS-1$
        method.addBodyLine("criteriaWithListValue.add(map);"); //$NON-NLS-1$
        answer.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName("addCriterion"); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value1")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value2")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
        method.addBodyLine(
                "throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        if (generateForJava5) {
            method.addBodyLine("List<Object> list = new ArrayList<Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("List list = new ArrayList();"); //$NON-NLS-1$
        }

        method.addBodyLine("list.add(value1);"); //$NON-NLS-1$
        method.addBodyLine("list.add(value2);"); //$NON-NLS-1$
        if (generateForJava5) {
            method.addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }
        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"values\", list);"); //$NON-NLS-1$
        method.addBodyLine("criteriaWithBetweenValue.add(map);"); //$NON-NLS-1$
        answer.addMethod(method);

        FullyQualifiedJavaType listOfDates;
        if (generateForJava5) {
            listOfDates = new FullyQualifiedJavaType("java.util.List<java.util.Date>"); //$NON-NLS-1$
        } else {
            listOfDates = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }

        if (introspectedTable.hasJDBCDateColumns()) {
            topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewIteratorInstance());
            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Date(value.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(listOfDates, "values")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value list for \" + property + \" cannot be null or empty\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            if (generateForJava5) {
                method.addBodyLine("List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();"); //$NON-NLS-1$
                method.addBodyLine("Iterator<Date> iter = values.iterator();"); //$NON-NLS-1$
                method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
                method.addBodyLine("dateList.add(new java.sql.Date(iter.next().getTime()));"); //$NON-NLS-1$
                method.addBodyLine("}"); //$NON-NLS-1$
            } else {
                method.addBodyLine("List dateList = new ArrayList();"); //$NON-NLS-1$
                method.addBodyLine("Iterator iter = values.iterator();"); //$NON-NLS-1$
                method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
                method.addBodyLine("dateList.add(new java.sql.Date(((Date)iter.next()).getTime()));"); //$NON-NLS-1$
                method.addBodyLine("}"); //$NON-NLS-1$
            }
            method.addBodyLine("addCriterion(condition, dateList, property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCDate"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value2")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);
        }

        if (introspectedTable.hasJDBCTimeColumns()) {
            topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewIteratorInstance());
            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getDateInstance(), "value")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine("addCriterion(condition, new java.sql.Time(value.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(listOfDates, "values")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Value list for \" + property + \" cannot be null or empty\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            if (generateForJava5) {
                method.addBodyLine("List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();"); //$NON-NLS-1$
                method.addBodyLine("Iterator<Date> iter = values.iterator();"); //$NON-NLS-1$
                method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
                method.addBodyLine("timeList.add(new java.sql.Time(iter.next().getTime()));"); //$NON-NLS-1$
                method.addBodyLine("}"); //$NON-NLS-1$
            } else {
                method.addBodyLine("List timeList = new ArrayList();"); //$NON-NLS-1$
                method.addBodyLine("Iterator iter = values.iterator();"); //$NON-NLS-1$
                method.addBodyLine("while (iter.hasNext()) {"); //$NON-NLS-1$
                method.addBodyLine("timeList.add(new java.sql.Time(((Date)iter.next()).getTime()));"); //$NON-NLS-1$
                method.addBodyLine("}"); //$NON-NLS-1$
            }
            method.addBodyLine("addCriterion(condition, timeList, property);"); //$NON-NLS-1$
            answer.addMethod(method);

            method = new Method();
            method.setVisibility(JavaVisibility.PROTECTED);
            method.setName("addCriterionForJDBCTime"); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getStringInstance(), "condition")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getDateInstance(), "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getDateInstance(), "value2")); //$NON-NLS-1$
            method.addParameter(new Parameter(FullyQualifiedJavaType
                    .getStringInstance(), "property")); //$NON-NLS-1$
            method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
            method.addBodyLine(
                    "throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
            method.addBodyLine(
                    "addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);"); //$NON-NLS-1$
            answer.addMethod(method);
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonBLOBColumns()) {
            topLevelClass.addImportedType(introspectedColumn
                    .getFullyQualifiedJavaType());

            // here we need to add the individual methods for setting the
            // conditions for a field
            answer.addMethod(getSetNullMethod(introspectedColumn));
            answer.addMethod(getSetNotNullMethod(introspectedColumn));
            answer.addMethod(getSetEqualMethod(introspectedColumn));
            answer.addMethod(getSetNotEqualMethod(introspectedColumn));
            answer.addMethod(getSetGreaterThanMethod(introspectedColumn));
            answer
                    .addMethod(getSetGreaterThenOrEqualMethod(introspectedColumn));
            answer.addMethod(getSetLessThanMethod(introspectedColumn));
            answer.addMethod(getSetLessThanOrEqualMethod(introspectedColumn));

            if (introspectedColumn.isJdbcCharacterColumn()) {
                answer.addMethod(getSetLikeMethod(introspectedColumn));
                answer.addMethod(getSetNotLikeMethod(introspectedColumn));
            }

            answer.addMethod(getSetInOrNotInMethod(introspectedColumn, true));
            answer.addMethod(getSetInOrNotInMethod(introspectedColumn, false));
            answer.addMethod(getSetBetweenOrNotBetweenMethod(
                    introspectedColumn, true));
            answer.addMethod(getSetBetweenOrNotBetweenMethod(
                    introspectedColumn, false));
        }

        return answer;
    }

    /**
     * This method adds all the extra methods and fields required to support a
     * user defined type handler on some column.
     * 
     * @param introspectedColumn the column with a user defined type handler
     * @param constructor the constructor will be modified to create new criteria lists for 
     *     the column
     * @param innerClass the inner class will be modified to add new criteria lists for the column
     * @return a list of the names of all Lists added to the class by this
     *         method
     */
    private List<String> addtypeHandledObjectsAndMethods(
            IntrospectedColumn introspectedColumn, Method constructor,
            InnerClass innerClass) {

        // add new private fields and public accessors in the class
        FullyQualifiedJavaType listOfMaps;
        if (generateForJava5) {
            listOfMaps = new FullyQualifiedJavaType(
                    "java.util.List<java.util.Map<java.lang.String, java.lang.Object>>"); //$NON-NLS-1$
        } else {
            listOfMaps = new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
        }

        List<String> answer = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithSingleValue"); //$NON-NLS-1$
        answer.add(sb.toString());

        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName(sb.toString());
        innerClass.addField(field);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        sb.insert(0, "return "); //$NON-NLS-1$
        sb.append(';');
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithListValue"); //$NON-NLS-1$
        answer.add(sb.toString());

        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName(sb.toString());
        innerClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        sb.insert(0, "return "); //$NON-NLS-1$
        sb.append(';');
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithBetweenValue"); //$NON-NLS-1$
        answer.add(sb.toString());

        field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(listOfMaps);
        field.setName(sb.toString());
        innerClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        sb.insert(0, "return "); //$NON-NLS-1$
        sb.append(';');
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        // add constructor initialization
        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        if (generateForJava5) {
            sb
                    .append("CriteriaWithSingleValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$;
        } else {
            sb.append("CriteriaWithSingleValue = new ArrayList();"); //$NON-NLS-1$;
        }
        constructor.addBodyLine(sb.toString());

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        if (generateForJava5) {
            sb
                    .append("CriteriaWithListValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$
        } else {
            sb.append("CriteriaWithListValue = new ArrayList();"); //$NON-NLS-1$
        }
        constructor.addBodyLine(sb.toString());

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        if (generateForJava5) {
            sb
                    .append("CriteriaWithBetweenValue = new ArrayList<Map<String, Object>>();"); //$NON-NLS-1$
        } else {
            sb.append("CriteriaWithBetweenValue = new ArrayList();"); //$NON-NLS-1$
        }
        constructor.addBodyLine(sb.toString());

        // now add the methods for simplifying the individual field set methods
        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        sb.setLength(0);
        sb.append("add"); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        sb.append("Criterion"); //$NON-NLS-1$

        method.setName(sb.toString());
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "condition")); //$NON-NLS-1$
        if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType().getPrimitiveTypeWrapper(),
                    "value")); //$NON-NLS-1$
        } else {
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType(), "value")); //$NON-NLS-1$
        }
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "property")); //$NON-NLS-1$
        if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
            method.addBodyLine("if (value == null) {"); //$NON-NLS-1$
            method
                .addBodyLine("throw new RuntimeException(\"Value for \" + property + \" cannot be null\");"); //$NON-NLS-1$
            method.addBodyLine("}"); //$NON-NLS-1$
        }
        if (generateForJava5) {
            method
                    .addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }
        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"value\", value);"); //$NON-NLS-1$

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithSingleValue.add(map);"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        FullyQualifiedJavaType listOfObjects = FullyQualifiedJavaType
                .getNewListInstance();
        if (generateForJava5) {
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                listOfObjects.addTypeArgument(introspectedColumn
                        .getFullyQualifiedJavaType().getPrimitiveTypeWrapper());
            } else {
                listOfObjects.addTypeArgument(introspectedColumn
                        .getFullyQualifiedJavaType());
            }
        }

        sb.setLength(0);
        sb.append("add"); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        sb.append("Criterion"); //$NON-NLS-1$

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName(sb.toString());
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "condition")); //$NON-NLS-1$
        method.addParameter(new Parameter(listOfObjects, "values")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (values == null || values.size() == 0) {"); //$NON-NLS-1$
        method
            .addBodyLine("throw new RuntimeException(\"Value list for \" + property + \" cannot be null or empty\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        if (generateForJava5) {
            method
                    .addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }
        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"values\", values);"); //$NON-NLS-1$

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithListValue.add(map);"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        sb.setLength(0);
        sb.append("add"); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
        sb.append("Criterion"); //$NON-NLS-1$

        method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName(sb.toString());
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "condition")); //$NON-NLS-1$
        if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType().getPrimitiveTypeWrapper(),
                    "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType().getPrimitiveTypeWrapper(),
                    "value2")); //$NON-NLS-1$
        } else {
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType(), "value1")); //$NON-NLS-1$
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType(), "value2")); //$NON-NLS-1$
        }
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getStringInstance(), "property")); //$NON-NLS-1$
        method.addBodyLine("if (value1 == null || value2 == null) {"); //$NON-NLS-1$
        method
            .addBodyLine("throw new RuntimeException(\"Between values for \" + property + \" cannot be null\");"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        if (generateForJava5) {
            String shortName;
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                shortName = introspectedColumn.getFullyQualifiedJavaType()
                        .getPrimitiveTypeWrapper().getShortName();
            } else {
                shortName = introspectedColumn.getFullyQualifiedJavaType()
                        .getShortName();
            }
            sb.setLength(0);
            sb.append("List<"); //$NON-NLS-1$
            sb.append(shortName);
            sb.append("> list = new ArrayList<"); //$NON-NLS-1$
            sb.append(shortName);
            sb.append(">();"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        } else {
            method.addBodyLine("List list = new ArrayList();"); //$NON-NLS-1$
        }
        method.addBodyLine("list.add(value1);"); //$NON-NLS-1$
        method.addBodyLine("list.add(value2);"); //$NON-NLS-1$
        if (generateForJava5) {
            method
                    .addBodyLine("Map<String, Object> map = new HashMap<String, Object>();"); //$NON-NLS-1$
        } else {
            method.addBodyLine("Map map = new HashMap();"); //$NON-NLS-1$
        }
        method.addBodyLine("map.put(\"condition\", condition);"); //$NON-NLS-1$
        method.addBodyLine("map.put(\"values\", list);"); //$NON-NLS-1$

        sb.setLength(0);
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("CriteriaWithBetweenValue.add(map);"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        innerClass.addMethod(method);

        return answer;
    }

    private Method getSetNullMethod(IntrospectedColumn introspectedColumn) {
        return getNoValueMethod(introspectedColumn, "IsNull", "is null"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetNotNullMethod(IntrospectedColumn introspectedColumn) {
        return getNoValueMethod(introspectedColumn, "IsNotNull", "is not null"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetEqualMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "EqualTo", "="); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetNotEqualMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "NotEqualTo", "<>"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetGreaterThanMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "GreaterThan", ">"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetGreaterThenOrEqualMethod(
            IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn,
                "GreaterThanOrEqualTo", ">="); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetLessThanMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "LessThan", "<"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetLessThanOrEqualMethod(
            IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn,
                "LessThanOrEqualTo", "<="); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetLikeMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "Like", "like"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSetNotLikeMethod(IntrospectedColumn introspectedColumn) {
        return getSingleValueMethod(introspectedColumn, "NotLike", "not like"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Method getSingleValueMethod(IntrospectedColumn introspectedColumn,
            String nameFragment, String operator) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(introspectedColumn
                .getFullyQualifiedJavaType(), "value")); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "and"); //$NON-NLS-1$
        sb.append(nameFragment);
        method.setName(sb.toString());
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        sb.setLength(0);

        if (introspectedColumn.isJDBCDateColumn()) {
            sb.append("addCriterionForJDBCDate(\""); //$NON-NLS-1$
        } else if (introspectedColumn.isJDBCTimeColumn()) {
            sb.append("addCriterionForJDBCTime(\""); //$NON-NLS-1$
        } else if (stringHasValue(introspectedColumn
                .getTypeHandler())) {
            sb.append("add"); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
            sb.append("Criterion(\""); //$NON-NLS-1$
        } else {
            sb.append("addCriterion(\""); //$NON-NLS-1$
        }

        sb.append(Ibatis2FormattingUtilities
                .getAliasedActualColumnName(introspectedColumn));
        sb.append(' ');
        sb.append(operator);
        sb.append("\", "); //$NON-NLS-1$

        if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive() && !introspectedTable.isJava5Targeted()) {
            sb.append("new "); //$NON-NLS-1$
            sb.append(introspectedColumn.getFullyQualifiedJavaType()
                    .getPrimitiveTypeWrapper().getShortName());
            sb.append("(value)"); //$NON-NLS-1$
        } else {
            sb.append("value"); //$NON-NLS-1$
        }

        sb.append(", \""); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("\");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    /**
     * Generates methods that set between and not between conditions.
     * 
     * @param introspectedColumn the introspected column
     * @param betweenMethod true is between, else not between
     * @return a generated method for the between or not between method
     */
    private Method getSetBetweenOrNotBetweenMethod(
            IntrospectedColumn introspectedColumn, boolean betweenMethod) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType type = introspectedColumn
                .getFullyQualifiedJavaType();

        method.addParameter(new Parameter(type, "value1")); //$NON-NLS-1$
        method.addParameter(new Parameter(type, "value2")); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "and"); //$NON-NLS-1$
        if (betweenMethod) {
            sb.append("Between"); //$NON-NLS-1$
        } else {
            sb.append("NotBetween"); //$NON-NLS-1$
        }
        method.setName(sb.toString());
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        sb.setLength(0);

        if (introspectedColumn.isJDBCDateColumn()) {
            sb.append("addCriterionForJDBCDate(\""); //$NON-NLS-1$
        } else if (introspectedColumn.isJDBCTimeColumn()) {
            sb.append("addCriterionForJDBCTime(\""); //$NON-NLS-1$
        } else if (stringHasValue(introspectedColumn
                .getTypeHandler())) {
            sb.append("add"); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
            sb.append("Criterion(\""); //$NON-NLS-1$
        } else {
            sb.append("addCriterion(\""); //$NON-NLS-1$
        }

        sb.append(Ibatis2FormattingUtilities
                .getAliasedActualColumnName(introspectedColumn));
        if (betweenMethod) {
            sb.append(" between"); //$NON-NLS-1$
        } else {
            sb.append(" not between"); //$NON-NLS-1$
        }
        sb.append("\", "); //$NON-NLS-1$
        if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive() && !introspectedTable.isJava5Targeted()) {
            sb.append("new "); //$NON-NLS-1$
            sb.append(introspectedColumn.getFullyQualifiedJavaType()
                    .getPrimitiveTypeWrapper().getShortName());
            sb.append("(value1), "); //$NON-NLS-1$
            sb.append("new "); //$NON-NLS-1$
            sb.append(introspectedColumn.getFullyQualifiedJavaType()
                    .getPrimitiveTypeWrapper().getShortName());
            sb.append("(value2)"); //$NON-NLS-1$
        } else {
            sb.append("value1, value2"); //$NON-NLS-1$
        }

        sb.append(", \""); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("\");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    /**
     * Calculates an appropriate "in" or "not in" method.
     * 
     * @param introspectedColumn the introspected column
     * @param inMethod
     *            if true generates an "in" method, else generates a "not in"
     *            method
     * @return a generated method for the in or not in method
     */
    private Method getSetInOrNotInMethod(IntrospectedColumn introspectedColumn,
            boolean inMethod) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType type = FullyQualifiedJavaType
                .getNewListInstance();
        if (generateForJava5) {
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                type.addTypeArgument(introspectedColumn
                        .getFullyQualifiedJavaType().getPrimitiveTypeWrapper());
            } else {
                type.addTypeArgument(introspectedColumn
                        .getFullyQualifiedJavaType());
            }
        }

        method.addParameter(new Parameter(type, "values")); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "and"); //$NON-NLS-1$
        if (inMethod) {
            sb.append("In"); //$NON-NLS-1$
        } else {
            sb.append("NotIn"); //$NON-NLS-1$
        }
        method.setName(sb.toString());
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        sb.setLength(0);

        if (introspectedColumn.isJDBCDateColumn()) {
            sb.append("addCriterionForJDBCDate(\""); //$NON-NLS-1$
        } else if (introspectedColumn.isJDBCTimeColumn()) {
            sb.append("addCriterionForJDBCTime(\""); //$NON-NLS-1$
        } else if (stringHasValue(introspectedColumn
                .getTypeHandler())) {
            sb.append("add"); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
            sb.append("Criterion(\""); //$NON-NLS-1$
        } else {
            sb.append("addCriterion(\""); //$NON-NLS-1$
        }

        sb.append(Ibatis2FormattingUtilities
                .getAliasedActualColumnName(introspectedColumn));
        if (inMethod) {
            sb.append(" in"); //$NON-NLS-1$
        } else {
            sb.append(" not in"); //$NON-NLS-1$
        }
        sb.append("\", values, \""); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.append("\");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }

    private Method getNoValueMethod(IntrospectedColumn introspectedColumn,
            String nameFragment, String operator) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedColumn.getJavaProperty());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "and"); //$NON-NLS-1$
        sb.append(nameFragment);
        method.setName(sb.toString());
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        sb.setLength(0);
        sb.append("addCriterion(\""); //$NON-NLS-1$
        sb.append(Ibatis2FormattingUtilities
                .getAliasedActualColumnName(introspectedColumn));
        sb.append(' ');
        sb.append(operator);
        sb.append("\");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

        return method;
    }
}
