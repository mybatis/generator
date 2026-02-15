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
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
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

public class ProviderInsertSelectiveMethodGenerator extends AbstractJavaClassMethodGenerator {
    protected ProviderInsertSelectiveMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateInsertSelective()) {
            return Optional.empty();
        }

        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        Set<FullyQualifiedJavaType> importedTypes = new HashSet<>();
        importedTypes.add(MyBatis3FormattingUtilities.BUILDER_IMPORT);
        importedTypes.add(fqjt);

        Method method = new Method(introspectedTable.getInsertSelectiveStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "row")); //$NON-NLS-1$

        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("SQL sql = new SQL();"); //$NON-NLS-1$

        method.addBodyLine(String.format("sql.INSERT_INTO(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));

        for (IntrospectedColumn introspectedColumn :
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {

            method.addBodyLine(""); //$NON-NLS-1$
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine(String.format("if (row.%s() != null) {", //$NON-NLS-1$
                        getCallingGetterMethodName(introspectedColumn)));
            }
            method.addBodyLine(String.format("sql.VALUES(\"%s\", \"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn)));

            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }
        }

        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return sql.toString();"); //$NON-NLS-1$

        JavaMethodAndImports answer = JavaMethodAndImports.withMethod(method)
                .withImports(importedTypes)
                .build();

        return Optional.of(answer);
    }

    @Override
    public boolean callPlugins(Method method, TopLevelClass topLevelClass) {
        return pluginAggregator.providerInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public ProviderInsertSelectiveMethodGenerator build() {
            return new ProviderInsertSelectiveMethodGenerator(this);
        }
    }
}
