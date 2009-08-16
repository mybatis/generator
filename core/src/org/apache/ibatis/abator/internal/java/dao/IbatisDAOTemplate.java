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
import org.apache.ibatis.abator.api.dom.java.Parameter;

/**
 * @author Jeff Butler
 */
public class IbatisDAOTemplate extends AbstractDAOTemplate {

    /**
     *  
     */
    public IbatisDAOTemplate() {
        super();

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
        	"com.ibatis.dao.client.DaoManager"); //$NON-NLS-1$

        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "daoManager")); //$NON-NLS-1$
        method.addBodyLine("super(daoManager);"); //$NON-NLS-1$
        setConstructorTemplate(method);

        setSuperClass(new FullyQualifiedJavaType(
                "com.ibatis.dao.client.template.SqlMapDaoTemplate")); //$NON-NLS-1$

        addImplementationImport(fqjt);

        setDeleteMethodTemplate("delete(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setInsertMethodTemplate("insert(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForObjectMethodTemplate("queryForObject(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForListMethodTemplate("queryForList(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setUpdateMethodTemplate("update(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }
}
