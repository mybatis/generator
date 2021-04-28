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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.config.Context;

public abstract class AbstractKotlinFunctionGenerator {
    protected final Context context;
    protected final IntrospectedTable introspectedTable;
    protected final String tableFieldName;

    protected AbstractKotlinFunctionGenerator(BaseBuilder<?> builder) {
        context = builder.context;
        introspectedTable = builder.introspectedTable;
        tableFieldName = builder.tableFieldName;
    }

    protected void acceptParts(KotlinFile kotlinFile, KotlinFunction kotlinFunction,
            KotlinFunctionParts functionParts) {
        for (KotlinArg argument : functionParts.getArguments()) {
            kotlinFunction.addArgument(argument);
        }

        for (String annotation : functionParts.getAnnotations()) {
            kotlinFunction.addAnnotation(annotation);
        }

        kotlinFunction.addCodeLines(functionParts.getCodeLines());
        kotlinFile.addImports(functionParts.getImports());
    }

    protected void acceptParts(KotlinFunctionAndImports functionAndImports, KotlinFunctionParts functionParts) {
        for (KotlinArg argument : functionParts.getArguments()) {
            functionAndImports.getFunction().addArgument(argument);
        }

        for (String annotation : functionParts.getAnnotations()) {
            functionAndImports.getFunction().addAnnotation(annotation);
        }

        functionAndImports.getFunction().addCodeLines(functionParts.getCodeLines());
        functionAndImports.getImports().addAll(functionParts.getImports());
    }

    protected void addFunctionComment(KotlinFunctionAndImports functionAndImports) {
        context.getCommentGenerator().addGeneralFunctionComment(functionAndImports.getFunction(), introspectedTable,
                functionAndImports.getImports());
    }

    public static FieldNameAndImport calculateFieldNameAndImport(String tableFieldName, String supportObjectImport,
                                                     IntrospectedColumn column) {
        FieldNameAndImport answer = new FieldNameAndImport();
        answer.fieldName = column.getJavaProperty();
        if (answer.fieldName.equals(tableFieldName)) {
            // name collision, no shortcut generated
            answer.fieldName = tableFieldName + "." + answer.fieldName; //$NON-NLS-1$
            answer.importString = supportObjectImport + "." + tableFieldName; //$NON-NLS-1$
        } else {
            answer.importString = supportObjectImport + "." + answer.fieldName; //$NON-NLS-1$
        }
        return answer;
    }

    public static class FieldNameAndImport {
        private String fieldName;
        private String importString;

        public String fieldName() {
            return fieldName;
        }

        public String importString() {
            return importString;
        }
    }

    public abstract KotlinFunctionAndImports generateMethodAndImports();

    public abstract boolean callPlugins(KotlinFunction method, KotlinFile kotlinFile);

    public abstract static class BaseBuilder<T extends BaseBuilder<T>> {
        private Context context;
        private IntrospectedTable introspectedTable;
        private String tableFieldName;

        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }

        public T withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return getThis();
        }

        public T withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return getThis();
        }

        public abstract T getThis();
    }
}
