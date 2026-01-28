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

import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByExampleWithBLOBsMethodGenerator;

public class AnnotatedUpdateByExampleWithBLOBsMethodGenerator extends UpdateByExampleWithBLOBsMethodGenerator {

    protected AnnotatedUpdateByExampleWithBLOBsMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected List<String> extraMethodAnnotations() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String annotation = "@UpdateProvider(type=" //$NON-NLS-1$
                + fqjt.getShortName()
                + ".class, method=\"" //$NON-NLS-1$
                + introspectedTable.getUpdateByExampleWithBLOBsStatementId()
                + "\")"; //$NON-NLS-1$

        return List.of(annotation);
    }

    @Override
    protected Set<FullyQualifiedJavaType> extraImports() {
        return Set.of(new FullyQualifiedJavaType("org.apache.ibatis.annotations.UpdateProvider")); //$NON-NLS-1$
    }

    public static class Builder extends UpdateByExampleWithBLOBsMethodGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedUpdateByExampleWithBLOBsMethodGenerator build() {
            return new AnnotatedUpdateByExampleWithBLOBsMethodGenerator(this);
        }
    }
}
