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
package org.mybatis.generator.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This plugin adds a CacheNamespace annotation to generated Java or Kotlin mapper interfaces.
 * The plugin accepts the following properties (all are optional):
 *
 * <ul>
 *   <li>cache_blocking</li>
 *   <li>cache_flushInterval</li>
 *   <li>cache_readWrite</li>
 *   <li>cache_size</li>
 *   <li>cache_implementation</li>
 *   <li>cache_eviction</li>
 *   <li>cache_skip</li>
 * </ul>
 *
 * <p>All properties (except cache_skip) correspond to properties of the MyBatis CacheNamespace annotation.
 * Most properties are passed "as is" to the corresponding properties of the generated
 * annotation.  The properties "cache_implementation" and "cache_eviction" must be fully qualified class names.
 * If specified, the values
 * will be added to the import list of the mapper file, and the short names will be used in the generated annotation.
 * All properties can be specified at the table level, or on the
 * plugin element.  The property on the table element will override any
 * property on the plugin element.
 *
 * <p>If the "cache_skip" property is set to "true" - either on the plugin or on a specific table,
 * the annotation will not be applied to the generated interface.
 *
 * @author Jeff Butler
 */
public class CacheNamespacePlugin extends PluginAdapter {

    public enum CacheProperty {
        BLOCKING("cache_blocking", "blocking", false), //$NON-NLS-1$ //$NON-NLS-2$
        FLUSH_INTERVAL("cache_flushInterval", "flushInterval", false), //$NON-NLS-1$ //$NON-NLS-2$
        READ_WRITE("cache_readWrite", "readWrite", false), //$NON-NLS-1$ //$NON-NLS-2$
        SIZE("cache_size", "size", false), //$NON-NLS-1$ //$NON-NLS-2$
        IMPLEMENTATION("cache_implementation", "implementation", true), //$NON-NLS-1$ //$NON-NLS-2$
        EVICTION("cache_eviction", "eviction", true); //$NON-NLS-1$ //$NON-NLS-2$

        private final String propertyName;
        private final String attributeName;
        private final boolean isClassName;

        CacheProperty(String propertyName, String attributeName, boolean isClassName) {
            this.propertyName = propertyName;
            this.attributeName = attributeName;
            this.isClassName = isClassName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public boolean isClassName() {
            return isClassName;
        }
    }

    @Override
    public boolean validate(List<String> arg0) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        if (!skip(introspectedTable)) {
            interfaze.addImportedType(
                    new FullyQualifiedJavaType("org.apache.ibatis.annotations.CacheNamespace")); //$NON-NLS-1$

            Arrays.stream(CacheProperty.values())
                    .filter(CacheProperty::isClassName)
                    .map(cp -> getRawPropertyValue(introspectedTable, cp.getPropertyName()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(FullyQualifiedJavaType::new)
                    .forEach(interfaze::addImportedType);

            interfaze.addAnnotation(calculateAnnotation(introspectedTable, ".class")); //$NON-NLS-1$
        }

        return true;
    }

    @Override
    public boolean mapperGenerated(KotlinFile mapperFile, KotlinType mapper, IntrospectedTable introspectedTable) {
        if (!skip(introspectedTable)) {
            mapperFile.addImport("org.apache.ibatis.annotations.CacheNamespace"); //$NON-NLS-1$

            Arrays.stream(CacheProperty.values())
                    .filter(CacheProperty::isClassName)
                    .map(cp -> getRawPropertyValue(introspectedTable, cp.getPropertyName()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(mapperFile::addImport);

            mapper.addAnnotation(calculateAnnotation(introspectedTable, "::class")); //$NON-NLS-1$
        }

        return true;
    }

    private boolean skip(IntrospectedTable introspectedTable) {
        return getRawPropertyValue(introspectedTable, "cache_skip") //$NON-NLS-1$
                .map("true"::equalsIgnoreCase) //$NON-NLS-1$
                .orElse(false);
    }

    private String calculateAnnotation(IntrospectedTable introspectedTable, String classAccessor) {
        String attributes = Arrays.stream(CacheProperty.values())
                .map(cp -> calculateAttribute(introspectedTable, cp, classAccessor))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(", ")); //$NON-NLS-1$

        if (StringUtility.stringHasValue(attributes)) {
            return "@CacheNamespace(" + attributes + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            return "@CacheNamespace"; //$NON-NLS-1$
        }
    }

    private Optional<String> calculateAttribute(IntrospectedTable introspectedTable,
                                                CacheProperty cacheProperty,
                                                String classAccessor) {
        return getPropertyValueForAttribute(introspectedTable, cacheProperty, classAccessor)
                .map(v -> String.format("%s = %s", cacheProperty.getAttributeName(), v)); //$NON-NLS-1$
    }

    private Optional<String> getPropertyValueForAttribute(IntrospectedTable introspectedTable,
                                                          CacheProperty cacheProperty,
                                                          String classAccessor) {
        Optional<String> value = getRawPropertyValue(introspectedTable, cacheProperty.getPropertyName());

        if (cacheProperty.isClassName()) {
            value = value.map(FullyQualifiedJavaType::new)
                    .map(FullyQualifiedJavaType::getShortName)
                    .map(s -> s + classAccessor);
        }

        return value;
    }

    private Optional<String> getRawPropertyValue(IntrospectedTable introspectedTable, String propertyName) {
        String value = introspectedTable.getTableConfigurationProperty(propertyName);
        if (value == null) {
            value = properties.getProperty(propertyName);
        }

        if (StringUtility.stringHasValue(value)) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }
}
