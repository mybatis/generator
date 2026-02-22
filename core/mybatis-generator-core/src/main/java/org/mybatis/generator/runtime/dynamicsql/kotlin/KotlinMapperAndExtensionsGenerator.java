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
package org.mybatis.generator.runtime.dynamicsql.kotlin;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicInsertFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicMultipleInsertFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicSelectManyFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicSelectOneFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.ColumnListGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.DeleteByPrimaryKeyExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralCountExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralDeleteExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectDistinctExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectOneExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralUpdateExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertMultipleExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertMultipleVarargExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertSelectiveExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.KotlinFragmentGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.KotlinPropertyAndImports;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.SelectByPrimaryKeyExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateAllColumnsExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateByPrimaryKeyExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateByPrimaryKeySelectiveExtensionFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateSelectiveColumnsExtensionFunctionGenerator;

public class KotlinMapperAndExtensionsGenerator extends AbstractKotlinGenerator {
    // record type for insert, select, update
    protected final FullyQualifiedKotlinType recordType;
    // id to use for the common result map
    protected final String resultMapId;
    protected final KotlinFragmentGenerator fragmentGenerator;
    protected final KotlinDynamicSqlSupportClassGenerator supportClassGenerator;
    protected final boolean hasGeneratedKeys;

    public KotlinMapperAndExtensionsGenerator(Builder builder) {
        super(builder);
        supportClassGenerator = initializeSubBuilder(new KotlinDynamicSqlSupportClassGenerator.Builder())
                .build();
        recordType = new FullyQualifiedKotlinType(introspectedTable.getKotlinRecordType());
        resultMapId = recordType.getShortNameWithoutTypeArguments() + "Result"; //$NON-NLS-1$
        fragmentGenerator = new KotlinFragmentGenerator.Builder()
                .withIntrospectedTable(introspectedTable)
                .withResultMapId(resultMapId)
                .withDynamicSqlSupportClassGenerator(supportClassGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .build();

        hasGeneratedKeys = introspectedTable.getGeneratedKey().isPresent();
    }

    @Override
    public List<KotlinFile> getKotlinFiles() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        KotlinFile mapperFile = createMapperInterfaceFile();
        KotlinType mapperType = createMapperInterfaceType(mapperFile);

        if (hasGeneratedKeys) {
            addBasicInsertFunction(mapperFile, mapperType);
            addBasicInsertMultipleFunction(mapperFile, mapperType);
        }

        boolean reuseResultMap = addBasicSelectManyFunction(mapperFile, mapperType);
        addBasicSelectOneFunction(mapperFile, mapperType, reuseResultMap);

        String mapperName = mapperType.getName();

        addGeneralCountExtensionFunction(mapperFile, mapperType, mapperName);
        addGeneralDeleteExtensionFunction(mapperFile, mapperType, mapperName);
        addDeleteByPrimaryKeyExtensionFunction(mapperFile, mapperName);
        addInsertOneExtensionFunction(mapperFile, mapperType, mapperName);
        addInsertMultipleExtensionFunction(mapperFile, mapperType, mapperName);
        addInsertMultipleVarargExtensionFunction(mapperFile, mapperName);
        addInsertSelectiveExtensionFunction(mapperFile, mapperType, mapperName);
        addColumnListProperty(mapperFile);
        addGeneralSelectExtensionFunction(mapperFile, mapperName);
        addGeneralSelectOneExtensionFunction(mapperFile, mapperName);
        addSelectDistinctExtensionFunction(mapperFile, mapperName);
        addSelectByPrimaryKeyExtensionFunction(mapperFile, mapperName);
        addGeneralUpdateExtensionFunction(mapperFile, mapperType, mapperName);
        addUpdateAllColumnsExtensionFunction(mapperFile);
        addUpdateSelectiveColumnsExtensionFunction(mapperFile);
        addUpdateByPrimaryKeyExtensionFunction(mapperFile, mapperName);
        addUpdateByPrimaryKeySelectiveExtensionFunction(mapperFile, mapperName);

        KotlinFile supportFile = supportClassGenerator.getKotlinFile();

        List<KotlinFile> answer = new ArrayList<>();
        if (pluginAggregator.dynamicSqlSupportGenerated(supportFile,
                supportClassGenerator.getOuterObject(),
                supportClassGenerator.getInnerClass(),
                introspectedTable)) {
            answer.add(supportFile);
        }

        if (pluginAggregator.mapperGenerated(mapperFile, mapperType, introspectedTable)) {
            answer.add(mapperFile);
        }

        return answer;
    }

    protected KotlinFile createMapperInterfaceFile() {
        FullyQualifiedKotlinType kotlinType =
                new FullyQualifiedKotlinType(introspectedTable.getMyBatis3JavaMapperType());

        KotlinFile kotlinFile = new KotlinFile(kotlinType.getShortNameWithoutTypeArguments());
        kotlinFile.setPackage(kotlinType.getPackageName());

        return kotlinFile;
    }

