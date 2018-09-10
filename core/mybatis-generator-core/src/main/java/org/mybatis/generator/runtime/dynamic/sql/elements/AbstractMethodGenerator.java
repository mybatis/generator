/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.Context;

public abstract class AbstractMethodGenerator {
    protected Context context;
    protected IntrospectedTable introspectedTable;
    protected String tableFieldName;
    
    protected AbstractMethodGenerator(BaseBuilder<?, ?> builder) {
        context = builder.context;
        introspectedTable = builder.introspectedTable;
        tableFieldName = builder.tableFieldName;
    }

    protected String calculateFieldName(IntrospectedColumn column) {
        return calculateFieldName(tableFieldName, column);
    }
    
    public static String calculateFieldName(String tableFieldName, IntrospectedColumn column) {
        String fieldName = column.getJavaProperty();
        if (fieldName.equals(tableFieldName)) {
            // name collision, no shortcut generated
            fieldName = tableFieldName + "." + fieldName; //$NON-NLS-1$
        }
        return fieldName;
    }
    
    protected void acceptParts(MethodAndImports.Builder builder, Method method, MethodParts methodParts) {
        for (Parameter parameter : methodParts.getParameters()) {
            method.addParameter(parameter);
        }
        
        for (String annotation : methodParts.getAnnotations()) {
            method.addAnnotation(annotation);
        }
        
        method.addBodyLines(methodParts.getBodyLines());
        builder.withImports(methodParts.getImports());
    }

    public abstract MethodAndImports generateMethodAndImports();
    
    public abstract boolean callPlugins(Method method, Interface interfaze);
    
    public abstract static class BaseBuilder<T extends BaseBuilder<T, R>, R> {
        private Context context;
        private IntrospectedTable introspectedTable;
        private String tableFieldName;
        
        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }
        
        public T withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return getThis();
        }

        public T withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return getThis();
        }

        public abstract T getThis();
        
        public abstract R build();
    }
}
