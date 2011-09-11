/*
 *  Copyright 2011 MyBatis
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
package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * This plugin will add selectByExample methods that include rowBounds
 * parameters to the generated mapper interface.  This plugin is only
 * valid for MyBatis3.
 * 
 * @author Jeff Butler
 */
public class RowBoundsPlugin extends PluginAdapter {
    
    private FullyQualifiedJavaType rowBounds;

    public RowBoundsPlugin() {
        rowBounds = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds"); //$NON-NLS-1$
    }
    
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            copyAndAddMethod(method, interfaze);
        }
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(
            Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            copyAndAddMethod(method, interfaze);
        }
        return true;
    }

    /**
     * Use the method copy constructor to create a new method, then
     * add the rowBounds parameter.
     * 
     * @param fullyQualifiedTable
     * @param method
     */
    private void copyAndAddMethod(Method method, Interface interfaze) {
        Method newMethod = new Method(method);
        newMethod.addParameter(new Parameter(rowBounds, "rowBounds")); //$NON-NLS-1$
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(rowBounds);
    }
}
