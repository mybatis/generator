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

package org.apache.ibatis.abator.internal;

import org.apache.ibatis.abator.api.DAOMethodNameCalculator;
import org.apache.ibatis.abator.api.IntrospectedTable;
import org.apache.ibatis.abator.internal.rules.AbatorRules;

/**
 * @author Jeff Butler
 *
 */
public class ExtendedDAOMethodNameCalculator implements DAOMethodNameCalculator {

    /**
     * 
     */
    public ExtendedDAOMethodNameCalculator() {
        super();
    }

    public String getInsertMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        
        return sb.toString();
    }

    /**
     * 1. if this will be the only updateByPrimaryKey, then the
     *    result should be updateByPrimaryKey.
     * 2. If the other method is enabled, but there are seperate
     *    base and blob classes, then the method name should be
     *    updateByPrimaryKey
     * 3. Else the method name should be updateByPrimaryKeyWithoutBLOBs
     */
    public String getUpdateByPrimaryKeyWithoutBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();

        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (!rules.generateUpdateByPrimaryKeyWithBLOBs()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else {
            sb.append("ByPrimaryKeyWithoutBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    /**
     * 1. if this will be the only updateByPrimaryKey, then the
     *    result should be updateByPrimaryKey.
     * 2. If the other method is enabled, but there are seperate
     *    base and blob classes, then the method name should be
     *    updateByPrimaryKey
     * 3. Else the method name should be updateByPrimaryKeyWithBLOBs
     */
    public String getUpdateByPrimaryKeyWithBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (!rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else {
            sb.append("ByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    public String getDeleteByExampleMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$
        
        return sb.toString();
    }

    public String getDeleteByPrimaryKeyMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$
        
        return sb.toString();
    }

    /**
     * 1. if this will be the only selectByExample, then the
     *    result should be selectByExample.
     * 2. Else the method name should be selectByExampleWithoutBLOBs
     */
    public String getSelectByExampleWithoutBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (rules.generateSelectByExampleWithBLOBs()) {
            sb.append("WithoutBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    /**
     * 1. if this will be the only selectByExample, then the
     *    result should be selectByExample.
     * 2. Else the method name should be selectByExampleWithBLOBs
     */
    public String getSelectByExampleWithBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (rules.generateSelectByExampleWithoutBLOBs()) {
            sb.append("WithBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    public String getSelectByPrimaryKeyMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$
        
        return sb.toString();
    }


    public String getUpdateByPrimaryKeySelectiveMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByPrimaryKeySelective"); //$NON-NLS-1$
        
        return sb.toString();
    }

    public String getCountByExampleMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("count"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$
        
        return sb.toString();
    }

    public String getUpdateByExampleSelectiveMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        sb.append("ByExampleSelective"); //$NON-NLS-1$
        
        return sb.toString();
    }

    public String getUpdateByExampleWithBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (!rules.generateUpdateByExampleWithoutBLOBs()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else {
            sb.append("ByExampleWithBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    public String getUpdateByExampleWithoutBLOBsMethodName(IntrospectedTable introspectedTable) {
        StringBuffer sb = new StringBuffer();

        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getTable().getDomainObjectName());
        
        AbatorRules rules = introspectedTable.getRules();
        
        if (!rules.generateUpdateByExampleWithBLOBs()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else {
            sb.append("ByExampleWithoutBLOBs"); //$NON-NLS-1$
        }
        
        return sb.toString();
    }
}
