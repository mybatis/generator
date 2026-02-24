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
package org.mybatis.generator.runtime.common;

import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;

import java.util.Optional;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.KotlinFunctionParts;

public class GeneratedKeyAnnotationUtility {
    private static final String OPTIONS_IMPORT = "org.apache.ibatis.annotations.Options"; //$NON-NLS-1$
    private static final String SELECT_KEY_IMPORT = "org.apache.ibatis.annotations.SelectKey"; //$NON-NLS-1$

    private static final String OPTIONS_TEMPLATE =
            "@Options(useGeneratedKeys=true, keyProperty=\"%s\", keyColumn=\"%s\")"; //$NON-NLS-1$
    private static final String JAVA_SElECT_KEY_TEMPLATE =
            "@SelectKey(statement=\"%s\", keyProperty=\"%s\", before=%s, resultType=%s.class)"; //$NON-NLS-1$
    private static final String KOTLIN_SElECT_KEY_TEMPLATE =
            "@SelectKey(statement=[\"%s\"], keyProperty=\"%s\", before=%s, resultType=%s::class)"; //$NON-NLS-1$

    public static Optional<JavaMethodParts> getLegacyJavaGeneratedKeyAnnotation(IntrospectedTable introspectedTable,
                                                                                GeneratedKey gk) {
        return introspectedTable.getColumn(gk.getColumn()).map(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                return calculateJavaOptionsAnnotation(Prefix.LEGACY, introspectedColumn);
            } else {
                return calculateJavaSelectKeyAnnotation(Prefix.LEGACY, introspectedColumn, gk);
            }
        });
    }

    public static Optional<JavaMethodParts> getJavaMultiRowGeneratedKeyAnnotation(IntrospectedTable introspectedTable,
                                                                                  GeneratedKey gk) {
        if (!gk.isJdbcStandard()) {
            return Optional.empty();
        }

        return introspectedTable.getColumn(gk.getColumn()).map(introspectedColumn ->
            calculateJavaOptionsAnnotation(Prefix.DSQL_MULTI_ROW, introspectedColumn)
        );
    }

    public static Optional<JavaMethodParts> getJavaSingleRowGeneratedKeyAnnotation(IntrospectedTable introspectedTable,
                                                                                   GeneratedKey gk) {
        return introspectedTable.getColumn(gk.getColumn()).map(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                return calculateJavaOptionsAnnotation(Prefix.DSQL_SINGLE_ROW, introspectedColumn);
            } else {
                return calculateJavaSelectKeyAnnotation(Prefix.DSQL_SINGLE_ROW, introspectedColumn, gk);
            }
        });
    }

    private static JavaMethodParts calculateJavaSelectKeyAnnotation(Prefix prefix,
                                                                    IntrospectedColumn introspectedColumn,
                                                                    GeneratedKey gk) {
        JavaMethodParts.Builder builder = new JavaMethodParts.Builder();
        builder.withImport(new FullyQualifiedJavaType(SELECT_KEY_IMPORT));
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String annotation = String.format(JAVA_SElECT_KEY_TEMPLATE,
                gk.getRuntimeSqlStatement(),
                prefix.prefix() + introspectedColumn.getJavaProperty(),
                !gk.isIdentity(),
                fqjt.getShortName());
        builder.withAnnotation(annotation);
        return builder.build();
    }

    private static JavaMethodParts calculateJavaOptionsAnnotation(Prefix prefix,
                                                                  IntrospectedColumn introspectedColumn) {
        JavaMethodParts.Builder builder = new JavaMethodParts.Builder();
        builder.withImport(new FullyQualifiedJavaType(OPTIONS_IMPORT));
        String annotation = String.format(OPTIONS_TEMPLATE,
                prefix.prefix() + introspectedColumn.getJavaProperty(),
                escapeStringForJava(introspectedColumn.getActualColumnName()));
        builder.withAnnotation(annotation);
        return builder.build();
    }

    public static Optional<KotlinFunctionParts> getKotlinMultiRowGeneratedKeyAnnotation(
            IntrospectedTable introspectedTable, GeneratedKey gk) {
        if (!gk.isJdbcStandard()) {
            return Optional.empty();
        }

        return introspectedTable.getColumn(gk.getColumn()).map(introspectedColumn ->
                calculateKotlinOptionsAnnotation(Prefix.DSQL_MULTI_ROW, introspectedColumn));
    }

    public static Optional<KotlinFunctionParts> getKotlinSingleRowGeneratedKeyAnnotation(
            IntrospectedTable introspectedTable, GeneratedKey gk) {
        return introspectedTable.getColumn(gk.getColumn()).map(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                return calculateKotlinOptionsAnnotation(Prefix.DSQL_SINGLE_ROW, introspectedColumn);
            } else {
                KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();
                builder.withImport(SELECT_KEY_IMPORT);
                FullyQualifiedKotlinType kt =
                        JavaToKotlinTypeConverter.convert(introspectedColumn.getFullyQualifiedJavaType());
                String annotation = String.format(KOTLIN_SElECT_KEY_TEMPLATE,
                        gk.getRuntimeSqlStatement(),
                        Prefix.DSQL_SINGLE_ROW.prefix() + introspectedColumn.getJavaProperty(),
                        !gk.isIdentity(),
                        kt.getShortNameWithoutTypeArguments());
                builder.withAnnotation(annotation);
                return builder.build();
            }
        });
    }

    private static KotlinFunctionParts calculateKotlinOptionsAnnotation(Prefix prefix,
                                                                        IntrospectedColumn introspectedColumn) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();
        builder.withImport(OPTIONS_IMPORT);
        String annotation = String.format(OPTIONS_TEMPLATE,
                prefix.prefix() + introspectedColumn.getJavaProperty(),
                escapeStringForKotlin(introspectedColumn.getActualColumnName()));
        builder.withAnnotation(annotation);
        return builder.build();
    }

    private enum Prefix {
        LEGACY(""), //$NON-NLS-1$
        DSQL_SINGLE_ROW("row."), //$NON-NLS-1$
        DSQL_MULTI_ROW("records."); //$NON-NLS-1$

        private final String prefix;

        Prefix(String prefix) {
            this.prefix = prefix;
        }

        public String prefix() {
            return prefix;
        }
    }
}
