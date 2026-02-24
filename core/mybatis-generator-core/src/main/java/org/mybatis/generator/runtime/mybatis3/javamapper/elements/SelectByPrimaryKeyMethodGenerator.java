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
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class SelectByPrimaryKeyMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private final boolean isSimple;

    protected SelectByPrimaryKeyMethodGenerator(AbstractBuilder<?> builder) {
        super(builder);
        this.isSimple = builder.isSimple;
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateSelectByPrimaryKey()) {
            return Optional.empty();
        }

        Method method = new Method(introspectedTable.getSelectByPrimaryKeyStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
        method.setReturnType(returnType);

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(returnType);

        addPrimaryKeyMethodParameters(isSimple, method, importedTypes);

        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        return JavaMethodAndImports.withMethod(method)
                .withImports(importedTypes)
                .withExtraMethodParts(extraMethodParts())
                .buildOptional();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> extends AbstractGeneratorBuilder<T> {
        private boolean isSimple;

        public T isSimple(boolean isSimple) {
            this.isSimple = isSimple;
            return getThis();
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public SelectByPrimaryKeyMethodGenerator build() {
            return new SelectByPrimaryKeyMethodGenerator(this);
        }
    }
}
