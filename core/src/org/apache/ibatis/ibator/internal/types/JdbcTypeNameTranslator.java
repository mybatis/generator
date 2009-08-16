/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.apache.ibatis.ibator.internal.types;

import java.sql.Types;

/**
 * 
 * @author Jeff Butler
 */
public class JdbcTypeNameTranslator {

    /**
     * Utility Class - no instances
     */
	private JdbcTypeNameTranslator() {
		super();
	}

	/**
	 * Translates from a java.sql.Types values to the proper iBATIS
	 * string representation of the type. 
	 * 
	 * @param jdbcType a value from java.sql.Types
	 * @return the iBATIS String representation of a JDBC type
	 */
	public static String getJdbcTypeName(int jdbcType) {
	    String answer;
	    
		switch (jdbcType) {
		case Types.ARRAY:
			answer = "ARRAY"; //$NON-NLS-1$
			break;

		case Types.BIGINT:
		    answer = "BIGINT"; //$NON-NLS-1$
			break;

		case Types.BINARY:
		    answer = "BINARY"; //$NON-NLS-1$
			break;

		case Types.BIT:
		    answer = "BIT"; //$NON-NLS-1$
			break;

		case Types.BLOB:
		    answer = "BLOB"; //$NON-NLS-1$
			break;

		case Types.BOOLEAN:
		    answer = "BOOLEAN"; //$NON-NLS-1$
			break;

		case Types.CHAR:
		    answer = "CHAR"; //$NON-NLS-1$
			break;

		case Types.CLOB:
		    answer = "CLOB"; //$NON-NLS-1$
			break;

		case Types.DATALINK:
		    answer = "DATALINK"; //$NON-NLS-1$
			break;

		case Types.DATE:
		    answer = "DATE"; //$NON-NLS-1$
			break;

		case Types.DECIMAL:
		    answer = "DECIMAL"; //$NON-NLS-1$
			break;

		case Types.DISTINCT:
		    answer = "DISTINCT"; //$NON-NLS-1$
			break;

		case Types.DOUBLE:
		    answer = "DOUBLE"; //$NON-NLS-1$
			break;

		case Types.FLOAT:
		    answer = "FLOAT"; //$NON-NLS-1$
			break;

		case Types.INTEGER:
		    answer = "INTEGER"; //$NON-NLS-1$
			break;

		case Types.JAVA_OBJECT:
		    answer = "JAVA_OBJECT"; //$NON-NLS-1$
			break;

		case Types.LONGVARBINARY:
		    answer = "LONGVARBINARY"; //$NON-NLS-1$
			break;

		case Types.LONGVARCHAR:
		    answer = "LONGVARCHAR"; //$NON-NLS-1$
			break;

		case Types.NULL:
		    answer = "NULL"; //$NON-NLS-1$
			break;

		case Types.NUMERIC:
		    answer = "NUMERIC"; //$NON-NLS-1$
			break;

		case Types.OTHER:
		    answer = "OTHER"; //$NON-NLS-1$
			break;

		case Types.REAL:
		    answer = "REAL"; //$NON-NLS-1$
			break;

		case Types.REF:
		    answer = "REF"; //$NON-NLS-1$
			break;

		case Types.SMALLINT:
		    answer = "SMALLINT"; //$NON-NLS-1$
			break;

		case Types.STRUCT:
		    answer = "STRUCT"; //$NON-NLS-1$
			break;

		case Types.TIME:
		    answer = "TIME"; //$NON-NLS-1$
			break;

		case Types.TIMESTAMP:
		    answer = "TIMESTAMP"; //$NON-NLS-1$
			break;

		case Types.TINYINT:
		    answer = "TINYINT"; //$NON-NLS-1$
			break;

		case Types.VARBINARY:
		    answer = "VARBINARY"; //$NON-NLS-1$
			break;

		case Types.VARCHAR:
		    answer = "VARCHAR"; //$NON-NLS-1$
			break;

		default:
            answer = "OTHER"; //$NON-NLS-1$
            break;
		}

        return answer;
	}
}
