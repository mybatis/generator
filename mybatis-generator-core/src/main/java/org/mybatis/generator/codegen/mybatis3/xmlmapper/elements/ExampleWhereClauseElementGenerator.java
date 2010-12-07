/*
 *  Copyright 2009 The Apache Software Foundation
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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ExampleWhereClauseElementGenerator extends
        AbstractXmlElementGenerator {

    private boolean isForUpdateByExample;

    public ExampleWhereClauseElementGenerator(boolean isForUpdateByExample) {
        super();
        this.isForUpdateByExample = isForUpdateByExample;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        if (isForUpdateByExample) {
            answer
                    .addAttribute(new Attribute(
                            "id", introspectedTable.getMyBatis3UpdateByExampleWhereClauseId())); //$NON-NLS-1$
        } else {
            answer.addAttribute(new Attribute(
                    "id", introspectedTable.getExampleWhereClauseId())); //$NON-NLS-1$
        }

        context.getCommentGenerator().addComment(answer);

        XmlElement whereElement = new XmlElement("where"); //$NON-NLS-1$
        answer.addElement(whereElement);

        XmlElement outerForEachElement = new XmlElement("foreach"); //$NON-NLS-1$
        if (isForUpdateByExample) {
            outerForEachElement.addAttribute(new Attribute(
                    "collection", "example.oredCriteria")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            outerForEachElement.addAttribute(new Attribute(
                    "collection", "oredCriteria")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        outerForEachElement.addAttribute(new Attribute("item", "criteria")); //$NON-NLS-1$ //$NON-NLS-2$
        outerForEachElement.addAttribute(new Attribute("separator", "or")); //$NON-NLS-1$ //$NON-NLS-2$
        whereElement.addElement(outerForEachElement);

        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement.addAttribute(new Attribute("test", "criteria.valid")); //$NON-NLS-1$ //$NON-NLS-2$
        outerForEachElement.addElement(ifElement);

        XmlElement trimElement = new XmlElement("trim"); //$NON-NLS-1$
        trimElement.addAttribute(new Attribute("prefix", "(")); //$NON-NLS-1$ //$NON-NLS-2$
        trimElement.addAttribute(new Attribute("suffix", ")")); //$NON-NLS-1$ //$NON-NLS-2$
        trimElement.addAttribute(new Attribute("prefixOverrides", "and")); //$NON-NLS-1$ //$NON-NLS-2$

        ifElement.addElement(trimElement);

        trimElement.addElement(getMiddleForEachElement(null));

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonBLOBColumns()) {
            if (stringHasValue(introspectedColumn
                    .getTypeHandler())) {
                trimElement
                        .addElement(getMiddleForEachElement(introspectedColumn));
            }
        }

        if (context.getPlugins()
                .sqlMapExampleWhereClauseElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private XmlElement getMiddleForEachElement(
            IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        String criteriaAttribute;
        boolean typeHandled;
        String typeHandlerString;
        if (introspectedColumn == null) {
            criteriaAttribute = "criteria.criteria"; //$NON-NLS-1$
            typeHandled = false;
            typeHandlerString = null;
        } else {
            sb.setLength(0);
            sb.append("criteria."); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("Criteria"); //$NON-NLS-1$
            criteriaAttribute = sb.toString();

            typeHandled = true;

            sb.setLength(0);
            sb.append(",typeHandler="); //$NON-NLS-1$
            sb.append(introspectedColumn.getTypeHandler());
            typeHandlerString = sb.toString();
        }

        XmlElement middleForEachElement = new XmlElement("foreach"); //$NON-NLS-1$
        middleForEachElement.addAttribute(new Attribute(
                "collection", criteriaAttribute)); //$NON-NLS-1$
        middleForEachElement.addAttribute(new Attribute("item", "criterion")); //$NON-NLS-1$ //$NON-NLS-2$

        XmlElement chooseElement = new XmlElement("choose"); //$NON-NLS-1$
        middleForEachElement.addElement(chooseElement);

        XmlElement when = new XmlElement("when"); //$NON-NLS-1$
        when.addAttribute(new Attribute("test", "criterion.noValue")); //$NON-NLS-1$ //$NON-NLS-2$
        when.addElement(new TextElement("and ${criterion.condition}")); //$NON-NLS-1$
        chooseElement.addElement(when);

        when = new XmlElement("when"); //$NON-NLS-1$
        when.addAttribute(new Attribute("test", "criterion.singleValue")); //$NON-NLS-1$ //$NON-NLS-2$
        sb.setLength(0);
        sb.append("and ${criterion.condition} #{criterion.value"); //$NON-NLS-1$
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        when.addElement(new TextElement(sb.toString()));
        chooseElement.addElement(when);

        when = new XmlElement("when"); //$NON-NLS-1$
        when.addAttribute(new Attribute("test", "criterion.betweenValue")); //$NON-NLS-1$ //$NON-NLS-2$
        sb.setLength(0);
        sb.append("and ${criterion.condition} #{criterion.value"); //$NON-NLS-1$
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append("} and #{criterion.secondValue"); //$NON-NLS-1$
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        when.addElement(new TextElement(sb.toString()));
        chooseElement.addElement(when);

        when = new XmlElement("when"); //$NON-NLS-1$
        when.addAttribute(new Attribute("test", "criterion.listValue")); //$NON-NLS-1$ //$NON-NLS-2$
        when.addElement(new TextElement("and ${criterion.condition}")); //$NON-NLS-1$
        XmlElement innerForEach = new XmlElement("foreach"); //$NON-NLS-1$
        innerForEach
                .addAttribute(new Attribute("collection", "criterion.value")); //$NON-NLS-1$ //$NON-NLS-2$
        innerForEach.addAttribute(new Attribute("item", "listItem")); //$NON-NLS-1$ //$NON-NLS-2$
        innerForEach.addAttribute(new Attribute("open", "(")); //$NON-NLS-1$ //$NON-NLS-2$
        innerForEach.addAttribute(new Attribute("close", ")")); //$NON-NLS-1$ //$NON-NLS-2$
        innerForEach.addAttribute(new Attribute("separator", ",")); //$NON-NLS-1$ //$NON-NLS-2$
        sb.setLength(0);
        sb.append("#{listItem"); //$NON-NLS-1$
        if (typeHandled) {
            sb.append(typeHandlerString);
        }
        sb.append('}');
        innerForEach.addElement(new TextElement(sb.toString()));
        when.addElement(innerForEach);
        chooseElement.addElement(when);

        return middleForEachElement;
    }
}
