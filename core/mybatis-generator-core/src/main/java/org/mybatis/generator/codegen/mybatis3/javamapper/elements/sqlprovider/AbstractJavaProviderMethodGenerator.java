/*
 *    Copyright 2006-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;

public abstract class AbstractJavaProviderMethodGenerator extends AbstractGenerator {

    protected static final FullyQualifiedJavaType BUILDER_IMPORT =
            new FullyQualifiedJavaType("org.apache.ibatis.jdbc.SQL"); //$NON-NLS-1$

    protected AbstractJavaProviderMethodGenerator() {
        super();
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

        importedTypes.add(BUILDER_IMPORT);

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
}
