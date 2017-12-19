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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

public class DeleteByPrimaryKeyMethodGenerator extends AbstractMethodGenerator {
    
    private String tableFieldName;
    private FragmentGenerator fragmentGenerator;
    
    private DeleteByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        tableFieldName = builder.tableFieldName;
        fragmentGenerator = builder.fragmentGenerator;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            return null;
        }

        Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        Set<String> staticImports = new HashSet<String>();
        
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.delete.DeleteDSL")); //$NON-NLS-1$
        staticImports.add("org.mybatis.dynamic.sql.SqlBuilder.*"); //$NON-NLS-1$
        
        Method method = new Method("deleteByPrimaryKey"); //$NON-NLS-1$
        method.setDefault(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        
        method.addBodyLine("return DeleteDSL.deleteFromWithMapper(this::delete, " + tableFieldName + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        
        MethodParts methodParts = fragmentGenerator.getPrimaryKeyWhereClauseAndParameters();
        for (Parameter parameter : methodParts.getParameters()) {
            method.addParameter(parameter);
        }
        method.addBodyLines(methodParts.getBodyLines());
        imports.addAll(methodParts.getImports());
        
        method.addBodyLine("        .build()"); //$NON-NLS-1$
        method.addBodyLine("        .execute();"); //$NON-NLS-1$
        
        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .withStaticImports(staticImports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder, DeleteByPrimaryKeyMethodGenerator> {
        
        private String tableFieldName;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }
        
        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public DeleteByPrimaryKeyMethodGenerator build() {
            return new DeleteByPrimaryKeyMethodGenerator(this);
        }
    }
}
