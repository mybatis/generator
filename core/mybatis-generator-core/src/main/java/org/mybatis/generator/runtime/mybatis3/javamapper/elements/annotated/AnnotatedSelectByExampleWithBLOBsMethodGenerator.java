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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.SelectByExampleWithBLOBsMethodGenerator;

public class AnnotatedSelectByExampleWithBLOBsMethodGenerator extends SelectByExampleWithBLOBsMethodGenerator {

    protected AnnotatedSelectByExampleWithBLOBsMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected List<String> extraMethodAnnotations() {
        List<String> annotations = new ArrayList<>();
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String s = "@SelectProvider(type=" //$NON-NLS-1$
                + fqjt.getShortName()
                + ".class, method=\"" //$NON-NLS-1$
                + introspectedTable.getSelectByExampleWithBLOBsStatementId()
                + "\")";//$NON-NLS-1$
        annotations.add(s);

        annotations.addAll(getAnnotatedResultAnnotations(introspectedTable.getNonPrimaryKeyColumns()));

        return annotations;
    }

    @Override
    protected Set<FullyQualifiedJavaType> extraImports() {
        Set<FullyQualifiedJavaType> answer = new HashSet<>(getAnnotatedSelectImports());

        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            answer.addAll(getAnnotatedResultImports(introspectedColumn, introspectedTable.isConstructorBased()));
        }

        answer.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider")); //$NON-NLS-1$

        return answer;
    }

    public static class Builder extends SelectByExampleWithBLOBsMethodGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedSelectByExampleWithBLOBsMethodGenerator build() {
            return new AnnotatedSelectByExampleWithBLOBsMethodGenerator(this);
        }
    }
}
