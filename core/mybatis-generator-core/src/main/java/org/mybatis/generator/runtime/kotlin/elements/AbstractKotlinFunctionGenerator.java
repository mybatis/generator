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

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.runtime.kotlin.KotlinDynamicSqlSupportClassGenerator;

public abstract class AbstractKotlinFunctionGenerator {
    protected Context context;
    protected IntrospectedTable introspectedTable;
    protected String tableFieldName;
    protected String tableFieldImport;
    
    protected AbstractKotlinFunctionGenerator(BaseBuilder<?, ?> builder) {
        context = builder.context;
        introspectedTable = builder.introspectedTable;
        tableFieldName = builder.tableFieldName;
        tableFieldImport = builder.tableFieldImport;
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

    public abstract KotlinFunctionAndImports generateMethodAndImports();
    
    public abstract boolean callPlugins(KotlinFunction method, KotlinFile kotlinFile);
    
    public abstract static class BaseBuilder<T extends BaseBuilder<T, R>, R> {
        private Context context;
        private IntrospectedTable introspectedTable;
        private String tableFieldName;
        private String tableFieldImport;
        
        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }
        
        public T withDynamicSqlSupportClassGenerator(KotlinDynamicSqlSupportClassGenerator generator) {
            tableFieldName = generator.getInnerObject().getName();
            tableFieldImport = generator.getInnerObjectImport();
            return getThis();
        }
        
        public T withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return getThis();
        }

        public abstract T getThis();
        
        public abstract R build();
    }
}
