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

package org.apache.ibatis.abator.config;

/**
 * @author Jeff Butler
 * 
 */
public abstract class GeneratorSet {
    protected String javaModelGeneratorType;

    protected String sqlMapGeneratorType;

    protected String javaTypeResolverType;

    /**
     * 
     */
    public GeneratorSet() {
        super();
    }

    /**
     * This method is used to translate the configuration nicknames for the
     * different types of DAO generators into the actual implementation
     * class names.  Typically, the method should handle translation
     * of the special values IBATIS, SPRING, GENERIC-SI and GENERIC-CI.
     * 
     * @param configurationType
     * @return the fully qualified class name of the correct implementation class
     */
    public abstract String translateDAOGeneratorType(String configurationType);

    public String getJavaModelGeneratorType() {
        return javaModelGeneratorType;
    }

    public void setJavaModelGeneratorType(String javaModelGeneratorType) {
        if (!"DEFAULT".equalsIgnoreCase(javaModelGeneratorType)) { //$NON-NLS-1$
            this.javaModelGeneratorType = javaModelGeneratorType;
        }
    }

    public String getJavaTypeResolverType() {
        return javaTypeResolverType;
    }

    public void setJavaTypeResolverType(String javaTypeResolverType) {
        if (!"DEFAULT".equalsIgnoreCase(javaTypeResolverType)) { //$NON-NLS-1$
            this.javaTypeResolverType = javaTypeResolverType;
        }
    }

    public String getSqlMapGeneratorType() {
        return sqlMapGeneratorType;
    }

    public void setSqlMapGeneratorType(String sqlMapGeneratorType) {
        if (!"DEFAULT".equalsIgnoreCase(sqlMapGeneratorType)) { //$NON-NLS-1$
            this.sqlMapGeneratorType = sqlMapGeneratorType;
        }
    }
}
