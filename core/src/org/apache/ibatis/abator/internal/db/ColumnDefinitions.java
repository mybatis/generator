/*
 *  Copyright 2005 The Apache Software Foundation
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
package org.apache.ibatis.abator.internal.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class holds the results of introspecting the database table.
 * 
 * @author Jeff Butler
 */
public class ColumnDefinitions {
	
    private List primaryKeyColumns;
	private List baseColumns;
    private List blobColumns;
    private boolean hasJDBCDateColumns;
    private boolean hasJDBCTimeColumns;

	public ColumnDefinitions() {
		super();
		primaryKeyColumns = new ArrayList();
        baseColumns = new ArrayList();
        blobColumns = new ArrayList();
	}

	public List getBLOBColumns() {
        return blobColumns;
	}

	public List getBaseColumns() {
        return baseColumns;
	}
	
	public List getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public void addColumn(ColumnDefinition cd) {
        if (cd.isBLOBColumn()) {
            blobColumns.add(cd);
        } else {
            baseColumns.add(cd);
        }
        
        if (cd.isJDBCDateColumn()) {
            hasJDBCDateColumns = true;
        }
        
        if (cd.isJDBCTimeColumn()) {
            hasJDBCTimeColumns = true;
        }
	}

	public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator iter = baseColumns.iterator();
        while (iter.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition) iter.next();
            if (cd.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(cd);
                iter.remove();
                found = true;
                break;
            }
        }
        
        // search blob columns in the wierd event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition) iter.next();
                if (cd.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(cd);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }

	public boolean hasPrimaryKeyColumns() {
		return primaryKeyColumns.size() > 0;
	}

	public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
	}

	public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
	}
	
	public ColumnDefinition getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            // search primary key columns
            Iterator iter = primaryKeyColumns.iterator();
            while (iter.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition) iter.next();
                if (cd.isColumnNameDelimited()) {
                    if (cd.getActualColumnName().equals(columnName)) {
                        return cd;
                    }
                } else {
                    if (cd.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return cd;
                    }
                }
            }
            
            // search base columns
            iter = baseColumns.iterator();
            while (iter.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition) iter.next();
                if (cd.isColumnNameDelimited()) {
                    if (cd.getActualColumnName().equals(columnName)) {
                        return cd;
                    }
                } else {
                    if (cd.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return cd;
                    }
                }
            }

            // search bblob columns
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition) iter.next();
                if (cd.isColumnNameDelimited()) {
                    if (cd.getActualColumnName().equals(columnName)) {
                        return cd;
                    }
                } else {
                    if (cd.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return cd;
                    }
                }
            }
            
            return null;
        }
	}
    
    public boolean hasJDBCDateColumns() {
        return hasJDBCDateColumns;
    }
    
    public boolean hasJDBCTimeColumns() {
        return hasJDBCTimeColumns;
    }
    
    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0
            || baseColumns.size() > 0
            || blobColumns.size() > 0;
    }
}
