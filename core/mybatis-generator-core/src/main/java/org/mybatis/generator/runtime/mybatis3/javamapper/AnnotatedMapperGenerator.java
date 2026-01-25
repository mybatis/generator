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

import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedCountByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedInsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

public class AnnotatedMapperGenerator extends JavaMapperGenerator {

    public AnnotatedMapperGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected void addCountByExampleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedCountByExampleMethodGenerator.Builder()).build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addDeleteByExampleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedDeleteByExampleMethodGenerator.Builder()).build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedDeleteByPrimaryKeyMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedInsertMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addInsertSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedInsertSelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedSelectByExampleWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedSelectByExampleWithoutBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder())
                .isSimple(false)
                .useResultMapIfAvailable(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByExampleSelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByExampleWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator.Builder())
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder())
                .isSimple(false)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    @Override
    public List<CompilationUnit> getExtraCompilationUnits() {
        return initializeSubBuilder(new SqlProviderGenerator.Builder().withProject(getProject()))
                .build()
                .getCompilationUnits();
    }

    @Override
    public Optional<AbstractXmlGenerator> getMatchedXMLGenerator() {
        // No XML required by the annotated client
        return Optional.empty();
    }

    public static class Builder extends JavaMapperGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedMapperGenerator build() {
            return new AnnotatedMapperGenerator(this);
        }
    }
}
