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
package org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements;

import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.TextElement;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.internal.util.StringUtility;

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
        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectByExampleWithBLOBsStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "resultMap", introspectedTable.getResultMapWithBLOBsId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "parameterClass", introspectedTable.getExampleType())); //$NON-NLS-1$

        ibatorContext.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$

        if (StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
        }
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());
        answer.addElement(new TextElement(",")); //$NON-NLS-1$
        answer.addElement(getBlobColumnListElement());

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement isParameterPresenteElement =
            new XmlElement("isParameterPresent"); //$NON-NLS-1$
        answer.addElement(isParameterPresenteElement);
        
        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getIbatis2SqlMapNamespace() + "." + introspectedTable.getExampleWhereClauseId())); //$NON-NLS-1$
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isNotNullElement = new XmlElement("isNotNull"); //$NON-NLS-1$
        isNotNullElement
                .addAttribute(new Attribute("property", "orderByClause")); //$NON-NLS-1$ //$NON-NLS-2$
        isNotNullElement
                .addElement(new TextElement("order by $orderByClause$")); //$NON-NLS-1$
        isParameterPresenteElement.addElement(isNotNullElement);

        if (ibatorContext.getPlugins().sqlMapSelectByExampleWithBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
