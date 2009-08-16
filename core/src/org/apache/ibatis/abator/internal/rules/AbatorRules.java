/*
 *  Copyright 2006 The Apache Software Foundation
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
package org.apache.ibatis.abator.internal.rules;

import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.JavaModelGenerator;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.config.TableConfiguration;
import org.apache.ibatis.abator.internal.db.ColumnDefinitions;

/**
 * This class centralizes all the rules related to code generation - including
 * the methods and objects to create, and certain attributes related to those
 * objects.
 * 
 * See package JavaDoc for more information.
 * 
 * @author Jeff Butler
 */
public abstract class AbatorRules {

    protected TableConfiguration tableConfiguration;
    protected ColumnDefinitions columnDefinitions;

    /**
     * 
     */
    public AbatorRules(TableConfiguration tableConfiguration,
            ColumnDefinitions columnDefinitions) {
        super();
        this.tableConfiguration = tableConfiguration;
        this.columnDefinitions = columnDefinitions;
    }

    /**
     * Implements the rule for generating the insert SQL Map element and DAO
     * method. If the insert statement is allowed, then generate the element and
     * method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateInsert() {
        return tableConfiguration.isInsertStatementEnabled();
    }

    /**
     * Calculates the class that contains all fields.  This class is used
     * as the insert statement parameter, as well as the returned value
     * from the select by primary key method.  The actual class depends
     * on how the domain model is generated.
     * 
     * @param javaModelGenerator
     * @param table
     * @return the type of the class that holds all fields
     */
    public FullyQualifiedJavaType calculateAllFieldsClass(JavaModelGenerator javaModelGenerator,
            FullyQualifiedTable table) {
        
        FullyQualifiedJavaType answer;
        
        if (generateRecordWithBLOBsClass()) {
            answer = javaModelGenerator.getRecordWithBLOBsType(table);
        } else if (generateBaseRecordClass()) {
            answer = javaModelGenerator.getBaseRecordType(table);
        } else {
            answer = javaModelGenerator.getPrimaryKeyType(table);
        }
        
        return answer;
    }
    
    /**
     * Implements the rule for generating the update by primary key without
     * BLOBs SQL Map element and DAO method. If the table has a primary key as
     * well as other non-BLOB fields, and the updateByPrimaryKey statement is
     * allowed, then generate the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        boolean rc = tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
            && columnDefinitions.hasPrimaryKeyColumns()
            && columnDefinitions.hasBaseColumns();
        
        return rc;
    }
    
    /**
     * Implements the rule for generating the update by primary key with BLOBs
     * SQL Map element and DAO method. If the table has a primary key as well as
     * other BLOB fields, and the updateByPrimaryKey statement is allowed, then
     * generate the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateUpdateByPrimaryKeyWithBLOBs() {
        boolean rc = tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
            && columnDefinitions.hasPrimaryKeyColumns()
            && columnDefinitions.hasBLOBColumns();
    
        return rc;
    }

    /**
     * Implements the rule for generating the update by primary key selective
     * SQL Map element and DAO method. If the table has a primary key as well as
     * other fields, and the updateByPrimaryKey statement is allowed, then
     * generate the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateUpdateByPrimaryKeySelective() {
        boolean rc = tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
            && columnDefinitions.hasPrimaryKeyColumns()
            && (columnDefinitions.hasBLOBColumns()
                    || columnDefinitions.hasBaseColumns());
    
        return rc;
    }

    /**
     * Implements the rule for generating the delete by primary key SQL Map
     * element and DAO method. If the table has a primary key, and the
     * deleteByPrimaryKey statement is allowed, then generate the element and
     * method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateDeleteByPrimaryKey() {
        boolean rc = tableConfiguration.isDeleteByPrimaryKeyStatementEnabled()
            && columnDefinitions.hasPrimaryKeyColumns();

        return rc;
    }
    
    /**
     * Implements the rule for generating the delete by example SQL Map element
     * and DAO method. If the deleteByExample statement is allowed, then
     * generate the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateDeleteByExample() {
        boolean rc = tableConfiguration.isDeleteByExampleStatementEnabled();

        return rc;
    }
    
    /**
     * Implements the rule for generating the result map without BLOBs. If
     * either select method is allowed, then generate the result map.
     * 
     * @return true if the result map should be generated
     */
    public boolean generateBaseResultMap() {
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
            || tableConfiguration.isSelectByPrimaryKeyStatementEnabled();
        
        return rc;
    }
    
