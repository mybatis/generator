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

    public static final String SQL_MAP_SYSTEM_ID = "http://ibatis.apache.org/dtd/sql-map-2.dtd"; //$NON-NLS-1$

    public static final String SQL_MAP_PUBLIC_ID = "-//ibatis.apache.org//DTD SQL Map 2.0//EN"; //$NON-NLS-1$
    
    public static final String ABATOR_CONFIG_SYSTEM_ID = "http://ibatis.apache.org/dtd/abator-config_1_0.dtd"; //$NON-NLS-1$
    
    public static final String ABATOR_CONFIG_PUBLIC_ID = "-//Apache Software Foundation//DTD Abator for iBATIS Configuration 1.0//EN"; //$NON-NLS-1$
}
