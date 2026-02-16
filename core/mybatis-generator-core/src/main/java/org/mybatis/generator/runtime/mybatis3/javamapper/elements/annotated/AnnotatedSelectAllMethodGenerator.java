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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectAllMethodGenerator;

public class AnnotatedSelectAllMethodGenerator extends SelectAllMethodGenerator {

    protected AnnotatedSelectAllMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected JavaMethodParts extraMethodParts() {
        var builder = new JavaMethodParts.Builder();
        addAnnotations(builder);
        addImports(builder);
        return builder.build();
    }

    private void addAnnotations(JavaMethodParts.Builder builder) {
        builder.withAnnotations(buildInitialSelectAnnotationStrings());

        StringBuilder sb = new StringBuilder();
        sb.append(javaIndent(1));
        sb.append("\"from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getAliasedFullyQualifiedRuntimeTableName()));
        sb.append('\"');

        String orderByClause =
                introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_SELECT_ALL_ORDER_BY_CLAUSE);
        boolean hasOrderBy = stringHasValue(orderByClause);
        if (hasOrderBy) {
            sb.append(',');
        }

        builder.withAnnotation(sb.toString());

        if (hasOrderBy) {
            String orderByPhrase = javaIndent(1)
                    + "\"order by " //$NON-NLS-1$
                    + orderByClause
                    + '\"';
            builder.withAnnotation(orderByPhrase);
        }

        builder.withAnnotation("})"); //$NON-NLS-1$

        builder.withAnnotations(getAnnotatedResultAnnotations(introspectedTable.getNonPrimaryKeyColumns()));
    }

    private void addImports(JavaMethodParts.Builder builder) {
        builder.withImports(getAnnotatedSelectImports());
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            builder.withImports(getAnnotatedResultImports(introspectedColumn));
        }

        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$
    }

    public static class Builder extends SelectAllMethodGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedSelectAllMethodGenerator build() {
            return new AnnotatedSelectAllMethodGenerator(this);
        }
    }
}
