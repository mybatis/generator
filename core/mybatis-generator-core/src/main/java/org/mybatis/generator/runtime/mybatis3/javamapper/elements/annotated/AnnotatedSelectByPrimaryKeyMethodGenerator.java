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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;

public class AnnotatedSelectByPrimaryKeyMethodGenerator extends SelectByPrimaryKeyMethodGenerator {

    private final boolean useResultMapIfAvailable;

    protected AnnotatedSelectByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        this.useResultMapIfAvailable = builder.useResultMapIfAvailable;
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

        String annotation = javaIndent(1)
                + "\"from " //$NON-NLS-1$
                + escapeStringForJava(introspectedTable.getAliasedFullyQualifiedRuntimeTableName())
                + "\","; //$NON-NLS-1$
        builder.withAnnotation(annotation);

        builder.withAnnotations(buildByPrimaryKeyWhereClause());

        builder.withAnnotation("})"); //$NON-NLS-1$

        if (useResultMapAnnotation()) {
            builder.withAnnotation(getResultMapAnnotation());
        } else {
            builder.withAnnotations(getAnnotatedResultAnnotations(introspectedTable.getNonPrimaryKeyColumns()));
        }
    }

    private boolean useResultMapAnnotation() {
        return useResultMapIfAvailable
                && (introspectedTable.getRules().generateBaseResultMap()
                || introspectedTable.getRules().generateResultMapWithBLOBs());
    }

    private String getResultMapAnnotation() {
        return String.format("@ResultMap(\"%s.%s\")", //$NON-NLS-1$
                introspectedTable.getMyBatis3SqlMapNamespace(),
                introspectedTable.getRules().generateResultMapWithBLOBs()
                    ? introspectedTable.getResultMapWithBLOBsId() : introspectedTable.getBaseResultMapId());
    }

    private void addImports(JavaMethodParts.Builder builder) {
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        if (useResultMapAnnotation()) {
            builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap")); //$NON-NLS-1$
        } else {
            builder.withImports(getAnnotatedSelectImports());
            for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
                builder.withImports(
                        getAnnotatedResultImports(introspectedColumn));
            }
        }
    }

    public static class Builder extends SelectByPrimaryKeyMethodGenerator.AbstractBuilder<Builder> {
        private boolean useResultMapIfAvailable;

        public Builder useResultMapIfAvailable(boolean useResultMapIfAvailable) {
            this.useResultMapIfAvailable = useResultMapIfAvailable;
            return this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public AnnotatedSelectByPrimaryKeyMethodGenerator build() {
            return new AnnotatedSelectByPrimaryKeyMethodGenerator(this);
        }
    }
}
