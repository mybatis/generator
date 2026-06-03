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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getCallingGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getAliasedEscapedColumnName;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getParameterClause;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.runtime.AbstractJavaClassMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;
import org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities;

public class ProviderUpdateByExampleSelectiveMethodGenerator extends AbstractJavaClassMethodGenerator {
    protected ProviderUpdateByExampleSelectiveMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateUpdateByExampleSelective()) {
            return Optional.empty();
        }

        Method method = new Method(introspectedTable.getUpdateByExampleSelectiveStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(
                new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$

        Set<FullyQualifiedJavaType> importedTypes = new HashSet<>();
        importedTypes.add(MyBatis3FormattingUtilities.BUILDER_IMPORT);
        importedTypes.add(new FullyQualifiedJavaType("java.util.Map")); //$NON-NLS-1$

        FullyQualifiedJavaType recordClass = introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(recordClass);
        method.addBodyLine("%s row = (%s) parameter.get(\"row\");".formatted(//$NON-NLS-1$
                recordClass.getShortName(), recordClass.getShortName()));

        FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine("%s example = (%s) parameter.get(\"example\");".formatted(//$NON-NLS-1$
                example.getShortName(), example.getShortName()));

        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, importedTypes);

        method.addBodyLine(""); //$NON-NLS-1$

        method.addBodyLine("SQL sql = new SQL();"); //$NON-NLS-1$

        method.addBodyLine("sql.UPDATE(\"%s\");".formatted(//$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedRuntimeTableName())));
        method.addBodyLine(""); //$NON-NLS-1$

        for (IntrospectedColumn introspectedColumn :
                ListUtilities.filterColumnsForUpdate(introspectedTable.getAllColumns())) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("if (row.%s() != null) {".formatted(//$NON-NLS-1$
                        getCallingGetterMethodName(introspectedColumn)));
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getParameterClause(introspectedColumn));
            sb.insert(2, "row."); //$NON-NLS-1$

            method.addBodyLine("sql.SET(\"%s = %s\");".formatted(//$NON-NLS-1$
                    escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)),
                    sb));

            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }

            method.addBodyLine(""); //$NON-NLS-1$
        }

        method.addBodyLine("applyWhere(sql, example, true);"); //$NON-NLS-1$
        method.addBodyLine("return sql.toString();"); //$NON-NLS-1$

        JavaMethodAndImports answer = JavaMethodAndImports.withMethod(method)
                .withImports(importedTypes)
                .build();

        return Optional.of(answer);
    }

    @Override
    public boolean callPlugins(Method method, TopLevelClass topLevelClass) {
        return pluginAggregator
                .providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public ProviderUpdateByExampleSelectiveMethodGenerator build() {
            return new ProviderUpdateByExampleSelectiveMethodGenerator(this);
        }
    }
}
