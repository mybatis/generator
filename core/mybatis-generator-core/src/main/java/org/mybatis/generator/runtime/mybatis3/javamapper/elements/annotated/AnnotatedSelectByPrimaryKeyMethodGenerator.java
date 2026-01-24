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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;

public class AnnotatedSelectByPrimaryKeyMethodGenerator extends SelectByPrimaryKeyMethodGenerator {

    private final boolean useResultMapIfAvailable;

    protected AnnotatedSelectByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        this.useResultMapIfAvailable = builder.useResultMapIfAvailable;
    }

    @Override
    protected List<String> extraMethodAnnotations() {
        List<String> annotations = new ArrayList<>(buildInitialSelectAnnotationStrings());

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getAliasedFullyQualifiedRuntimeTableName()));
        sb.append("\","); //$NON-NLS-1$
        annotations.add(sb.toString());

        annotations.addAll(buildByPrimaryKeyWhereClause());

        annotations.add("})"); //$NON-NLS-1$

        if (useResultMapIfAvailable) {
            // TODO - combine with if above
            if (introspectedTable.getRules().generateBaseResultMap()
                    || introspectedTable.getRules().generateResultMapWithBLOBs()) {
                annotations.add(getResultMapAnnotation());
            } else {
                annotations.addAll(getAnnotatedResultAnnotations(introspectedTable.getNonPrimaryKeyColumns()));
            }
        } else {
            annotations.addAll(getAnnotatedResultAnnotations(introspectedTable.getNonPrimaryKeyColumns()));
        }

        return annotations;
    }

    private String getResultMapAnnotation() {
        return String.format("@ResultMap(\"%s.%s\")", //$NON-NLS-1$
                introspectedTable.getMyBatis3SqlMapNamespace(),
                introspectedTable.getRules().generateResultMapWithBLOBs()
                    ? introspectedTable.getResultMapWithBLOBsId() : introspectedTable.getBaseResultMapId());
    }

    @Override
    protected Set<FullyQualifiedJavaType> extraImports() {
        Set<FullyQualifiedJavaType> answer = new HashSet<>();
        answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        if (useResultMapIfAvailable) {
            // TODO - combine with if above
            if (introspectedTable.getRules().generateBaseResultMap()
                    || introspectedTable.getRules().generateResultMapWithBLOBs()) {
                answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap")); //$NON-NLS-1$
            } else {
                answer.addAll(getAnnotatedSelectImports());
                for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
                    answer.addAll(getAnnotatedResultImports(introspectedColumn, introspectedTable.isConstructorBased()));
                }
            }
        } else {
            answer.addAll(getAnnotatedSelectImports());
            for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
                answer.addAll(getAnnotatedResultImports(introspectedColumn, introspectedTable.isConstructorBased()));
            }
        }

        return answer;
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
