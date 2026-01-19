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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class DeleteByExampleElementGenerator extends AbstractXmlElementGenerator {

    protected DeleteByExampleElementGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", introspectedTable.getDeleteByExampleStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getExampleType())); //$NON-NLS-1$

        commentGenerator.addComment(answer);

        String s = "delete from " + introspectedTable.getAliasedFullyQualifiedRuntimeTableName(); //$NON-NLS-1$
        answer.addElement(new TextElement(s));
        answer.addElement(getExampleIncludeElement());

        if (pluginAggregator.sqlMapDeleteByExampleElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    public static class Builder extends AbstractXmlElementGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public DeleteByExampleElementGenerator build() {
            return new DeleteByExampleElementGenerator(this);
        }
    }
}
