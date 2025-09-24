/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * This plugin generates service interface and implementation classes with CRUD operations using DTOs.
 *
 * <p>The plugin creates both service interfaces and their implementations with standard CRUD operations
 * including save, update, delete, and find methods. The implementations use Spring annotations and
 * work with both the original and customer mapper interfaces.
 *
 * @author Goody
 */
public class ServiceGeneratorPlugin extends PluginAdapter {

    private static final String SERVICE_INTERFACE_PACKAGE = "service.interfaces";
    private static final String SERVICE_IMPL_PACKAGE = "service.impl";
    private static final String SERVICE_INTERFACE_PREFIX = "I";
    private static final String SERVICE_INTERFACE_SUFFIX = "Service";
    private static final String SERVICE_IMPL_SUFFIX = "ServiceImpl";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        // Generate Service Interface
        Interface serviceInterface = generateServiceInterface(introspectedTable);
        GeneratedJavaFile serviceInterfaceFile = new GeneratedJavaFile(serviceInterface,
                context.getJavaModelGeneratorConfiguration().getTargetProject(),
                context.getProperty("javaFileEncoding"),
                context.getJavaFormatter());

        // Generate Service Implementation
        TopLevelClass serviceImpl = generateServiceImplementation(introspectedTable);
        GeneratedJavaFile serviceImplFile = new GeneratedJavaFile(serviceImpl,
                context.getJavaModelGeneratorConfiguration().getTargetProject(),
                context.getProperty("javaFileEncoding"),
                context.getJavaFormatter());

        answer.add(serviceInterfaceFile);
        answer.add(serviceImplFile);

