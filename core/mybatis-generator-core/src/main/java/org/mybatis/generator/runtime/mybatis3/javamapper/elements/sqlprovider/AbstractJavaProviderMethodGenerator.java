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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements.sqlprovider;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.runtime.mybatis3.MyBatis3FormattingUtilities;

public abstract class AbstractJavaProviderMethodGenerator extends AbstractGenerator {

    protected AbstractJavaProviderMethodGenerator(AbstractJavaProviderMethodGeneratorBuilder<?> builder) {
        super(builder);
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

        importedTypes.add(MyBatis3FormattingUtilities.BUILDER_IMPORT);

        return importedTypes;
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes(String extraType) {
        return initializeImportedTypes(new FullyQualifiedJavaType(extraType));
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes(FullyQualifiedJavaType extraType) {
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes();

        importedTypes.add(extraType);

        return importedTypes;
    }

    public abstract void addClassElements(TopLevelClass topLevelClass);

    public abstract static class AbstractJavaProviderMethodGeneratorBuilder
            <T extends AbstractJavaProviderMethodGeneratorBuilder<T>> extends AbstractGeneratorBuilder<T> {
        public abstract AbstractJavaProviderMethodGenerator build();
    }
}
