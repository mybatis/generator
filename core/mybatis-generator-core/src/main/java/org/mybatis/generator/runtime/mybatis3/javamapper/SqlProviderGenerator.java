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
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderApplyWhereMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderCountByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderDeleteByExampleMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderInsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderSelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderSelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderUpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderUpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderUpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider.ProviderUpdateByPrimaryKeySelectiveMethodGenerator;

public class SqlProviderGenerator extends AbstractJavaGenerator {

    public SqlProviderGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.18", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        boolean addApplyWhereMethod = addCountByExampleMethod(topLevelClass);
        addApplyWhereMethod |= addDeleteByExampleMethod(topLevelClass);
        addInsertSelectiveMethod(topLevelClass);
        addApplyWhereMethod |= addSelectByExampleWithBLOBsMethod(topLevelClass);
        addApplyWhereMethod |= addSelectByExampleWithoutBLOBsMethod(topLevelClass);
        addApplyWhereMethod |= addUpdateByExampleSelectiveMethod(topLevelClass);
        addApplyWhereMethod |= addUpdateByExampleWithBLOBsMethod(topLevelClass);
        addApplyWhereMethod |= addUpdateByExampleWithoutBLOBsMethod(topLevelClass);
        addUpdateByPrimaryKeySelectiveMethod(topLevelClass);

        if (addApplyWhereMethod) {
            addApplyWhereMethod(topLevelClass);
        }

        List<CompilationUnit> answer = new ArrayList<>();

        if (!topLevelClass.getMethods().isEmpty()
                && pluginAggregator.providerGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }

        return answer;
    }

    protected boolean addCountByExampleMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderCountByExampleMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addDeleteByExampleMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderDeleteByExampleMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected void addInsertSelectiveMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderInsertSelectiveMethodGenerator.Builder()).build();

        CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addSelectByExampleWithBLOBsMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderSelectByExampleWithBLOBsMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addSelectByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderSelectByExampleWithoutBLOBsMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addUpdateByExampleSelectiveMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderUpdateByExampleSelectiveMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addUpdateByExampleWithBLOBsMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderUpdateByExampleWithBLOBsMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected boolean addUpdateByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderUpdateByExampleWithoutBLOBsMethodGenerator.Builder()).build();

        return CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderUpdateByPrimaryKeySelectiveMethodGenerator.Builder()).build();

        CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    protected void addApplyWhereMethod(TopLevelClass topLevelClass) {
        var generator = initializeSubBuilder(new ProviderApplyWhereMethodGenerator.Builder()).build();

        CodeGenUtils.executeClassMethodGenerator(topLevelClass, generator);
    }

    public static class Builder extends AbstractJavaGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public SqlProviderGenerator build() {
            return new SqlProviderGenerator(this);
        }
    }
}
