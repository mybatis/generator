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
package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * Template for DAO classes created for the iBatis DAO library.
 * 
 * @author Jeff Butler
 */
public class IbatisDAOTemplate extends AbstractDAOTemplate {

    private FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
            "com.ibatis.dao.client.DaoManager"); //$NON-NLS-1$

    public IbatisDAOTemplate() {
        super();
    }

    @Override
    protected void configureConstructorTemplate() {
        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "daoManager")); //$NON-NLS-1$
        method.addBodyLine("super(daoManager);"); //$NON-NLS-1$
        setConstructorTemplate(method);
    }

    @Override
    protected void configureDeleteMethodTemplate() {
        setDeleteMethodTemplate("delete(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureImplementationImports() {
        addImplementationImport(fqjt);
    }

    @Override
    protected void configureInsertMethodTemplate() {
        setInsertMethodTemplate("insert(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureQueryForListMethodTemplate() {
        setQueryForListMethodTemplate("queryForList(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureQueryForObjectMethodTemplate() {
        setQueryForObjectMethodTemplate("queryForObject(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureSuperClass() {
        setSuperClass(new FullyQualifiedJavaType(
                "com.ibatis.dao.client.template.SqlMapDaoTemplate")); //$NON-NLS-1$
    }

    @Override
    protected void configureUpdateMethodTemplate() {
        setUpdateMethodTemplate("update(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }
}
