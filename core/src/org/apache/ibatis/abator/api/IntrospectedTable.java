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

package org.apache.ibatis.abator.api;

import java.util.Iterator;

import org.apache.ibatis.abator.config.GeneratedKey;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.rules.AbatorRules;

/**
 * Read only interface for dealing with introspected tables.
 * 
 * @author Jeff Butler
 *
 */
public interface IntrospectedTable {
    FullyQualifiedTable getTable();
    String getSelectByExampleQueryId();
    String getSelectByPrimaryKeyQueryId();
    GeneratedKey getGeneratedKey();

    ColumnDefinition getColumn(String columnName);
    
    /**
     * Returns true if any of the columns in the table are JDBC Dates
     * (as opposed to timestamps).
     * 
     * @return true if the table contains DATE columns
     */
    boolean hasJDBCDateColumns();

    /**
     * Returns true if any of the columns in the table are JDBC Times
     * (as opposed to timestamps).
     * 
     * @return true if the table contains TIME columns
     */
    boolean hasJDBCTimeColumns();
    
    /**
     * Returns the columns in the primary key.  If the
     * generatePrimaryKeyClass() method returns false, then these
     * columns will be iterated as the parameters of the 
     * selectByPrimaryKay and deleteByPrimaryKey methods
     * 
     * @return an Iterator of ColumnDefinition objects for
     *   columns in the primary key
     */
    Iterator getPrimaryKeyColumns();
    
    boolean hasPrimaryKeyColumns();
    
    Iterator getBaseColumns();
    
    /**
     * Returns all columns in the table (for use by the select by
     * primary key and select by example with BLOBs methods)
     * 
     * @return an Iterator of ColumnDefinition objects for
     *   all columns in the table
     */
    Iterator getAllColumns();
    
    /**
     * Returns all columns axcept BLOBs (for use by the select by
     * example without BLOBs method)
     * 
     * @return an Iterator of ColumnDefinition objects for
     *   columns in the table that are non BLOBs
     */
    Iterator getNonBLOBColumns();
    
    int getNonBLOBColumnCount();
    
    Iterator getNonPrimaryKeyColumns();
    
    Iterator getBLOBColumns();
    
    boolean hasBLOBColumns();
    
    AbatorRules getRules();
    
    String getTableConfigurationProperty(String property);
}
