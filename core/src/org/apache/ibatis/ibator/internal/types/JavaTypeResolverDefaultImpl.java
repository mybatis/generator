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
package org.apache.ibatis.ibator.internal.types;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.JavaTypeResolver;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.config.PropertyRegistry;
import org.apache.ibatis.ibator.internal.util.StringUtility;

/**
 * 
 * @author Jeff Butler
 */
public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {

	protected List<String> warnings;
	
	protected Properties properties;
    
    protected IbatorContext ibatorContext;

	public JavaTypeResolverDefaultImpl() {
		super();
        properties = new Properties();
	}

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
    }

	/*
	 *  (non-Javadoc)
	 * @see org.apache.ibatis.ibator.api.JavaTypeResolver#initializeResolvedJavaType(org.apache.ibatis.ibator.internal.db.ColumnDefinition)
	 */
	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
		boolean forceBigDecimals = StringUtility.isTrue(properties
				.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));

        FullyQualifiedJavaType answer;
		switch (introspectedColumn.getJdbcType()) {
		case Types.ARRAY:
			answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.BIGINT:
		    answer = new FullyQualifiedJavaType(Long.class.getName());
			break;

		case Types.BINARY:
		    answer = new FullyQualifiedJavaType("byte[]"); //$NON-NLS-1$
			break;

		case Types.BIT:
		    answer = new FullyQualifiedJavaType(Boolean.class.getName());
			break;

		case Types.BLOB:
		    answer = new FullyQualifiedJavaType("byte[]"); //$NON-NLS-1$
			break;

		case Types.BOOLEAN:
		    answer = new FullyQualifiedJavaType(Boolean.class.getName());
			break;

		case Types.CHAR:
		    answer = new FullyQualifiedJavaType(String.class.getName());
			break;

		case Types.CLOB:
		    answer = new FullyQualifiedJavaType(String.class.getName());
			break;

		case Types.DATALINK:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.DATE:
		    answer = new FullyQualifiedJavaType(Date.class.getName());
			break;

		case Types.DECIMAL:
			if (introspectedColumn.getScale() > 0 || introspectedColumn.getLength() > 18 || forceBigDecimals) {
			    answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
			} else if (introspectedColumn.getLength() > 9) {
			    answer = new FullyQualifiedJavaType(Long.class.getName());
			} else if (introspectedColumn.getLength() > 4) {
			    answer = new FullyQualifiedJavaType(Integer.class.getName());
			} else {
			    answer = new FullyQualifiedJavaType(Short.class.getName());
			}
			break;

		case Types.DISTINCT:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.DOUBLE:
		    answer = new FullyQualifiedJavaType(Double.class.getName());
			break;

		case Types.FLOAT:
		    answer = new FullyQualifiedJavaType(Double.class.getName());
			break;

		case Types.INTEGER:
		    answer = new FullyQualifiedJavaType(Integer.class.getName());
			break;

		case Types.JAVA_OBJECT:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.LONGVARBINARY:
		    answer = new FullyQualifiedJavaType("byte[]"); //$NON-NLS-1$
			break;

		case Types.LONGVARCHAR:
		    answer = new FullyQualifiedJavaType(String.class.getName());
			break;

		case Types.NULL:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.NUMERIC:
			if (introspectedColumn.getScale() > 0 || introspectedColumn.getLength() > 18 || forceBigDecimals) {
			    answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
			} else if (introspectedColumn.getLength() > 9) {
			    answer = new FullyQualifiedJavaType(Long.class.getName());
			} else if (introspectedColumn.getLength() > 4) {
			    answer = new FullyQualifiedJavaType(Integer.class.getName());
			} else {
			    answer = new FullyQualifiedJavaType(Short.class.getName());
			}
			break;

		case Types.OTHER:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.REAL:
		    answer = new FullyQualifiedJavaType(Float.class.getName());
			break;

		case Types.REF:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.SMALLINT:
		    answer = new FullyQualifiedJavaType(Short.class.getName());
			break;

		case Types.STRUCT:
		    answer = new FullyQualifiedJavaType(Object.class.getName());
			break;

		case Types.TIME:
		    answer = new FullyQualifiedJavaType(Date.class.getName());
			break;

		case Types.TIMESTAMP:
		    answer = new FullyQualifiedJavaType(Date.class.getName());
			break;

		case Types.TINYINT:
		    answer = new FullyQualifiedJavaType(Byte.class.getName());
			break;

		case Types.VARBINARY:
		    answer = new FullyQualifiedJavaType("byte[]"); //$NON-NLS-1$
			break;

		case Types.VARCHAR:
		    answer = new FullyQualifiedJavaType(String.class.getName());
			break;

		default:
		    answer = null;
            break;
		}

        return answer;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.ibatis.ibator.api.JavaTypeResolver#setWarnings(java.util.List)
	 */
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

    public void setIbatorContext(IbatorContext ibatorContext) {
        this.ibatorContext = ibatorContext;
    }
}
