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
package org.mybatis.generator.internal.db;

/**
 * Typesafe enum of known database dialects
 * 
 * @author Jeff Butler
 */
public enum DatabaseDialects {

    DB2("VALUES IDENTITY_VAL_LOCAL()"), //$NON-NLS-1$
    MYSQL("SELECT LAST_INSERT_ID()"), //$NON-NLS-1$
    SQLSERVER("SELECT SCOPE_IDENTITY()"), //$NON-NLS-1$
    CLOUDSCAPE("VALUES IDENTITY_VAL_LOCAL()"), //$NON-NLS-1$
    DERBY("VALUES IDENTITY_VAL_LOCAL()"), //$NON-NLS-1$
    HSQLDB("CALL IDENTITY()"), //$NON-NLS-1$
    SYBASE("SELECT @@IDENTITY"), //$NON-NLS-1$
    DB2_MF("SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1"), //$NON-NLS-1$
    INFORMIX("select dbinfo('sqlca.sqlerrd1') from systables where tabid=1"); //$NON-NLS-1$

    private String identityRetrievalStatement;

    /**
     *  
     */
    private DatabaseDialects(String identityRetrievalStatement) {
        this.identityRetrievalStatement = identityRetrievalStatement;
    }

    public String getIdentityRetrievalStatement() {
        return identityRetrievalStatement;
    }

    /**
     * 
     * @param database
     * @return the database dialect for the selected database. May return null
     *         if there is no known dialect for the selected db
     */
    public static DatabaseDialects getDatabaseDialect(String database) {
        DatabaseDialects returnValue = null;

        if ("DB2".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = DB2;
        } else if ("MySQL".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = MYSQL;
        } else if ("SqlServer".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = SQLSERVER;
        } else if ("Cloudscape".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = CLOUDSCAPE;
        } else if ("Derby".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = DERBY;
        } else if ("HSQLDB".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = HSQLDB;
        } else if ("SYBASE".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = SYBASE;
        } else if ("DB2_MF".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = DB2_MF;
        } else if ("Informix".equalsIgnoreCase(database)) { //$NON-NLS-1$
            returnValue = INFORMIX;
        }

        return returnValue;
    }
}
