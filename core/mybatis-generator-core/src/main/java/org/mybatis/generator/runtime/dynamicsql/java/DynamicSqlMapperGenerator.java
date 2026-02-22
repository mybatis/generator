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
package org.mybatis.generator.runtime.dynamicsql.java;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.JavaFieldAndImports;
import org.mybatis.generator.runtime.common.RootInterfaceUtility;
import org.mybatis.generator.runtime.dynamicsql.java.elements.BasicInsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.BasicMultipleInsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.BasicSelectManyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.BasicSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.FragmentGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralCountMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralDeleteMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralSelectDistinctMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralSelectMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.GeneralUpdateMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.InsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.InsertMultipleMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.SelectListGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.UpdateAllColumnsMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.UpdateByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.java.elements.UpdateSelectiveColumnsMethodGenerator;

public class DynamicSqlMapperGenerator extends AbstractJavaGenerator {
    // record type for insert, select, update
    protected final FullyQualifiedJavaType recordType;
    // id to use for the common result map
    protected final String resultMapId;
    // name of the field containing the table in the support class
    protected final String tableFieldName;
    protected final FragmentGenerator fragmentGenerator;
    protected final boolean hasGeneratedKeys;

    public DynamicSqlMapperGenerator(Builder builder) {
        super(builder);

        recordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        resultMapId = recordType.getShortNameWithoutTypeArguments() + "Result"; //$NON-NLS-1$
        boolean useSnakeCase = CodeGenUtils.findTableOrClientPropertyAsBoolean(
                PropertyRegistry.ANY_USE_SNAKE_CASE_IDENTIFIERS, introspectedTable);

        tableFieldName = calculateTableFieldName(useSnakeCase);
        fragmentGenerator = new FragmentGenerator.Builder()
                .withIntrospectedTable(introspectedTable)
                .withResultMapId(resultMapId)
                .withTableFieldName(tableFieldName)
                .useSnakeCase(useSnakeCase)
                .build();

        hasGeneratedKeys = introspectedTable.getGeneratedKey().isPresent();
    }

    private String calculateTableFieldName(boolean useSnakeCase) {
        String tableFieldName =
                JavaBeansUtil.getValidPropertyName(introspectedTable.getMyBatisDynamicSQLTableObjectName());
        if (useSnakeCase) {
            tableFieldName = StringUtility.convertCamelCaseToSnakeCase(tableFieldName);
        }

        return tableFieldName;
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        Interface interfaze = createBasicInterface();

        TopLevelClass supportClass = getSupportClass();
        String staticImportString =
                supportClass.getType().getFullyQualifiedNameWithoutTypeParameters() + ".*"; //$NON-NLS-1$
        interfaze.addStaticImport(staticImportString);

        if (hasGeneratedKeys) {
            addBasicInsertMethod(interfaze);
            addBasicInsertMultipleMethod(interfaze);
        }

        boolean reuseResultMap = addBasicSelectManyMethod(interfaze);
        addBasicSelectOneMethod(interfaze, reuseResultMap);

        addGeneralCountMethod(interfaze);
        addGeneralDeleteMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertOneMethod(interfaze);
        addInsertMultipleMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectListField(interfaze);
        addGeneralSelectOneMethod(interfaze);
        addGeneralSelectMethod(interfaze);
        addSelectDistinctMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addGeneralUpdateMethod(interfaze);
        addUpdateAllMethod(interfaze);
        addUpdateSelectiveMethod(interfaze);
        addUpdateByPrimaryKeyMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.clientGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        if (pluginAggregator.dynamicSqlSupportGenerated(supportClass, introspectedTable)) {
            answer.add(supportClass);
        }

        return answer;
    }

    protected Interface createBasicInterface() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper")); //$NON-NLS-1$
        interfaze.addAnnotation("@Mapper"); //$NON-NLS-1$

        RootInterfaceUtility.addRootInterfaceIsNecessary(interfaze, introspectedTable);
        return interfaze;
    }

    protected TopLevelClass getSupportClass() {
        return initializeSubBuilder(new DynamicSqlSupportClassGenerator.Builder())
                .build()
                .generate();
    }

    protected void addInsertOneMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator) && !hasGeneratedKeys) {
            // add common interface
            addCommonInsertInterface(interfaze);
        }
    }

    protected void addCommonInsertInterface(Interface interfaze) {
        FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper<" //$NON-NLS-1$
                        + recordType.getFullyQualifiedName() + ">"); //$NON-NLS-1$
        interfaze.addSuperInterface(superInterface);
        interfaze.addImportedTypes(superInterface.getImportList().stream()
                .map(FullyQualifiedJavaType::new)
                .collect(Collectors.toSet()));
    }

    protected void addBasicInsertMultipleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new BasicMultipleInsertMethodGenerator.Builder())
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addInsertMultipleMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertMultipleMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator) && !hasGeneratedKeys) {
            // add common interface
            addCommonInsertInterface(interfaze);
        }
    }

    protected void addGeneralCountMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralCountMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator)) {
            // add common interface
            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
                    "org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper"); //$NON-NLS-1$
            interfaze.addSuperInterface(superInterface);
            interfaze.addImportedType(superInterface);
        }
    }

    protected void addGeneralDeleteMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralDeleteMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator)) {
            // add common interface
            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
                    "org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper"); //$NON-NLS-1$
            interfaze.addSuperInterface(superInterface);
            interfaze.addImportedType(superInterface);
        }
    }

    protected void addSelectListField(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectListGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        JavaFieldAndImports javaFieldAndImports = generator.generateFieldAndImports();

        if (generator.callPlugins(javaFieldAndImports.getField(), interfaze)) {
            interfaze.addField(javaFieldAndImports.getField());
            interfaze.addImportedTypes(javaFieldAndImports.getImports());
        }
    }

    protected void addGeneralSelectMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralSelectMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addSelectDistinctMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralSelectDistinctMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addGeneralSelectOneMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralSelectOneMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addGeneralUpdateMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new GeneralUpdateMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator)) {
            // add common interface
            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
                    "org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper"); //$NON-NLS-1$
            interfaze.addSuperInterface(superInterface);
            interfaze.addImportedType(superInterface);
        }
    }

    protected void addUpdateAllMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateAllColumnsMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateSelectiveColumnsMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addBasicSelectOneMethod(Interface interfaze, boolean reuseResultMap) {
        var generator = initializeSubBuilder(new BasicSelectOneMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .withResultMapId(resultMapId)
                .withReuseResultMap(reuseResultMap)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new DeleteByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new InsertSelectiveMethodGenerator.Builder())
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator) && !hasGeneratedKeys) {
            // add common interface
            addCommonInsertInterface(interfaze);
        }
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new SelectByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeySelectiveMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected void addBasicInsertMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new BasicInsertMethodGenerator.Builder())
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    protected boolean addBasicSelectManyMethod(Interface interfaze) {
        var generator = initializeSubBuilder(new BasicSelectManyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        return CodeGenUtils.executeInterfaceMethodGenerator(interfaze, generator);
    }

    public static class Builder extends AbstractJavaGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public DynamicSqlMapperGenerator build() {
            return new DynamicSqlMapperGenerator(this);
        }
    }
}
