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
package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ResultMapWithBLOBsElementGenerator extends
        AbstractXmlElementGenerator {

    public ResultMapWithBLOBsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        boolean useColumnIndex = isTrue(introspectedTable
                        .getTableConfigurationProperty(PropertyRegistry.TABLE_USE_COLUMN_INDEXES));

        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                introspectedTable.getResultMapWithBLOBsId()));

        String returnType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = introspectedTable.getRecordWithBLOBsType();
        } else {
            // table has BLOBs, but no BLOB class - BLOB fields must be
            // in the base class
            returnType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("class", //$NON-NLS-1$
                returnType));

        StringBuilder sb = new StringBuilder();
        sb.append(introspectedTable.getIbatis2SqlMapNamespace());
        sb.append('.');
        sb.append(introspectedTable.getBaseResultMapId());
        answer.addAttribute(new Attribute("extends", sb.toString())); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        int i = introspectedTable.getNonBLOBColumnCount() + 1;
        if (stringHasValue(introspectedTable
                .getSelectByPrimaryKeyQueryId())
                || stringHasValue(introspectedTable
                        .getSelectByExampleQueryId())) {
            i++;
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getBLOBColumns()) {
            XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

            if (useColumnIndex) {
                resultElement.addAttribute(new Attribute(
                        "columnIndex", Integer.toString(i++))); //$NON-NLS-1$
            } else {
                resultElement
                        .addAttribute(new Attribute(
                                "column", Ibatis2FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn))); //$NON-NLS-1$
            }
            resultElement.addAttribute(new Attribute(
                    "property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute(
                    "jdbcType", introspectedColumn.getJdbcTypeName())); //$NON-NLS-1$

            if (stringHasValue(introspectedColumn
                    .getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
            }

            answer.addElement(resultElement);
        }

        if (context.getPlugins()
                .sqlMapResultMapWithBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
