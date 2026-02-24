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
package org.mybatis.generator.runtime.mybatis3.model;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.runtime.common.RootClassAndInterfaceUtility;

public class PrimaryKeyGenerator extends AbstractJavaGenerator {

    protected PrimaryKeyGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.7", table.toString())); //$NON-NLS-1$

        TopLevelClass topLevelClass = new TopLevelClass(introspectedTable.getPrimaryKeyType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        RootClassAndInterfaceUtility.getRootClass(introspectedTable).ifPresent(rc -> {
            topLevelClass.setSuperClass(rc);
            topLevelClass.addImportedType(rc);
        });

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);

            if (!introspectedTable.isImmutable()) {
                Method method = topLevelClass.generateBasicConstructor();
                commentGenerator.addGeneralMethodComment(method, introspectedTable);
                topLevelClass.addMethod(method);
            }
        }

        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        Optional<RootClassInfo> rootClassInfo = RootClassAndInterfaceUtility.getRootClass(introspectedTable)
                .map(rc -> RootClassInfo.getInstance(rc, warnings));
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            if (rootClassInfo.map(rci -> rci.containsProperty(introspectedColumn)).orElse(false)) {
                continue;
            }

            Field field = getJavaBeansField(introspectedColumn, commentGenerator, introspectedTable);
            if (pluginAggregator.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }

            Method method = getJavaBeansGetter(introspectedColumn, commentGenerator, introspectedTable);
            if (pluginAggregator.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn,
                    introspectedTable, Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addMethod(method);
            }

            if (!introspectedTable.isImmutable()) {
                method = getJavaBeansSetter(introspectedColumn, commentGenerator, introspectedTable);
                if (pluginAggregator.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn,
                        introspectedTable, Plugin.ModelClassType.PRIMARY_KEY)) {
                    topLevelClass.addMethod(method);
                }
            }
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }

        return answer;
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method(topLevelClass.getType().getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                    introspectedColumn.getJavaProperty()));
            method.addBodyLine(JavaBeansUtil.generateFieldSetterForConstructor(introspectedColumn));
        }

        topLevelClass.addMethod(method);
    }

    public static class Builder extends AbstractJavaGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public PrimaryKeyGenerator build() {
            return new PrimaryKeyGenerator(this);
        }
    }
}
