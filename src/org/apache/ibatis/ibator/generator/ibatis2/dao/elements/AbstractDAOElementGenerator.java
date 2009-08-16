/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import org.apache.ibatis.ibator.api.DAOMethodNameCalculator;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.PropertyRegistry;
import org.apache.ibatis.ibator.generator.AbstractGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.templates.AbstractDAOTemplate;
import org.apache.ibatis.ibator.internal.DefaultDAOMethodNameCalculator;
import org.apache.ibatis.ibator.internal.ExtendedDAOMethodNameCalculator;
import org.apache.ibatis.ibator.internal.IbatorObjectFactory;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * 
 * @author Jeff Butler
 */
public abstract class AbstractDAOElementGenerator extends AbstractGenerator {
    public abstract void addInterfaceElements(Interface interfaze);
    public abstract void addImplementationElements(TopLevelClass topLevelClass);

    protected AbstractDAOTemplate daoTemplate;
    private DAOMethodNameCalculator dAOMethodNameCalculator;
    private JavaVisibility exampleMethodVisibility;

    public AbstractDAOElementGenerator() {
        super();
    }

    public void setDAOTemplate(AbstractDAOTemplate abstractDAOTemplate) {
        this.daoTemplate = abstractDAOTemplate;
    }
    
    public DAOMethodNameCalculator getDAOMethodNameCalculator() {
        if (dAOMethodNameCalculator == null) {
            String type = ibatorContext.getDaoGeneratorConfiguration().getProperty(
                    PropertyRegistry.DAO_METHOD_NAME_CALCULATOR);
            if (StringUtility.stringHasValue(type)) {
                if ("extended".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    type = ExtendedDAOMethodNameCalculator.class.getName();
                } else if ("default".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    type = DefaultDAOMethodNameCalculator.class.getName();
                }
            } else {
                type = DefaultDAOMethodNameCalculator.class.getName();
            }
            
            try {
                dAOMethodNameCalculator = (DAOMethodNameCalculator)
                    IbatorObjectFactory.createInternalObject(type);
            } catch (Exception e) {
                dAOMethodNameCalculator = new DefaultDAOMethodNameCalculator();
                warnings.add(Messages.getString("Warning.17", type, e.getMessage())); //$NON-NLS-1$
            }
        }
        
        return dAOMethodNameCalculator;
    }
    
    public JavaVisibility getExampleMethodVisibility() {
        if (exampleMethodVisibility == null) {
            String type = ibatorContext.getDaoGeneratorConfiguration().getProperty(
                    PropertyRegistry.DAO_EXAMPLE_METHOD_VISIBILITY);
            if (StringUtility.stringHasValue(type)) {
                if ("public".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    exampleMethodVisibility = JavaVisibility.PUBLIC;
                } else if ("private".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    exampleMethodVisibility = JavaVisibility.PRIVATE;
                } else if ("protected".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    exampleMethodVisibility = JavaVisibility.PROTECTED;
                } else if ("default".equalsIgnoreCase(type)) { //$NON-NLS-1$
                    exampleMethodVisibility = JavaVisibility.DEFAULT;
                } else {
                    exampleMethodVisibility = JavaVisibility.PUBLIC;
                    warnings.add(Messages.getString("Warning.16", type)); //$NON-NLS-1$
                }
            } else {
                exampleMethodVisibility = JavaVisibility.PUBLIC;
            }
        }
        
        return exampleMethodVisibility;
    }
}
