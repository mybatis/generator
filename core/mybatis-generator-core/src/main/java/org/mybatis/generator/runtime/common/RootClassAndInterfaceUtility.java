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
package org.mybatis.generator.runtime.common;

import java.util.Optional;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.config.PropertyRegistry;

public class RootClassAndInterfaceUtility {
    public static Optional<String> getRootClass(IntrospectedTable introspectedTable) {
        return introspectedTable.findTableOrModelGeneratorProperty(PropertyRegistry.ANY_ROOT_CLASS);
    }

    public static void addRootInterfaceIfNecessary(Interface mapper, IntrospectedTable introspectedTable) {
        introspectedTable.findTableOrClientGeneratorProperty(PropertyRegistry.ANY_ROOT_INTERFACE)
                .map(FullyQualifiedJavaType::new)
                .ifPresent(rootInterface -> addRootInterface(mapper, introspectedTable, rootInterface));
    }

    private static void addRootInterface(Interface mapper, IntrospectedTable introspectedTable,
                                         FullyQualifiedJavaType rootInterface) {
        boolean inject = introspectedTable.findTableOrClientGeneratorPropertyAsBoolean(
                PropertyRegistry.ANY_INJECT_MODEL_INTO_ROOT_INTERFACE);

        FullyQualifiedJavaType rootInterfaceType;
        if (inject) {
            rootInterfaceType = injectRootModel(rootInterface, calculateRootModel(introspectedTable));
        } else {
            rootInterfaceType = rootInterface;
        }

        mapper.addSuperInterface(rootInterfaceType);
        mapper.addImportedTypes(rootInterfaceType.getImportList().stream()
                .map(FullyQualifiedJavaType::new).collect(Collectors.toSet()));
    }

    private static FullyQualifiedJavaType injectRootModel(FullyQualifiedJavaType baseInterface,
                                                          FullyQualifiedJavaType injectedType) {
        if (baseInterface.getTypeArguments().size() == 1) {
            FullyQualifiedJavaType answer =
                    new FullyQualifiedJavaType(baseInterface.getFullyQualifiedNameWithoutTypeParameters());
            answer.addTypeArgument(new FullyQualifiedJavaType(injectedType.getShortName()));
            return answer;
        } else {
            return baseInterface;
        }
    }

    private static FullyQualifiedJavaType calculateRootModel(IntrospectedTable introspectedTable) {
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            return new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            return new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
    }
}
