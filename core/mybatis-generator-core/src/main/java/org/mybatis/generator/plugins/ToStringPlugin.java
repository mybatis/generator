/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.plugins;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ToStringPlugin extends PluginAdapter {

    private boolean useToStringFromRoot;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        useToStringFromRoot = isTrue(properties.getProperty("useToStringFromRoot")); //$NON-NLS-1$
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable,
            TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString"); //$NON-NLS-1$
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override"); //$NON-NLS-1$
        }

        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3_DSQL) {
            context.getCommentGenerator().addGeneralMethodAnnotation(method,
                    introspectedTable, topLevelClass.getImportedTypes());
        } else {
            context.getCommentGenerator().addGeneralMethodComment(method,
                    introspectedTable);
        }

        method.addBodyLine("StringBuilder sb = new StringBuilder();"); //$NON-NLS-1$
        method.addBodyLine("sb.append(getClass().getSimpleName());"); //$NON-NLS-1$
        method.addBodyLine("sb.append(\" [\");"); //$NON-NLS-1$
        method.addBodyLine("sb.append(\"Hash = \").append(hashCode());"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        for (Field field : topLevelClass.getFields()) {
            String property = field.getName();
            sb.setLength(0);
            sb.append("sb.append(\"").append(", ").append(property) //$NON-NLS-1$ //$NON-NLS-2$
                    .append("=\")").append(".append(").append(property) //$NON-NLS-1$ //$NON-NLS-2$
                    .append(");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        }

        method.addBodyLine("sb.append(\"]\");"); //$NON-NLS-1$
        if (useToStringFromRoot && topLevelClass.getSuperClass() != null) {
            method.addBodyLine("sb.append(\", from super class \");"); //$NON-NLS-1$
            method.addBodyLine("sb.append(super.toString());"); //$NON-NLS-1$
        }
        method.addBodyLine("return sb.toString();"); //$NON-NLS-1$

        topLevelClass.addMethod(method);
    }
}