    protected KotlinType createMapperInterfaceType(KotlinFile kotlinFile) {
        FullyQualifiedKotlinType type = new FullyQualifiedKotlinType(introspectedTable.getMyBatis3JavaMapperType());

        KotlinType kotlinType = KotlinType.newInterface(type.getShortNameWithoutTypeArguments())
                .withAnnotation("@Mapper") //$NON-NLS-1$
                .build();

        kotlinFile.addImport("org.apache.ibatis.annotations.Mapper"); //$NON-NLS-1$
        kotlinFile.addNamedItem(kotlinType);

        commentGenerator.addFileComment(kotlinFile);

        return kotlinType;
    }

    protected void addBasicInsertFunction(KotlinFile kotlinFile, KotlinType kotlinType) {
        var generator = initializeSubBuilder(new BasicInsertFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeKotlinFunctionGenerator(kotlinFile, kotlinType, generator);
    }

    protected void addBasicInsertMultipleFunction(KotlinFile kotlinFile, KotlinType kotlinType) {
        var generator = initializeSubBuilder(new BasicMultipleInsertFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeKotlinFunctionGenerator(kotlinFile, kotlinType, generator);
    }

    protected void addBasicSelectOneFunction(KotlinFile kotlinFile, KotlinType kotlinType, boolean reuseResultMap) {
        var generator = initializeSubBuilder(new BasicSelectOneFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withResultMapId(resultMapId)
                .withReuseResultMap(reuseResultMap)
                .build();

        CodeGenUtils.executeKotlinFunctionGenerator(kotlinFile, kotlinType, generator);
    }

    protected boolean addBasicSelectManyFunction(KotlinFile kotlinFile, KotlinType kotlinType) {
        var generator = initializeSubBuilder(new BasicSelectManyFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        return CodeGenUtils.executeKotlinFunctionGenerator(kotlinFile, kotlinType, generator);
    }

    protected void addInsertOneExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addCommonInsertInterface(KotlinFile mapperFile, KotlinType mapper) {
        mapper.addSuperType("CommonInsertMapper<" //$NON-NLS-1$
                + recordType.getShortNameWithTypeArguments() + ">"); //$NON-NLS-1$
        mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper"); //$NON-NLS-1$
        mapperFile.addImports(recordType.getImportList());
    }

    protected void addInsertMultipleExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertMultipleExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addInsertMultipleVarargExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new InsertMultipleVarargExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addGeneralCountExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralCountExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withTableFieldImport(supportClassGenerator.getTablePropertyImport())
                .withMapperName(mapperName)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonCountMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper"); //$NON-NLS-1$
        }
    }

    protected void addGeneralDeleteExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralDeleteExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonDeleteMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper"); //$NON-NLS-1$
        }
    }

    protected void addColumnListProperty(KotlinFile kotlinFile) {
        ColumnListGenerator generator = initializeSubBuilder(new ColumnListGenerator.Builder())
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        KotlinPropertyAndImports propertyAndImports = generator.generatePropertyAndImports();

        if (generator.callPlugins(propertyAndImports.getProperty(), kotlinFile)) {
            kotlinFile.addNamedItem(propertyAndImports.getProperty());
            kotlinFile.addImports(propertyAndImports.getImports());
        }
    }

    protected void addGeneralSelectExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new GeneralSelectExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addSelectDistinctExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new GeneralSelectDistinctExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addGeneralSelectOneExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new GeneralSelectOneExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addGeneralUpdateExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralUpdateExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonUpdateMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper"); //$NON-NLS-1$
        }
    }

    protected void addUpdateAllColumnsExtensionFunction(KotlinFile kotlinFile) {
        var generator = initializeSubBuilder(new UpdateAllColumnsExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addUpdateSelectiveColumnsExtensionFunction(KotlinFile kotlinFile) {
        var generator = initializeSubBuilder(new UpdateSelectiveColumnsExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addDeleteByPrimaryKeyExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new DeleteByPrimaryKeyExtensionFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addInsertSelectiveExtensionFunction(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertSelectiveExtensionFunctionGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .withFragmentGenerator(fragmentGenerator)
                .build();

        if (CodeGenUtils.executeKotlinExtensionFunctionGenerator(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addSelectByPrimaryKeyExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new SelectByPrimaryKeyExtensionFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addUpdateByPrimaryKeyExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyExtensionFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveExtensionFunction(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeySelectiveExtensionFunctionGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        CodeGenUtils.executeKotlinExtensionFunctionGenerator(kotlinFile, generator);
    }

    public static class Builder extends AbstractKotlinGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public KotlinMapperAndExtensionsGenerator build() {
            return new KotlinMapperAndExtensionsGenerator(this);
        }
    }
}
