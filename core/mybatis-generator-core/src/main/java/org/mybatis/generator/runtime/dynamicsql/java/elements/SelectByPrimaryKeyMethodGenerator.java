/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.runtime.dynamicsql.java.elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class SelectByPrimaryKeyMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final FullyQualifiedJavaType recordType;
    private final FragmentGenerator fragmentGenerator;

    private SelectByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateSelectByPrimaryKeyForDSQL()) {
            return Optional.empty();
        }

        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.util.Optional"); //$NON-NLS-1$
        returnType.addTypeArgument(recordType);
        imports.add(returnType);

        Method method = new Method("selectByPrimaryKey"); //$NON-NLS-1$
        method.setDefault(true);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(returnType);

        method.addBodyLine("return selectOne(c ->"); //$NON-NLS-1$

        return JavaMethodAndImports.withMethod(method)
                .withStaticImport("org.mybatis.dynamic.sql.SqlBuilder.isEqualTo") //$NON-NLS-1$
                .withImports(imports)
                .withExtraMethodParts(fragmentGenerator.getPrimaryKeyWhereClauseAndParameters())
                .buildOptional();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FullyQualifiedJavaType recordType;
        private @Nullable FragmentGenerator fragmentGenerator;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
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

        public SelectByPrimaryKeyMethodGenerator build() {
            return new SelectByPrimaryKeyMethodGenerator(this);
        }
    }
}
