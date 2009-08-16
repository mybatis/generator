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
package org.apache.ibatis.abator.internal.sqlmap;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.abator.internal.db.ColumnDefinition;
import org.apache.ibatis.abator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 */
public class ExampleClause {
	private static final int IS_NULL_CLAUSE_ID = 1;

	private static final int IS_NOT_NULL_CLAUSE_ID = 2;

	private static final int EQUALS_CLAUSE_ID = 3;

	private static final int NOT_EQUALS_CLAUSE_ID = 4;

	private static final int GREATER_THAN_CLAUSE_ID = 5;

	private static final int GREATER_THAN_OR_EQUAL_CLAUSE_ID = 6;

	private static final int LESS_THAN_CLAUSE_ID = 7;

	private static final int LESS_THAN_OR_EQUAL_CLAUSE_ID = 8;

	private static final int LIKE_CLAUSE_ID = 9;

	private static final List clauses;

	private String selectorProperty;

	private String clause;

	private boolean propertyInMapRequired;

	private boolean characterOnly;

	private String examplePropertyName;

	private int examplePropertyValue;

	static {
		List list = new ArrayList();

		list.add(new ExampleClause("{0}_{1}_NULL", //$NON-NLS-1$ 
				"{0} is null", //$NON-NLS-1$
				false, false, "EXAMPLE_NULL", IS_NULL_CLAUSE_ID)); //$NON-NLS-1$
		list.add(new ExampleClause("{0}_{1}_NOT_NULL", //$NON-NLS-1$
				"{0} is not null", false, false, "EXAMPLE_NOT_NULL", //$NON-NLS-1$  //$NON-NLS-2$
				IS_NOT_NULL_CLAUSE_ID));
		list.add(new ExampleClause("{0}_{1}_EQUALS", "{0} = {1}", //$NON-NLS-1$  //$NON-NLS-2$
				true, false, "EXAMPLE_EQUALS", EQUALS_CLAUSE_ID)); //$NON-NLS-1$
		list
				.add(new ExampleClause(
						"{0}_{1}_NOT_EQUALS", //$NON-NLS-1$
						"{0} <![CDATA[ <> ]]> {1}", true, false, "EXAMPLE_NOT_EQUALS", //$NON-NLS-1$ //$NON-NLS-2$
						NOT_EQUALS_CLAUSE_ID));
		list
				.add(new ExampleClause(
						"{0}_{1}_GT", //$NON-NLS-1$
						"{0} <![CDATA[ > ]]> {1}", true, false, "EXAMPLE_GREATER_THAN", //$NON-NLS-1$ //$NON-NLS-2$
						GREATER_THAN_CLAUSE_ID));
		list
				.add(new ExampleClause(
						"{0}_{1}_GE", //$NON-NLS-1$
						"{0} <![CDATA[ >= ]]> {1}", true, false, //$NON-NLS-1$
						"EXAMPLE_GREATER_THAN_OR_EQUAL", GREATER_THAN_OR_EQUAL_CLAUSE_ID)); //$NON-NLS-1$
		list.add(new ExampleClause("{0}_{1}_LT", //$NON-NLS-1$
				"{0} <![CDATA[ < ]]> {1}", true, false, "EXAMPLE_LESS_THAN", //$NON-NLS-1$ //$NON-NLS-2$
				LESS_THAN_CLAUSE_ID));
		list.add(new ExampleClause("{0}_{1}_LE", //$NON-NLS-1$
				"{0} <![CDATA[ <= ]]> {1}", true, false, //$NON-NLS-1$
				"EXAMPLE_LESS_THAN_OR_EQUAL", LESS_THAN_OR_EQUAL_CLAUSE_ID)); //$NON-NLS-1$
		list.add(new ExampleClause("{0}_{1}_LIKE", "{0} like {1}", //$NON-NLS-1$ //$NON-NLS-2$
				true, true, "EXAMPLE_LIKE", LIKE_CLAUSE_ID)); //$NON-NLS-1$

		clauses = Collections.unmodifiableList(list);
	}

	public static Iterator getAllExampleClauses() {
		return clauses.iterator();
	}

	/**
	 *  
	 */
	private ExampleClause(String selectorProperty, String clause,
			boolean propertyInMapRequired, boolean characterOnly,
			String examplePropertyName, int examplePropertyValue) {
		super();
		this.selectorProperty = selectorProperty;
		this.clause = clause;
		this.propertyInMapRequired = propertyInMapRequired;
		this.characterOnly = characterOnly;
		this.examplePropertyName = examplePropertyName;
		this.examplePropertyValue = examplePropertyValue;
	}

	public String getSelectorAndProperty(ColumnDefinition cd, boolean forJava) {
		Object[] arguments = { "AND", cd.getEscapedColumnName() }; //$NON-NLS-1$
        
        String s = MessageFormat.format(selectorProperty, arguments);

        if (forJava) {
            s = StringUtility.escapeStringForJava(s);
        } else {
            s = StringUtility.escapeStringForXml(s);
        }
        
		return s;
	}

	public String getSelectorOrProperty(ColumnDefinition cd, boolean forJava) {
		Object[] arguments = { "OR", cd.getEscapedColumnName() }; //$NON-NLS-1$

        String s = MessageFormat.format(selectorProperty, arguments);

        if (forJava) {
            s = StringUtility.escapeStringForJava(s);
        } else {
            s = StringUtility.escapeStringForXml(s);
        }
        
        return s;
	}

	public String getClause(ColumnDefinition cd) {
		Object[] arguments = { cd.getAliasedEscapedColumnName(), cd.getIbatisFormattedParameterClause() };

		return MessageFormat.format(clause, arguments);
	}

	public boolean isCharacterOnly() {
		return characterOnly;
	}

	public boolean isPropertyInMapRequired() {
		return propertyInMapRequired;
	}

	public String getExamplePropertyName() {
		return examplePropertyName;
	}

	public int getExamplePropertyValue() {
		return examplePropertyValue;
	}
}
