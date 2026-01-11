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

import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedCountByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

public class AnnotatedClientGenerator extends JavaMapperGenerator {

    public AnnotatedClientGenerator(String project) {
        super(project, false);
    }

    @Override
    protected void addCountByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateCountByExample()) {
            var builder = new AnnotatedCountByExampleMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addDeleteByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            var builder = new AnnotatedDeleteByExampleMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
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
    protected void addInsertSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            var builder = new AnnotatedInsertSelectiveMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            var builder = new AnnotatedSelectByExampleWithBLOBsMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            var builder = new AnnotatedSelectByExampleWithoutBLOBsMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            var builder = new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            var builder = new AnnotatedUpdateByExampleSelectiveMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            var builder = new AnnotatedUpdateByExampleWithBLOBsMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            var builder = new AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator.Builder();
            initializeSubBuilder(builder).build().addInterfaceElements(interfaze);
        }
    }

    @Override
    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            var builder = new AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator.Builder();
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
}
