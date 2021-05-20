/*
 *    Copyright 2006-2021 the original author or authors.
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

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class ResultMapWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

    private final boolean isSimple;

    public ResultMapWithoutBLOBsElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$

        String returnType;
        if (isSimple) {
            returnType = introspectedTable.getBaseRecordType();
        } else {
            if (introspectedTable.getRules().generateBaseRecordClass()) {
                returnType = introspectedTable.getBaseRecordType();
            } else {
                returnType = introspectedTable.getPrimaryKeyType();
            }
        }

        answer.addAttribute(new Attribute("type", returnType)); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        if (introspectedTable.isConstructorBased()) {
            addResultMapConstructorElements(answer);
        } else {
            addResultMapElements(answer);
        }

        if (context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private void addResultMapElements(XmlElement answer) {
        buildResultMapItems(ResultElementType.ID, introspectedTable.getPrimaryKeyColumns()).forEach(answer::addElement);

        List<IntrospectedColumn> columns;
        if (isSimple) {
            columns = introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = introspectedTable.getBaseColumns();
        }

        buildResultMapItems(ResultElementType.RESULT, columns).forEach(answer::addElement);
    }

    private void addResultMapConstructorElements(XmlElement answer) {
        answer.addElement(buildConstructorElement(isSimple));
    }
}
