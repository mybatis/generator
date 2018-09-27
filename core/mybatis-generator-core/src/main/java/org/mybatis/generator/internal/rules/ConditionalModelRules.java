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
package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * This class encapsulates all the code generation rules for a table using the
 * conditional model. In this model we do not generate primary key or record
 * with BLOBs classes if the class would only hold one field.
 * 
 * @author Jeff Butler
 * 
 */
public class ConditionalModelRules extends BaseRules {

    /**
     * Instantiates a new conditional model rules.
     *
     * @param introspectedTable
     *            the introspected table
     */
    public ConditionalModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }

    /**
     * We generate a primary key if there is more than one primary key field.
     * 
     * @return true if the primary key should be generated
     */
    @Override
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.getPrimaryKeyColumns().size() > 1;
    }

    /**
     * Generate a base record if there are any base columns, or if there is only
     * one primary key coulmn (in which case we will not generate a primary key
     * class), or if there is only one BLOB column (in which case we will not
     * generate a record with BLOBs class).
     * 
     * @return true if the class should be generated
     */
    @Override
    public boolean generateBaseRecordClass() {
        return introspectedTable.hasBaseColumns()
                || introspectedTable.getPrimaryKeyColumns().size() == 1
                || blobsAreInBaseRecord();
    }

    /**
     * Blobs will be in the base record class if there is only one blob column.
     * 
     * @return true if there are blobs but they are in the base record class
     */
    private boolean blobsAreInBaseRecord() {
        return introspectedTable.hasBLOBColumns() && !generateRecordWithBLOBsClass();
    }

    /**
     * We generate a record with BLOBs class if there is more than one BLOB
     * column. Do not generate a BLOBs class if any other super class would only
     * contain one field
     * 
     * @return true if the record with BLOBs class should be generated
     */
    @Override
    public boolean generateRecordWithBLOBsClass() {
        int otherColumnCount = introspectedTable.getPrimaryKeyColumns().size()
                + introspectedTable.getBaseColumns().size();

        return otherColumnCount > 1
                && introspectedTable.getBLOBColumns().size() > 1;
    }
}
