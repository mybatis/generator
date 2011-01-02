/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.codegen.ibatis2.IntrospectedTableIbatis2Java2Impl;
import org.mybatis.generator.codegen.ibatis2.IntrospectedTableIbatis2Java5Impl;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * This class creates the different object needed by the generator
 * 
 * @author Jeff Butler
 */
public class ObjectFactory {
    private static ClassLoader externalClassLoader;

    /**
     * Utility class. No instances allowed
     */
    private ObjectFactory() {
        super();
    }

    private static ClassLoader getClassLoader() {
        if (externalClassLoader != null) {
            return externalClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    public static synchronized void setExternalClassLoader(
            ClassLoader classLoader) {
        ObjectFactory.externalClassLoader = classLoader;
    }

    /**
     * This method returns a class loaded from the context classloader, or the
     * classloader supplied by a client. This is appropriate for JDBC drivers,
     * model root classes, etc. It is not appropriate for any class that extends
     * one of the supplied classes or interfaces.
     * 
     * @param type
     * @return the Class loaded from the external classloader
     * @throws ClassNotFoundException
     */
    public static Class<?> externalClassForName(String type)
            throws ClassNotFoundException {

        Class<?> clazz;

        try {
            clazz = getClassLoader().loadClass(type);
        } catch (Throwable e) {
            // ignore - fail safe below
            clazz = null;
        }

        if (clazz == null) {
            clazz = Class.forName(type);
        }

        return clazz;
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
            clazz = cl.loadClass(type);
        } catch (Exception e) {
            // ignore - failsafe below
        }

        if (clazz == null) {
            clazz = Class.forName(type);
        }

        return clazz;
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

    public static IntrospectedTable createIntrospectedTable(
            TableConfiguration tableConfiguration, FullyQualifiedTable table,
            Context context) {

        IntrospectedTable answer = createIntrospectedTableForValidation(context);
        answer.setFullyQualifiedTable(table);
        answer.setTableConfiguration(tableConfiguration);

        return answer;
    }

    /**
     * This method creates an introspected table implementation that is
     * only usable for validation (i.e. for a context to determine
     * if the target is ibatis2 or mybatis3).
     *  
     * @param context
     * @return
     */
    public static IntrospectedTable createIntrospectedTableForValidation(Context context) {
        String type = context.getTargetRuntime();
        if (!stringHasValue(type)) {
            type = IntrospectedTableMyBatis3Impl.class.getName();
        } else if ("Ibatis2Java2".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableIbatis2Java2Impl.class.getName();
        } else if ("Ibatis2Java5".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableIbatis2Java5Impl.class.getName();
        } else if ("Ibatis3".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableMyBatis3Impl.class.getName();
        } else if ("MyBatis3".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableMyBatis3Impl.class.getName();
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
