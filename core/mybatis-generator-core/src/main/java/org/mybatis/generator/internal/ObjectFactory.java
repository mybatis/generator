/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.runtime.dynamic.sql.IntrospectedTableMyBatis3DynamicSqlImpl;

/**
 * This class creates the different objects needed by the generator.
 *
 * @author Jeff Butler
 */
public class ObjectFactory {

    private static List<ClassLoader> externalClassLoaders;

    static {
        externalClassLoaders = new ArrayList<>();
    }

    /**
     * Utility class. No instances allowed.
     */
    private ObjectFactory() {
        super();
    }

    /**
     * Clears the class loaders.  This method should be called at the beginning of
     * a generation run so that and change to the classloading configuration
     * will be reflected.  For example, if the eclipse launcher changes configuration
     * it might not be updated if eclipse hasn't been restarted.
     * 
     */
    public static void reset() {
        externalClassLoaders.clear();
    }

    /**
     * Adds a custom classloader to the collection of classloaders searched for "external" classes. These are classes
     * that do not depend on any of the generator's classes or interfaces. Examples are JDBC drivers, root classes, root
     * interfaces, etc.
     *
     * @param classLoader
     *            the class loader
     */
    public static synchronized void addExternalClassLoader(
            ClassLoader classLoader) {
        ObjectFactory.externalClassLoaders.add(classLoader);
    }

    /**
     * Returns a class loaded from the context classloader, or the classloader supplied by a client. This is
     * appropriate for JDBC drivers, model root classes, etc. It is not appropriate for any class that extends one of
     * the supplied classes or interfaces.
     *
     * @param type
     *            the type
     * @return the Class loaded from the external classloader
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public static Class<?> externalClassForName(String type)
            throws ClassNotFoundException {

        Class<?> clazz;

        for (ClassLoader classLoader : externalClassLoaders) {
            try {
                clazz = Class.forName(type, true, classLoader);
                return clazz;
            } catch (Throwable e) {
                // ignore - fail safe below
            }
        }

        return internalClassForName(type);
    }

    public static Object createExternalObject(String type) {
        Object answer;

        try {
            Class<?> clazz = externalClassForName(type);
            answer = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString(
                    "RuntimeError.6", type), e); //$NON-NLS-1$
        }

        return answer;
    }

    public static Class<?> internalClassForName(String type)
            throws ClassNotFoundException {
        Class<?> clazz = null;

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = Class.forName(type, true, cl);
        } catch (Exception e) {
            // ignore - failsafe below
        }

        if (clazz == null) {
            clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
        }

        return clazz;
    }

    public static URL getResource(String resource) {
        URL url;

        for (ClassLoader classLoader : externalClassLoaders) {
            url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(resource);

        if (url == null) {
            url = ObjectFactory.class.getClassLoader().getResource(resource);
        }

        return url;
    }

    public static Object createInternalObject(String type) {
        Object answer;

        try {
            Class<?> clazz = internalClassForName(type);

            answer = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString(
                    "RuntimeError.6", type), e); //$NON-NLS-1$

        }

        return answer;
    }

    public static JavaTypeResolver createJavaTypeResolver(Context context,
            List<String> warnings) {
        JavaTypeResolverConfiguration config = context
                .getJavaTypeResolverConfiguration();
        String type;

        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
            if ("DEFAULT".equalsIgnoreCase(type)) { //$NON-NLS-1$
                type = JavaTypeResolverDefaultImpl.class.getName();
            }
        } else {
            type = JavaTypeResolverDefaultImpl.class.getName();
        }

        JavaTypeResolver answer = (JavaTypeResolver) createInternalObject(type);
        answer.setWarnings(warnings);

        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }

        answer.setContext(context);

        return answer;
    }

    public static Plugin createPlugin(Context context,
            PluginConfiguration pluginConfiguration) {
        Plugin plugin = (Plugin) createInternalObject(pluginConfiguration
                .getConfigurationType());
        plugin.setContext(context);
        plugin.setProperties(pluginConfiguration.getProperties());
        return plugin;
    }

    public static CommentGenerator createCommentGenerator(Context context) {

        CommentGeneratorConfiguration config = context
                .getCommentGeneratorConfiguration();
        CommentGenerator answer;

        String type;
        if (config == null || config.getConfigurationType() == null) {
            type = DefaultCommentGenerator.class.getName();
        } else {
            type = config.getConfigurationType();
        }

        answer = (CommentGenerator) createInternalObject(type);

        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }

        return answer;
    }

    public static ConnectionFactory createConnectionFactory(Context context) {

        ConnectionFactoryConfiguration config = context
                .getConnectionFactoryConfiguration();
        ConnectionFactory answer;

        String type;
        if (config == null || config.getConfigurationType() == null) {
            type = JDBCConnectionFactory.class.getName();
        } else {
            type = config.getConfigurationType();
        }

        answer = (ConnectionFactory) createInternalObject(type);

        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }

        return answer;
    }

    public static JavaFormatter createJavaFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER);
        if (!stringHasValue(type)) {
            type = DefaultJavaFormatter.class.getName();
        }

        JavaFormatter answer = (JavaFormatter) createInternalObject(type);

        answer.setContext(context);

        return answer;
    }

    public static XmlFormatter createXmlFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_XML_FORMATTER);
        if (!stringHasValue(type)) {
            type = DefaultXmlFormatter.class.getName();
        }

        XmlFormatter answer = (XmlFormatter) createInternalObject(type);

        answer.setContext(context);

        return answer;
    }

    public static IntrospectedTable createIntrospectedTable(
            TableConfiguration tableConfiguration, FullyQualifiedTable table,
            Context context) {

        IntrospectedTable answer = createIntrospectedTableForValidation(context);
        answer.setFullyQualifiedTable(table);
        answer.setTableConfiguration(tableConfiguration);

        return answer;
    }

    /**
     * Creates an introspected table implementation that is only usable for validation .
     * 
     *
     * @param context
     *            the context
     * @return the introspected table
     */
    public static IntrospectedTable createIntrospectedTableForValidation(Context context) {
        String type = context.getTargetRuntime();
        if (!stringHasValue(type)) {
            type = IntrospectedTableMyBatis3Impl.class.getName();
        } else if ("MyBatis3".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableMyBatis3Impl.class.getName();
        } else if ("MyBatis3Simple".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableMyBatis3SimpleImpl.class.getName();
        } else if ("MyBatis3DynamicSql".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableMyBatis3DynamicSqlImpl.class.getName();
        }

        IntrospectedTable answer = (IntrospectedTable) createInternalObject(type);
        answer.setContext(context);

        return answer;
    }

    public static IntrospectedColumn createIntrospectedColumn(Context context) {
        String type = context.getIntrospectedColumnImpl();
        if (!stringHasValue(type)) {
            type = IntrospectedColumn.class.getName();
        }

        IntrospectedColumn answer = (IntrospectedColumn) createInternalObject(type);
        answer.setContext(context);

        return answer;
    }
}
