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

import org.mybatis.generator.api.dom.xml.XmlElement;

public class SelectByExampleWithoutBLOBsElementGenerator extends AbstractXmlMapperElementGenerator {

    protected SelectByExampleWithoutBLOBsElementGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<XmlElement> generateElement() {
        if (!introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            return Optional.empty();
        }

        return Optional.of(generateSelectByExample(false));
    }

    @Override
    public boolean callPlugins(XmlElement element) {
        return pluginAggregator.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public SelectByExampleWithoutBLOBsElementGenerator build() {
            return new SelectByExampleWithoutBLOBsElementGenerator(this);
        }
    }
}
