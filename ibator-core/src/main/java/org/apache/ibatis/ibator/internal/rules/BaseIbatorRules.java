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
package org.apache.ibatis.ibator.internal.rules;

import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.IntrospectedTable.TargetRuntime;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.config.TableConfiguration;

/**
 * This class centralizes all the rules related to code generation - including
 * the methods and objects to create, and certain attributes related to those
 * objects.
 * 
 * @author Jeff Butler
 */
public abstract class BaseIbatorRules implements IbatorRules {

    protected TableConfiguration tableConfiguration;
    protected IntrospectedTable introspectedTable;

    /**
     * 
     */
    public BaseIbatorRules(IntrospectedTable introspectedTable) {
        super();
        this.introspectedTable = introspectedTable;
        this.tableConfiguration = introspectedTable.getTableConfiguration();
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
     * Implements the rule for generating the insert selective SQL Map element and DAO
     * method. If the insert statement is allowed, then generate the element and
     * method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateInsertSelective() {
        return tableConfiguration.isInsertStatementEnabled();
    }
    
    /**
     * Calculates the class that contains all fields.  This class is used
     * as the insert statement parameter, as well as the returned value
     * from the select by primary key method.  The actual class depends
     * on how the domain model is generated.
     * 
     * @return the type of the class that holds all fields
     */
    public FullyQualifiedJavaType calculateAllFieldsClass() {
        
        String answer;
        
        if (generateRecordWithBLOBsClass()) {
            answer = introspectedTable.getRecordWithBLOBsType();
        } else if (generateBaseRecordClass()) {
            answer = introspectedTable.getBaseRecordType();
        } else {
            answer = introspectedTable.getPrimaryKeyType();
        }
        
        return new FullyQualifiedJavaType(answer);
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
            && introspectedTable.hasPrimaryKeyColumns()
            && introspectedTable.hasBaseColumns();
        
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
            && introspectedTable.hasPrimaryKeyColumns()
            && introspectedTable.hasBLOBColumns();
    
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
            && introspectedTable.hasPrimaryKeyColumns()
            && (introspectedTable.hasBLOBColumns()
                    || introspectedTable.hasBaseColumns());
    
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
            && introspectedTable.hasPrimaryKeyColumns();

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
            && introspectedTable.hasBLOBColumns();
    
        return rc;
    }
    
    /**
     * Implements the rule for generating the SQL example where clause element.
     * 
     * In iBATIS2, generate the element if the selectByExample, deleteByExample,
     * updateByExample, or countByExample statements are allowed.
     * 
     * In iBATIS3, generate the element if the selectByExample, deleteByExample,
     * or countByExample statements are allowed.
     * 
     * @return true if the SQL where clause element should be generated
     */
    public boolean generateSQLExampleWhereClause() {
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
            || tableConfiguration.isDeleteByExampleStatementEnabled()
            || tableConfiguration.isCountByExampleStatementEnabled();
        
        if (introspectedTable.getTargetRuntime() == TargetRuntime.IBATIS2) {
            rc |= tableConfiguration.isUpdateByExampleStatementEnabled();
        }
        
        return rc;
    }
    
    /**
     * Implements the rule for generating the SQL example where clause element
     * specifically for use in the update by example methods.
     * 
     * In iBATIS2, do not generate the element.
     * 
     * In iBATIS3, generate the element if the updateByExample
     * statements are allowed.
     * 
     * @return true if the SQL where clause element should be generated
     */
    public boolean generateIbatis3UpdateByExampleWhereClause() {
        return introspectedTable.getTargetRuntime() == TargetRuntime.IBATIS3
            && tableConfiguration.isUpdateByExampleStatementEnabled();
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
            && introspectedTable.hasPrimaryKeyColumns()
            && (introspectedTable.hasBaseColumns()
                    || introspectedTable.hasBLOBColumns());
        
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
            && introspectedTable.hasBLOBColumns();
        
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
            && (introspectedTable.hasPrimaryKeyColumns()
            || introspectedTable.hasBaseColumns());
        
        return rc;
    }
    
    public boolean generateUpdateByExampleWithBLOBs() {
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled()
            && introspectedTable.hasBLOBColumns();
    
        return rc;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public boolean generateBaseColumnList() {
        return generateSelectByPrimaryKey()
            || generateSelectByExampleWithoutBLOBs();
    }

    public boolean generateBlobColumnList() {
        return introspectedTable.hasBLOBColumns()
            && (tableConfiguration.isSelectByExampleStatementEnabled()
                    || tableConfiguration.isSelectByPrimaryKeyStatementEnabled());
    }
}
