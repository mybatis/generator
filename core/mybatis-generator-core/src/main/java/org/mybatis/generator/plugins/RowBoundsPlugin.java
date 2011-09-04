package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
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
        rowBounds = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds");
    }
    
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        copyAndAddMethod(method, interfaze);
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(
            Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        copyAndAddMethod(method, interfaze);
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
        newMethod.addParameter(new Parameter(rowBounds, "rowBounds"));
        interfaze.addMethod(newMethod);
        interfaze.addImportedType(rowBounds);
    }
}
