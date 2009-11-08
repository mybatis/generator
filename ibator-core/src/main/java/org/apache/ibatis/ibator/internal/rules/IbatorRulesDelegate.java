/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.ibator.internal.rules;

import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;

/**
 * This class can be used by plugins to easily implement a custom
 * rules implementation.  Plugins should respect the rules implementation
 * calculated by Ibator, as well as implementations from other plugins.
 * In general if something is disabled by the default rules, or is
 * disabled by some other plugin, it should not be re-enabled.
 * Therefore, the following pattern of use is recommended:
 * <pre>
 * public class MyPlugin extends IbatorPluginAdapter {
 *   &#64;Override
 *   public void initialized(IntrospectedTable introspectedTable) {
 *     MyRules myRules = new MyRules(introspectedTable.getRules());
 *     introspectedTable.setRules(myRules);
 *   }
 * }
 * 
 * public class MyRules extends IbatorRulesDelegate (
 *   public MyRules(IbatorRules ibatorRules) {
 *     super(ibatorRules);
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
public class IbatorRulesDelegate implements IbatorRules {
    protected IbatorRules ibatorRules;
    
    public IbatorRulesDelegate(IbatorRules ibatorRules) {
        this.ibatorRules = ibatorRules;
    }

    public FullyQualifiedJavaType calculateAllFieldsClass() {
        return ibatorRules.calculateAllFieldsClass();
    }

    public boolean generateBaseRecordClass() {
        return ibatorRules.generateBaseRecordClass();
    }

    public boolean generateBaseResultMap() {
        return ibatorRules.generateBaseResultMap();
    }

    public boolean generateCountByExample() {
        return ibatorRules.generateCountByExample();
    }

    public boolean generateDeleteByExample() {
        return ibatorRules.generateDeleteByExample();
    }

    public boolean generateDeleteByPrimaryKey() {
        return ibatorRules.generateDeleteByPrimaryKey();
    }

    public boolean generateExampleClass() {
        return ibatorRules.generateExampleClass();
    }

    public boolean generateInsert() {
        return ibatorRules.generateInsert();
    }

    public boolean generateInsertSelective() {
        return ibatorRules.generateInsertSelective();
    }

    public boolean generatePrimaryKeyClass() {
        return ibatorRules.generatePrimaryKeyClass();
    }

    public boolean generateRecordWithBLOBsClass() {
        return ibatorRules.generateRecordWithBLOBsClass();
    }

    public boolean generateResultMapWithBLOBs() {
        return ibatorRules.generateResultMapWithBLOBs();
    }

    public boolean generateSelectByExampleWithBLOBs() {
        return ibatorRules.generateSelectByExampleWithBLOBs();
    }

    public boolean generateSelectByExampleWithoutBLOBs() {
        return ibatorRules.generateSelectByExampleWithoutBLOBs();
    }

    public boolean generateSelectByPrimaryKey() {
        return ibatorRules.generateSelectByPrimaryKey();
    }

    public boolean generateSQLExampleWhereClause() {
        return ibatorRules.generateSQLExampleWhereClause();
    }

    public boolean generateIbatis3UpdateByExampleWhereClause() {
        return ibatorRules.generateIbatis3UpdateByExampleWhereClause();
    }

    public boolean generateUpdateByExampleSelective() {
        return ibatorRules.generateUpdateByExampleSelective();
    }

    public boolean generateUpdateByExampleWithBLOBs() {
        return ibatorRules.generateUpdateByExampleWithBLOBs();
    }

    public boolean generateUpdateByExampleWithoutBLOBs() {
        return ibatorRules.generateUpdateByExampleWithoutBLOBs();
    }

    public boolean generateUpdateByPrimaryKeySelective() {
        return ibatorRules.generateUpdateByPrimaryKeySelective();
    }

    public boolean generateUpdateByPrimaryKeyWithBLOBs() {
        return ibatorRules.generateUpdateByPrimaryKeyWithBLOBs();
    }

    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        return ibatorRules.generateUpdateByPrimaryKeyWithoutBLOBs();
    }

    public IntrospectedTable getIntrospectedTable() {
        return ibatorRules.getIntrospectedTable();
    }

    public boolean generateBaseColumnList() {
        return ibatorRules.generateBaseColumnList();
    }

    public boolean generateBlobColumnList() {
        return ibatorRules.generateBlobColumnList();
    }
}
