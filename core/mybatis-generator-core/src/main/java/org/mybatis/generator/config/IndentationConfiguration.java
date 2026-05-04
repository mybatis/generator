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
package org.mybatis.generator.config;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IndentType;
import org.mybatis.generator.api.Indenter;

public class IndentationConfiguration {
    private final @Nullable IndentType javaIndentType;
    private final @Nullable Integer javaIndentAmount;
    private final @Nullable IndentType xmlIndentType;
    private final @Nullable Integer xmlIndentAmount;

    private IndentationConfiguration(Builder builder) {
        javaIndentType = builder.javaIndentType;
        javaIndentAmount = builder.javaIndentAmount;
        xmlIndentType = builder.xmlIndentType;
        xmlIndentAmount = builder.xmlIndentAmount;
    }

    public Indenter getIndenter() {
        return new Indenter.Builder()
                .withJavaIndentType(javaIndentType)
                .withJavaIndentAmount(javaIndentAmount)
                .withXmlIndentType(xmlIndentType)
                .withXmlIndentAmount(xmlIndentAmount)
                .build();
    }

    public static class Builder {
        private @Nullable IndentType javaIndentType;
        private @Nullable Integer javaIndentAmount;
        private @Nullable IndentType xmlIndentType;
        private @Nullable Integer xmlIndentAmount;

        public Builder withJavaIndentType(IndentType javaIndentType) {
            this.javaIndentType = javaIndentType;
            return this;
        }

        public Builder withJavaIndentAmount(Integer javaIndentAmount) {
            this.javaIndentAmount = javaIndentAmount;
            return this;
        }

        public Builder withXmlIndentType(IndentType xmlIndentType) {
            this.xmlIndentType = xmlIndentType;
            return this;
        }

        public Builder withXmlIndentAmount(Integer xmlIndentAmount) {
            this.xmlIndentAmount = xmlIndentAmount;
            return this;
        }

        public IndentationConfiguration build() {
            return new IndentationConfiguration(this);
        }
    }
}
