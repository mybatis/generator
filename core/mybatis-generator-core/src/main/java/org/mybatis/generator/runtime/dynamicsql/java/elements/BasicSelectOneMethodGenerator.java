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

public class BasicSelectOneMethodGenerator extends AbstractJavaInterfaceMethodGenerator {

    private final FullyQualifiedJavaType recordType;
    private final String resultMapId;
    private final FragmentGenerator fragmentGenerator;
    private final boolean reuseResultMap;

    private BasicSelectOneMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        resultMapId = Objects.requireNonNull(builder.resultMapId);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
        reuseResultMap = builder.reuseResultMap;
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterType =
                new FullyQualifiedJavaType(
                        "org.mybatis.dynamic.sql.select.render.SelectStatementProvider"); //$NON-NLS-1$
        imports.add(parameterType);

        FullyQualifiedJavaType adapter =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter"); //$NON-NLS-1$
        imports.add(adapter);

        FullyQualifiedJavaType annotation =
                new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider"); //$NON-NLS-1$
        imports.add(annotation);

        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.util.Optional"); //$NON-NLS-1$
        returnType.addTypeArgument(recordType);

        imports.add(returnType);

        Method method = new Method("selectOne"); //$NON-NLS-1$
        method.setAbstract(true);

        imports.add(recordType);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(parameterType, "selectStatement")); //$NON-NLS-1$
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")"); //$NON-NLS-1$

        JavaMethodAndImports.Builder builder = JavaMethodAndImports.withMethod(method)
                .withImports(imports);

        if (reuseResultMap) {
            FullyQualifiedJavaType rmAnnotation =
                    new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap"); //$NON-NLS-1$
            builder.withImport(rmAnnotation);
            method.addAnnotation("@ResultMap(\"" + resultMapId + "\")"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            JavaMethodParts javaMethodParts;
            if (introspectedTable.isConstructorBased() || introspectedTable.isRecordBased()) {
                javaMethodParts = fragmentGenerator.getAnnotatedConstructorArgs();
            } else {
                javaMethodParts = fragmentGenerator.getAnnotatedResults();
            }
            builder.withExtraMethodParts(javaMethodParts);
        }

        return builder.buildOptional();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientBasicSelectOneMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FullyQualifiedJavaType recordType;
        private @Nullable String resultMapId;
        private @Nullable FragmentGenerator fragmentGenerator;
        private boolean reuseResultMap;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withResultMapId(String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        public Builder withReuseResultMap(boolean reuseResultMap) {
            this.reuseResultMap = reuseResultMap;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public BasicSelectOneMethodGenerator build() {
            return new BasicSelectOneMethodGenerator(this);
        }
    }
}
