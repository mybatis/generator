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
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.AbstractKotlinFunctionGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicInsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicMultipleInsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicSelectManyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.BasicSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.ColumnListGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralCountMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralDeleteMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectDistinctMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.GeneralUpdateMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertMultipleMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertMultipleVarargMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.KotlinFragmentGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.KotlinFunctionAndImports;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.KotlinPropertyAndImports;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateAllColumnsMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamicsql.kotlin.elements.UpdateSelectiveColumnsMethodGenerator;

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

    protected KotlinFile createMapperInterfaceFile() {
        FullyQualifiedKotlinType type = new FullyQualifiedKotlinType(introspectedTable.getMyBatis3JavaMapperType());

        KotlinFile kf = new KotlinFile(type.getShortNameWithoutTypeArguments());
        kf.setPackage(type.getPackageName());

        return kf;
    }

    protected KotlinType createMapperInterface(KotlinFile kotlinFile) {
        FullyQualifiedKotlinType type = new FullyQualifiedKotlinType(introspectedTable.getMyBatis3JavaMapperType());

        KotlinType intf = KotlinType.newInterface(type.getShortNameWithoutTypeArguments())
                .withAnnotation("@Mapper") //$NON-NLS-1$
                .build();

        kotlinFile.addImport("org.apache.ibatis.annotations.Mapper"); //$NON-NLS-1$
        kotlinFile.addNamedItem(intf);

        commentGenerator.addFileComment(kotlinFile);

        return intf;
    }

    protected void addBasicInsertMethod(KotlinFile kotlinFile, KotlinType kotlinType) {
        BasicInsertMethodGenerator generator = initializeSubBuilder(new BasicInsertMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        generate(kotlinFile, kotlinType, generator);
    }

    protected boolean addBasicSelectManyMethod(KotlinFile kotlinFile, KotlinType kotlinType) {
        BasicSelectManyMethodGenerator generator = initializeSubBuilder(new BasicSelectManyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        return generate(kotlinFile, kotlinType, generator);
    }

    protected boolean generate(KotlinFile kotlinFile, AbstractKotlinFunctionGenerator generator) {
        KotlinFunctionAndImports mi = generator.generateMethodAndImports();
        if (mi != null && generator.callPlugins(mi.getFunction(), kotlinFile)) {
            kotlinFile.addNamedItem(mi.getFunction());
            kotlinFile.addImports(mi.getImports());
            return true;
        }
        return false;
    }

    protected boolean generate(KotlinFile kotlinFile, KotlinType kotlinType,
                               AbstractKotlinFunctionGenerator generator) {
        KotlinFunctionAndImports mi = generator.generateMethodAndImports();
        if (mi != null && generator.callPlugins(mi.getFunction(), kotlinFile)) {
            kotlinType.addNamedItem(mi.getFunction());
            kotlinFile.addImports(mi.getImports());
            return true;
        }
        return false;
    }

    @Override
    public List<KotlinFile> getKotlinFiles() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        KotlinFile mapperFile = createMapperInterfaceFile();
        KotlinType mapper = createMapperInterface(mapperFile);

        if (hasGeneratedKeys) {
            addBasicInsertMethod(mapperFile, mapper);
            addBasicInsertMultipleMethod(mapperFile, mapper);
        }

        boolean reuseResultMap = addBasicSelectManyMethod(mapperFile, mapper);
        addBasicSelectOneMethod(mapperFile, mapper, reuseResultMap);

        String mapperName = mapper.getName();

        addGeneralCountMethod(mapperFile, mapper, mapperName);
        addGeneralDeleteMethod(mapperFile, mapper, mapperName);
        addDeleteByPrimaryKeyMethod(mapperFile, mapperName);
        addInsertOneMethod(mapperFile, mapper, mapperName);
        addInsertMultipleMethod(mapperFile, mapper, mapperName);
        addInsertMultipleVarargMethod(mapperFile, mapperName);
        addInsertSelectiveMethod(mapperFile, mapper, mapperName);
        addColumnListProperty(mapperFile);
        addGeneralSelectMethod(mapperFile, mapperName);
        addSelectDistinctMethod(mapperFile, mapperName);
        addSelectByPrimaryKeyMethod(mapperFile, mapperName);
        addGeneralUpdateMethod(mapperFile, mapper, mapperName);
        addUpdateAllMethod(mapperFile);
        addUpdateSelectiveMethod(mapperFile);
        addUpdateByPrimaryKeyMethod(mapperFile, mapperName);
        addUpdateByPrimaryKeySelectiveMethod(mapperFile, mapperName);

        KotlinFile supportFile = supportClassGenerator.getKotlinFile();

        List<KotlinFile> answer = new ArrayList<>();
        if (pluginAggregator.dynamicSqlSupportGenerated(supportFile,
                supportClassGenerator.getOuterObject(),
                supportClassGenerator.getInnerClass(),
                introspectedTable)) {
            answer.add(supportFile);
        }

        if (pluginAggregator.mapperGenerated(mapperFile, mapper, introspectedTable)) {
            answer.add(mapperFile);
        }

        return answer;
    }

    protected void addInsertOneMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .build();

        if (generate(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addCommonInsertInterface(KotlinFile mapperFile, KotlinType mapper) {
        mapper.addSuperType("CommonInsertMapper<" //$NON-NLS-1$
                + recordType.getShortNameWithTypeArguments() + ">"); //$NON-NLS-1$
        mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper"); //$NON-NLS-1$
        mapperFile.addImports(recordType.getImportList());
    }

    protected void addBasicInsertMultipleMethod(KotlinFile kotlinFile, KotlinType kotlinType) {
        var generator = initializeSubBuilder(new BasicMultipleInsertMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .build();

        generate(kotlinFile, kotlinType, generator);
    }

    protected void addInsertMultipleMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertMultipleMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .build();

        if (generate(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addInsertMultipleVarargMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new InsertMultipleVarargMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addGeneralCountMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralCountMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withTableFieldImport(supportClassGenerator.getTablePropertyImport())
                .withMapperName(mapperName)
                .build();

        if (generate(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonCountMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper"); //$NON-NLS-1$
        }
    }

    protected void addGeneralDeleteMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralDeleteMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        if (generate(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonDeleteMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper"); //$NON-NLS-1$
        }
    }

    protected void addColumnListProperty(KotlinFile kotlinFile) {
        ColumnListGenerator generator = initializeSubBuilder(new ColumnListGenerator.Builder())
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .build();

        KotlinPropertyAndImports propertyAndImports = generator.generatePropertyAndImports();

        if (generator.callPlugins(propertyAndImports.getProperty(), kotlinFile)) {
            kotlinFile.addNamedItem(propertyAndImports.getProperty());
            kotlinFile.addImports(propertyAndImports.getImports());
        }
    }

    protected void addGeneralSelectMethod(KotlinFile kotlinFile, String mapperName) {
        addGeneralSelectOneMethod(kotlinFile, mapperName);
        var generator = initializeSubBuilder(new GeneralSelectMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addSelectDistinctMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new GeneralSelectDistinctMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addGeneralSelectOneMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new GeneralSelectOneMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addGeneralUpdateMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new GeneralUpdateMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        if (generate(mapperFile, generator)) {
            // add common interface
            mapper.addSuperType("CommonUpdateMapper"); //$NON-NLS-1$
            mapperFile.addImport("org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper"); //$NON-NLS-1$
        }
    }

    protected void addUpdateAllMethod(KotlinFile kotlinFile) {
        var generator = initializeSubBuilder(new UpdateAllColumnsMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addUpdateSelectiveMethod(KotlinFile kotlinFile) {
        var generator = initializeSubBuilder(new UpdateSelectiveColumnsMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addBasicSelectOneMethod(KotlinFile kotlinFile, KotlinType kotlinType, boolean reuseResultMap) {
        var generator = initializeSubBuilder(new BasicSelectOneMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withResultMapId(resultMapId)
                .withReuseResultMap(reuseResultMap)
                .build();

        generate(kotlinFile, kotlinType, generator);
    }

    protected void addDeleteByPrimaryKeyMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new DeleteByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addInsertSelectiveMethod(KotlinFile mapperFile, KotlinType mapper, String mapperName) {
        var generator = initializeSubBuilder(new InsertSelectiveMethodGenerator.Builder())
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .withSupportObjectImport(supportClassGenerator.getSupportObjectImport())
                .build();

        if (generate(mapperFile, generator) && !hasGeneratedKeys) {
            addCommonInsertInterface(mapperFile, mapper);
        }
    }

    protected void addSelectByPrimaryKeyMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new SelectByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addUpdateByPrimaryKeyMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeyMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(KotlinFile kotlinFile, String mapperName) {
        var generator = initializeSubBuilder(new UpdateByPrimaryKeySelectiveMethodGenerator.Builder())
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(supportClassGenerator.getTablePropertyName())
                .withRecordType(recordType)
                .withMapperName(mapperName)
                .build();

        generate(kotlinFile, generator);
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
