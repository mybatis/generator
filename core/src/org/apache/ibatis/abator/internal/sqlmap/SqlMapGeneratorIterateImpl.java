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
package org.apache.ibatis.abator.internal.sqlmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.GeneratedXmlFile;
import org.apache.ibatis.abator.api.IntrospectedTable;
import org.apache.ibatis.abator.api.JavaModelGenerator;
import org.apache.ibatis.abator.api.ProgressCallback;
import org.apache.ibatis.abator.api.SqlMapGenerator;
import org.apache.ibatis.abator.api.dom.OutputUtilities;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.xml.Attribute;
import org.apache.ibatis.abator.api.dom.xml.Document;
import org.apache.ibatis.abator.api.dom.xml.TextElement;
import org.apache.ibatis.abator.api.dom.xml.XmlElement;
import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.config.GeneratedKey;
import org.apache.ibatis.abator.config.PropertyRegistry;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.rules.AbatorRules;
import org.apache.ibatis.abator.internal.util.StringUtility;
import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * @author Jeff Butler
 */
public class SqlMapGeneratorIterateImpl implements SqlMapGenerator {

    protected List warnings;
    protected AbatorContext abatorContext;

    /**
     * Contains any properties passed in from the SqlMap configuration element.
     */
    protected Properties properties;

    /**
     * This is the target package from the SqlMap configuration element
     */
    protected String targetPackage;

    /**
     * This is the target project from the SqlMap configuration element
     */
    protected String targetProject;

    /**
     * This is the java model generator associated with the current generation
     * context. Methods in this interface can be used to determine the
     * appropriate result and parameter class names.
     */
    protected JavaModelGenerator javaModelGenerator;

    /**
     * This is a map of maps. The map is keyed by a FullyQualifiedTable object.
     * The inner map holds generated strings keyed by the String name. This Map
     * is used to cache generated Strings.
     */
    private Map tableStringMaps;

    /**
     * Constructs an instance of SqlMapGeneratorDefaultImpl
     */
    public SqlMapGeneratorIterateImpl() {
        super();
        tableStringMaps = new HashMap();
        properties = new Properties();
    }

