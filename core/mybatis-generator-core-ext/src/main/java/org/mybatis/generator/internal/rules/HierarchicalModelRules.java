/**
 *    Copyright 2006-2017 the original author or authors.
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

/**
 * This class encapsulates all the code generation rules for a table using the
 * hierarchical model.
 * 
 * @author Jeff Butler
 * 
 */
public class HierarchicalModelRules extends BaseRules {

    /**
     * Instantiates a new hierarchical model rules.
     *
     * @param introspectedTable
     *            the introspected table
     */
    public HierarchicalModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }

    /**
     * Implements the rule for determining whether to generate a primary key
     * class. If the physical table has a primary key, then we generate the
     * class.
     * 
     * @return true if the primary key should be generated
     */
    @Override
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.hasPrimaryKeyColumns();
    }

    /**
     * Implements the rule for generating a base record. If the table has fields
     * that are not in the primary key, and non-BLOB fields, then generate the
     * class.
     * 
     * @return true if the class should be generated
     */
    @Override
    public boolean generateBaseRecordClass() {
        return introspectedTable.hasBaseColumns();
    }

    /**
     * Implements the rule for generating a record with BLOBs. A record with
     * BLOBs is generated if the table contains any BLOB fields.
     * 
     * @return true if the record with BLOBs class should be generated
     */
    @Override
    public boolean generateRecordWithBLOBsClass() {
        return introspectedTable.hasBLOBColumns();
    }
}
