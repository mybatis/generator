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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class CountByExampleMethodGenerator extends AbstractJavaMapperMethodGenerator {

    protected CountByExampleMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateCountByExample()) {
            return Optional.empty();
        }

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(fqjt);

        Method method = new Method(introspectedTable.getCountByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(new FullyQualifiedJavaType("long")); //$NON-NLS-1$
        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        return JavaMethodAndImports.withMethod(method)
                .withImports(importedTypes)
                .withExtraMethodParts(extraMethodParts())
                .buildOptional();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public CountByExampleMethodGenerator build() {
            return new CountByExampleMethodGenerator(this);
        }
    }
}
