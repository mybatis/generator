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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.config.GeneratedKey;

public abstract class AbstractJavaMapperMethodGenerator extends
        AbstractGenerator {
    public abstract void addInterfaceElements(Interface interfaze);

    protected AbstractJavaMapperMethodGenerator() {
        super();
    }

    protected static String getResultAnnotation(Interface interfaze, IntrospectedColumn introspectedColumn,
            boolean idColumn, boolean constructorBased) {
        StringBuilder sb = new StringBuilder();
        if (constructorBased) {
            interfaze.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
            sb.append("@Arg(column=\""); //$NON-NLS-1$
            sb.append(getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", javaType="); //$NON-NLS-1$
            sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
            sb.append(".class"); //$NON-NLS-1$
        } else {
            sb.append("@Result(column=\""); //$NON-NLS-1$
            sb.append(getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", property=\""); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append('\"');
        }

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            FullyQualifiedJavaType fqjt =
                    new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            interfaze.addImportedType(fqjt);
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(fqjt.getShortName());
            sb.append(".class"); //$NON-NLS-1$
        }

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }
        sb.append(')');

        return sb.toString();
    }

    protected void addGeneratedKeyAnnotation(Method method, GeneratedKey gk) {
        StringBuilder sb = new StringBuilder();
        introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\""); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")"); //$NON-NLS-1$
                method.addAnnotation(sb.toString());
            } else {
                FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                sb.append("@SelectKey(statement=\""); //$NON-NLS-1$
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\", keyProperty=\""); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before="); //$NON-NLS-1$
                sb.append(gk.isIdentity() ? "false" : "true"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.append(", resultType="); //$NON-NLS-1$
                sb.append(fqjt.getShortName());
                sb.append(".class)"); //$NON-NLS-1$
                method.addAnnotation(sb.toString());
            }
        });
    }

    protected void addGeneratedKeyImports(Interface interfaze, GeneratedKey gk) {
        introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                interfaze.addImportedType(
                        new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options")); //$NON-NLS-1$
            } else {
                interfaze.addImportedType(
                        new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey")); //$NON-NLS-1$
                FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                interfaze.addImportedType(fqjt);
            }
        });
    }

    protected void addAnnotatedSelectImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$

        if (introspectedTable.isConstructorBased()) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); //$NON-NLS-1$
            interfaze.addImportedType(
                    new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); //$NON-NLS-1$
        } else {
            interfaze.addImportedType(
                    new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); //$NON-NLS-1$
            interfaze.addImportedType(
                    new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); //$NON-NLS-1$
        }
    }

    protected List<String> buildByPrimaryKeyWhereClause() {
        List<String> answer = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean and = false;
        Iterator<IntrospectedColumn> iter = introspectedTable.getPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            sb.setLength(0);
            javaIndent(sb, 1);
            if (and) {
                sb.append("  \"and "); //$NON-NLS-1$
            } else {
                sb.append("\"where "); //$NON-NLS-1$
                and = true;
            }

            IntrospectedColumn introspectedColumn = iter.next();
            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            answer.add(sb.toString());
        }

        return answer;
    }

    protected List<String> buildUpdateByPrimaryKeyAnnotations(List<IntrospectedColumn> columnList) {
        List<String> answer = new ArrayList<>();
        answer.add("@Update({"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"update "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        answer.add(sb.toString());

        // set up for first column
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"set "); //$NON-NLS-1$

        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(columnList).iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            sb.append("\","); //$NON-NLS-1$
            answer.add(sb.toString());

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append("  \""); //$NON-NLS-1$
            }
        }

        answer.addAll(buildByPrimaryKeyWhereClause());

        answer.add("})"); //$NON-NLS-1$
        return answer;
    }
}
