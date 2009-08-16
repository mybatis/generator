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

import org.apache.ibatis.abator.api.dom.java.Field;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.java.JavaVisibility;
import org.apache.ibatis.abator.api.dom.java.Method;
import org.apache.ibatis.abator.api.dom.java.Parameter;

/**
 * @author Jeff Butler
 */
public class GenericCIDAOTemplate extends AbstractDAOTemplate {

    /**
     *  
     */
    public GenericCIDAOTemplate() {
        super();

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("com.ibatis.sqlmap.client.SqlMapClient"); //$NON-NLS-1$
        
        Method constructor = new Method();
        constructor.setConstructor(true);
        constructor.setVisibility(JavaVisibility.PUBLIC);
        constructor.addParameter(new Parameter(fqjt, "sqlMapClient")); //$NON-NLS-1$
        constructor.addBodyLine("super();"); //$NON-NLS-1$
        constructor.addBodyLine("this.sqlMapClient = sqlMapClient;"); //$NON-NLS-1$
        setConstructorTemplate(constructor);

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName("sqlMapClient"); //$NON-NLS-1$
        addField(field);

        addImplementationImport(fqjt);

        addCheckedException(new FullyQualifiedJavaType("java.sql.SQLException")); //$NON-NLS-1$
        setDeleteMethodTemplate("sqlMapClient.delete(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setInsertMethodTemplate("sqlMapClient.insert(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForObjectMethodTemplate("sqlMapClient.queryForObject(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setQueryForListMethodTemplate("sqlMapClient.queryForList(\"{0}.{1}\", {2});"); //$NON-NLS-1$
        setUpdateMethodTemplate("sqlMapClient.update(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }
}