    private Map getTableStringMap(FullyQualifiedTable table) {
        Map map = (Map) tableStringMaps.get(table);
        if (map == null) {
            map = new HashMap();
            tableStringMaps.put(table, map);
        }

        return map;
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#setTargetPackage(java.lang.String)
     */
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#setJavaModelGenerator(org.apache.ibatis.abator.api.JavaModelGenerator)
     */
    public void setJavaModelGenerator(JavaModelGenerator javaModelGenerator) {
        this.javaModelGenerator = javaModelGenerator;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getGeneratedXMLFiles(org.apache.ibatis.abator.api.IntrospectedTable, org.apache.ibatis.abator.api.ProgressCallback)
     */
    public List getGeneratedXMLFiles(IntrospectedTable introspectedTable,
            ProgressCallback callback) {
        ArrayList list = new ArrayList();

        FullyQualifiedTable table = introspectedTable.getTable();
        callback.startSubTask(Messages.getString(
                "Progress.12", //$NON-NLS-1$
                table.toString()));
        list.add(getSqlMap(introspectedTable));

        return list;
    }

    /**
     * Creates the default implementation of the Sql Map
     * 
     * @param introspectedTable
     * @return A GeneratedXMLFile for the current table
     */
    protected GeneratedXmlFile getSqlMap(IntrospectedTable introspectedTable) {

        Document document = new Document(XmlConstants.SQL_MAP_PUBLIC_ID,
                XmlConstants.SQL_MAP_SYSTEM_ID);
        document.setRootElement(getSqlMapElement(introspectedTable));
        
        afterGenerationHook(introspectedTable, document);

        FullyQualifiedTable table = introspectedTable.getTable();
        GeneratedXmlFile answer = new GeneratedXmlFile(document,
                getSqlMapFileName(table), getSqlMapPackage(table),
                targetProject);

        return answer;
    }

    /**
     * Creates the sqlMap element (the root element, and all child elements).
     * 
     * @param introspectedTable
     * @return the sqlMap element including all child elements
     */
    protected XmlElement getSqlMapElement(IntrospectedTable introspectedTable) {

        FullyQualifiedTable table = introspectedTable.getTable();
        XmlElement answer = new XmlElement("sqlMap"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                getSqlMapNamespace(table)));

        abatorContext.getCommentGenerator().addRootComment(answer);
        
        AbatorRules rules = introspectedTable.getRules();
        XmlElement element;
        if (rules.generateBaseResultMap()) {
            element = getBaseResultMapElement(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateResultMapWithBLOBs()) {
            element = getResultMapWithBLOBsElement(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateSQLExampleWhereClause()) {
            element = getByExampleWhereClauseFragment(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateSelectByPrimaryKey()) {
            element = getSelectByPrimaryKey(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateSelectByExampleWithoutBLOBs()) {
            element = getSelectByExample(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateSelectByExampleWithBLOBs()) {
            element = getSelectByExampleWithBLOBs(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateDeleteByPrimaryKey()) {
            element = getDeleteByPrimaryKey(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateDeleteByExample()) {
            element = getDeleteByExample(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateInsert()) {
            element = getInsertElement(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateUpdateByPrimaryKeyWithBLOBs()) {
            element = getUpdateByPrimaryKeyWithBLOBs(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            element = getUpdateByPrimaryKeyWithoutBLOBs(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateUpdateByPrimaryKeySelective()) {
            element = getUpdateByPrimaryKeySelective(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }
        
        if (rules.generateCountByExample()) {
            element = getCountByExample(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }
        
        if (rules.generateUpdateByExampleSelective()) {
            element = getUpdateByExampleSelective(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }
        
        if (rules.generateUpdateByExampleWithBLOBs()) {
            element = getUpdateByExampleWithBLOBs(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        if (rules.generateUpdateByExampleWithoutBLOBs()) {
            element = getUpdateByExampleWithoutBLOBs(introspectedTable);
            if (element != null) {
                answer.addElement(element);
            }
        }

        return answer;
    }

    /**
     * Override this method to provide any extra Elements needed in the 
     * generated XML.
     * 
     * @param generatedDocument the generated document
     */
    protected void afterGenerationHook(IntrospectedTable introspectedTable, Document generatedDocument) {
        return;
    }

    /**
     * This method should return an XmlElement which is the result map (without
     * any BLOBs if they exist in the table).
     * 
     * @param introspectedTable
     * @return the resultMap element
     */
    protected XmlElement getBaseResultMapElement(IntrospectedTable introspectedTable) {
        boolean useColumnIndex =
            "true".equalsIgnoreCase(introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_USE_COLUMN_INDEXES)); //$NON-NLS-1$
        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                getResultMapName(table)));

        FullyQualifiedJavaType returnType;
        if (introspectedTable.getRules().generateBaseRecordClass()) {
            returnType = javaModelGenerator.getBaseRecordType(table);
        } else {
            returnType = javaModelGenerator.getPrimaryKeyType(table);
        }
        
        answer.addAttribute(new Attribute("class", //$NON-NLS-1$
                returnType.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        int i = 1;
        if (StringUtility.stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())
                || StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            i++;
        }
        
        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

            if (useColumnIndex) {
                resultElement.addAttribute(new Attribute(
                        "columnIndex", Integer.toString(i++))); //$NON-NLS-1$
            } else {
                resultElement.addAttribute(new Attribute(
                    "column", cd.getRenamedColumnNameForResultMap())); //$NON-NLS-1$
            }
            
            resultElement.addAttribute(new Attribute(
                    "property", cd.getJavaProperty())); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute("jdbcType", //$NON-NLS-1$
                    cd.getResolvedJavaType().getJdbcTypeName()));

            if (StringUtility.stringHasValue(cd.getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", cd.getTypeHandler())); //$NON-NLS-1$
            }

            answer.addElement(resultElement);
        }

        return answer;
    }

    /**
     * This method should return an XmlElement which is the result map (with any
     * BLOBs if they exist in the table). Typically this result map extends the
     * base result map.
     * 
     * @param introspectedTable
     * @return the resultMap element
     */
    protected XmlElement getResultMapWithBLOBsElement(IntrospectedTable introspectedTable) {
        boolean useColumnIndex =
            "true".equalsIgnoreCase(introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_USE_COLUMN_INDEXES)); //$NON-NLS-1$

        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        StringBuffer sb = new StringBuffer();
        sb.append(getResultMapName(table));
        sb.append("WithBLOBs"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", sb.toString())); //$NON-NLS-1$
        
        FullyQualifiedJavaType returnType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = javaModelGenerator.getRecordWithBLOBsType(table);
        } else {
            // table has BLOBs, but no BLOB class - BLOB fields must be
            // in the base class
            returnType = javaModelGenerator.getBaseRecordType(table);
        }
        
        answer.addAttribute(new Attribute("class", //$NON-NLS-1$
                returnType.getFullyQualifiedName()));

        sb.setLength(0);
        sb.append(getSqlMapNamespace(table));
        sb.append('.');
        sb.append(getResultMapName(table));
        answer.addAttribute(new Attribute("extends", sb.toString())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        int i = introspectedTable.getNonBLOBColumnCount() + 1;
        if (StringUtility.stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())
                || StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            i++;
        }
        Iterator iter = introspectedTable.getBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$
            
            if (useColumnIndex) {
                resultElement.addAttribute(new Attribute(
                        "columnIndex", Integer.toString(i++))); //$NON-NLS-1$
            } else {
                resultElement.addAttribute(new Attribute(
                    "column", cd.getRenamedColumnNameForResultMap())); //$NON-NLS-1$
            }
            resultElement.addAttribute(new Attribute(
                    "property", cd.getJavaProperty())); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute(
                    "jdbcType", cd.getResolvedJavaType().getJdbcTypeName())); //$NON-NLS-1$

            if (StringUtility.stringHasValue(cd.getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", cd.getTypeHandler())); //$NON-NLS-1$
            }

            answer.addElement(resultElement);
        }

        return answer;
    }

    /**
     * This method should return an XmlElement which the insert statement.
     * 
     * @param introspectedTable
     * @return the insert element
     */
    protected XmlElement getInsertElement(IntrospectedTable introspectedTable) {
        
        XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", getInsertStatementId())); //$NON-NLS-1$
        
        FullyQualifiedJavaType parameterType =
            introspectedTable.getRules().calculateAllFieldsClass(javaModelGenerator, table);
        
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey();

        if (gk != null && gk.isBeforeInsert()) {
            ColumnDefinition cd = introspectedTable.getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (cd != null) {
                // pre-generated key
                answer.addElement(getSelectKey(cd, gk));
            }
        }

        StringBuffer insertClause = new StringBuffer();
        StringBuffer valuesClause = new StringBuffer();

        insertClause.append("insert into "); //$NON-NLS-1$
        insertClause.append(table.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" ("); //$NON-NLS-1$

        valuesClause.append("values ("); //$NON-NLS-1$

        boolean comma = false;
        Iterator iter = introspectedTable.getAllColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            if (cd.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }

            if (comma) {
                insertClause.append(", "); //$NON-NLS-1$
                valuesClause.append(", "); //$NON-NLS-1$
            } else {
                comma = true; // turn on comma for next time
            }

            insertClause.append(cd.getEscapedColumnName());
            valuesClause.append(cd.getIbatisFormattedParameterClause());
        }
        insertClause.append(')');
        valuesClause.append(')');

        answer.addElement(new TextElement(insertClause.toString()));
        answer.addElement(new TextElement(valuesClause.toString()));

        if (gk != null && !gk.isBeforeInsert()) {
            ColumnDefinition cd = introspectedTable.getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (cd != null) {
                // pre-generated key
                answer.addElement(getSelectKey(cd, gk));
            }
        }

        return answer;
    }

    /**
     * This method should return an XmlElement for the update by primary key
     * statement that updates all fields in the table (including BLOB fields).
     * 
     * @param introspectedTable
     * @return the update element
     */
    protected XmlElement getUpdateByPrimaryKeyWithBLOBs(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByPrimaryKeyWithBLOBsStatementId())); //$NON-NLS-1$

        FullyQualifiedJavaType parameterType;
        
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = javaModelGenerator.getRecordWithBLOBsType(table);
        } else {
            parameterType = javaModelGenerator.getBaseRecordType(table);
        }
        
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();

        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator iter = introspectedTable.getNonPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }

    /**
     * This method should return an XmlElement for the update by primary key
     * statement that updates all fields in the table (excluding BLOB fields).
     * 
     * @param introspectedTable
     * @return the update element
     */
    protected XmlElement getUpdateByPrimaryKeyWithoutBLOBs(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                javaModelGenerator.getBaseRecordType(table).getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator iter = introspectedTable.getBaseColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }

    /**
     * This method should return an XmlElement for the delete by primary key
     * statement.
     * 
     * @param introspectedTable
     * @return the delete element
     */
    protected XmlElement getDeleteByPrimaryKey(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
        FullyQualifiedJavaType parameterClass;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterClass = javaModelGenerator.getPrimaryKeyType(table);
        } else {
            parameterClass = javaModelGenerator.getBaseRecordType(table);
        }
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                parameterClass.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("delete from "); //$NON-NLS-1$
        sb.append(table.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        Iterator iter = introspectedTable.getPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }

    /**
     * This method should return an XmlElement for the delete by example
     * statement. This statement uses the "by example" SQL fragment
     * 
     * @param introspectedTable
     * @return the delete by example element
     */
    protected XmlElement getDeleteByExample(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType fqjt = javaModelGenerator.getExampleType(table);

        answer
                .addAttribute(new Attribute(
                        "id", getDeleteByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "parameterClass", fqjt.getFullyQualifiedName())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("delete from "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        sb.setLength(0);
        sb.append(getSqlMapNamespace(table));
        sb.append('.');
        sb.append(getExampleWhereClauseId());
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                sb.toString()));

        answer.addElement(includeElement);

        return answer;
    }

    /**
     * This method should return an XmlElement for the count by example
     * statement. This statement uses the "by example" SQL fragment
     * 
     * @param introspectedTable
     * @return the count by example element
     */
    protected XmlElement getCountByExample(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType fqjt = javaModelGenerator.getExampleType(table);

        answer
                .addAttribute(new Attribute(
                        "id", getCountByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "parameterClass", fqjt.getFullyQualifiedName())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "resultClass", "java.lang.Integer")); //$NON-NLS-1$ //$NON-NLS-2$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("select count(*) from "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        sb.setLength(0);
        sb.append(getSqlMapNamespace(table));
        sb.append('.');
        sb.append(getExampleWhereClauseId());
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                sb.toString()));

        answer.addElement(includeElement);

        return answer;
    }
    
    /**
     * This method should return an XmlElement for the select by primary key
     * statement. The statement should include all fields in the table,
     * including BLOB fields.
     * 
     * @param introspectedTable
     * @return the select by primary key element
     */
    protected XmlElement getSelectByPrimaryKey(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getSelectByPrimaryKeyStatementId())); //$NON-NLS-1$
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    getResultMapName(table) + "WithBLOBs")); //$NON-NLS-1$
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    getResultMapName(table)));
        }
        
        FullyQualifiedJavaType parameterType;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterType = javaModelGenerator.getPrimaryKeyType(table);
        } else {
            // select by primary key, but no primary key class.  Fields
            // must be in the base record
            parameterType = javaModelGenerator.getBaseRecordType(table);
        }
        
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("select "); //$NON-NLS-1$

        boolean comma = false;
        if (StringUtility.stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID"); //$NON-NLS-1$
            comma = true;
        }

        Iterator iter = introspectedTable.getAllColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            if (comma) {
                sb.append(", "); //$NON-NLS-1$
            } else {
                comma = true;
            }

            sb.append(cd.getSelectListPhrase());
        }

        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(cd.getAliasedEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }

    /**
     * This method should return an XmlElement for the select key used to
     * automatically generate keys.
     * 
     * @param columnDefinition
     *            the column related to the select key statement
     * @param generatedKey
     *            the generated key for the current table
     * @return the selectKey element
     */
    protected XmlElement getSelectKey(ColumnDefinition columnDefinition,
            GeneratedKey generatedKey) {
        String identityColumnType = columnDefinition.getResolvedJavaType()
                .getFullyQualifiedJavaType().getFullyQualifiedName();

        XmlElement answer = new XmlElement("selectKey"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultClass", identityColumnType)); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "keyProperty", columnDefinition.getJavaProperty())); //$NON-NLS-1$
        if(StringUtility.stringHasValue(generatedKey.getType())) {
          answer.addAttribute(new Attribute("type", generatedKey.getType())); //$NON-NLS-1$  
        }
        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));

        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getSqlMapNamespace(org.apache.ibatis.abator.config.FullyQualifiedTable)
     */
    public String getSqlMapNamespace(FullyQualifiedTable table) {
        String key = "getSqlMapNamespace"; //$NON-NLS-1$
        String s;

        Map map = getTableStringMap(table);
        s = (String) map.get(key);
        if (s == null) {
            s = table.getSqlMapNamespace();
            map.put(key, s);
        }

        return s;
    }

    /**
     * Calculates the name of the result map. Typically this is the String
     * "abatorgenerated_XXXXResult" where XXXX is the name of the domain object
     * related to this table. The prefix "abatorgenerated_" is important because
     * it allows Abator to regenerate this element on subsequent runs.
     * 
     * @param table
     *            the current table
     * @return the name of the result map
     */
    protected String getResultMapName(FullyQualifiedTable table) {
        String key = "getResultMapName"; //$NON-NLS-1$
        String s;

        Map map = getTableStringMap(table);
        s = (String) map.get(key);
        if (s == null) {
            StringBuffer sb = new StringBuffer();

            sb.append("abatorgenerated_"); //$NON-NLS-1$
            sb.append(table.getDomainObjectName());
            sb.append("Result"); //$NON-NLS-1$

            s = sb.toString();
            map.put(key, s);
        }

        return s;
    }

    /**
     * Calculates a file name for the current table. Typically the name is
     * "XXXX_SqlMap.xml" where XXXX is the fully qualified table name (delimited
     * with underscores).
     * 
     * @param table
     *            the current table
     * @return tha name of the SqlMap file
     */
    protected String getSqlMapFileName(FullyQualifiedTable table) {
        String key = "getSqlMapFileName"; //$NON-NLS-1$
        String s;

        Map map = getTableStringMap(table);
        s = (String) map.get(key);
        if (s == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(table.getSqlMapNamespace());

            sb.append("_SqlMap.xml"); //$NON-NLS-1$

            s = sb.toString();
            map.put(key, s);
        }

        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getDeleteByPrimaryKeyStatementId()
     */
    public String getDeleteByPrimaryKeyStatementId() {
        return "abatorgenerated_deleteByPrimaryKey"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getDeleteByExampleStatementId()
     */
    public String getDeleteByExampleStatementId() {
        return "abatorgenerated_deleteByExample"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getCountByExampleStatementId()
     */
    public String getCountByExampleStatementId() {
        return "abatorgenerated_countByExample"; //$NON-NLS-1$
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getInsertStatementId()
     */
    public String getInsertStatementId() {
        return "abatorgenerated_insert"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getSelectByPrimaryKeyStatementId()
     */
    public String getSelectByPrimaryKeyStatementId() {
        return "abatorgenerated_selectByPrimaryKey"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getSelectByExampleStatementId()
     */
    public String getSelectByExampleStatementId() {
        return "abatorgenerated_selectByExample"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getSelectByExampleWithBLOBsStatementId()
     */
    public String getSelectByExampleWithBLOBsStatementId() {
        return "abatorgenerated_selectByExampleWithBLOBs"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getUpdateByPrimaryKeyWithBLOBsStatementId()
     */
    public String getUpdateByPrimaryKeyWithBLOBsStatementId() {
        return "abatorgenerated_updateByPrimaryKeyWithBLOBs"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#getUpdateByPrimaryKeyStatementId()
     */
    public String getUpdateByPrimaryKeyStatementId() {
        return "abatorgenerated_updateByPrimaryKey"; //$NON-NLS-1$
    }

    public String getUpdateByPrimaryKeySelectiveStatementId() {
        return "abatorgenerated_updateByPrimaryKeySelective"; //$NON-NLS-1$
    }
    
    public String getUpdateByExampleSelectiveStatementId() {
        return "abatorgenerated_updateByExampleSelective"; //$NON-NLS-1$
    }
    
    /**
     * Calculates the package for the current table.
     * 
     * @param table
     *            the current table
     * @return the package for the SqlMap for the current table
     */
    protected String getSqlMapPackage(FullyQualifiedTable table) {
        String key = "getSqlMapPackage"; //$NON-NLS-1$
        String s;

        Map map = getTableStringMap(table);
        s = (String) map.get(key);
        if (s == null) {
            StringBuffer sb = new StringBuffer(targetPackage);
            if ("true".equalsIgnoreCase(properties.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES))) { //$NON-NLS-1$
                sb.append(table.getSubPackage());
            }
            
            s = sb.toString();
            map.put(key, s);
        }

        return s;
    }

    /**
     * Calculates the name of the example where clause element.
     * 
     * @return the name of the example where clause element
     */
    protected String getExampleWhereClauseId() {
        return "abatorgenerated_Example_Where_Clause"; //$NON-NLS-1$
    }

    /**
     * This method should return an XmlElement for the example where clause SQL
     * fragment (an sql fragment).
     *
     * @param introspectedTable 
     * @return the SQL element
     */
    protected XmlElement getByExampleWhereClauseFragment(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", getExampleWhereClauseId())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        XmlElement outerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        outerIterateElement.addAttribute(new Attribute(
                "property", "oredCriteria")); //$NON-NLS-1$ //$NON-NLS-2$
        outerIterateElement.addAttribute(new Attribute("conjunction", "or")); //$NON-NLS-1$ //$NON-NLS-2$
        outerIterateElement.addAttribute(new Attribute("prepend", "where")); //$NON-NLS-1$ //$NON-NLS-2$
        outerIterateElement.addAttribute(new Attribute("removeFirstPrepend", "iterate")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(outerIterateElement);
        
        XmlElement isEqualElement = new XmlElement("isEqual"); //$NON-NLS-1$
        isEqualElement.addAttribute(new Attribute("property", "oredCriteria[].valid")); //$NON-NLS-1$ //$NON-NLS-2$
        isEqualElement.addAttribute(new Attribute("compareValue", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        outerIterateElement.addElement(isEqualElement);

        isEqualElement.addElement(new TextElement("(")); //$NON-NLS-1$

        XmlElement innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute(
                "property", "oredCriteria[].criteriaWithoutValue")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addElement(new TextElement(
                "$oredCriteria[].criteriaWithoutValue[]$")); //$NON-NLS-1$
        isEqualElement.addElement(innerIterateElement);

        innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute(
                "property", "oredCriteria[].criteriaWithSingleValue")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement
                .addElement(new TextElement(
                        "$oredCriteria[].criteriaWithSingleValue[].condition$ #oredCriteria[].criteriaWithSingleValue[].value#")); //$NON-NLS-1$
        isEqualElement.addElement(innerIterateElement);

        innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute(
                "property", "oredCriteria[].criteriaWithListValue")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addElement(new TextElement(
                "$oredCriteria[].criteriaWithListValue[].condition$")); //$NON-NLS-1$
        XmlElement innerInnerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        innerInnerIterateElement.addAttribute(new Attribute("property", //$NON-NLS-1$
                "oredCriteria[].criteriaWithListValue[].values")); //$NON-NLS-1$
        innerInnerIterateElement.addAttribute(new Attribute("open", "(")); //$NON-NLS-1$ //$NON-NLS-2$
        innerInnerIterateElement.addAttribute(new Attribute("close", ")")); //$NON-NLS-1$ //$NON-NLS-2$
        innerInnerIterateElement
                .addAttribute(new Attribute("conjunction", ",")); //$NON-NLS-1$ //$NON-NLS-2$
        innerInnerIterateElement.addElement(new TextElement(
                "#oredCriteria[].criteriaWithListValue[].values[]#")); //$NON-NLS-1$
        innerIterateElement.addElement(innerInnerIterateElement);
        isEqualElement.addElement(innerIterateElement);

        innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
        innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute(
                "property", "oredCriteria[].criteriaWithBetweenValue")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
        innerIterateElement.addElement(new TextElement(
                "$oredCriteria[].criteriaWithBetweenValue[].condition$")); //$NON-NLS-1$
        innerIterateElement
                .addElement(new TextElement(
                        "#oredCriteria[].criteriaWithBetweenValue[].values[0]# and")); //$NON-NLS-1$
        innerIterateElement.addElement(new TextElement(
                "#oredCriteria[].criteriaWithBetweenValue[].values[1]#")); //$NON-NLS-1$
        isEqualElement.addElement(innerIterateElement);
        
        // if any of the columns have a user defined type handler, then we need
        // to add additional inner iterate elements that specify the type handler
        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            
            if (StringUtility.stringHasValue(cd.getTypeHandler())) {
                // name the property based on the column name, then
                // add the type handler to the parameter declaration
                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                
                sb1.append("oredCriteria[]."); //$NON-NLS-1$
                sb1.append(cd.getJavaProperty());
                sb1.append("CriteriaWithSingleValue"); //$NON-NLS-1$
                
                innerIterateElement.addAttribute(new Attribute(
                        "property", sb1.toString())); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                
                sb2.append(sb1);
                
                sb1.insert(0, '$');
                sb1.append("[].condition$ ");//$NON-NLS-1$

                sb2.insert(0, '#');
                sb2.append("[].value,handler=");//$NON-NLS-1$
                sb2.append(cd.getTypeHandler());
                sb2.append('#');
                
                sb1.append(sb2);
                
                innerIterateElement.addElement(new TextElement(sb1.toString()));
                isEqualElement.addElement(innerIterateElement);
                
                sb1.setLength(0);
                sb2.setLength(0);
                sb1.append("oredCriteria[]."); //$NON-NLS-1$
                sb1.append(cd.getJavaProperty());
                sb1.append("CriteriaWithListValue"); //$NON-NLS-1$

                innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                innerIterateElement.addAttribute(new Attribute(
                        "property", sb1.toString())); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                
                sb2.append('$');
                sb2.append(sb1);
                sb2.append("[].condition$"); //$NON-NLS-1$
                
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                
                sb2.setLength(0);
                sb2.append(sb1);
                sb2.append("[].values"); //$NON-NLS-1$
                
                innerInnerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
                innerInnerIterateElement.addAttribute(new Attribute("property", //$NON-NLS-1$
                        sb2.toString()));
                innerInnerIterateElement.addAttribute(new Attribute("open", "(")); //$NON-NLS-1$ //$NON-NLS-2$
                innerInnerIterateElement.addAttribute(new Attribute("close", ")")); //$NON-NLS-1$ //$NON-NLS-2$
                innerInnerIterateElement
                        .addAttribute(new Attribute("conjunction", ",")); //$NON-NLS-1$ //$NON-NLS-2$
                
                sb2.setLength(0);
                sb2.append('#');
                sb2.append(sb1);
                sb2.append("[].values[],handler="); //$NON-NLS-1$
                sb2.append(cd.getTypeHandler());
                sb2.append('#');
                
                innerInnerIterateElement.addElement(new TextElement(sb2.toString()));
                innerIterateElement.addElement(innerInnerIterateElement);
                isEqualElement.addElement(innerIterateElement);

                sb1.setLength(0);
                sb2.setLength(0);
                sb1.append("oredCriteria[]."); //$NON-NLS-1$
                sb1.append(cd.getJavaProperty());
                sb1.append("CriteriaWithBetweenValue"); //$NON-NLS-1$

                innerIterateElement = new XmlElement("iterate"); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                innerIterateElement.addAttribute(new Attribute(
                        "property", sb1.toString())); //$NON-NLS-1$
                innerIterateElement.addAttribute(new Attribute("conjunction", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                
                sb2.append('$');
                sb2.append(sb1);
                sb2.append("[].condition$"); //$NON-NLS-1$
                
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                
                sb2.setLength(0);
                sb2.append(sb1);
                
                sb1.insert(0, '#');
                sb1.append("[].values[0],handler="); //$NON-NLS-1$
                sb1.append(cd.getTypeHandler());
                sb1.append("# and"); //$NON-NLS-1$
                
                sb2.insert(0, '#');
                sb2.append("[].values[1],handler="); //$NON-NLS-1$
                sb2.append(cd.getTypeHandler());
                sb2.append('#');
                
                innerIterateElement.addElement(new TextElement(sb1.toString()));
                innerIterateElement.addElement(new TextElement(sb2.toString()));
                isEqualElement.addElement(innerIterateElement);
            }
        }

        isEqualElement.addElement(new TextElement(")")); //$NON-NLS-1$

        return answer;
    }

    /**
     * This method should an XmlElement for the select by example statement that
     * returns all fields in the table (except BLOB fields).
     * 
     * @param introspectedTable
     * @return the select element
     */
    protected XmlElement getSelectByExample(IntrospectedTable introspectedTable) {
        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType fqjt = javaModelGenerator.getExampleType(table);

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", getSelectByExampleStatementId())); //$NON-NLS-1$
        answer
                .addAttribute(new Attribute(
                        "resultMap", getResultMapName(table))); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "parameterClass", fqjt.getFullyQualifiedName())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("select "); //$NON-NLS-1$

        boolean comma = false;
        if (StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID"); //$NON-NLS-1$
            comma = true;
        }

        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            if (comma) {
                sb.append(", "); //$NON-NLS-1$
            } else {
                comma = true;
            }

            sb.append(cd.getSelectListPhrase());
        }
        answer.addElement((new TextElement(sb.toString())));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement((new TextElement(sb.toString())));

        XmlElement isParameterPresenteElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresenteElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                getSqlMapNamespace(table) + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
        isNotNullElement
                .addAttribute(new Attribute("property", "orderByClause")); //$NON-NLS-1$ //$NON-NLS-2$
        isNotNullElement
                .addElement(new TextElement("order by $orderByClause$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isNotNullElement);

        return answer;
    }

    /**
     * This method should return an XmlElement for the select by example
     * statement that returns all fields in the table (including BLOB fields).
     * 
     * @param introspectedTable
     * @return the select element
     */
    protected XmlElement getSelectByExampleWithBLOBs(IntrospectedTable introspectedTable) {

        FullyQualifiedTable table = introspectedTable.getTable();
        FullyQualifiedJavaType fqjt = javaModelGenerator.getExampleType(table);

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "id", getSelectByExampleWithBLOBsStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "resultMap", getResultMapName(table) + "WithBLOBs")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addAttribute(new Attribute(
                "parameterClass", fqjt.getFullyQualifiedName())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("select "); //$NON-NLS-1$

        boolean comma = false;

        if (StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID"); //$NON-NLS-1$
            comma = true;
        }

        Iterator iter = introspectedTable.getAllColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            if (comma) {
                sb.append(", "); //$NON-NLS-1$
            } else {
                comma = true;
            }

            sb.append(cd.getSelectListPhrase());
        }
        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement isParameterPresenteElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresenteElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                getSqlMapNamespace(table) + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
        isNotNullElement
                .addAttribute(new Attribute("property", "orderByClause")); //$NON-NLS-1$ //$NON-NLS-2$
        isNotNullElement
                .addElement(new TextElement("order by $orderByClause$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isNotNullElement);

        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#setTargetProject(java.lang.String)
     */
    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.SqlMapGenerator#setWarnings(java.util.List)
     */
    public void setWarnings(List warnings) {
        this.warnings = warnings;
    }

    /**
     * This method should return an XmlElement for the update by primary key
     * statement that updates all fields in the table - but only if the field is
     * not null in the parameter object.
     * 
     * @param introspectedTable
     * @return the update element
     */
    protected XmlElement getUpdateByPrimaryKeySelective(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$

        FullyQualifiedJavaType parameterType;
        
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = javaModelGenerator.getRecordWithBLOBsType(table);
        } else {
            parameterType = javaModelGenerator.getBaseRecordType(table);
        }
        
        answer.addAttribute(new Attribute("parameterClass", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();

        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("dynamic"); //$NON-NLS-1$
        dynamicElement.addAttribute(new Attribute("prepend", "set")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(dynamicElement);

        Iterator iter = introspectedTable.getNonPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            
            XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
            isNotNullElement.addAttribute(new Attribute("property", cd.getJavaProperty())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(cd.getEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause());
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }

    public void setAbatorContext(AbatorContext abatorContext) {
        this.abatorContext = abatorContext;
    }

    /**
     * This method should return an XmlElement for the update by example
     * statement that updates all fields in the table - but only if the field is
     * not null in the parameter object.
     * 
     * @param introspectedTable
     * @return the update element
     */
    protected XmlElement getUpdateByExampleSelective(IntrospectedTable introspectedTable) {
        
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByExampleSelectiveStatementId())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();

        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("dynamic"); //$NON-NLS-1$
        dynamicElement.addAttribute(new Attribute("prepend", "set")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addElement(dynamicElement);

        Iterator iter = introspectedTable.getAllColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            
            XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("prepend", ",")); //$NON-NLS-1$ //$NON-NLS-2$
            isNotNullElement.addAttribute(new Attribute("property", cd.getJavaProperty("record."))); //$NON-NLS-1$ //$NON-NLS-2$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(cd.getAliasedEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause("record.")); //$NON-NLS-1$
            
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        XmlElement isParameterPresentElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresentElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                getSqlMapNamespace(table) + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresentElement.addElement(includeElement);

        return answer;
    }

    protected XmlElement getUpdateByExampleWithBLOBs(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByExampleWithBLOBsStatementId())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator iter = introspectedTable.getAllColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.append(cd.getAliasedEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause("record.")); //$NON-NLS-1$

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        XmlElement isParameterPresentElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresentElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                getSqlMapNamespace(table) + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresentElement.addElement(includeElement);

        return answer;
    }

    protected XmlElement getUpdateByExampleWithoutBLOBs(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        FullyQualifiedTable table = introspectedTable.getTable();

        answer.addAttribute(new Attribute(
                "id", getUpdateByExampleStatementId())); //$NON-NLS-1$

        abatorContext.getCommentGenerator().addComment(answer);

        StringBuffer sb = new StringBuffer();
        sb.append("update "); //$NON-NLS-1$
        sb.append(table.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            sb.append(cd.getAliasedEscapedColumnName());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(cd.getIbatisFormattedParameterClause("record.")); //$NON-NLS-1$

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        XmlElement isParameterPresentElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresentElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                getSqlMapNamespace(table) + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresentElement.addElement(includeElement);

        return answer;
    }

    public String getUpdateByExampleStatementId() {
        return "abatorgenerated_updateByExample"; //$NON-NLS-1$
    }

    public String getUpdateByExampleWithBLOBsStatementId() {
        return "abatorgenerated_updateByExampleWithBLOBs"; //$NON-NLS-1$
    }
}
