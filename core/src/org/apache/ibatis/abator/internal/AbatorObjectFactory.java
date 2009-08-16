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
package org.apache.ibatis.abator.internal;

import java.util.List;

import org.apache.ibatis.abator.api.CommentGenerator;
import org.apache.ibatis.abator.api.DAOGenerator;
import org.apache.ibatis.abator.api.JavaModelGenerator;
import org.apache.ibatis.abator.api.JavaTypeResolver;
import org.apache.ibatis.abator.api.SqlMapGenerator;
import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.config.CommentGeneratorConfiguration;
import org.apache.ibatis.abator.config.DAOGeneratorConfiguration;
import org.apache.ibatis.abator.config.JavaModelGeneratorConfiguration;
import org.apache.ibatis.abator.config.JavaTypeResolverConfiguration;
import org.apache.ibatis.abator.config.SqlMapGeneratorConfiguration;
import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * This class creates the different configurable Abator generators
 * 
 * @author Jeff Butler
 */
public class AbatorObjectFactory {

    /**
     * Utility class.  No instances allowed 
     */
    private AbatorObjectFactory() {
        super();
    }

	public static Object createObject(String type) {
        Class clazz = null;
        
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = cl.loadClass(type);
        } catch (Exception e) {
            // ignore - failsafe below
        }
        
        Object answer;
        
        try {
            if (clazz == null) {
                clazz = Class.forName(type);
            }
        
            answer = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
              Messages.getString("RuntimeError.6", type), e); //$NON-NLS-1$
            
        }
        
        return answer;
	}
	
	public static JavaTypeResolver createJavaTypeResolver(AbatorContext context,
			List warnings) {
        JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
        String type;
        
        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
        } else {
            type = context.getGeneratorSet().getJavaTypeResolverType();
        }
        
	    JavaTypeResolver answer = (JavaTypeResolver) createObject(type);
	    answer.setWarnings(warnings);

        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        
        answer.setAbatorContext(context);
	    
	    return answer;
	}
	
	public static SqlMapGenerator createSqlMapGenerator(AbatorContext context,
	        JavaModelGenerator javaModelGenerator, List warnings) {
        
        SqlMapGeneratorConfiguration config = context.getSqlMapGeneratorConfiguration();
        String type;

        if (config.getConfigurationType() != null) {
            type = config.getConfigurationType();
        } else {
            type = context.getGeneratorSet().getSqlMapGeneratorType();
        }
        
	    SqlMapGenerator answer = (SqlMapGenerator) createObject(type);
	    answer.setWarnings(warnings);

	    answer.setJavaModelGenerator(javaModelGenerator);
	    answer.addConfigurationProperties(config.getProperties());
        answer.setAbatorContext(context);
	    answer.setTargetPackage(config.getTargetPackage());
	    answer.setTargetProject(config.getTargetProject());
	    
	    return answer;
	}
	
	public static JavaModelGenerator createJavaModelGenerator(AbatorContext context,
			List warnings) {

        JavaModelGeneratorConfiguration config = context.getJavaModelGeneratorConfiguration();
        String type;

        if (config.getConfigurationType() != null) {
            type = config.getConfigurationType();
        } else {
            type = context.getGeneratorSet().getJavaModelGeneratorType();
        }
        
	    JavaModelGenerator answer = (JavaModelGenerator) createObject(type);
	    answer.setWarnings(warnings);
	    
	    answer.addConfigurationProperties(config.getProperties());
        answer.setAbatorContext(context);
	    answer.setTargetPackage(config.getTargetPackage());
	    answer.setTargetProject(config.getTargetProject());
	    
	    return answer;
	}
	
	public static DAOGenerator createDAOGenerator(AbatorContext context,
	        JavaModelGenerator javaModelGenerator, SqlMapGenerator sqlMapGenerator, List warnings) {
        
        DAOGeneratorConfiguration config = context.getDaoGeneratorConfiguration();
        
	    if (config == null) {
	        return null;
	    }
        
        String type = context.getGeneratorSet().translateDAOGeneratorType(config.getConfigurationType());
	    
	    DAOGenerator answer = (DAOGenerator) createObject(type);
	    answer.setWarnings(warnings);

	    answer.setJavaModelGenerator(javaModelGenerator);
	    answer.addConfigurationProperties(config.getProperties());
        answer.setAbatorContext(context);
	    answer.setSqlMapGenerator(sqlMapGenerator);
	    answer.setTargetPackage(config.getTargetPackage());
	    answer.setTargetProject(config.getTargetProject());
	    
	    return answer;
	}

    public static CommentGenerator createCommentGenerator(AbatorContext context) {
        
        CommentGeneratorConfiguration config = context.getCommentGeneratorConfiguration();
        CommentGenerator answer;
        
        String type;
        if (config == null || config.getConfigurationType() == null) {
            type = DefaultCommentGenerator.class.getName();
        } else {
            type = config.getConfigurationType();
        }
        
        answer = (CommentGenerator) createObject(type);
        
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        
        return answer;
    }
}
