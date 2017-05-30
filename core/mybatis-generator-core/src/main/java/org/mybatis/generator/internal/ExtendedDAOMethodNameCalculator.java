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
package org.mybatis.generator.internal;

import org.mybatis.generator.api.DAOMethodNameCalculator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.rules.Rules;

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

    @Override
    public String getInsertMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        return sb.toString();
    }

    /**
     * 1. if this will be the only updateByPrimaryKey, then the result should be
     * updateByPrimaryKey. 2. If the other method is enabled, but there are
     * seperate base and blob classes, then the method name should be
     * updateByPrimaryKey 3. Else the method name should be
     * updateByPrimaryKeyWithoutBLOBs
     */
    @Override
    public String getUpdateByPrimaryKeyWithoutBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();

        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

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
     * 1. if this will be the only updateByPrimaryKey, then the result should be
     * updateByPrimaryKey. 2. If the other method is enabled, but there are
     * seperate base and blob classes, then the method name should be
     * updateByPrimaryKey 3. Else the method name should be
     * updateByPrimaryKeyWithBLOBs
     */
    @Override
    public String getUpdateByPrimaryKeyWithBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

        if (!rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else {
            sb.append("ByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    @Override
    public String getDeleteByExampleMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$

        return sb.toString();
    }

    @Override
    public String getDeleteByPrimaryKeyMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$

        return sb.toString();
    }

    /**
     * 1. if this will be the only selectByExample, then the result should be
     * selectByExample. 2. Else the method name should be
     * selectByExampleWithoutBLOBs
     */
    @Override
    public String getSelectByExampleWithoutBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$

        Rules rules = introspectedTable.getRules();

        if (rules.generateSelectByExampleWithBLOBs()) {
            sb.append("WithoutBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    /**
     * 1. if this will be the only selectByExample, then the result should be
     * selectByExample. 2. Else the method name should be
     * selectByExampleWithBLOBs
     */
    @Override
    public String getSelectByExampleWithBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$

        Rules rules = introspectedTable.getRules();

        if (rules.generateSelectByExampleWithoutBLOBs()) {
            sb.append("WithBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    @Override
    public String getSelectByPrimaryKeyMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$

        return sb.toString();
    }

    @Override
    public String getUpdateByPrimaryKeySelectiveMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKeySelective"); //$NON-NLS-1$

        return sb.toString();
    }

    @Override
    public String getCountByExampleMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("count"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByExample"); //$NON-NLS-1$

        return sb.toString();
    }

    @Override
    public String getUpdateByExampleSelectiveMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByExampleSelective"); //$NON-NLS-1$

        return sb.toString();
    }

    @Override
    public String getUpdateByExampleWithBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

        if (!rules.generateUpdateByExampleWithoutBLOBs()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else {
            sb.append("ByExampleWithBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    @Override
    public String getUpdateByExampleWithoutBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();

        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

        if (!rules.generateUpdateByExampleWithBLOBs()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByExample"); //$NON-NLS-1$
        } else {
            sb.append("ByExampleWithoutBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    @Override
    public String getInsertSelectiveMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("Selective"); //$NON-NLS-1$

        return sb.toString();
    }
}
