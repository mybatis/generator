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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.config.DomainObjectRenamingRule;

class FullyQualifiedTableTest {

    @ParameterizedTest
    @MethodSource("variants")
    void testIt(FullyQualifiedTable fullyQualifiedTable, String expectedName) {
        assertThat(fullyQualifiedTable.getDomainObjectName()).isEqualTo(expectedName);
    }

    static Stream<Arguments> variants() {
        return Stream.of(testNormalCase(), testNormalCaseWithPrefix(), testRenamingRule(), testRenamingRule2(),
                testRenamingRuleNoUnderscore(), testRenamingRuleNoUnderscore2());
    }

    static Arguments testNormalCase() {
        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("mytable")
                .build();

        return Arguments.argumentSet("normalCase", fqt, "Mytable");
    }

    static Arguments testNormalCaseWithPrefix() {
        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("sys_mytable")
                .build();

        return Arguments.argumentSet("normalCaseWithPrefix", fqt, "SysMytable");
    }

    static Arguments testRenamingRule() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule("^Sys", "");

        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("sys_mytable")
                .withDomainObjectRenamingRule(renamingRule)
                .build();

        return Arguments.argumentSet("renamingRule", fqt, "Mytable");
    }

    static Arguments testRenamingRule2() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule("^Sys", "");
        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("sys_my_table")
                .withDomainObjectRenamingRule(renamingRule)
                .build();

        return Arguments.argumentSet("renamingRule2", fqt, "MyTable");
    }

    static Arguments testRenamingRuleNoUnderscore() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule("^Sys", "");
        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("sysmytable")
                .withDomainObjectRenamingRule(renamingRule)
                .build();

        return Arguments.argumentSet("renamingRuleNoUnderscore", fqt, "Mytable");
    }

    static Arguments testRenamingRuleNoUnderscore2() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule("^Sys", "");

        FullyQualifiedTable fqt = new FullyQualifiedTable.Builder()
                .withIntrospectedSchema("myschema")
                .withIntrospectedTableName("sysmy_table")
                .withDomainObjectRenamingRule(renamingRule)
                .build();

        return Arguments.argumentSet("renamingRuleNoUnderscore2", fqt, "MyTable");
    }
}
