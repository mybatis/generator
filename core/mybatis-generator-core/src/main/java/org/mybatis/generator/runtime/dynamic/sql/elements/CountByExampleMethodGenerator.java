/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;

public class CountByExampleMethodGenerator extends AbstractMethodGenerator {

    private CountByExampleMethodGenerator(Builder builder) {
        super(builder);
    }
    
    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!introspectedTable.getRules().generateCountByExample()) {
            return null;
        }
        
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.QueryExpressionDSL")); //$NON-NLS-1$
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter")); //$NON-NLS-1$
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder")); //$NON-NLS-1$
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSL")); //$NON-NLS-1$
        
        Method method = new Method("countByExample"); //$NON-NLS-1$
        method.setDefault(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>>"); //$NON-NLS-1$
        method.setReturnType(returnType);
        method.addBodyLine("return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())"); //$NON-NLS-1$
        method.addBodyLine("        .from(" + tableFieldName + ");"); //$NON-NLS-1$ //$NON-NLS-2$
        
        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder, CountByExampleMethodGenerator> {
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public CountByExampleMethodGenerator build() {
            return new CountByExampleMethodGenerator(this);
        }
    }
}
