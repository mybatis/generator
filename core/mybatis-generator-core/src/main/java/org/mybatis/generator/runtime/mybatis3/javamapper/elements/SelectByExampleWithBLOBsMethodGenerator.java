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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class SelectByExampleWithBLOBsMethodGenerator extends AbstractJavaMapperMethodGenerator {

    protected SelectByExampleWithBLOBsMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            return Optional.empty();
        }

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method(introspectedTable.getSelectByExampleWithBLOBsStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            // the blob fields must be rolled up into the base class
            listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$

        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        return JavaMethodAndImports.withMethod(method)
                .withImports(importedTypes)
                .withExtraMethodParts(extraMethodParts())
                .buildOptional();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public SelectByExampleWithBLOBsMethodGenerator build() {
            return new SelectByExampleWithBLOBsMethodGenerator(this);
        }
    }
}