        return answer;
    }

    private Interface generateServiceInterface(IntrospectedTable introspectedTable) {
        // Get entity info
        String entityFullType = introspectedTable.getBaseRecordType();
        String entityPackage = entityFullType.substring(0, entityFullType.lastIndexOf('.'));
        String entityClassName = entityFullType.substring(entityFullType.lastIndexOf('.') + 1);

        // Generate service interface info
        String basePackage = entityPackage.substring(0, entityPackage.indexOf(".model"));
        String serviceInterfacePackage = basePackage + "." + SERVICE_INTERFACE_PACKAGE;
        String serviceInterfaceClassName = SERVICE_INTERFACE_PREFIX + entityClassName + SERVICE_INTERFACE_SUFFIX;
        String serviceInterfaceFullType = serviceInterfacePackage + "." + serviceInterfaceClassName;

        // Create service interface
        FullyQualifiedJavaType serviceInterfaceType = new FullyQualifiedJavaType(serviceInterfaceFullType);
        Interface serviceInterface = new Interface(serviceInterfaceType);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);

        // Add imports
        String dtoPackage = entityPackage.replace(".entity", ".dto");
        String dtoClassName = entityClassName + "DTO";
        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(dtoPackage + "." + dtoClassName);
        serviceInterface.addImportedType(dtoType);
        serviceInterface.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        // Get primary key columns for method generation
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        // Add imports for primary key types
        for (IntrospectedColumn column : primaryKeyColumns) {
            serviceInterface.addImportedType(column.getFullyQualifiedJavaType());
        }

        // Add interface comment
        serviceInterface.addJavaDocLine("/**");
        serviceInterface.addJavaDocLine(" * Service interface for " + entityClassName);
        serviceInterface.addJavaDocLine(" * Generated by MyBatis Generator Service Plugin");
        serviceInterface.addJavaDocLine(" */");

        // Add CRUD methods
        addServiceInterfaceMethods(serviceInterface, dtoType, primaryKeyColumns, dtoClassName);

        return serviceInterface;
    }

    private void addServiceInterfaceMethods(Interface serviceInterface, FullyQualifiedJavaType dtoType,
                                            List<IntrospectedColumn> primaryKeyColumns, String dtoClassName) {

        // save method
        Method save = new Method("save");
        save.setVisibility(JavaVisibility.PUBLIC);
        save.setAbstract(true);
        save.setReturnType(new FullyQualifiedJavaType("int"));
        save.addParameter(new Parameter(dtoType, "dto"));
        save.addJavaDocLine("/**");
        save.addJavaDocLine(" * Save a single entity");
        save.addJavaDocLine(" *");
        save.addJavaDocLine(" * @param dto the DTO object to save");
        save.addJavaDocLine(" * @return number of affected rows, 1 for success, 0 for failure");
        save.addJavaDocLine(" */");
        serviceInterface.addMethod(save);

        // saveBatch method
        Method saveBatch = new Method("saveBatch");
        saveBatch.setVisibility(JavaVisibility.PUBLIC);
        saveBatch.setAbstract(true);
        saveBatch.setReturnType(new FullyQualifiedJavaType("int"));
        saveBatch.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + dtoClassName + ">"), "dtoList"));
        saveBatch.addJavaDocLine("/**");
        saveBatch.addJavaDocLine(" * Batch save entities");
        saveBatch.addJavaDocLine(" *");
        saveBatch.addJavaDocLine(" * @param dtoList list of DTO objects to save");
        saveBatch.addJavaDocLine(" * @return number of affected rows");
        saveBatch.addJavaDocLine(" */");
        serviceInterface.addMethod(saveBatch);

        // update method
        Method update = new Method("update");
        update.setVisibility(JavaVisibility.PUBLIC);
        update.setAbstract(true);
        update.setReturnType(new FullyQualifiedJavaType("int"));
        update.addParameter(new Parameter(dtoType, "dto"));
        update.addJavaDocLine("/**");
        update.addJavaDocLine(" * Update entity by primary key");
        update.addJavaDocLine(" *");
        update.addJavaDocLine(" * @param dto the DTO object to update, must contain primary key");
        update.addJavaDocLine(" * @return number of affected rows, 1 for success, 0 for failure");
        update.addJavaDocLine(" */");
        serviceInterface.addMethod(update);

        // deleteById method
        Method deleteById = new Method("deleteById");
        deleteById.setVisibility(JavaVisibility.PUBLIC);
        deleteById.setAbstract(true);
        deleteById.setReturnType(new FullyQualifiedJavaType("int"));

        // Add parameters for all primary key columns
        for (IntrospectedColumn column : primaryKeyColumns) {
            String paramName = column.getJavaProperty();
            deleteById.addParameter(new Parameter(column.getFullyQualifiedJavaType(), paramName));
        }

        deleteById.addJavaDocLine("/**");
        deleteById.addJavaDocLine(" * Delete entity by primary key");
        deleteById.addJavaDocLine(" *");
        for (IntrospectedColumn column : primaryKeyColumns) {
            deleteById.addJavaDocLine(" * @param " + column.getJavaProperty() + " " + column.getRemarks());
        }
        deleteById.addJavaDocLine(" * @return number of affected rows, 1 for success, 0 for failure");
        deleteById.addJavaDocLine(" */");
        serviceInterface.addMethod(deleteById);

        // findById method
        Method findById = new Method("findById");
        findById.setVisibility(JavaVisibility.PUBLIC);
        findById.setAbstract(true);
        findById.setReturnType(dtoType);

        // Add parameters for all primary key columns
        for (IntrospectedColumn column : primaryKeyColumns) {
            String paramName = column.getJavaProperty();
            findById.addParameter(new Parameter(column.getFullyQualifiedJavaType(), paramName));
        }

        findById.addJavaDocLine("/**");
        findById.addJavaDocLine(" * Find entity by primary key");
        findById.addJavaDocLine(" *");
        for (IntrospectedColumn column : primaryKeyColumns) {
            findById.addJavaDocLine(" * @param " + column.getJavaProperty() + " " + column.getRemarks());
        }
        findById.addJavaDocLine(" * @return DTO object, null if not found");
        findById.addJavaDocLine(" */");
        serviceInterface.addMethod(findById);

        // findAll method
        Method findAll = new Method("findAll");
        findAll.setVisibility(JavaVisibility.PUBLIC);
        findAll.setAbstract(true);
        findAll.setReturnType(new FullyQualifiedJavaType("List<" + dtoClassName + ">"));
        findAll.addJavaDocLine("/**");
        findAll.addJavaDocLine(" * Find all entities");
        findAll.addJavaDocLine(" *");
        findAll.addJavaDocLine(" * @return list of DTO objects");
        findAll.addJavaDocLine(" */");
        serviceInterface.addMethod(findAll);
    }

    private TopLevelClass generateServiceImplementation(IntrospectedTable introspectedTable) {
        // Get entity info
        String entityFullType = introspectedTable.getBaseRecordType();
        String entityPackage = entityFullType.substring(0, entityFullType.lastIndexOf('.'));
        String entityClassName = entityFullType.substring(entityFullType.lastIndexOf('.') + 1);

        // Generate service implementation info
        String basePackage = entityPackage.substring(0, entityPackage.indexOf(".model"));
        String serviceImplPackage = basePackage + "." + SERVICE_IMPL_PACKAGE;
        String serviceImplClassName = entityClassName + SERVICE_IMPL_SUFFIX;
        String serviceImplFullType = serviceImplPackage + "." + serviceImplClassName;

        String serviceInterfacePackage = basePackage + "." + SERVICE_INTERFACE_PACKAGE;
        String serviceInterfaceClassName = SERVICE_INTERFACE_PREFIX + entityClassName + SERVICE_INTERFACE_SUFFIX;

        // Create service implementation class
        FullyQualifiedJavaType serviceImplType = new FullyQualifiedJavaType(serviceImplFullType);
        TopLevelClass serviceImpl = new TopLevelClass(serviceImplType);
        serviceImpl.setVisibility(JavaVisibility.PUBLIC);

        // Add imports
        serviceImpl.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        serviceImpl.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        serviceImpl.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        serviceImpl.addImportedType(new FullyQualifiedJavaType("java.util.stream.Collectors"));

        // Add interface and DTO imports
        FullyQualifiedJavaType serviceInterfaceType = new FullyQualifiedJavaType(serviceInterfacePackage + "." + serviceInterfaceClassName);
        serviceImpl.addImportedType(serviceInterfaceType);
        serviceImpl.addSuperInterface(serviceInterfaceType);

        String dtoPackage = entityPackage.replace(".entity", ".dto");
        String dtoClassName = entityClassName + "DTO";
        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(dtoPackage + "." + dtoClassName);
        serviceImpl.addImportedType(dtoType);

        // Add entity import
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(entityFullType);
        serviceImpl.addImportedType(entityType);

        // Add mapper imports
        String mapperPackage = entityPackage.replace(".entity", ".dao");
        String originalMapperClassName = entityClassName + "Mapper";
        String customerMapperClassName = "Customer" + entityClassName + "Mapper";

        FullyQualifiedJavaType originalMapperType = new FullyQualifiedJavaType(mapperPackage + "." + originalMapperClassName);
        FullyQualifiedJavaType customerMapperType = new FullyQualifiedJavaType(mapperPackage + ".customer." + customerMapperClassName);
        serviceImpl.addImportedType(originalMapperType);
        serviceImpl.addImportedType(customerMapperType);

        // Add @Service annotation
        serviceImpl.addAnnotation("@Service");

        // Add class comment
        serviceImpl.addJavaDocLine("/**");
        serviceImpl.addJavaDocLine(" * Service implementation for " + entityClassName);
        serviceImpl.addJavaDocLine(" * Generated by MyBatis Generator Service Plugin");
        serviceImpl.addJavaDocLine(" */");

        // Add mapper fields
        addMapperFields(serviceImpl, originalMapperType, customerMapperType, originalMapperClassName, customerMapperClassName);

        // Get primary key columns for method generation
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        // Add imports for primary key types
        for (IntrospectedColumn column : primaryKeyColumns) {
            serviceImpl.addImportedType(column.getFullyQualifiedJavaType());
        }

        // Add service implementation methods
        addServiceImplementationMethods(serviceImpl, dtoType, entityType, primaryKeyColumns, dtoClassName, entityClassName);

        return serviceImpl;
    }

    private void addMapperFields(TopLevelClass serviceImpl, FullyQualifiedJavaType originalMapperType,
                                 FullyQualifiedJavaType customerMapperType, String originalMapperClassName, String customerMapperClassName) {

        // Original mapper field
        Field originalMapperField = new Field(lowercaseFirstLetter(originalMapperClassName), originalMapperType);
        originalMapperField.setVisibility(JavaVisibility.PRIVATE);
        originalMapperField.addAnnotation("@Autowired");
        serviceImpl.addField(originalMapperField);

        // Customer mapper field
        Field customerMapperField = new Field(lowercaseFirstLetter(customerMapperClassName), customerMapperType);
        customerMapperField.setVisibility(JavaVisibility.PRIVATE);
        customerMapperField.addAnnotation("@Autowired");
        serviceImpl.addField(customerMapperField);
    }

    private void addServiceImplementationMethods(TopLevelClass serviceImpl, FullyQualifiedJavaType dtoType,
                                                 FullyQualifiedJavaType entityType, List<IntrospectedColumn> primaryKeyColumns,
                                                 String dtoClassName, String entityClassName) {

        String mapperFieldName = lowercaseFirstLetter(entityClassName) + "Mapper";

        // save method
        Method save = new Method("save");
        save.setVisibility(JavaVisibility.PUBLIC);
        save.addAnnotation("@Override");
        save.setReturnType(new FullyQualifiedJavaType("int"));
        save.addParameter(new Parameter(dtoType, "dto"));
        save.addBodyLine("if (dto == null) {");
        save.addBodyLine("    return 0;");
        save.addBodyLine("}");
        save.addBodyLine("");
        save.addBodyLine(entityClassName + " entity = dto.toEntity();");
        save.addBodyLine("return " + mapperFieldName + ".insert(entity);");
        serviceImpl.addMethod(save);

        // saveBatch method
        Method saveBatch = new Method("saveBatch");
        saveBatch.setVisibility(JavaVisibility.PUBLIC);
        saveBatch.addAnnotation("@Override");
        saveBatch.setReturnType(new FullyQualifiedJavaType("int"));
        saveBatch.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + dtoClassName + ">"), "dtoList"));
        saveBatch.addBodyLine("if (dtoList == null || dtoList.isEmpty()) {");
        saveBatch.addBodyLine("    return 0;");
        saveBatch.addBodyLine("}");
        saveBatch.addBodyLine("");
        saveBatch.addBodyLine("List<" + entityClassName + "> entityList = dtoList.stream()");
        saveBatch.addBodyLine("        .filter(dto -> dto != null)");
        saveBatch.addBodyLine("        .map(" + dtoClassName + "::toEntity)");
        saveBatch.addBodyLine("        .collect(Collectors.toList());");
        saveBatch.addBodyLine("");
        saveBatch.addBodyLine("if (entityList.isEmpty()) {");
        saveBatch.addBodyLine("    return 0;");
        saveBatch.addBodyLine("}");
        saveBatch.addBodyLine("");
        saveBatch.addBodyLine("return " + mapperFieldName + ".insertMultiple(entityList);");
        serviceImpl.addMethod(saveBatch);

        // update method
        Method update = new Method("update");
        update.setVisibility(JavaVisibility.PUBLIC);
        update.addAnnotation("@Override");
        update.setReturnType(new FullyQualifiedJavaType("int"));
        update.addParameter(new Parameter(dtoType, "dto"));
        update.addBodyLine("if (dto == null) {");
        update.addBodyLine("    return 0;");
        update.addBodyLine("}");
        update.addBodyLine("");
        update.addBodyLine(entityClassName + " entity = dto.toEntity();");
        update.addBodyLine("return " + mapperFieldName + ".updateByPrimaryKey(entity);");
        serviceImpl.addMethod(update);

        // deleteById method
        Method deleteById = new Method("deleteById");
        deleteById.setVisibility(JavaVisibility.PUBLIC);
        deleteById.addAnnotation("@Override");
        deleteById.setReturnType(new FullyQualifiedJavaType("int"));

        // Add parameters for all primary key columns
        for (IntrospectedColumn column : primaryKeyColumns) {
            String paramName = column.getJavaProperty();
            deleteById.addParameter(new Parameter(column.getFullyQualifiedJavaType(), paramName));
        }

        // Add null checks for primary key parameters
        for (IntrospectedColumn column : primaryKeyColumns) {
            deleteById.addBodyLine("if (" + column.getJavaProperty() + " == null) {");
            deleteById.addBodyLine("    return 0;");
            deleteById.addBodyLine("}");
        }
        deleteById.addBodyLine("");

        // Build method call with all primary key parameters
        StringBuilder deleteMethodCall = new StringBuilder("return " + mapperFieldName + ".deleteByPrimaryKey(");
        for (int i = 0; i < primaryKeyColumns.size(); i++) {
            if (i > 0) deleteMethodCall.append(", ");
            deleteMethodCall.append(primaryKeyColumns.get(i).getJavaProperty());
        }
        deleteMethodCall.append(");");
        deleteById.addBodyLine(deleteMethodCall.toString());
        serviceImpl.addMethod(deleteById);

        // findById method
        Method findById = new Method("findById");
        findById.setVisibility(JavaVisibility.PUBLIC);
        findById.addAnnotation("@Override");
        findById.setReturnType(dtoType);

        // Add parameters for all primary key columns
        for (IntrospectedColumn column : primaryKeyColumns) {
            String paramName = column.getJavaProperty();
            findById.addParameter(new Parameter(column.getFullyQualifiedJavaType(), paramName));
        }

        // Add null checks for primary key parameters
        for (IntrospectedColumn column : primaryKeyColumns) {
            findById.addBodyLine("if (" + column.getJavaProperty() + " == null) {");
            findById.addBodyLine("    return null;");
            findById.addBodyLine("}");
        }
        findById.addBodyLine("");

        // Build method call with all primary key parameters
        StringBuilder findMethodCall = new StringBuilder(entityClassName + " entity = " + mapperFieldName + ".selectByPrimaryKey(");
        for (int i = 0; i < primaryKeyColumns.size(); i++) {
            if (i > 0) findMethodCall.append(", ");
            findMethodCall.append(primaryKeyColumns.get(i).getJavaProperty());
        }
        findMethodCall.append(").orElse(null);");
        findById.addBodyLine(findMethodCall.toString());
        findById.addBodyLine("return entity != null ? " + dtoClassName + ".fromEntity(entity) : null;");
        serviceImpl.addMethod(findById);

        // findAll method
        Method findAll = new Method("findAll");
        findAll.setVisibility(JavaVisibility.PUBLIC);
        findAll.addAnnotation("@Override");
        findAll.setReturnType(new FullyQualifiedJavaType("List<" + dtoClassName + ">"));
        findAll.addBodyLine("List<" + entityClassName + "> entityList = " + mapperFieldName + ".select(c -> c);");
        findAll.addBodyLine("return entityList.stream()");
        findAll.addBodyLine("        .map(" + dtoClassName + "::fromEntity)");
        findAll.addBodyLine("        .collect(Collectors.toList());");
        serviceImpl.addMethod(findAll);
    }

    private String lowercaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
