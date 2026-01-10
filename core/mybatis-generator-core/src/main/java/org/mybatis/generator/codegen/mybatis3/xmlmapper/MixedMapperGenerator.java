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
package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.dom.xml.XmlElement;

public class MixedMapperGenerator extends XMLMapperGenerator {

    protected MixedMapperGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        // turn off this element in the mixed mapper
    }

    @Override
    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        // turn off this element in the mixed mapper
    }

    @Override
    protected void addInsertElement(XmlElement parentElement) {
        // turn off this element in the mixed mapper
    }

    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
        // turn off this element in the mixed mapper
    }

    @Override
    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
        // turn off this element in the mixed mapper
    }

    public static class Builder extends XMLMapperGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MixedMapperGenerator build() {
            return new MixedMapperGenerator(this);
        }
    }
}
