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
package org.mybatis.generator.api;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class Indenter {
    // Kotlin indents are always four spaces per level (from the published Kotlin code conventions)
    private static final String kotlinIndent = "    "; //$NON-NLS-1$

    private final String javaIndent;
    private final String xmlIndent;
    private final IndentType javaIndentType;
    private final Integer javaIndentAmount;
    private final IndentType xmlIndentType;
    private final Integer xmlIndentAmount;

    private Indenter(Builder builder) {
        javaIndentType = Objects.requireNonNullElse(builder.javaIndentType, IndentType.SPACES);
        javaIndentAmount = Objects.requireNonNullElseGet(builder.javaIndentAmount, () -> switch (javaIndentType) {
        case SPACES -> 4;
        case TABS -> 1;
        });

        xmlIndentType = Objects.requireNonNullElse(builder.xmlIndentType, IndentType.SPACES);
        xmlIndentAmount = Objects.requireNonNullElseGet(builder.xmlIndentAmount, () -> switch (xmlIndentType) {
        case SPACES -> 2;
        case TABS -> 1;
        });

        javaIndent = calculateIndent(javaIndentType, javaIndentAmount);
        xmlIndent = calculateIndent(xmlIndentType, xmlIndentAmount);
    }

    private String calculateIndent(IndentType indentType, Integer amount) {
        return switch (indentType) {
        case SPACES -> " ".repeat(amount); //$NON-NLS-1$
        case TABS -> "\t".repeat(amount); //$NON-NLS-1$
        };
    }

    public IndentType javaIndentType() {
        return javaIndentType;
    }

    public Integer javaIndentAmount() {
        return javaIndentAmount;
    }

    public String javaIndent(int indentLevel) {
        return javaIndent.repeat(indentLevel);
    }

    public static String kotlinIndent(int indentLevel) {
        return kotlinIndent.repeat(indentLevel);
    }

    public String xmlIndent(int indentLevel) {
        return xmlIndent.repeat(indentLevel);
    }

    public static Indenter defaultIndenter() {
        return new Indenter.Builder()
                .withJavaIndentType(IndentType.SPACES)
                .withJavaIndentAmount(4)
                .withXmlIndentType(IndentType.SPACES)
                .withXmlIndentAmount(2)
                .build();
    }

    public static class Builder {
        private @Nullable IndentType javaIndentType;
        private @Nullable Integer javaIndentAmount;
        private @Nullable IndentType xmlIndentType;
        private @Nullable Integer xmlIndentAmount;

        public Builder withJavaIndentType(@Nullable IndentType javaIndentType) {
            this.javaIndentType = javaIndentType;
            return this;
        }

        public Builder withJavaIndentAmount(@Nullable Integer javaIndentAmount) {
            this.javaIndentAmount = javaIndentAmount;
            return this;
        }

        public Builder withXmlIndentType(@Nullable IndentType xmlIndentType) {
            this.xmlIndentType = xmlIndentType;
            return this;
        }

        public Builder withXmlIndentAmount(@Nullable Integer xmlIndentAmount) {
            this.xmlIndentAmount = xmlIndentAmount;
            return this;
        }

        public Indenter build() {
            return new Indenter(this);
        }
    }
}