    /**
     * Implements the rule for generating the result map with BLOBs. If the
     * table has BLOB columns, and either select method is allowed, then
     * generate the result map.
     * 
     * @return true if the result map should be generated
     */
    public boolean generateResultMapWithBLOBs() {
        boolean rc = (tableConfiguration.isSelectByExampleStatementEnabled()
            || tableConfiguration.isSelectByPrimaryKeyStatementEnabled())
            && columnDefinitions.hasBLOBColumns();
    
        return rc;
    }
    
    /**
     * Implements the rule for generating the SQL example where clause element.
     * Generate the element if the selectByExample or deleteByExample
     * or countByExample statements are allowed.
     * 
     * @return true if the SQL where clause element should be generated
     */
    public boolean generateSQLExampleWhereClause() {
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
            || tableConfiguration.isDeleteByExampleStatementEnabled()
            || tableConfiguration.isCountByExampleStatementEnabled();
        
        return rc;
    }
    
    /**
     * Implements the rule for generating the select by primary key SQL Map
     * element and DAO method. If the table has a primary key as well as other
     * fields, and the selectByPrimaryKey statement is allowed, then generate
     * the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateSelectByPrimaryKey() {
        boolean rc = tableConfiguration.isSelectByPrimaryKeyStatementEnabled()
            && columnDefinitions.hasPrimaryKeyColumns()
            && (columnDefinitions.hasBaseColumns()
                    || columnDefinitions.hasBLOBColumns());
        
        return rc;
    }
    
    /**
     * Implements the rule for generating the select by example without BLOBs
     * SQL Map element and DAO method. If the selectByExample statement is
     * allowed, then generate the element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateSelectByExampleWithoutBLOBs() {
        return tableConfiguration.isSelectByExampleStatementEnabled();
    }
    
    /**
     * Implements the rule for generating the select by example with BLOBs SQL
     * Map element and DAO method. If the table has BLOB fields and the
     * selectByExample statement is allowed, then generate the element and
     * method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateSelectByExampleWithBLOBs() {
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
            && columnDefinitions.hasBLOBColumns();
        
        return rc;
    }

    /**
     * Implements the rule for generating an example class.
     * The class should be generated if the selectByExample or
     * deleteByExample or countByExample methods are allowed.
     * 
     * @return true if the example class should be generated
     */
    public boolean generateExampleClass() {
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
                || tableConfiguration.isDeleteByExampleStatementEnabled()
                || tableConfiguration.isCountByExampleStatementEnabled()
                || tableConfiguration.isUpdateByExampleStatementEnabled();
    
        return rc;
    }
    
    public boolean generateCountByExample() {
        boolean rc = tableConfiguration.isCountByExampleStatementEnabled();

        return rc;
    }

    public boolean generateUpdateByExampleSelective() {
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled();

        return rc;
    }

    public boolean generateUpdateByExampleWithoutBLOBs() {
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled()
            && (columnDefinitions.hasPrimaryKeyColumns()
            || columnDefinitions.hasBaseColumns());
        
        return rc;
    }
    
    public boolean generateUpdateByExampleWithBLOBs() {
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled()
            && columnDefinitions.hasBLOBColumns();
    
        return rc;
    }
    
    /**
     * Implements the rule for determining whether to generate a primary key
     * class.  If you return false from this method, and the table has
     * primary key columns, then the primary key columns will be
     * added to the base class.
     * 
     * @return true if a seperate primary key class should be generated
     */
    public abstract boolean generatePrimaryKeyClass();

    /**
     * Implements the rule for generating a base record.
     * 
     * @return true if the class should be generated
     */
    public abstract boolean generateBaseRecordClass();

    /**
     * Implements the rule for generating a record with BLOBs.  If you 
     * return false from this method, and the table had BLOB columns,
     * then the BLOB columns will be added to the base class.  
     * 
     * @return true if the record with BLOBs class should be generated
     */
    public abstract boolean generateRecordWithBLOBsClass();
}
