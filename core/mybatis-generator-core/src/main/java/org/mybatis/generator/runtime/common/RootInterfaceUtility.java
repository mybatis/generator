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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.runtime.CodeGenUtils;

public class RootInterfaceUtility {
    public static void addRootInterfaceIsNecessary(Interface mapper, IntrospectedTable introspectedTable) {
        String rootInterface = CodeGenUtils.findTableOrClientProperty(PropertyRegistry.ANY_ROOT_INTERFACE,
                introspectedTable);

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType baseInterface = new FullyQualifiedJavaType(rootInterface);
            String inject = CodeGenUtils.findTableOrClientProperty(
                    PropertyRegistry.ANY_INJECT_MODEL_INTO_ROOT_INTERFACE, introspectedTable);

            FullyQualifiedJavaType rootInterfaceType;
            if (Boolean.parseBoolean(inject)) {
                rootInterfaceType = injectRootModel(baseInterface, calculateRootModel(introspectedTable));
            } else {
                rootInterfaceType = baseInterface;
            }

            mapper.addSuperInterface(rootInterfaceType);
            mapper.addImportedTypes(rootInterfaceType.getImportList().stream()
                    .map(FullyQualifiedJavaType::new).collect(Collectors.toSet()));
        }
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
