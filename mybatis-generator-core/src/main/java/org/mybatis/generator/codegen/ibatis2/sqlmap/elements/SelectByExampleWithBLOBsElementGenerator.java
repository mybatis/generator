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
package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class SelectByExampleWithBLOBsElementGenerator extends
        AbstractXmlElementGenerator {

    public SelectByExampleWithBLOBsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer
                .addAttribute(new Attribute(
                        "id", introspectedTable.getSelectByExampleWithBLOBsStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "resultMap", introspectedTable.getResultMapWithBLOBsId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "parameterClass", introspectedTable.getExampleType())); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select")); //$NON-NLS-1$
        XmlElement isParameterPresent = new XmlElement("isParameterPresent"); //$NON-NLS-1$
        XmlElement isEqualElement = new XmlElement("isEqual"); //$NON-NLS-1$
        isEqualElement.addAttribute(new Attribute("property", "distinct")); //$NON-NLS-1$ //$NON-NLS-2$
        isEqualElement.addAttribute(new Attribute("compareValue", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        isEqualElement.addElement(new TextElement("distinct")); //$NON-NLS-1$
        isParameterPresent.addElement(isEqualElement);
        answer.addElement(isParameterPresent);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable
                .getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
            answer.addElement(new TextElement(sb.toString()));
        }

        answer.addElement(getBaseColumnListElement());
        answer.addElement(new TextElement(",")); //$NON-NLS-1$
        answer.addElement(getBlobColumnListElement());

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement isParameterPresenteElement = new XmlElement(
                "isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresenteElement);

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
        isNotNullElement
                .addAttribute(new Attribute("property", "orderByClause")); //$NON-NLS-1$ //$NON-NLS-2$
        isNotNullElement
                .addElement(new TextElement("order by $orderByClause$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isNotNullElement);

        if (context.getPlugins()
                .sqlMapSelectByExampleWithBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
