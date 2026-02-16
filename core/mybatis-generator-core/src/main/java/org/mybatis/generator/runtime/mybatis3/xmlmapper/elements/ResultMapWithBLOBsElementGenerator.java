/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.runtime.mybatis3.xmlmapper.elements;

import java.util.Optional;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class ResultMapWithBLOBsElementGenerator extends AbstractXmlMapperElementGenerator {

    protected ResultMapWithBLOBsElementGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<XmlElement> generateElement() {
        if (!introspectedTable.getRules().generateResultMapWithBLOBs()) {
            return Optional.empty();
        }

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

        answer.addAttribute(new Attribute("type", returnType)); //$NON-NLS-1$

        commentGenerator.addComment(answer);

        if (introspectedTable.isConstructorBased() || introspectedTable.isRecordBased()) {
            addResultMapConstructorElements(answer);
        } else {
            answer.addAttribute(new Attribute("extends", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
            addResultMapElements(answer);
        }

        return Optional.of(answer);
    }

    private void addResultMapElements(XmlElement answer) {
        buildResultMapItems(ResultElementType.RESULT, introspectedTable.getBLOBColumns()).forEach(answer::addElement);
    }

    private void addResultMapConstructorElements(XmlElement answer) {
        answer.addElement(buildConstructorElement(true));
    }

    @Override
    public boolean callPlugins(XmlElement element) {
        return pluginAggregator.sqlMapResultMapWithBLOBsElementGenerated(element, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public ResultMapWithBLOBsElementGenerator build() {
            return new ResultMapWithBLOBsElementGenerator(this);
        }
    }
}
