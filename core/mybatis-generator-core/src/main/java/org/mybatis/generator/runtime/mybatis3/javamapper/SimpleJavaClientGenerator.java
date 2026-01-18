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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.runtime.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectAllMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.SimpleXMLMapperGenerator;

public class SimpleJavaClientGenerator extends AbstractJavaClientGenerator {

    public SimpleJavaClientGenerator(Builder builder) {
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

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration()
                    .map(c -> c.getProperty(PropertyRegistry.ANY_ROOT_INTERFACE))
                    .orElse(null);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }

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
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            initializeSubBuilder(new DeleteByPrimaryKeyMethodGenerator.Builder().isSimple(true))
                    .build().addInterfaceElements(interfaze);
        }
    }

    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            initializeSubBuilder(new InsertMethodGenerator.Builder().isSimple(true))
                    .build().addInterfaceElements(interfaze);
        }
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            initializeSubBuilder(new SelectByPrimaryKeyMethodGenerator.Builder().isSimple(true))
                    .build().addInterfaceElements(interfaze);
        }
    }

    protected void addSelectAllMethod(Interface interfaze) {
        initializeSubBuilder(new SelectAllMethodGenerator.Builder())
                    .build().addInterfaceElements(interfaze);
    }

    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            initializeSubBuilder(new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder())
                    .build().addInterfaceElements(interfaze);
        }
    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return Collections.emptyList();
    }

    @Override
    public Optional<AbstractXmlGenerator> getMatchedXMLGenerator() {
        var generator = initializeSubBuilder(new SimpleXMLMapperGenerator.Builder())
                .withMyBatis3SqlMapNamespace(introspectedTable.getMyBatis3SqlMapNamespace().orElseThrow(
                        () -> new InternalException(getString("RuntimeError.24", context.getId())))) //$NON-NLS-1$
                .build();
        return Optional.of(generator);
    }

    public static class Builder extends AbstractJavaClientGenerator.AbstractJavaClientGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public SimpleJavaClientGenerator build() {
            return new SimpleJavaClientGenerator(this);
        }

        @Override
        protected boolean requiresXMLGenerator() {
            return true;
        }
    }
}
