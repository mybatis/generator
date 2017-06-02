package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * Builder Plugin adds a builder static class to every domain class
 *
 * @author Silviu BURCEA (silviuburceadev@gmail.com)
 * @since 1.3.3
 */
public class BuilderPlugin extends PluginAdapter {

    private static final String BUILDER_CLASS_NAME = "Builder";
    private static final String BUILDER_INSTANCE_NAME = "instance";
    private static final String BUILDER_METHOD_NAME = "build";

    /**
     * Constructor.
     */
    public BuilderPlugin() {
        super();
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateBuilder(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateBuilder(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        generateBuilder(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * This method generates a static builder class for a domain class
     *
     * @param topLevelClass         the domain class
     * @param introspectedTable     the table associated with the domain class
     */
    private void generateBuilder(TopLevelClass topLevelClass,
                                       IntrospectedTable introspectedTable) {

        InnerClass builderClass = new InnerClass(BUILDER_CLASS_NAME);
        context.getCommentGenerator().addClassComment(builderClass,
                introspectedTable);
        builderClass.setVisibility(JavaVisibility.PUBLIC);
        builderClass.setStatic(true);
        // private instance field
        Field instance = new Field(BUILDER_INSTANCE_NAME, topLevelClass.getType());
        instance.setVisibility(JavaVisibility.PRIVATE);
        builderClass.addField(instance);
        // constructors
        Method constructor = new Method();
        constructor.setConstructor(true);
        StringBuilder constructorBody = new StringBuilder(BUILDER_INSTANCE_NAME)
                .append(" = new ")
                .append(topLevelClass.getType().getShortName())
                .append("();");
        constructor.addBodyLine(constructorBody.toString());
        builderClass.addMethod(constructor);
        // builder methods
        for (Field field : topLevelClass.getFields()) {
            // field
            Field f = new Field(field);
            f.setVisibility(JavaVisibility.PRIVATE); // make sure it's not exposed
            builderClass.addField(f);
            // builder method
            Method builderMethod = new Method(field.getName());
            builderMethod.setVisibility(JavaVisibility.PUBLIC);
            builderMethod.setReturnType(builderClass.getType());
            Parameter parameter = new Parameter(field.getType(), field.getName());
            builderMethod.addParameter(parameter);
            StringBuilder methodBody = new StringBuilder("this.")
                    .append(field.getName())
                    .append(" = ")
                    .append(parameter.getName())
                    .append("; return this;");
            builderMethod.addBodyLine(methodBody.toString());
        }
        // build() method
        Method buildMethod = new Method(BUILDER_METHOD_NAME);
        buildMethod.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder buildBody = new StringBuilder("return ")
                .append(BUILDER_INSTANCE_NAME)
                .append(";");
        buildMethod.addBodyLine(buildBody.toString());
        builderClass.addMethod(buildMethod);
        // add builder class
        topLevelClass.addInnerClass(builderClass);
        // add builder() method
        Method staticBuilderMethod = new Method(BUILDER_CLASS_NAME.toLowerCase());
        staticBuilderMethod.setStatic(true);
        staticBuilderMethod.setReturnType(builderClass.getType());
        StringBuilder staticBuilderBody = new StringBuilder("return new ")
                .append(BUILDER_CLASS_NAME)
                .append("();");
        staticBuilderMethod.addBodyLine(staticBuilderBody.toString());
        topLevelClass.addMethod(staticBuilderMethod);
    }
}
