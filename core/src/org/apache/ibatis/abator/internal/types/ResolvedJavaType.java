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
package org.apache.ibatis.abator.internal.types;

import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;

/**
 * 
 * @author Jeff Butler
 */
public class ResolvedJavaType {
    private FullyQualifiedJavaType fullyQualifiedJavaType;

    private String jdbcTypeName;

    public ResolvedJavaType() {
        super();
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }
    /**
     * @return Returns the fullyQualifiedJavaType.
     */
    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return fullyQualifiedJavaType;
    }
    /**
     * @param fullyQualifiedJavaType The fullyQualifiedJavaType to set.
     */
    public void setFullyQualifiedJavaType(
            FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }
    
    public boolean isJDBCDate() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance())
            && "DATE".equalsIgnoreCase(jdbcTypeName); //$NON-NLS-1$
    }
    
    public boolean isJDBCTime() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance())
            && "TIME".equalsIgnoreCase(jdbcTypeName); //$NON-NLS-1$
    }
}
