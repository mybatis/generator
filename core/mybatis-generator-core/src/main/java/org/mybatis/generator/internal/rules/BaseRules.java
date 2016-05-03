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
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This class centralizes all the rules related to code generation - including
 * the methods and objects to create, and certain attributes related to those
 * objects.
 * 
 * @author Jeff Butler
 */
public abstract class BaseRules implements Rules {

    /** The table configuration. */
    protected TableConfiguration tableConfiguration;
    
    /** The introspected table. */
    protected IntrospectedTable introspectedTable;
    
    /** The is model only. */
    protected final boolean isModelOnly;

    /**
     * Instantiates a new base rules.
     *
     * @param introspectedTable
     *            the introspected table
     */
    public BaseRules(IntrospectedTable introspectedTable) {
        super();
        this.introspectedTable = introspectedTable;
        this.tableConfiguration = introspectedTable.getTableConfiguration();
        String modelOnly = tableConfiguration.getProperty(PropertyRegistry.TABLE_MODEL_ONLY);
        isModelOnly = StringUtility.isTrue(modelOnly);
    }

    /**
     * Implements the rule for generating the insert SQL Map element and DAO
     * method. If the insert statement is allowed, then generate the element and
     * method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateInsert() {
        if (isModelOnly) {
            return false;
        }
        
        return tableConfiguration.isInsertStatementEnabled();
    }

    /**
     * Implements the rule for generating the insert selective SQL Map element
     * and DAO method. If the insert statement is allowed, then generate the
     * element and method.
     * 
     * @return true if the element and method should be generated
     */
    public boolean generateInsertSelective() {
        if (isModelOnly) {
            return false;
        }
        
        return tableConfiguration.isInsertStatementEnabled();
    }

    /**
     * Calculates the class that contains all fields. This class is used as the
     * insert statement parameter, as well as the returned value from the select
     * by primary key method. The actual class depends on how the domain model
     * is generated.
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
        if (isModelOnly) {
            return false;
        }
        
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
        if (isModelOnly) {
            return false;
        }
        
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
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
                && introspectedTable.hasPrimaryKeyColumns()
                && (introspectedTable.hasBLOBColumns() || introspectedTable
                        .hasBaseColumns());

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
        if (isModelOnly) {
            return false;
        }
        
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
        if (isModelOnly) {
            return false;
        }
        
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
        if (isModelOnly) {
            return true;
        }
        
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
        boolean rc;
        
        if (introspectedTable.hasBLOBColumns()) {
            if (isModelOnly) {
                rc = true;
            } else {
                rc = tableConfiguration.isSelectByExampleStatementEnabled() 
                        || tableConfiguration.isSelectByPrimaryKeyStatementEnabled();
            }
        } else {
            rc = false;
        }
        
        return rc;
    }

    /**
     * Implements the rule for generating the SQL example where clause element.
     * 
     * In iBATIS2, generate the element if the selectByExample, deleteByExample,
     * updateByExample, or countByExample statements are allowed.
     * 
     * In MyBatis3, generate the element if the selectByExample,
     * deleteByExample, or countByExample statements are allowed.
     * 
     * @return true if the SQL where clause element should be generated
     */
    public boolean generateSQLExampleWhereClause() {
        if (isModelOnly) {
            return false;
        }
        
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
     * In MyBatis3, generate the element if the updateByExample statements are
     * allowed.
     * 
     * @return true if the SQL where clause element should be generated
     */
    public boolean generateMyBatis3UpdateByExampleWhereClause() {
        if (isModelOnly) {
            return false;
        }
        
        return introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3
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
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isSelectByPrimaryKeyStatementEnabled()
                && introspectedTable.hasPrimaryKeyColumns()
                && (introspectedTable.hasBaseColumns() || introspectedTable
                        .hasBLOBColumns());

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
        if (isModelOnly) {
            return false;
        }
        
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
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
                && introspectedTable.hasBLOBColumns();

        return rc;
    }

    /**
     * Implements the rule for generating an example class. The class should be
     * generated if the selectByExample or deleteByExample or countByExample
     * methods are allowed.
     * 
     * @return true if the example class should be generated
     */
    public boolean generateExampleClass() {
        if (introspectedTable.getContext().getSqlMapGeneratorConfiguration() == null
                && introspectedTable.getContext().getJavaClientGeneratorConfiguration() == null) {
            // this is a model only context - don't generate the example class
            return false;
        }
        
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isSelectByExampleStatementEnabled()
                || tableConfiguration.isDeleteByExampleStatementEnabled()
                || tableConfiguration.isCountByExampleStatementEnabled()
                || tableConfiguration.isUpdateByExampleStatementEnabled();

        return rc;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateCountByExample()
     */
    public boolean generateCountByExample() {
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isCountByExampleStatementEnabled();

        return rc;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateUpdateByExampleSelective()
     */
    public boolean generateUpdateByExampleSelective() {
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled();

        return rc;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateUpdateByExampleWithoutBLOBs()
     */
    public boolean generateUpdateByExampleWithoutBLOBs() {
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled()
                && (introspectedTable.hasPrimaryKeyColumns() || introspectedTable
                        .hasBaseColumns());

        return rc;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateUpdateByExampleWithBLOBs()
     */
    public boolean generateUpdateByExampleWithBLOBs() {
        if (isModelOnly) {
            return false;
        }
        
        boolean rc = tableConfiguration.isUpdateByExampleStatementEnabled()
                && introspectedTable.hasBLOBColumns();

        return rc;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#getIntrospectedTable()
     */
    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateBaseColumnList()
     */
    public boolean generateBaseColumnList() {
        if (isModelOnly) {
            return false;
        }
        
        return generateSelectByPrimaryKey()
                || generateSelectByExampleWithoutBLOBs();
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateBlobColumnList()
     */
    public boolean generateBlobColumnList() {
        if (isModelOnly) {
            return false;
        }
        
        return introspectedTable.hasBLOBColumns()
                && (tableConfiguration.isSelectByExampleStatementEnabled() || tableConfiguration
                        .isSelectByPrimaryKeyStatementEnabled());
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.internal.rules.Rules#generateJavaClient()
     */
    public boolean generateJavaClient() {
        return !isModelOnly;
    }
}
