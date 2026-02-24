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

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansFieldWithGeneratedAnnotation;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetterWithGeneratedAnnotation;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetterWithGeneratedAnnotation;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * This model generator builds a flat model. It will build an immutable or constructor-based model depending
 * on configuration settings.
 *
 * @author Jeff Butler
 */
public class SimpleModelGenerator extends AbstractJavaGenerator {

    public SimpleModelGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.8", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        getSuperClass().ifPresent(sc -> {
            topLevelClass.setSuperClass(sc);
            topLevelClass.addImportedType(sc);
        });

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);

            if (!introspectedTable.isImmutable()) {
                topLevelClass.addMethod(getDefaultConstructorWithGeneratedAnnotation(topLevelClass));
            }
        }

        Optional<RootClassInfo> rootClassInfo = RootClassAndInterfaceUtility.getRootClass(introspectedTable)
                .map(rc -> RootClassInfo.getInstance(rc, warnings));
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (rootClassInfo.map(rci -> rci.containsProperty(introspectedColumn)).orElse(false)) {
                continue;
            }

            Field field = getJavaBeansFieldWithGeneratedAnnotation(introspectedColumn, commentGenerator,
                    introspectedTable, topLevelClass);
            if (pluginAggregator.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }

            Method method = getJavaBeansGetterWithGeneratedAnnotation(introspectedColumn, commentGenerator,
                    introspectedTable, topLevelClass);
            if (pluginAggregator.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn,
                    introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method);
            }

            if (!introspectedTable.isImmutable()) {
                method = getJavaBeansSetterWithGeneratedAnnotation(introspectedColumn, commentGenerator,
                        introspectedTable, topLevelClass);
                if (pluginAggregator.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn,
                        introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                    topLevelClass.addMethod(method);
                }
            }
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private Optional<FullyQualifiedJavaType> getSuperClass() {
        return RootClassAndInterfaceUtility.getRootClass(introspectedTable).map(FullyQualifiedJavaType::new);
    }

    private Method getDefaultConstructorWithGeneratedAnnotation(TopLevelClass topLevelClass) {
        Method method = topLevelClass.generateBasicConstructor();
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable,
                topLevelClass.getImportedTypes());
        return method;
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method(topLevelClass.getType().getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());

        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
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
        public SimpleModelGenerator build() {
            return new SimpleModelGenerator(this);
        }
    }
}
