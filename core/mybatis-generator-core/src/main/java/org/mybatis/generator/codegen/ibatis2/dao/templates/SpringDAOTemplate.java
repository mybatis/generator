/**
 *    Copyright 2006-2016 the original author or authors.
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

/**
 * @author Jeff Butler
 */
public class SpringDAOTemplate extends AbstractDAOTemplate {

    /**
     *  
     */
    public SpringDAOTemplate() {
        super();
    }

    @Override
    protected void configureConstructorTemplate() {
        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("super();"); //$NON-NLS-1$
        setConstructorTemplate(method);
    }

    @Override
    protected void configureDeleteMethodTemplate() {
        setDeleteMethodTemplate("getSqlMapClientTemplate().delete(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureInsertMethodTemplate() {
        setInsertMethodTemplate("getSqlMapClientTemplate().insert(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureQueryForListMethodTemplate() {
        setQueryForListMethodTemplate("getSqlMapClientTemplate().queryForList(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureQueryForObjectMethodTemplate() {
        setQueryForObjectMethodTemplate("getSqlMapClientTemplate().queryForObject(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }

    @Override
    protected void configureSuperClass() {
        setSuperClass(new FullyQualifiedJavaType(
                "org.springframework.orm.ibatis.support.SqlMapClientDaoSupport")); //$NON-NLS-1$
    }

    @Override
    protected void configureUpdateMethodTemplate() {
        setUpdateMethodTemplate("getSqlMapClientTemplate().update(\"{0}.{1}\", {2});"); //$NON-NLS-1$
    }
}
