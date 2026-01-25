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

import java.util.Optional;

import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

/**
 * This class overrides the base mapper to provide annotated methods for the
 * methods that don't require SQL provider support.
 *
 * @author Jeff Butler
 */
public class MixedMapperGenerator extends JavaMapperGenerator {

    public MixedMapperGenerator(Builder builder) {
        super(builder);
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
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder())
                .isSimple(false)
                .useResultMapIfAvailable(true)
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
    public Optional<AbstractXmlGenerator> getMatchedXMLGenerator() {
        var generator = initializeSubBuilder(new org.mybatis.generator.runtime.mybatis3.xmlmapper.MixedMapperGenerator.Builder()).build();
        return Optional.of(generator);
    }

    public static class Builder extends JavaMapperGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MixedMapperGenerator build() {
            return new MixedMapperGenerator(this);
        }
    }
}
