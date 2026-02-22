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
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.CountByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.DeleteByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

public class JavaMapperGenerator extends AbstractJavaGenerator {

    public JavaMapperGenerator(Builder builder) {
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

        addCountByExampleMethod(interfaze);
        addDeleteByExampleMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectByExampleWithBLOBsMethod(interfaze);
        addSelectByExampleWithoutBLOBsMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addUpdateByExampleSelectiveMethod(interfaze);
        addUpdateByExampleWithBLOBsMethod(interfaze);
        addUpdateByExampleWithoutBLOBsMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);
        addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
        addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.clientGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        answer.addAll(getExtraCompilationUnits());

        return answer;
    }

    protected void addCountByExampleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new CountByExampleMethodGenerator.Builder()).build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addDeleteByExampleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new DeleteByExampleMethodGenerator.Builder()).build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new DeleteByPrimaryKeyMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addInsertMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertSelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectByExampleWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectByExampleWithoutBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectByPrimaryKeyMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByExampleSelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByExampleWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByExampleWithoutBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeySelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder())
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
        public JavaMapperGenerator build() {
            return new JavaMapperGenerator(this);
        }
    }
}
