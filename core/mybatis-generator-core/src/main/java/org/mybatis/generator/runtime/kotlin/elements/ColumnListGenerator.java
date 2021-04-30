/*
 *    Copyright 2006-2021 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin.elements;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinModifier;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.runtime.kotlin.elements.AbstractKotlinFunctionGenerator.FieldNameAndImport;

public class ColumnListGenerator {

    private final Context context;
    private final IntrospectedTable introspectedTable;
    private final String supportObjectImport;
    private final String tableFieldName;

    private ColumnListGenerator(Builder builder) {
        this.context = Objects.requireNonNull(builder.context);
        this.introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        this.supportObjectImport = Objects.requireNonNull(builder.supportObjectImport);
        this.tableFieldName = Objects.requireNonNull(builder.tableFieldName);
    }

    public KotlinPropertyAndImports generatePropertyAndImports() {
        List<FieldNameAndImport> fieldsAndImports = introspectedTable.getAllColumns().stream()
                .map(this::calculateFieldAndImport)
                .collect(Collectors.toList());

        KotlinPropertyAndImports propertyAndImports = KotlinPropertyAndImports.withProperty(
                KotlinProperty.newVal("columnList") //$NON-NLS-1$
                .withModifier(KotlinModifier.PRIVATE)
                .withInitializationString(getInitializationString(fieldsAndImports))
                .build())
                .withImports(getImports(fieldsAndImports))
                .build();

        context.getCommentGenerator().addGeneralPropertyComment(propertyAndImports.getProperty(), introspectedTable,
                propertyAndImports.getImports());
        return propertyAndImports;
    }

    private FieldNameAndImport calculateFieldAndImport(IntrospectedColumn column) {
        return AbstractKotlinFunctionGenerator.calculateFieldNameAndImport(tableFieldName, supportObjectImport, column);
    }

    private String getInitializationString(List<FieldNameAndImport> fieldsAndImports) {
        return fieldsAndImports.stream()
                .map(FieldNameAndImport::fieldName)
                .collect(Collectors.joining(", ", "listOf(", ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    private Set<String> getImports(List<FieldNameAndImport> fieldsAndImports) {
        return fieldsAndImports.stream()
                .map(FieldNameAndImport::importString)
                .collect(Collectors.toSet());
    }

    public boolean callPlugins(KotlinProperty kotlinProperty, KotlinFile kotlinFile) {
        return context.getPlugins().clientColumnListPropertyGenerated(kotlinProperty, kotlinFile, introspectedTable);
    }

    public static class Builder {
        private Context context;
        private IntrospectedTable introspectedTable;
        private String supportObjectImport;
        private String tableFieldName;

        public Builder withSupportObjectImport(String supportObjectImport) {
            this.supportObjectImport = supportObjectImport;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return this;
        }

        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }

        public ColumnListGenerator build() {
            return new ColumnListGenerator(this);
        }
    }
}
