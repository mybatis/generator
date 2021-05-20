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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getSelectListPhrase;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ProviderSelectByExampleWithoutBLOBsMethodGenerator extends AbstractJavaProviderMethodGenerator {

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes(fqjt);

        Method method = new Method(getMethodName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("SQL sql = new SQL();"); //$NON-NLS-1$

        boolean distinctCheck = true;
        for (IntrospectedColumn introspectedColumn : getColumns()) {
            if (distinctCheck) {
                method.addBodyLine("if (example != null && example.isDistinct()) {"); //$NON-NLS-1$
                method.addBodyLine(String.format("sql.SELECT_DISTINCT(\"%s\");", //$NON-NLS-1$
                        escapeStringForJava(getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("} else {"); //$NON-NLS-1$
                method.addBodyLine(String.format("sql.SELECT(\"%s\");", //$NON-NLS-1$
                        escapeStringForJava(getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("}"); //$NON-NLS-1$
            } else {
                method.addBodyLine(String.format("sql.SELECT(\"%s\");", //$NON-NLS-1$
                        escapeStringForJava(getSelectListPhrase(introspectedColumn))));
            }

            distinctCheck = false;
        }

        method.addBodyLine(String.format("sql.FROM(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("applyWhere(sql, example, false);"); //$NON-NLS-1$

        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("if (example != null && example.getOrderByClause() != null) {"); //$NON-NLS-1$
        method.addBodyLine("sql.ORDER_BY(example.getOrderByClause());"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$

        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return sql.toString();"); //$NON-NLS-1$

        if (callPlugins(method, topLevelClass)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    public List<IntrospectedColumn> getColumns() {
        return introspectedTable.getNonBLOBColumns();
    }

    public String getMethodName() {
        return introspectedTable.getSelectByExampleStatementId();
    }

    public boolean callPlugins(Method method, TopLevelClass topLevelClass) {
        return context.getPlugins()
                .providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }
}
