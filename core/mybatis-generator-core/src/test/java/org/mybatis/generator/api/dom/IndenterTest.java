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
package org.mybatis.generator.api.dom;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.IndentType;

class IndenterTest {
    @Test
    void testDefaults() {
        Indenter indenter = Indenter.defaultIndenter();

        assertThat(indenter.javaIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo(" ".repeat(2));
    }

    @Test
    void testTabs() {
        Indenter indenter = new Indenter.Builder()
                .withJavaIndentType(IndentType.TABS)
                .withXmlIndentType(IndentType.TABS)
                .build();

        assertThat(indenter.javaIndent(1)).isEqualTo("\t".repeat(1));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo("\t".repeat(1));
    }

    @Test
    void testManyTabs() {
        Indenter indenter = new Indenter.Builder()
                .withJavaIndentType(IndentType.TABS)
                .withJavaIndentAmount(2)
                .withXmlIndentType(IndentType.TABS)
                .withXmlIndentAmount(2)
                .build();

        assertThat(indenter.javaIndent(1)).isEqualTo("\t".repeat(2));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo("\t".repeat(2));
    }

    @Test
    void testMixedTabsAndSpaces() {
        Indenter indenter = new Indenter.Builder()
                .withJavaIndentType(IndentType.TABS)
                .withJavaIndentAmount(1)
                .withXmlIndentType(IndentType.SPACES)
                .withXmlIndentAmount(2)
                .build();

        assertThat(indenter.javaIndent(1)).isEqualTo("\t".repeat(1));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo(" ".repeat(2));
    }

    @Test
    void testMixedTabsAndSpacesDefaultAmounts() {
        Indenter indenter = new Indenter.Builder()
                .withJavaIndentType(IndentType.SPACES)
                .withXmlIndentType(IndentType.TABS)
                .build();

        assertThat(indenter.javaIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo("\t".repeat(1));
    }

    @Test
    void testChangeSpaces() {
        Indenter indenter = new Indenter.Builder()
                .withJavaIndentAmount(2)
                .withXmlIndentAmount(4)
                .build();

        assertThat(indenter.javaIndent(1)).isEqualTo(" ".repeat(2));
        assertThat(Indenter.kotlinIndent(1)).isEqualTo(" ".repeat(4));
        assertThat(indenter.xmlIndent(1)).isEqualTo(" ".repeat(4));
    }
}
