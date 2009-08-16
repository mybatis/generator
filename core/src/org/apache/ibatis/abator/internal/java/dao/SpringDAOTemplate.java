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
package org.apache.ibatis.abator.internal.java.dao;

import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.java.JavaVisibility;
import org.apache.ibatis.abator.api.dom.java.Method;

/**
 * @author Jeff Butler
 */
public class SpringDAOTemplate extends AbstractDAOTemplate {

    /**
     *  
     */
    public SpringDAOTemplate() {
        super();

        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("super();"); //$NON-NLS-1$
        setConstructorTemplate(method);

        setSuperClass(new FullyQualifiedJavaType(
                "org.springframework.orm.ibatis.support.SqlMapClientDaoSupport")); //$NON-NLS-1$

        setDeleteMethodTemplate("getSqlMapClientTemplate().delete(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setInsertMethodTemplate("getSqlMapClientTemplate().insert(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForObjectMethodTemplate("getSqlMapClientTemplate().queryForObject(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForListMethodTemplate("getSqlMapClientTemplate().queryForList(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setUpdateMethodTemplate("getSqlMapClientTemplate().update(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }
}
