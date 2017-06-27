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
package org.mybatis.generator.internal.types;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO - Delete Me
 * 
 * @author Jeff Butler
 */
public class JdbcTypeNameTranslator {

    private static Map<Integer, String> typeToName;
    private static Map<String, Integer> nameToType;

    static {
        typeToName = new HashMap<Integer, String>();
        typeToName.put(Types.ARRAY, "ARRAY"); //$NON-NLS-1$
        typeToName.put(Types.BIGINT, "BIGINT"); //$NON-NLS-1$
        typeToName.put(Types.BINARY, "BINARY"); //$NON-NLS-1$
        typeToName.put(Types.BIT, "BIT"); //$NON-NLS-1$
        typeToName.put(Types.BLOB, "BLOB"); //$NON-NLS-1$
        typeToName.put(Types.BOOLEAN, "BOOLEAN"); //$NON-NLS-1$
        typeToName.put(Types.CHAR, "CHAR"); //$NON-NLS-1$
        typeToName.put(Types.CLOB, "CLOB"); //$NON-NLS-1$
        typeToName.put(Types.DATALINK, "DATALINK"); //$NON-NLS-1$
        typeToName.put(Types.DATE, "DATE"); //$NON-NLS-1$
        typeToName.put(Types.DECIMAL, "DECIMAL"); //$NON-NLS-1$
        typeToName.put(Types.DISTINCT, "DISTINCT"); //$NON-NLS-1$
        typeToName.put(Types.DOUBLE, "DOUBLE"); //$NON-NLS-1$
        typeToName.put(Types.FLOAT, "FLOAT"); //$NON-NLS-1$
        typeToName.put(Types.INTEGER, "INTEGER"); //$NON-NLS-1$
        typeToName.put(Types.JAVA_OBJECT, "JAVA_OBJECT"); //$NON-NLS-1$
        typeToName.put(Types.LONGVARBINARY, "LONGVARBINARY"); //$NON-NLS-1$
        typeToName.put(Types.LONGVARCHAR, "LONGVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NCHAR, "NCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NCLOB, "NCLOB"); //$NON-NLS-1$
        typeToName.put(Types.NVARCHAR, "NVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.LONGNVARCHAR, "LONGNVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NULL, "NULL"); //$NON-NLS-1$
        typeToName.put(Types.NUMERIC, "NUMERIC"); //$NON-NLS-1$
        typeToName.put(Types.OTHER, "OTHER"); //$NON-NLS-1$
        typeToName.put(Types.REAL, "REAL"); //$NON-NLS-1$
        typeToName.put(Types.REF, "REF"); //$NON-NLS-1$
        typeToName.put(Types.SMALLINT, "SMALLINT"); //$NON-NLS-1$
        typeToName.put(Types.STRUCT, "STRUCT"); //$NON-NLS-1$
        typeToName.put(Types.TIME, "TIME"); //$NON-NLS-1$
        typeToName.put(Types.TIMESTAMP, "TIMESTAMP"); //$NON-NLS-1$
        typeToName.put(Types.TINYINT, "TINYINT"); //$NON-NLS-1$
        typeToName.put(Types.VARBINARY, "VARBINARY"); //$NON-NLS-1$
        typeToName.put(Types.VARCHAR, "VARCHAR"); //$NON-NLS-1$

        nameToType = new HashMap<String, Integer>();
        nameToType.put("ARRAY", Types.ARRAY); //$NON-NLS-1$
        nameToType.put("BIGINT", Types.BIGINT); //$NON-NLS-1$
        nameToType.put("BINARY", Types.BINARY); //$NON-NLS-1$
        nameToType.put("BIT", Types.BIT); //$NON-NLS-1$
        nameToType.put("BLOB", Types.BLOB); //$NON-NLS-1$
        nameToType.put("BOOLEAN", Types.BOOLEAN); //$NON-NLS-1$
        nameToType.put("CHAR", Types.CHAR); //$NON-NLS-1$
        nameToType.put("CLOB", Types.CLOB); //$NON-NLS-1$
        nameToType.put("DATALINK", Types.DATALINK); //$NON-NLS-1$
        nameToType.put("DATE", Types.DATE); //$NON-NLS-1$
        nameToType.put("DECIMAL", Types.DECIMAL); //$NON-NLS-1$
        nameToType.put("DISTINCT", Types.DISTINCT); //$NON-NLS-1$
        nameToType.put("DOUBLE", Types.DOUBLE); //$NON-NLS-1$
        nameToType.put("FLOAT", Types.FLOAT); //$NON-NLS-1$
        nameToType.put("INTEGER", Types.INTEGER); //$NON-NLS-1$
        nameToType.put("JAVA_OBJECT", Types.JAVA_OBJECT); //$NON-NLS-1$
        nameToType.put("LONGVARBINARY", Types.LONGVARBINARY); //$NON-NLS-1$
        nameToType.put("LONGVARCHAR", Types.LONGVARCHAR); //$NON-NLS-1$
        nameToType.put("NCHAR", Types.NCHAR); //$NON-NLS-1$
        nameToType.put("NCLOB", Types.NCLOB); //$NON-NLS-1$
        nameToType.put("NVARCHAR", Types.NVARCHAR); //$NON-NLS-1$
        nameToType.put("LONGNVARCHAR", Types.LONGNVARCHAR); //$NON-NLS-1$
        nameToType.put("NULL", Types.NULL); //$NON-NLS-1$
        nameToType.put("NUMERIC", Types.NUMERIC); //$NON-NLS-1$
        nameToType.put("OTHER", Types.OTHER); //$NON-NLS-1$
        nameToType.put("REAL", Types.REAL); //$NON-NLS-1$
        nameToType.put("REF", Types.REF); //$NON-NLS-1$
        nameToType.put("SMALLINT", Types.SMALLINT); //$NON-NLS-1$
        nameToType.put("STRUCT", Types.STRUCT); //$NON-NLS-1$
        nameToType.put("TIME", Types.TIME); //$NON-NLS-1$
        nameToType.put("TIMESTAMP", Types.TIMESTAMP); //$NON-NLS-1$
        nameToType.put("TINYINT", Types.TINYINT); //$NON-NLS-1$
        nameToType.put("VARBINARY", Types.VARBINARY); //$NON-NLS-1$
        nameToType.put("VARCHAR", Types.VARCHAR); //$NON-NLS-1$
    }

    /**
     * Utility Class - no instances
     */
    private JdbcTypeNameTranslator() {
        super();
    }

    /**
     * Translates from a java.sql.Types values to the proper iBATIS string
     * representation of the type.
     * 
     * @param jdbcType
     *            a value from java.sql.Types
     * @return the iBATIS String representation of a JDBC type
     */
    public static String getJdbcTypeName(int jdbcType) {
        String answer = typeToName.get(jdbcType);
        if (answer == null) {
            answer = "OTHER"; //$NON-NLS-1$
        }

        return answer;
    }

    /**
     * Translates from the string representation of the type to the
     * java.sql.Types value.
     * 
     * @param jdbcTypeName
     *            the iBATIS String representation of a JDBC type
     * @return a value from java.sql.Types
     */
    public static int getJdbcType(String jdbcTypeName) {
        Integer answer = nameToType.get(jdbcTypeName);
        if (answer == null) {
            answer = Types.OTHER;
        }

        return answer;
    }
}
