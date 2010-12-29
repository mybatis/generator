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

package org.mybatis.generator.config;

/**
 * This class holds constants for all properties recognized by the different
 * configuration elements. This helps document and maintain the different
 * properties, and helps to avoid spelling errors.
 * 
 * @author Jeff Butler
 * 
 */
public class PropertyRegistry {
    public static final String ANY_ENABLE_SUB_PACKAGES = "enableSubPackages"; //$NON-NLS-1$

    /**
     * recognized by table and java model generator
     */
    public static final String ANY_ROOT_CLASS = "rootClass"; //$NON-NLS-1$
    public static final String ANY_IMMUTABLE = "immutable"; //$NON-NLS-1$
    public static final String ANY_CONSTRUCTOR_BASED = "constructorBased"; //$NON-NLS-1$

    /**
     * recognized by table and java client generator
     */
    public static final String ANY_ROOT_INTERFACE = "rootInterface"; //$NON-NLS-1$

    public static final String TABLE_USE_COLUMN_INDEXES = "useColumnIndexes"; //$NON-NLS-1$
    public static final String TABLE_USE_ACTUAL_COLUMN_NAMES = "useActualColumnNames"; //$NON-NLS-1$
    public static final String TABLE_USE_COMPOUND_PROPERTY_NAMES = "useCompoundPropertyNames"; //$NON-NLS-1$
    public static final String TABLE_IGNORE_QUALIFIERS_AT_RUNTIME = "ignoreQualifiersAtRuntime"; //$NON-NLS-1$
    public static final String TABLE_RUNTIME_CATALOG = "runtimeCatalog"; //$NON-NLS-1$
    public static final String TABLE_RUNTIME_SCHEMA = "runtimeSchema"; //$NON-NLS-1$
    public static final String TABLE_RUNTIME_TABLE_NAME = "runtimeTableName"; //$NON-NLS-1$

    public static final String CONTEXT_BEGINNING_DELIMITER = "beginningDelimiter"; //$NON-NLS-1$
    public static final String CONTEXT_ENDING_DELIMITER = "endingDelimiter"; //$NON-NLS-1$
    public static final String CONTEXT_AUTO_DELIMIT_KEYWORDS = "autoDelimitKeywords"; //$NON-NLS-1$

    public static final String DAO_EXAMPLE_METHOD_VISIBILITY = "exampleMethodVisibility"; //$NON-NLS-1$
    public static final String DAO_METHOD_NAME_CALCULATOR = "methodNameCalculator"; //$NON-NLS-1$

    public static final String TYPE_RESOLVER_FORCE_BIG_DECIMALS = "forceBigDecimals"; //$NON-NLS-1$

    public static final String MODEL_GENERATOR_TRIM_STRINGS = "trimStrings"; //$NON-NLS-1$

    public static final String COMMENT_GENERATOR_SUPPRESS_DATE = "suppressDate"; //$NON-NLS-1$
    public static final String COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS = "suppressAllComments"; //$NON-NLS-1$
}
