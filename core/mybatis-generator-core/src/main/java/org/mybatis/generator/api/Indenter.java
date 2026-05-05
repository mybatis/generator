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
    private final String javaIndent;
    private final String xmlIndent;

    private Indenter(Builder builder) {
        javaIndent = calculateIndent(builder.javaIndentType, builder.javaIndentAmount, 4);
        xmlIndent = calculateIndent(builder.xmlIndentType, builder.xmlIndentAmount, 2);
    }

    private String calculateIndent(@Nullable IndentType indentType, @Nullable Integer amount, int defaultSpaces) {
        return switch (Objects.requireNonNullElse(indentType, IndentType.SPACES)) {
        case SPACES -> {
            int amt = Objects.requireNonNullElse(amount, defaultSpaces);
            yield " ".repeat(amt); //$NON-NLS-1$
        }
        case TABS -> {
            int amt = Objects.requireNonNullElse(amount, 1);
            yield "\t".repeat(amt); //$NON-NLS-1$
        }
        };
    }

    /**
     * Utility method that indents the buffer by the default amount for Java
     * (four spaces per indent level).
     *
     * @param sb
     *            a StringBuilder to append to
     * @param indentLevel
     *            the required indent level
     */
    public void javaIndent(StringBuilder sb, int indentLevel) {
        sb.append(javaIndent(indentLevel));
    }

    public String javaIndent(int indentLevel) {
        return javaIndent.repeat(indentLevel);
    }

    /**
     * Utility method that indents the buffer by the default amount for Kotlin
     * (four spaces per indent level).
     *
     * @param sb
     *            a StringBuilder to append to
     * @param indentLevel
     *            the required indent level
     */
    public static void kotlinIndent(StringBuilder sb, int indentLevel) {
        sb.append(kotlinIndent(indentLevel));
    }

    public static String kotlinIndent(int indentLevel) {
        // Kotlin indents are always four spaces per level (from the published Kotlin code conventions)
        return "    ".repeat(indentLevel); //$NON-NLS-1$
    }

    /**
     * Utility method that indents the buffer by the default amount for XML (two
     * spaces per indent level).
     *
     * @param sb
     *            a StringBuilder to append to
     * @param indentLevel
     *            the required indent level
     */
    public void xmlIndent(StringBuilder sb, int indentLevel) {
        sb.append(xmlIndent(indentLevel));
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
