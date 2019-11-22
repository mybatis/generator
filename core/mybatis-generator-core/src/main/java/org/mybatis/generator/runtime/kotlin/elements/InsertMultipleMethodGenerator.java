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

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.Utils;

public class InsertMultipleMethodGenerator extends AbstractKotlinFunctionGenerator {
    private FullyQualifiedKotlinType recordType;
    private String mapperName;
    private String tableFieldImport;
    
    private InsertMultipleMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        mapperName = builder.mapperName;
        tableFieldImport = builder.tableFieldImport;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        if (!Utils.generateMultipleRowInsert(introspectedTable)) {
            return null;
        }
        
        // Kotlin type inference gets lost if we don't name the helper method something different from the
        // regular mapper method
        String mapperMethod = Utils.generateMultipleRowInsertHelper(introspectedTable)
                ? "insertMultipleHelper" : "insertMultiple"; //$NON-NLS-1$ //$NON-NLS-2$
        
        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".insertMultiple") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("records") //$NON-NLS-1$
                        .withDataType("Collection<" //$NON-NLS-1$
                                + recordType.getShortNameWithTypeArguments()
                                + ">") //$NON-NLS-1$
                        .build())
                .build())
                .withImport("org.mybatis.dynamic.sql.util.kotlin.mybatis3.*") //$NON-NLS-1$
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);
        
        KotlinFunction function = functionAndImports.getFunction();
        
        function.addCodeLine("insertMultiple(this::" + mapperMethod //$NON-NLS-1$
                + ", records, " + tableFieldName //$NON-NLS-1$
                + ") {"); //$NON-NLS-1$
        
        List<IntrospectedColumn> columns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (IntrospectedColumn column : columns) {
            String fieldName = column.getJavaProperty();
            functionAndImports.getImports().add(tableFieldImport + "." + fieldName); //$NON-NLS-1$
            
            function.addCodeLine("    map(" + fieldName //$NON-NLS-1$
                    + ").toProperty(\"" + column.getJavaProperty() //$NON-NLS-1$
                    + "\")"); //$NON-NLS-1$
        }
        
        function.addCodeLine("}"); //$NON-NLS-1$
        
        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins().clientInsertMultipleMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder, InsertMultipleMethodGenerator> {
        private FullyQualifiedKotlinType recordType;
        private String mapperName;
        private String tableFieldImport;
        
        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }
        
        public Builder withTableFieldImport(String tableFieldImport) {
            this.tableFieldImport = tableFieldImport;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public InsertMultipleMethodGenerator build() {
            return new InsertMultipleMethodGenerator(this);
        }
    }
}
