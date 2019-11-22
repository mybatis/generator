/**
 *    Copyright 2006-2019 the original author or authors.
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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinModifier;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.config.Context;

public class ColumnListGenerator {

    private Context context;
    private IntrospectedTable introspectedTable;
    private String tableFieldImport;
    
    private ColumnListGenerator(Builder builder) {
        this.context = Objects.requireNonNull(builder.context);
        this.introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        this.tableFieldImport = Objects.requireNonNull(builder.tableFieldImport);
    }
    
    public KotlinPropertyAndImports generatePropertyAndImports() {
        KotlinPropertyAndImports propertyAndImports = KotlinPropertyAndImports.withProperty(
                KotlinProperty.newVal("columnList") //$NON-NLS-1$
                .withModifier(KotlinModifier.PRIVATE)
                .withInitializationString(getInitializationString())
                .build())
                .withImports(getImports())
                .build();
        
        context.getCommentGenerator().addGeneralPropertyComment(propertyAndImports.getProperty(), introspectedTable,
                propertyAndImports.getImports());
        return propertyAndImports;
    }

    private String getInitializationString() {
        return introspectedTable.getAllColumns().stream()
                .map(IntrospectedColumn::getJavaProperty)
                .collect(Collectors.joining(", ", "listOf(", ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    private Set<String> getImports() {
        return introspectedTable.getAllColumns().stream()
                .map(c -> tableFieldImport + "." + c.getJavaProperty())//$NON-NLS-1$
                .collect(Collectors.toSet());
    }
    
    public boolean callPlugins(KotlinProperty kotlinProperty, KotlinFile kotlinFile) {
        return context.getPlugins().clientColumnListPropertyGenerated(kotlinProperty, kotlinFile, introspectedTable);
    }
    
    public static class Builder {
        private Context context;
        private IntrospectedTable introspectedTable;
        private String tableFieldImport;

        public Builder withTableFieldImport(String tableFieldImport) {
            this.tableFieldImport = tableFieldImport;
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
        
        public ColumnListGenerator build() {
            return new ColumnListGenerator(this);
        }
    }
}
