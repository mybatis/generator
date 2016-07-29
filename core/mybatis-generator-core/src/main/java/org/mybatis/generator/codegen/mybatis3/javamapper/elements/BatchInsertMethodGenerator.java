package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * 
 * @author ljw
 * 
 */
public class BatchInsertMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private boolean isSimple;

    public BatchInsertMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(introspectedTable.getInsertBatchStatementId());

        FullyQualifiedJavaType parameterType;
//        if (isSimple) {
//            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
//        } else {
//            parameterType = introspectedTable.getRules().calculateAllFieldsClass();
//        }
        String baseRecordType = introspectedTable.getBaseRecordType();
        String shortName = new FullyQualifiedJavaType(baseRecordType).getShortName();
		parameterType = new FullyQualifiedJavaType("java.util.List<" + shortName + ">");

        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "list")); //需要跟mapper.xml里面对应

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(interfaze, method);

        if (context.getPlugins().clientInsertMethodGenerated(method, interfaze,
                introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
