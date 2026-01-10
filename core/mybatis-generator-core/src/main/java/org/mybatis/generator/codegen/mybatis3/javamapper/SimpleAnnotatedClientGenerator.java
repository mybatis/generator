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
package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectAllMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

import java.util.Optional;

public class SimpleAnnotatedClientGenerator extends SimpleJavaClientGenerator {

    public SimpleAnnotatedClientGenerator(String project) {
        super(project, false);
    }

    @Override
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            var builder = new AnnotatedDeleteByPrimaryKeyMethodGenerator.Builder().isSimple(true);
            initializeAndExecuteGenerator(builder, interfaze);
        }
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            var builder = new AnnotatedInsertMethodGenerator.Builder().isSimple(true);
            initializeAndExecuteGenerator(builder, interfaze);
        }
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            var builder = new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder()
                    .useResultMapIfAvailable(false)
                    .isSimple(true);
            initializeAndExecuteGenerator(builder, interfaze);
        }
    }

    @Override
    protected void addSelectAllMethod(Interface interfaze) {
        var builder = new AnnotatedSelectAllMethodGenerator.Builder();
        initializeAndExecuteGenerator(builder, interfaze);
    }

    @Override
    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            var builder = new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder().isSimple(true);
            initializeAndExecuteGenerator(builder, interfaze);
        }
    }

    @Override
    public Optional<AbstractXmlGenerator> getMatchedXMLGenerator() {
        return Optional.empty();
    }
}
