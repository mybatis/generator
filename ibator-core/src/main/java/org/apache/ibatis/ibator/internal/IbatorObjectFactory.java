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
package org.apache.ibatis.ibator.internal;

import java.util.List;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IbatorPlugin;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.JavaTypeResolver;
import org.apache.ibatis.ibator.config.CommentGeneratorConfiguration;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.config.IbatorPluginConfiguration;
import org.apache.ibatis.ibator.config.JavaTypeResolverConfiguration;
import org.apache.ibatis.ibator.config.TableConfiguration;
import org.apache.ibatis.ibator.generator.ibatis2.IntrospectedTableIbatis2Java2Impl;
import org.apache.ibatis.ibator.generator.ibatis2.IntrospectedTableIbatis2Java5Impl;
import org.apache.ibatis.ibator.internal.types.JavaTypeResolverDefaultImpl;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * This class creates the different configurable Ibator generators
 * 
 * @author Jeff Butler
 */
public class IbatorObjectFactory {
    private static ClassLoader externalClassLoader;
    
    /**
     * Utility class.  No instances allowed 
     */
    private IbatorObjectFactory() {
        super();
    }
    
    private static ClassLoader getClassLoader() {
        if (externalClassLoader != null) {
            return externalClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }
    
    public static synchronized void setExternalClassLoader(ClassLoader classLoader) {
        IbatorObjectFactory.externalClassLoader = classLoader;
    }

    /**
     * This method returns a class loaded from the context classloader,
     * or the classloader supplied by a client.  This is appropriate
     * for JDBC drivers, model root classes, etc.
     * It is not appropriate for any class that extends one of ibator's
     * supplied classes or interfaces.
     * 
     * @param type
     * @return the Class loaded from the external classloader
     * @throws ClassNotFoundException
     */
    public static Class<?> externalClassForName(String type) throws ClassNotFoundException {
        
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
            throw new RuntimeException(
              Messages.getString("RuntimeError.6", type), e); //$NON-NLS-1$
        }
        
        return answer;
	}
	
    public static Class<?> internalClassForName(String type) throws ClassNotFoundException {
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
            throw new RuntimeException(
              Messages.getString("RuntimeError.6", type), e); //$NON-NLS-1$
            
        }
        
        return answer;
    }
    
	public static JavaTypeResolver createJavaTypeResolver(IbatorContext context,
			List<String> warnings) {
        JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
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
        
        answer.setIbatorContext(context);
	    
	    return answer;
	}
    
    public static IbatorPlugin createIbatorPlugin(IbatorContext ibatorContext, IbatorPluginConfiguration ibatorPluginConfiguration) {
        IbatorPlugin ibatorPlugin = (IbatorPlugin) createInternalObject(ibatorPluginConfiguration.getConfigurationType());
        ibatorPlugin.setIbatorContext(ibatorContext);
        ibatorPlugin.setProperties(ibatorPluginConfiguration.getProperties());
        return ibatorPlugin;
    }
	
    public static CommentGenerator createCommentGenerator(IbatorContext context) {
        
        CommentGeneratorConfiguration config = context.getCommentGeneratorConfiguration();
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
    
    public static IntrospectedTable createIntrospectedTable(TableConfiguration tableConfiguration, 
            FullyQualifiedTable table, IbatorContext ibatorContext) {

        String type = ibatorContext.getTargetRuntime();
        if (!StringUtility.stringHasValue(type)) {
            type = IntrospectedTableIbatis2Java2Impl.class.getName();
        } else if ("Ibatis2Java2".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableIbatis2Java2Impl.class.getName();
        } else if ("Ibatis2Java5".equalsIgnoreCase(type)) { //$NON-NLS-1$
            type = IntrospectedTableIbatis2Java5Impl.class.getName();
        }
        
        IntrospectedTable answer = (IntrospectedTable) createInternalObject(type);
        answer.setFullyQualifiedTable(table);
        answer.setIbatorContext(ibatorContext);
        answer.setTableConfiguration(tableConfiguration);
        
        return answer;
    }
    
    public static IntrospectedColumn createIntrospectedColumn(IbatorContext ibatorContext) {
        String type = ibatorContext.getIntrospectedColumnImpl();
        if (!StringUtility.stringHasValue(type)) {
            type = IntrospectedColumn.class.getName();
        }
        
        IntrospectedColumn answer = (IntrospectedColumn) createInternalObject(type);
        answer.setIbatorContext(ibatorContext);
        
        return answer;
   }
}
