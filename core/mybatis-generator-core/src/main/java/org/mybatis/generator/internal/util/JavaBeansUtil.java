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
package org.mybatis.generator.internal.util;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.Locale;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;

public class JavaBeansUtil {

    private JavaBeansUtil() {
        super();
    }

    /**
     * Computes a getter method name.  Warning - does not check to see that the property is a valid
     * property.  Call getValidPropertyName first.
     *
     * @param property
     *            the property
     * @param fullyQualifiedJavaType
     *            the fully qualified java type
     * @return the getter method name
     */
    public static String getGetterMethodName(String property,
            FullyQualifiedJavaType fullyQualifiedJavaType) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))
                && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType
                .getBooleanPrimitiveInstance())) {
            sb.insert(0, "is"); //$NON-NLS-1$
        } else {
            sb.insert(0, "get"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    /**
     * Computes a setter method name.  Warning - does not check to see that the property is a valid
     * property.  Call getValidPropertyName first.
     *
     * @param property
     *            the property
     * @return the setter method name
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))
                && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        sb.insert(0, "set"); //$NON-NLS-1$

        return sb.toString();
    }

    public static String getFirstCharacterUppercase(String inputString) {
        StringBuilder sb = new StringBuilder(inputString);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static String getCamelCaseString(String inputString,
            boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
            case '_':
            case '-':
            case '@':
            case '$':
            case '#':
            case ' ':
            case '/':
            case '&':
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
                break;

            default:
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
                break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    /**
     * This method ensures that the specified input string is a valid Java property name.
     *
     * <p>The rules are as follows:
     *
     * <ol>
     *   <li>If the first character is lower case, then OK</li>
     *   <li>If the first two characters are upper case, then OK</li>
     *   <li>If the first character is upper case, and the second character is lower case, then the first character
     *       should be made lower case</li>
     * </ol>
     *
     * <p>For example:
     *
     * <ul>
     *   <li>eMail &gt; eMail</li>
     *   <li>firstName &gt; firstName</li>
     *   <li>URL &gt; URL</li>
     *   <li>XAxis &gt; XAxis</li>
     *   <li>a &gt; a</li>
     *   <li>B &gt; b</li>
     *   <li>Yaxis &gt; yaxis</li>
     * </ul>
     *
     * @param inputString
     *            the input string
     * @return the valid property name
     */
    public static String getValidPropertyName(String inputString) {
        String answer;

        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0))
                    && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US)
                        + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }

        return answer;
    }

    public static Method getJavaBeansGetter(IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable) {
        Method method = getBasicJavaBeansGetter(introspectedColumn);
        addGeneratedGetterJavaDoc(method, introspectedColumn, context, introspectedTable);
        return method;
    }

    public static Method getJavaBeansGetterWithGeneratedAnnotation(IntrospectedColumn introspectedColumn,
            Context context, IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        Method method = getBasicJavaBeansGetter(introspectedColumn);
        addGeneratedGetterAnnotation(method, introspectedColumn, context, introspectedTable, compilationUnit);
        return method;
    }

    private static Method getBasicJavaBeansGetter(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn
                .getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Method method = new Method(getGetterMethodName(property, fqjt));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);

        String s = "return " + property + ';'; //$NON-NLS-1$
        method.addBodyLine(s);

        return method;
    }

    private static void addGeneratedGetterJavaDoc(Method method, IntrospectedColumn introspectedColumn,
            Context context, IntrospectedTable introspectedTable) {
        context.getCommentGenerator().addGetterComment(method,
                introspectedTable, introspectedColumn);
    }

    private static void addGeneratedGetterAnnotation(Method method, IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, introspectedColumn,
                compilationUnit.getImportedTypes());
    }

    public static Field getJavaBeansField(IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable) {
        Field field = getBasicJavaBeansField(introspectedColumn);
        addGeneratedJavaDoc(field, context, introspectedColumn, introspectedTable);
        return field;
    }

    public static Field getJavaBeansFieldWithGeneratedAnnotation(IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable,
            CompilationUnit compilationUnit) {
        Field field = getBasicJavaBeansField(introspectedColumn);
        addGeneratedAnnotation(field, context, introspectedColumn, introspectedTable, compilationUnit);
        return field;
    }

    private static Field getBasicJavaBeansField(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn
                .getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Field field = new Field(property, fqjt);
        field.setVisibility(JavaVisibility.PRIVATE);

        return field;
    }

    private static void addGeneratedJavaDoc(Field field, Context context, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable) {
        context.getCommentGenerator().addFieldComment(field,
                introspectedTable, introspectedColumn);
    }

    private static void addGeneratedAnnotation(Field field, Context context, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        context.getCommentGenerator().addFieldAnnotation(field, introspectedTable, introspectedColumn,
                compilationUnit.getImportedTypes());
    }

    public static Method getJavaBeansSetter(IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable) {
        Method method = getBasicJavaBeansSetter(introspectedColumn);
        addGeneratedSetterJavaDoc(method, introspectedColumn, context, introspectedTable);
        return method;
    }

    public static Method getJavaBeansSetterWithGeneratedAnnotation(IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        Method method = getBasicJavaBeansSetter(introspectedColumn);
        addGeneratedSetterAnnotation(method, introspectedColumn, context, introspectedTable, compilationUnit);
        return method;
    }

    private static Method getBasicJavaBeansSetter(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn
                .getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Method method = new Method(getSetterMethodName(property));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, property));

        StringBuilder sb = new StringBuilder();
        if (introspectedColumn.isStringColumn() && isTrimStringsEnabled(introspectedColumn)) {
            sb.append("this."); //$NON-NLS-1$
            sb.append(property);
            sb.append(" = "); //$NON-NLS-1$
            sb.append(property);
            sb.append(" == null ? null : "); //$NON-NLS-1$
            sb.append(property);
            sb.append(".trim();"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        } else {
            sb.append("this."); //$NON-NLS-1$
            sb.append(property);
            sb.append(" = "); //$NON-NLS-1$
            sb.append(property);
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        return method;
    }

    private static void addGeneratedSetterJavaDoc(Method method, IntrospectedColumn introspectedColumn, Context context,
            IntrospectedTable introspectedTable) {
        context.getCommentGenerator().addSetterComment(method,
                introspectedTable, introspectedColumn);
    }

    private static void addGeneratedSetterAnnotation(Method method, IntrospectedColumn introspectedColumn,
            Context context,
            IntrospectedTable introspectedTable, CompilationUnit compilationUnit) {
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, introspectedColumn,
                compilationUnit.getImportedTypes());
    }

    private static boolean isTrimStringsEnabled(Context context) {
        Properties properties = context
                .getJavaModelGeneratorConfiguration().getProperties();
        return isTrue(properties
                .getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS));
    }

    private static boolean isTrimStringsEnabled(IntrospectedTable table) {
        TableConfiguration tableConfiguration = table.getTableConfiguration();
        String trimSpaces = tableConfiguration.getProperties().getProperty(
                PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
        if (trimSpaces != null) {
            return isTrue(trimSpaces);
        }
        return isTrimStringsEnabled(table.getContext());
    }

    private static boolean isTrimStringsEnabled(IntrospectedColumn column) {
        String trimSpaces = column.getProperties().getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
        if (trimSpaces != null) {
            return isTrue(trimSpaces);
        }
        return isTrimStringsEnabled(column.getIntrospectedTable());
    }
}
