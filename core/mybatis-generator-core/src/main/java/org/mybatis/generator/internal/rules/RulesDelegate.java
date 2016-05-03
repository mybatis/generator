/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * This class can be used by plugins to easily implement a custom rules
 * implementation. Plugins should respect the rules implementation calculated by
 * the generator, as well as implementations from other plugins. In general if
 * something is disabled by the default rules, or is disabled by some other
 * plugin, it should not be re-enabled. Therefore, the following pattern of use
 * is recommended:
 * 
 * <pre>
 * public class MyPlugin extends PluginAdapter {
 *   &#64;Override
 *   public void initialized(IntrospectedTable introspectedTable) {
 *     MyRules myRules = new MyRules(introspectedTable.getRules());
 *     introspectedTable.setRules(myRules);
 *   }
 * }
 * 
 * public class MyRules extends RulesDelegate (
 *   public MyRules(Rules rules) {
 *     super(rules);
 *   }
 *   
 *   &#64;Override
 *   public boolean generateInsert() {
 *     boolean rc = super.generateInsert();
 *     if (rc) {
 *       // Other plugins, and the default rules, enable generation
 *       // of the insert method.  We can decide to disable it here
 *       // if needed.
 *     }
 *     
 *     return rc;
 *   }
 * </pre>
 * 
 * 
 * @author Jeff Butler
 * 
 */
public class RulesDelegate implements Rules {
    protected Rules rules;

    public RulesDelegate(Rules rules) {
        this.rules = rules;
    }

    public FullyQualifiedJavaType calculateAllFieldsClass() {
        return rules.calculateAllFieldsClass();
    }

    public boolean generateBaseRecordClass() {
        return rules.generateBaseRecordClass();
    }

    public boolean generateBaseResultMap() {
        return rules.generateBaseResultMap();
    }

    public boolean generateCountByExample() {
        return rules.generateCountByExample();
    }

    public boolean generateDeleteByExample() {
        return rules.generateDeleteByExample();
    }

    public boolean generateDeleteByPrimaryKey() {
        return rules.generateDeleteByPrimaryKey();
    }

    public boolean generateExampleClass() {
        return rules.generateExampleClass();
    }

    public boolean generateInsert() {
        return rules.generateInsert();
    }

    public boolean generateInsertSelective() {
        return rules.generateInsertSelective();
    }

    public boolean generatePrimaryKeyClass() {
        return rules.generatePrimaryKeyClass();
    }

    public boolean generateRecordWithBLOBsClass() {
        return rules.generateRecordWithBLOBsClass();
    }

    public boolean generateResultMapWithBLOBs() {
        return rules.generateResultMapWithBLOBs();
    }

    public boolean generateSelectByExampleWithBLOBs() {
        return rules.generateSelectByExampleWithBLOBs();
    }

    public boolean generateSelectByExampleWithoutBLOBs() {
        return rules.generateSelectByExampleWithoutBLOBs();
    }

    public boolean generateSelectByPrimaryKey() {
        return rules.generateSelectByPrimaryKey();
    }

    public boolean generateSQLExampleWhereClause() {
        return rules.generateSQLExampleWhereClause();
    }

    public boolean generateMyBatis3UpdateByExampleWhereClause() {
        return rules.generateMyBatis3UpdateByExampleWhereClause();
    }

    public boolean generateUpdateByExampleSelective() {
        return rules.generateUpdateByExampleSelective();
    }

    public boolean generateUpdateByExampleWithBLOBs() {
        return rules.generateUpdateByExampleWithBLOBs();
    }

    public boolean generateUpdateByExampleWithoutBLOBs() {
        return rules.generateUpdateByExampleWithoutBLOBs();
    }

    public boolean generateUpdateByPrimaryKeySelective() {
        return rules.generateUpdateByPrimaryKeySelective();
    }

    public boolean generateUpdateByPrimaryKeyWithBLOBs() {
        return rules.generateUpdateByPrimaryKeyWithBLOBs();
    }

    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        return rules.generateUpdateByPrimaryKeyWithoutBLOBs();
    }

    public IntrospectedTable getIntrospectedTable() {
        return rules.getIntrospectedTable();
    }

    public boolean generateBaseColumnList() {
        return rules.generateBaseColumnList();
    }

    public boolean generateBlobColumnList() {
        return rules.generateBlobColumnList();
    }

    public boolean generateJavaClient() {
        return rules.generateJavaClient();
    }
}
