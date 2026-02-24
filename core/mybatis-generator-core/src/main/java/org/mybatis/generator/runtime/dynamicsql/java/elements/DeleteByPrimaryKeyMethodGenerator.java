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
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;
import org.mybatis.generator.runtime.JavaMethodParts;

public class DeleteByPrimaryKeyMethodGenerator extends AbstractJavaInterfaceMethodGenerator {

    private final FragmentGenerator fragmentGenerator;

    private DeleteByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateDeleteByPrimaryKeyForDSQL()) {
            return Optional.empty();
        }

        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Set<String> staticImports = new HashSet<>();

        staticImports.add("org.mybatis.dynamic.sql.SqlBuilder.isEqualTo"); //$NON-NLS-1$

        Method method = new Method("deleteByPrimaryKey"); //$NON-NLS-1$
        method.setDefault(true);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        method.addBodyLine("return delete(c -> "); //$NON-NLS-1$

        JavaMethodParts javaMethodParts = fragmentGenerator.getPrimaryKeyWhereClauseAndParameters();
        for (Parameter parameter : javaMethodParts.getParameters()) {
            method.addParameter(parameter);
        }
        method.addBodyLines(javaMethodParts.getBodyLines());
        imports.addAll(javaMethodParts.getImports());

        JavaMethodAndImports answer = JavaMethodAndImports.withMethod(method)
                .withImports(imports)
                .withStaticImports(staticImports)
                .build();

        return Optional.of(answer);
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FragmentGenerator fragmentGenerator;

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public DeleteByPrimaryKeyMethodGenerator build() {
            return new DeleteByPrimaryKeyMethodGenerator(this);
        }
    }
}
