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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.runtime.JavaFieldAndImports;

public class SelectListGenerator extends AbstractGenerator {

    private final FragmentGenerator fragmentGenerator;

    private SelectListGenerator(Builder builder) {
        super(builder);
        this.fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    public JavaFieldAndImports generateFieldAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType fieldType =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.BasicColumn[]"); //$NON-NLS-1$
        imports.add(fieldType);
        Field field = new Field("selectList", fieldType); //$NON-NLS-1$
        field.setInitializationString("BasicColumn.columnList(" //$NON-NLS-1$
                + fragmentGenerator.getSelectList() + ")"); //$NON-NLS-1$
        commentGenerator.addFieldAnnotation(field, introspectedTable, imports);

        return JavaFieldAndImports.withField(field)
                .withImports(imports)
                .build();
    }

    public boolean callPlugins(Field field, Interface interfaze) {
        return pluginAggregator.clientSelectListFieldGenerated(field, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FragmentGenerator fragmentGenerator;

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public SelectListGenerator build() {
            return new SelectListGenerator(this);
        }
    }
}
