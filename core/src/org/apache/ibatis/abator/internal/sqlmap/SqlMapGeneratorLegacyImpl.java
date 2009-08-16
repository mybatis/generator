/*
 *  Copyright 2005 The Apache Software Foundation
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

import java.util.Iterator;

import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.IntrospectedTable;
import org.apache.ibatis.abator.api.SqlMapGenerator;
import org.apache.ibatis.abator.api.dom.xml.Attribute;
import org.apache.ibatis.abator.api.dom.xml.TextElement;
import org.apache.ibatis.abator.api.dom.xml.XmlElement;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.util.StringUtility;

/**
 * This class overrides the iterate implementation to provide the legacy
 * "by example" methods.
 * 
 * This class can be removed when we remove the Legacy generator set.
 *
 * @author Jeff Butler
 */
public class SqlMapGeneratorLegacyImpl extends SqlMapGeneratorIterateImpl implements SqlMapGenerator {

    /**
     * Constructs an instance of SqlMapGeneratorDefaultImpl
     */
    public SqlMapGeneratorLegacyImpl() {
        super();
    }

    /**
     * This method should return an XmlElement for the delete by example
     * statement. This statement uses the "by example" SQL fragment
     * 
     * @param introspectedTable
     * @return the delete element
     */
    protected XmlElement getDeleteByExample(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", getDeleteByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterClass", "java.util.Map")); //$NON-NLS-1$ //$NON-NLS-2$

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
     * @return the select element
     */
    protected XmlElement getCountByExample(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", getCountByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterClass", "java.util.Map")); //$NON-NLS-1$ //$NON-NLS-2$
        answer.addAttribute(new Attribute("resultClass", "java.lang.Integer")); //$NON-NLS-1$ //$NON-NLS-2$

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
     * This method should return an XmlElement for the example where clause
     * SQL fragment (an sql fragment).
     * 
     * @param introspectedTable
     * @return a well formatted String containing the SQL element
     */
    protected XmlElement getByExampleWhereClauseFragment(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$
        
        answer.addAttribute(new Attribute("id", getExampleWhereClauseId())); //$NON-NLS-1$
        
        abatorContext.getCommentGenerator().addComment(answer);

        XmlElement dynamicElement = new XmlElement("dynamic"); //$NON-NLS-1$
        dynamicElement.addAttribute(new Attribute("prepend", "where")); //$NON-NLS-1$ //$NON-NLS-2$

        Iterator iter = introspectedTable.getNonBLOBColumns();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();

            Iterator clauseIterator = ExampleClause.getAllExampleClauses();
            while (clauseIterator.hasNext()) {
                ExampleClause ec = (ExampleClause) clauseIterator.next();

                if (ec.isCharacterOnly() && !cd.isJdbcCharacterColumn()) {
                    continue;
                }

                XmlElement isPropAvail = new XmlElement("isPropertyAvailable"); //$NON-NLS-1$
                isPropAvail.addAttribute(new Attribute("prepend", "and")); //$NON-NLS-1$ //$NON-NLS-2$
                isPropAvail.addAttribute(new Attribute("property", ec.getSelectorAndProperty(cd, false))); //$NON-NLS-1$
                isPropAvail.addElement(new TextElement(ec.getClause(cd)));
                dynamicElement.addElement(isPropAvail);

                isPropAvail = new XmlElement("isPropertyAvailable"); //$NON-NLS-1$
                isPropAvail.addAttribute(new Attribute("prepend", "or")); //$NON-NLS-1$ //$NON-NLS-2$
                isPropAvail.addAttribute(new Attribute("property", ec.getSelectorOrProperty(cd, false))); //$NON-NLS-1$
                isPropAvail.addElement(new TextElement(ec.getClause(cd)));
                dynamicElement.addElement(isPropAvail);
            }
        }

        answer.addElement(dynamicElement);

        return answer;
    }

    /**
     * This method should return an XmlElement for the select by example
     * statement that returns all fields in the table (except BLOB fields).
     * 
     * @param introspectedTable
     * @return the select element
     */
    protected XmlElement getSelectByExample(IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        
        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", getSelectByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                getResultMapName(table)));
        answer.addAttribute(new Attribute("parameterClass", "java.util.Map")); //$NON-NLS-1$ //$NON-NLS-2$

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
                getSqlMapNamespace(table)
                        + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isPropAvail = new XmlElement("isPropertyAvailable"); //$NON-NLS-1$
        isPropAvail.addAttribute(new Attribute("property", "ABATOR_ORDER_BY_CLAUSE")); //$NON-NLS-1$ //$NON-NLS-2$
        isPropAvail.addElement(new TextElement("order by $ABATOR_ORDER_BY_CLAUSE$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isPropAvail);

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

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        
        FullyQualifiedTable table = introspectedTable.getTable();
        answer.addAttribute(new Attribute("id", getSelectByExampleWithBLOBsStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                getResultMapName(table) + "WithBLOBs")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterClass", "java.util.Map")); //$NON-NLS-1$ //$NON-NLS-2$

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
                getSqlMapNamespace(table)
                        + "." + getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isPropAvail = new XmlElement("isPropertyAvailable"); //$NON-NLS-1$
        isPropAvail.addAttribute(new Attribute("property", "ABATOR_ORDER_BY_CLAUSE")); //$NON-NLS-1$ //$NON-NLS-2$
        isPropAvail.addElement(new TextElement("order by $ABATOR_ORDER_BY_CLAUSE$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isPropAvail);
        
        return answer;
    }

    protected XmlElement getUpdateByExampleSelective(IntrospectedTable introspectedTable) {
        // this method is not supported in the legacy generator set
        return null;
    }

    protected XmlElement getUpdateByExampleWithBLOBs(IntrospectedTable introspectedTable) {
        // this method is not supported in the legacy generator set
        return null;
    }

    protected XmlElement getUpdateByExampleWithoutBLOBs(IntrospectedTable introspectedTable) {
        // this method is not supported in the legacy generator set
        return null;
    }
}
