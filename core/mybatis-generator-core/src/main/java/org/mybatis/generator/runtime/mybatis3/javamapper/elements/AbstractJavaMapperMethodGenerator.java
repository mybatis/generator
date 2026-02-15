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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap;
import static org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities.getSelectListPhrase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;

public abstract class AbstractJavaMapperMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    protected AbstractJavaMapperMethodGenerator(AbstractGeneratorBuilder<?> builder) {
        super(builder);
    }

    protected Set<FullyQualifiedJavaType> getAnnotatedResultImports(IntrospectedColumn introspectedColumn,
                                                                    boolean isConstructorBased) {
        Set<FullyQualifiedJavaType> answer = new HashSet<>();
        if (isConstructorBased) {
            answer.add(introspectedColumn.getFullyQualifiedJavaType());
        }

        introspectedColumn.getTypeHandler().map(FullyQualifiedJavaType::new).ifPresent(answer::add);

        return answer;
    }

    protected static String getResultAnnotation(IntrospectedColumn introspectedColumn, boolean idColumn,
                                                boolean constructorBased) {
        StringBuilder sb = new StringBuilder();
        if (constructorBased) {
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

        introspectedColumn.getTypeHandler().map(FullyQualifiedJavaType::new).ifPresent(th -> {
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(th.getShortName());
            sb.append(".class"); //$NON-NLS-1$
        });

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }
        sb.append(')');

        return sb.toString();
    }

    protected Set<FullyQualifiedJavaType> getAnnotatedSelectImports() {
        Set<FullyQualifiedJavaType> answer = new HashSet<>();
        answer.add(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$

        if (introspectedTable.isConstructorBased() || introspectedTable.isRecordBased()) {
            answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); //$NON-NLS-1$
            answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); //$NON-NLS-1$
        } else {
            answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); //$NON-NLS-1$
            answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); //$NON-NLS-1$
        }

        return answer;
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

    protected void addPrimaryKeyMethodParameters(boolean isSimple, Method method,
            Set<FullyQualifiedJavaType> importedTypes) {
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key")); //$NON-NLS-1$
        } else {
            // no primary key class - fields are in the base class
            // if more than one PK field, then we need to annotate the
            // parameters for MyBatis3
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            boolean annotate = introspectedColumns.size() > 1;
            if (annotate) {
                importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
            }
            StringBuilder sb = new StringBuilder();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                if (annotate) {
                    sb.setLength(0);
                    sb.append("@Param(\""); //$NON-NLS-1$
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append("\")"); //$NON-NLS-1$
                    parameter.addAnnotation(sb.toString());
                }
                method.addParameter(parameter);
            }
        }
    }

    protected Method buildBasicUpdateByExampleMethod(String statementId, FullyQualifiedJavaType parameterType,
            Set<FullyQualifiedJavaType> importedTypes) {
        Method method = new Method(statementId);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        method.addParameter(new Parameter(parameterType, "row", "@Param(\"row\")")); //$NON-NLS-1$ //$NON-NLS-2$

        importedTypes.add(parameterType);

        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        method.addParameter(new Parameter(exampleType, "example", "@Param(\"example\")")); //$NON-NLS-1$ //$NON-NLS-2$
        importedTypes.add(exampleType);

        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$

        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        return method;
    }

    protected List<String> getAnnotatedResultAnnotations(List<IntrospectedColumn> nonPrimaryKeyColumns) {
        List<String> annotations = new ArrayList<>();

        boolean useConstructorArgs = introspectedTable.isConstructorBased() || introspectedTable.isRecordBased();
        if (useConstructorArgs) {
            annotations.add("@ConstructorArgs({"); //$NON-NLS-1$
        } else {
            annotations.add("@Results({"); //$NON-NLS-1$
        }

        StringBuilder sb = new StringBuilder();

        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = nonPrimaryKeyColumns.iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(introspectedColumn, true, useConstructorArgs));

            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }

            annotations.add(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(introspectedColumn, false, useConstructorArgs));

            if (iterNonPk.hasNext()) {
                sb.append(',');
            }

            annotations.add(sb.toString());
        }

        annotations.add("})"); //$NON-NLS-1$

        return annotations;
    }

    protected Method buildBasicUpdateByPrimaryKeyMethod(String statementId, FullyQualifiedJavaType parameterType) {
        Method method = new Method(statementId);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(parameterType, "row")); //$NON-NLS-1$

        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        return method;
    }

    protected List<String> buildInitialSelectAnnotationStrings() {
        List<String> answer = new ArrayList<>();
        answer.add("@Select({"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"select\","); //$NON-NLS-1$
        answer.add(sb.toString());

        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append('"');
        boolean hasColumns = false;
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            sb.append(escapeStringForJava(getSelectListPhrase(iter.next())));
            hasColumns = true;

            if (iter.hasNext()) {
                sb.append(", "); //$NON-NLS-1$
            }

            if (sb.length() > 80) {
                sb.append("\","); //$NON-NLS-1$
                answer.add(sb.toString());

                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append('"');
                hasColumns = false;
            }
        }

        if (hasColumns) {
            sb.append("\","); //$NON-NLS-1$
            answer.add(sb.toString());
        }

        return answer;
    }

    protected JavaMethodParts extraMethodParts() {
        // extension point for annotated method generators
        return new JavaMethodParts.Builder().build();
    }
}
