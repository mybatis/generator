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
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.MixedMapperGenerator;

/**
 * This class overrides the base mapper to provide annotated methods for the
 * methods that don't require SQL provider support.
 *
 * @author Jeff Butler
 */
public class MixedClientGenerator extends JavaMapperGenerator {

    public MixedClientGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            var builder = new AnnotatedDeleteByPrimaryKeyMethodGenerator.Builder().isSimple(false);
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            var builder = new AnnotatedInsertMethodGenerator.Builder().isSimple(false);
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            var builder = new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder()
                    .useResultMapIfAvailable(true)
                    .isSimple(false);
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            var builder = new AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            var builder = new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder().isSimple(false);
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    public Optional<AbstractXmlGenerator> getMatchedXMLGenerator() {
        var generator = initializeSubBuilder(new MixedMapperGenerator.Builder()).build();
        return Optional.of(generator);
    }

    public static class Builder extends JavaMapperGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MixedClientGenerator build() {
            return new MixedClientGenerator(this);
        }
    }
}
