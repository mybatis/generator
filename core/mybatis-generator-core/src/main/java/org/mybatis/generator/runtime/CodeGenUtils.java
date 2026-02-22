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
package org.mybatis.generator.runtime;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class CodeGenUtils {
    private CodeGenUtils() {
        // Utility class, no instantiation allowed
    }

    public static String generateFieldSetterForConstructor(IntrospectedColumn introspectedColumn) {
        return "this." //$NON-NLS-1$
                + introspectedColumn.getJavaProperty()
                + " = " //$NON-NLS-1$
                + introspectedColumn.getJavaProperty()
                + ';';
    }

    public static void addPartsToMethod(JavaMethodAndImports.Builder builder, Method method,
                                        JavaMethodParts javaMethodParts) {
        for (Parameter parameter : javaMethodParts.getParameters()) {
            method.addParameter(parameter);
        }

        for (String annotation : javaMethodParts.getAnnotations()) {
            method.addAnnotation(annotation);
        }

        method.addBodyLines(javaMethodParts.getBodyLines());
        builder.withImports(javaMethodParts.getImports());
    }

    /**
     * Executes the given interface method generator, calls plugins, and applies the generated method to the interface.
     *
     * @param interfaze The interface to which the method will be added.
     * @param generator The interface method generator to execute.
     * @return true if the method was successfully generated and added to the interface, false otherwise.
     */
    public static boolean executeInterfaceMethodGenerator(Interface interfaze,
                                                          AbstractJavaInterfaceMethodGenerator generator) {
        return generator.generateMethodAndImports()
                .filter(mi -> generator.callPlugins(mi.getMethod(), interfaze))
                .map(mi -> {
                    interfaze.addMethod(mi.getMethod());
                    interfaze.addImportedTypes(mi.getImports());
                    interfaze.addStaticImports(mi.getStaticImports());
                    return true;
                })
                .orElse(false);
    }

    /**
     * Executes the given class method generator, calls plugins, and applies the generated method to the class.
     *
     * @param topLevelClass The class to which the method will be added.
     * @param generator The class method generator to execute.
     * @return true if the method was successfully generated and added to the class, false otherwise.
     */
    public static boolean executeClassMethodGenerator(TopLevelClass topLevelClass,
                                                      AbstractJavaClassMethodGenerator generator) {
        return generator.generateMethodAndImports()
                .filter(mi -> generator.callPlugins(mi.getMethod(), topLevelClass))
                .map(mi -> {
                    topLevelClass.addMethod(mi.getMethod());
                    topLevelClass.addImportedTypes(mi.getImports());
                    topLevelClass.addStaticImports(mi.getStaticImports());
                    return true;
                })
                .orElse(false);
    }

    /**
     * Executes the given XML element generator, calls plugins, and applies the generated element to the parent element.
     *
     * @param parentElement The parent element to which the generated element will be added.
     * @param generator The XML element generator to execute.
     * @return true if the element was successfully generated and added to the parent element, false otherwise.
     */
    public static boolean executeXmlElementGenerator(XmlElement parentElement, AbstractXmlElementGenerator generator) {
        return generator.generateElement()
                .filter(generator::callPlugins)
                .map(mi -> {
                    parentElement.addElement(mi);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Executes the given Kotlin function generator, calls plugins, and applies the generated function and imports to
     * the Kotlin file. This variant expects the generated function to be an extension function.
     *
     * @param kotlinFile The Kotlin file to which the generated function and imports will be added.
     * @param generator The Kotlin function generator to execute.
     * @return true if the function and imports were successfully generated and added to the Kotlin file, false
     *     otherwise.
     */
    public static boolean executeKotlinExtensionFunctionGenerator(KotlinFile kotlinFile,
                                                                  AbstractKotlinFunctionGenerator generator) {
        return generator.generateFunctionAndImports()
                .filter(fi -> generator.callPlugins(fi.getFunction(), kotlinFile))
                .map(mi -> {
                    kotlinFile.addNamedItem(mi.getFunction());
                    kotlinFile.addImports(mi.getImports());
                    return true;
                })
                .orElse(false);
    }

    /**
     * Executes the given Kotlin function generator, calls plugins, and applies the generated function and imports to
     * the Kotlin file. This variant expects the generated function to be a function in a type.
     *
     * @param kotlinFile The Kotlin file to which the generated imports will be added.
     * @param kotlinType The Kotlin type to which the generated function will be added.
     * @param generator The Kotlin function generator to execute.
     * @return true if the function and imports were successfully generated and added to the Kotlin file, false
     *     otherwise.
     */
    public static boolean executeKotlinFunctionGenerator(KotlinFile kotlinFile,
                                                         KotlinType kotlinType,
                                                         AbstractKotlinFunctionGenerator generator) {
        return generator.generateFunctionAndImports()
                .filter(fi -> generator.callPlugins(fi.getFunction(), kotlinFile))
                .map(mi -> {
                    kotlinType.addNamedItem(mi.getFunction());
                    kotlinFile.addImports(mi.getImports());
                    return true;
                })
                .orElse(false);
    }

    public static @Nullable String findTableOrClientProperty(String property, IntrospectedTable introspectedTable) {
        String value = introspectedTable.getTableConfigurationProperty(property);
        if (!stringHasValue(value)) {
            value = introspectedTable.getContext().getJavaClientGeneratorConfiguration()
                    .map(c -> c.getProperty(property))
                    .orElse(null);
        }

        return value;
    }

    public static boolean findTableOrClientPropertyAsBoolean(String property, IntrospectedTable introspectedTable) {
        return Boolean.parseBoolean(findTableOrClientProperty(property, introspectedTable));
    }
}
