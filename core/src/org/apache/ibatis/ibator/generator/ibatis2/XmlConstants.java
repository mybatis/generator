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
package org.apache.ibatis.ibator.generator.ibatis2;

import org.apache.ibatis.ibator.config.MergeConstants;

/**
 * @author Jeff Butler
 */
public class XmlConstants {

    /**
     * Utility Class, no instances
     */
    private XmlConstants() {
        super();
    }
    
    static {
        StringBuilder sb = new StringBuilder();

        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("countByExample"); //$NON-NLS-1$
        COUNT_BY_EXAMPLE_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("deleteByExample"); //$NON-NLS-1$
        DELETE_BY_EXAMPLE_STATEMENT_ID = sb.toString();
        
        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("deleteByPrimaryKey"); //$NON-NLS-1$
        DELETE_BY_PRIMARY_KEY_STATEMENT_ID = sb.toString();
        
        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("insert"); //$NON-NLS-1$
        INSERT_STATEMENT_ID = sb.toString();
        
        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("insertSelective"); //$NON-NLS-1$
        INSERT_SELECTIVE_STATEMENT_ID = sb.toString();
        
        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("selectByExample"); //$NON-NLS-1$
        SELECT_BY_EXAMPLE_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("selectByExampleWithBLOBs"); //$NON-NLS-1$
        SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("selectByPrimaryKey"); //$NON-NLS-1$
        SELECT_BY_PRIMARY_KEY_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByExample"); //$NON-NLS-1$
        UPDATE_BY_EXAMPLE_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByExampleSelective"); //$NON-NLS-1$
        UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByExampleWithBLOBs"); //$NON-NLS-1$
        UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByPrimaryKey"); //$NON-NLS-1$
        UPDATE_BY_PRIMARY_KEY_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByPrimaryKeySelective"); //$NON-NLS-1$
        UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("updateByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("BaseResultMap"); //$NON-NLS-1$
        BASE_RESULT_MAP_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("ResultMapWithBLOBs"); //$NON-NLS-1$
        RESULT_MAP_WITH_BLOBS_ID = sb.toString();

        sb.setLength(0);
        sb.append(MergeConstants.NEW_XML_ELEMENT_PREFIX);
        sb.append("Example_Where_Clause"); //$NON-NLS-1$
        EXAMPLE_WHERE_CLAUSE_ID = sb.toString();
    }

    public static final String SQL_MAP_SYSTEM_ID = "http://ibatis.apache.org/dtd/sql-map-2.dtd"; //$NON-NLS-1$

    public static final String SQL_MAP_PUBLIC_ID = "-//ibatis.apache.org//DTD SQL Map 2.0//EN"; //$NON-NLS-1$
    
    public static final String SQL_MAP_CONFIG_SYSTEM_ID = "http://ibatis.apache.org/dtd/sql-map-config-2.dtd"; //$NON-NLS-1$

    public static final String SQL_MAP_CONFIG_PUBLIC_ID = "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN"; //$NON-NLS-1$
    
    public static final String IBATOR_CONFIG_SYSTEM_ID = "http://ibatis.apache.org/dtd/ibator-config_1_0.dtd"; //$NON-NLS-1$
    
    public static final String IBATOR_CONFIG_PUBLIC_ID = "-//Apache Software Foundation//DTD Apache iBATIS Ibator Configuration 1.0//EN"; //$NON-NLS-1$
    
    public static final String COUNT_BY_EXAMPLE_STATEMENT_ID;
    
    public static final String DELETE_BY_EXAMPLE_STATEMENT_ID;
    
    public static final String DELETE_BY_PRIMARY_KEY_STATEMENT_ID;
    
    public static final String INSERT_STATEMENT_ID;
    
    public static final String INSERT_SELECTIVE_STATEMENT_ID;
    
    public static final String SELECT_BY_EXAMPLE_STATEMENT_ID;
    
    public static final String SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID;
    
    public static final String SELECT_BY_PRIMARY_KEY_STATEMENT_ID;
    
    public static final String UPDATE_BY_EXAMPLE_STATEMENT_ID;

    public static final String UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID;

    public static final String UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID;

    public static final String UPDATE_BY_PRIMARY_KEY_STATEMENT_ID;
    
    public static final String UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID;

    public static final String UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID;

    public static final String BASE_RESULT_MAP_ID;

    public static final String RESULT_MAP_WITH_BLOBS_ID;
    
    public static final String EXAMPLE_WHERE_CLAUSE_ID;
}
