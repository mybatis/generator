/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.DomainObjectRenamingRule;

public class FullyQualifiedTableTest {

    @Test
    public void testNormalCase() {
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "mytable", null, null, false, null, null, null, false, null, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("Mytable");
    }

    @Test
    public void testNormalCaseWithPrefix() {
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "sys_mytable", null, null, false, null, null, null, false, null, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("SysMytable");
    }

    @Test
    public void testRenamingRule() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule();
        renamingRule.setSearchString("^Sys");
        renamingRule.setReplaceString("");
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "sys_mytable", null, null, false, null, null, null, false, renamingRule, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("Mytable");
    }

    @Test
    public void testRenamingRule2() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule();
        renamingRule.setSearchString("^Sys");
        renamingRule.setReplaceString("");
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "sys_my_table", null, null, false, null, null, null, false, renamingRule, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("MyTable");
    }

    @Test
    public void testRenamingRuleNoUnderscore() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule();
        renamingRule.setSearchString("^Sys");
        renamingRule.setReplaceString("");
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "sysmytable", null, null, false, null, null, null, false, renamingRule, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("Mytable");
    }

    @Test
    public void testRenamingRuleNoUnderscore2() {
        DomainObjectRenamingRule renamingRule = new DomainObjectRenamingRule();
        renamingRule.setSearchString("^Sys");
        renamingRule.setReplaceString("");
        FullyQualifiedTable fqt = new FullyQualifiedTable(null, "myschema", "sysmy_table", null, null, false, null, null, null, false, renamingRule, null);

        assertThat(fqt.getDomainObjectName()).isEqualTo("MyTable");
    }
}
