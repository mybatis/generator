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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.XmlElement;

public class UpdateByPrimaryKeyWithBLOBsElementGenerator extends AbstractXmlElementGenerator {

    protected UpdateByPrimaryKeyWithBLOBsElementGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public void addElements(XmlElement parentElement) {
        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        XmlElement answer = buildUpdateByPrimaryKeyElement(
                introspectedTable.getUpdateByPrimaryKeyWithBLOBsStatementId(),
                parameterType,
                introspectedTable.getNonPrimaryKeyColumns());

        if (pluginAggregator.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    public static class Builder extends AbstractXmlElementGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public UpdateByPrimaryKeyWithBLOBsElementGenerator build() {
            return new UpdateByPrimaryKeyWithBLOBsElementGenerator(this);
        }
    }
}
