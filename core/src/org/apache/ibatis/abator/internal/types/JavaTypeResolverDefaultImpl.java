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

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.abator.api.JavaTypeResolver;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.config.PropertyRegistry;
import org.apache.ibatis.abator.exception.UnsupportedDataTypeException;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;

/**
 * 
 * @author Jeff Butler
 */
public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {

	protected List warnings;
	
	protected Properties properties;
    
    protected AbatorContext abatorContext;

	public JavaTypeResolverDefaultImpl() {
		super();
        properties = new Properties();
	}

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
    }

	/*
	 *  (non-Javadoc)
	 * @see org.apache.ibatis.abator.api.JavaTypeResolver#initializeResolvedJavaType(org.apache.ibatis.abator.internal.db.ColumnDefinition)
	 */
	public void initializeResolvedJavaType(ColumnDefinition cd)
			throws UnsupportedDataTypeException {
		boolean forceBigDecimals = "true".equalsIgnoreCase(properties //$NON-NLS-1$
				.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));

		ResolvedJavaType type = new ResolvedJavaType();

		switch (cd.getJdbcType()) {
		case Types.ARRAY:
			type.setJdbcTypeName("ARRAY"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.BIGINT:
			type.setJdbcTypeName("BIGINT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Long.class.getName()));
			break;

		case Types.BINARY:
			type.setJdbcTypeName("BINARY"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType("byte[]")); //$NON-NLS-1$
			break;

		case Types.BIT:
			type.setJdbcTypeName("BIT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Boolean.class.getName()));
			break;

		case Types.BLOB:
			type.setJdbcTypeName("BLOB"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType("byte[]")); //$NON-NLS-1$
			break;

		case Types.BOOLEAN:
			type.setJdbcTypeName("BOOLEAN"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Boolean.class.getName()));
			break;

		case Types.CHAR:
			type.setJdbcTypeName("CHAR"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(String.class.getName()));
			break;

		case Types.CLOB:
			type.setJdbcTypeName("CLOB"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(String.class.getName()));
			break;

		case Types.DATALINK:
			type.setJdbcTypeName("DATALINK"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.DATE:
			type.setJdbcTypeName("DATE"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Date.class.getName()));
			break;

		case Types.DECIMAL:
			type.setJdbcTypeName("DECIMAL"); //$NON-NLS-1$
			if (cd.getScale() > 0 || cd.getLength() > 18 || forceBigDecimals) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(BigDecimal.class.getName()));
			} else if (cd.getLength() > 9) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Long.class.getName()));
			} else if (cd.getLength() > 4) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Integer.class.getName()));
			} else {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Short.class.getName()));
			}
			break;

		case Types.DISTINCT:
			type.setJdbcTypeName("DISTINCT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.DOUBLE:
			type.setJdbcTypeName("DOUBLE"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Double.class.getName()));
			break;

		case Types.FLOAT:
			type.setJdbcTypeName("FLOAT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Double.class.getName()));
			break;

		case Types.INTEGER:
			type.setJdbcTypeName("INTEGER"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Integer.class.getName()));
			break;

		case Types.JAVA_OBJECT:
			type.setJdbcTypeName("JAVA_OBJECT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.LONGVARBINARY:
			type.setJdbcTypeName("LONGVARBINARY"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType("byte[]")); //$NON-NLS-1$
			break;

		case Types.LONGVARCHAR:
			type.setJdbcTypeName("LONGVARCHAR"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(String.class.getName()));
			break;

		case Types.NULL:
			type.setJdbcTypeName("NULL"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.NUMERIC:
			type.setJdbcTypeName("NUMERIC"); //$NON-NLS-1$
			if (cd.getScale() > 0 || cd.getLength() > 18 || forceBigDecimals) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(BigDecimal.class.getName()));
			} else if (cd.getLength() > 9) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Long.class.getName()));
			} else if (cd.getLength() > 4) {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Integer.class.getName()));
			} else {
				type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Short.class.getName()));
			}
			break;

		case Types.OTHER:
			type.setJdbcTypeName("OTHER"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.REAL:
			type.setJdbcTypeName("REAL"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Float.class.getName()));
			break;

		case Types.REF:
			type.setJdbcTypeName("REF"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.SMALLINT:
			type.setJdbcTypeName("SMALLINT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Short.class.getName()));
			break;

		case Types.STRUCT:
			type.setJdbcTypeName("STRUCT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Object.class.getName()));
			break;

		case Types.TIME:
			type.setJdbcTypeName("TIME"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Date.class.getName()));
			break;

		case Types.TIMESTAMP:
			type.setJdbcTypeName("TIMESTAMP"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Date.class.getName()));
			break;

		case Types.TINYINT:
			type.setJdbcTypeName("TINYINT"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Byte.class.getName()));
			break;

		case Types.VARBINARY:
			type.setJdbcTypeName("VARBINARY"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType("byte[]")); //$NON-NLS-1$
			break;

		case Types.VARCHAR:
			type.setJdbcTypeName("VARCHAR"); //$NON-NLS-1$
			type.setFullyQualifiedJavaType(new FullyQualifiedJavaType(String.class.getName()));
			break;

		default:
			throw new UnsupportedDataTypeException();
		}

		cd.setResolvedJavaType(type);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.ibatis.abator.api.JavaTypeResolver#setWarnings(java.util.List)
	 */
	public void setWarnings(List warnings) {
		this.warnings = warnings;
	}

    public void setAbatorContext(AbatorContext abatorContext) {
        this.abatorContext = abatorContext;
    }
}
