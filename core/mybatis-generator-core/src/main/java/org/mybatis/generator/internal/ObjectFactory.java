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
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.Defaults;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * This class creates the different objects needed by the generator.
 *
 * @author Jeff Butler
 */
public class ObjectFactory {

    private static final List<ClassLoader> externalClassLoaders;

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
    public static synchronized void addExternalClassLoader(ClassLoader classLoader) {
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
    @SuppressWarnings("unchecked")
    public static <T> Class<T> externalClassForName(String type, Class<T> t) throws ClassNotFoundException {
        Class<T> clazz;

        for (ClassLoader classLoader : externalClassLoaders) {
            try {
                clazz = (Class<T>) Class.forName(type, true, classLoader);
                return clazz;
            } catch (Exception e) {
                // ignore - fail safe below
            }
        }

        return internalClassForName(type, t);
    }

    public static <T> T createExternalObject(String type, Class<T> t) {
        T answer;

        try {
            Class<T> clazz = externalClassForName(type, t);
            answer = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.6", type), e); //$NON-NLS-1$
        }

        return answer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> internalClassForName(String type, Class<T> t) throws ClassNotFoundException {
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

        return (Class<T>) clazz;
    }

    public static Optional<URL> getResource(String resource) {
        URL url;

        for (ClassLoader classLoader : externalClassLoaders) {
            url = classLoader.getResource(resource);
            if (url != null) {
                return Optional.of(url);
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(resource);

        if (url == null) {
            url = ObjectFactory.class.getClassLoader().getResource(resource);
        }

        return Optional.ofNullable(url);
    }

    public static <T> T createInternalObject(String type, Class<T> t) {
        T answer;

        try {
            Class<T> clazz = internalClassForName(type, t);
            answer = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.6", type), e); //$NON-NLS-1$

        }

        return answer;
    }

    public static JavaTypeResolver createJavaTypeResolver(Context context, List<String> warnings) {
        String type = context.getJavaTypeResolverConfiguration()
                .map(JavaTypeResolverConfiguration::getImplementationType)
                .orElse(Defaults.DEFAULT_JAVA_TYPE_RESOLVER);

        JavaTypeResolver answer = createInternalObject(type, JavaTypeResolver.class);
        answer.setWarnings(warnings);

        context.getJavaTypeResolverConfiguration()
                .ifPresent(c -> answer.addConfigurationProperties(c.getProperties()));

        answer.setContext(context);

        return answer;
    }

    public static Plugin createPlugin(Context context, PluginConfiguration pluginConfiguration,
                                      CommentGenerator commentGenerator) {
        Plugin plugin = createInternalObject(pluginConfiguration.getConfigurationType().orElseThrow(), Plugin.class);
        plugin.setContext(context);
        plugin.setProperties(pluginConfiguration.getProperties());
        plugin.setCommentGenerator(commentGenerator);
        return plugin;
    }

    public static CommentGenerator createCommentGenerator(Context context) {
        CommentGenerator answer;

        String type = context.getCommentGeneratorConfiguration()
                .map(CommentGeneratorConfiguration::getImplementationType)
                .orElse(Defaults.DEFAULT_COMMENT_GENERATOR);

        answer = createInternalObject(type, CommentGenerator.class);

        context.getCommentGeneratorConfiguration()
                .ifPresent(c -> answer.addConfigurationProperties(c.getProperties()));

        return answer;
    }

    public static ConnectionFactory createConnectionFactory(ConnectionFactoryConfiguration config) {
        ConnectionFactory answer;

        String type = config.getImplementationType();

        answer = createInternalObject(type, ConnectionFactory.class);
        answer.addConfigurationProperties(config.getProperties());

        return answer;
    }

    public static JavaFormatter createJavaFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER);
        if (!stringHasValue(type)) {
            type = Defaults.DEFAULT_JAVA_FORMATTER;
        }

        JavaFormatter answer = createInternalObject(type, JavaFormatter.class);

        answer.setContext(context);

        return answer;
    }

    public static KotlinFormatter createKotlinFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_KOTLIN_FORMATTER);
        if (!stringHasValue(type)) {
            type = Defaults.DEFAULT_KOTLIN_FORMATTER;
        }

        KotlinFormatter answer = createInternalObject(type, KotlinFormatter.class);

        answer.setContext(context);

        return answer;
    }

    public static XmlFormatter createXmlFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_XML_FORMATTER);
        if (!stringHasValue(type)) {
            type = Defaults.DEFAULT_XML_FORMATTER;
        }

        XmlFormatter answer = createInternalObject(type, XmlFormatter.class);

        answer.setContext(context);

        return answer;
    }

    public static IntrospectedColumn createIntrospectedColumn(Context context) {
        String type = context.getIntrospectedColumnImpl().orElse(IntrospectedColumn.class.getName());
        IntrospectedColumn answer = createInternalObject(type, IntrospectedColumn.class);
        answer.setContext(context);

        return answer;
    }
}
