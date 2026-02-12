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
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getParameterClause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.common.GeneratedKeyAnnotationUtility;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.InsertMethodGenerator;

public class AnnotatedInsertMethodGenerator extends InsertMethodGenerator {
    protected AnnotatedInsertMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected JavaMethodParts extraMethodParts() {
        var builder = new JavaMethodParts.Builder()
                .withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert")); //$NON-NLS-1$
        addAnnotations(builder);

        introspectedTable.getGeneratedKey()
                .flatMap(gk -> GeneratedKeyAnnotationUtility.getLegacyJavaGeneratedKeyAnnotation(introspectedTable, gk))
                .ifPresent(builder::withJavaMethodParts);

        return builder.build();
    }


    private void addAnnotations(JavaMethodParts.Builder builder) {
        builder.withAnnotation("@Insert({"); //$NON-NLS-1$
        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        javaIndent(insertClause, 1);
        javaIndent(valuesClause, 1);

        insertClause.append("\"insert into "); //$NON-NLS-1$
        insertClause.append(escapeStringForJava(introspectedTable
                .getFullyQualifiedTableNameAtRuntime()));
        insertClause.append(" ("); //$NON-NLS-1$

        valuesClause.append("\"values ("); //$NON-NLS-1$

        List<String> valuesClauses = new ArrayList<>();
        Iterator<IntrospectedColumn> iter =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())
                .iterator();
        boolean hasFields = false;
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            insertClause.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            valuesClause.append(getParameterClause(introspectedColumn));
            hasFields = true;
            if (iter.hasNext()) {
                insertClause.append(", "); //$NON-NLS-1$
                valuesClause.append(", "); //$NON-NLS-1$
            }

            if (valuesClause.length() > 60) {
                if (!iter.hasNext()) {
                    insertClause.append(')');
                    valuesClause.append(')');
                }
                insertClause.append("\","); //$NON-NLS-1$
                valuesClause.append('\"');
                if (iter.hasNext()) {
                    valuesClause.append(',');
                }

                builder.withAnnotation(insertClause.toString());
                insertClause.setLength(0);
                javaIndent(insertClause, 1);
                insertClause.append('\"');

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                javaIndent(valuesClause, 1);
                valuesClause.append('\"');
                hasFields = false;
            }
        }

        if (hasFields) {
            insertClause.append(")\","); //$NON-NLS-1$
            builder.withAnnotation(insertClause.toString());

            valuesClause.append(")\""); //$NON-NLS-1$
            valuesClauses.add(valuesClause.toString());
        }

        builder.withAnnotations(valuesClauses);

        builder.withAnnotation("})"); //$NON-NLS-1$
    }

    public static class Builder extends InsertMethodGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedInsertMethodGenerator build() {
            return new AnnotatedInsertMethodGenerator(this);
        }
    }
}
