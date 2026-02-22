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
package org.mybatis.generator.runtime.mybatis3.javamapper;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.common.RootInterfaceUtility;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectAllMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

public class SimpleJavaMapperGenerator extends AbstractJavaGenerator {

    public SimpleJavaMapperGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        RootInterfaceUtility.addRootInterfaceIsNecessary(interfaze, introspectedTable);

        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addSelectAllMethod(interfaze);
        addUpdateByPrimaryKeyMethod(interfaze);

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.clientGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        answer.addAll(getExtraCompilationUnits());

        return answer;
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new DeleteByPrimaryKeyMethodGenerator.Builder())
                .isSimple(true)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addInsertMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertMethodGenerator.Builder())
                .isSimple(true)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectByPrimaryKeyMethodGenerator.Builder())
                .isSimple(true)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectAllMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectAllMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder())
                .isSimple(true)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return Collections.emptyList();
    }

    public static class Builder extends AbstractJavaGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public SimpleJavaMapperGenerator build() {
            return new SimpleJavaMapperGenerator(this);
        }
    }
}
