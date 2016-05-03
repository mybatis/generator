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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

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

        answer.addAttribute(new Attribute("type", //$NON-NLS-1$
                returnType));

        if (!introspectedTable.isConstructorBased()) {
            answer.addAttribute(new Attribute("extends", //$NON-NLS-1$
                introspectedTable.getBaseResultMapId()));
        }

        context.getCommentGenerator().addComment(answer);

        if (introspectedTable.isConstructorBased()) {
            addResultMapConstructorElements(answer);
        } else {
            addResultMapElements(answer);
        }

        if (context.getPlugins()
                .sqlMapResultMapWithBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private void addResultMapElements(XmlElement answer) {
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getBLOBColumns()) {
            XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

            resultElement
                    .addAttribute(new Attribute(
                            "column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn))); //$NON-NLS-1$
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
    }

    private void addResultMapConstructorElements(XmlElement answer) {
        XmlElement constructor = new XmlElement("constructor"); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("idArg"); //$NON-NLS-1$

            resultElement
                    .addAttribute(new Attribute(
                            "column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn))); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute(
                    "jdbcType", introspectedColumn.getJdbcTypeName())); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
                    introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

            if (stringHasValue(introspectedColumn
                    .getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
            }

            constructor.addElement(resultElement);
        }
        
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("arg"); //$NON-NLS-1$

            resultElement
                    .addAttribute(new Attribute(
                            "column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn))); //$NON-NLS-1$
            resultElement.addAttribute(new Attribute(
                    "jdbcType", introspectedColumn.getJdbcTypeName())); //$NON-NLS-1$

            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                // need to use the MyBatis type alias for a primitive byte
                StringBuilder sb = new StringBuilder();
                sb.append('_');
                sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
                resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
                        sb.toString()));
                
            } else if ("byte[]".equals(introspectedColumn.getFullyQualifiedJavaType() //$NON-NLS-1$
                    .getFullyQualifiedName())) {
                // need to use the MyBatis type alias for a primitive byte arry
                resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
                        "_byte[]")); //$NON-NLS-1$
            } else {
                resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
                        introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            }
            
            if (stringHasValue(introspectedColumn
                    .getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
            }

            constructor.addElement(resultElement);
        }

        answer.addElement(constructor);
    }
}
