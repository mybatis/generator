package org.mybatis.generator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.internal.PluginAggregator;
import org.mybatis.generator.internal.rules.ConditionalModelRules;
import org.mybatis.generator.internal.rules.FlatModelRules;

class CodeGenerationAttributesTest {

    private static final String TEST_KEY = "TEST-KEY"; //$NON-NLS-1$
    private static final String TEST_VALUE = "TEST-VALUE"; //$NON-NLS-1$
    private static final String TABLE_NAME = "MY_TABLE";

    @Test
    void testThatTestObjectBuilds() {
        assertThat(generateMinimalTestObject()).isNotNull();
    }

    @Test
    void testAttributes() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();

        assertThat(attributes.getAttribute(TEST_KEY)).isNull();

        attributes.setAttribute(TEST_KEY, TEST_VALUE);
        assertThat(attributes.getAttribute(TEST_KEY)).isEqualTo(TEST_VALUE);

        assertThat(attributes.removeAttribute(TEST_KEY)).isEqualTo(TEST_VALUE);
        assertThat(attributes.getAttribute(TEST_KEY)).isNull();
    }

    @Test
    void testAliasedFullyQualifiedRuntimeTableName() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getAliasedFullyQualifiedRuntimeTableName()).isEqualTo(TABLE_NAME);

        attributes.setAliasedFullyQualifiedRuntimeTableName(TEST_VALUE);
        assertThat(attributes.getAliasedFullyQualifiedRuntimeTableName()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testBaseColumnListId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getBaseColumnListId()).isEqualTo("Base_Column_List");

        attributes.setBaseColumnListId(TEST_VALUE);
        assertThat(attributes.getBaseColumnListId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testBaseRecordType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getBaseRecordType()).isEqualTo("foo.bar.MyTable");

        attributes.setBaseRecordType(TEST_VALUE);
        assertThat(attributes.getBaseRecordType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testBaseResultMapId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getBaseResultMapId()).isEqualTo("BaseResultMap");

        attributes.setBaseResultMapId(TEST_VALUE);
        assertThat(attributes.getBaseResultMapId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testBlobColumnListId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getBlobColumnListId()).isEqualTo("Blob_Column_List");

        attributes.setBlobColumnListId(TEST_VALUE);
        assertThat(attributes.getBlobColumnListId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testCountByExampleStatementId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getCountByExampleStatementId()).isEqualTo("countByExample");

        attributes.setCountByExampleStatementId(TEST_VALUE);
        assertThat(attributes.getCountByExampleStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testDeleteByExampleStatementId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getDeleteByExampleStatementId()).isEqualTo("deleteByExample");

        attributes.setDeleteByExampleStatementId(TEST_VALUE);
        assertThat(attributes.getDeleteByExampleStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testDeleteByPrimaryKeyStatementId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getDeleteByPrimaryKeyStatementId()).isEqualTo("deleteByPrimaryKey");

        attributes.setDeleteByPrimaryKeyStatementId(TEST_VALUE);
        assertThat(attributes.getDeleteByPrimaryKeyStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testExampleType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getExampleType()).isEqualTo("foo.bar.MyTableExample");

        attributes.setExampleType(TEST_VALUE);
        assertThat(attributes.getExampleType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testExampleWhereClauseId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getExampleWhereClauseId()).isEqualTo("Example_Where_Clause");

        attributes.setExampleWhereClauseId(TEST_VALUE);
        assertThat(attributes.getExampleWhereClauseId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testFullyQualifiedTableNameAtRuntime() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getFullyQualifiedTableNameAtRuntime()).isEqualTo(TABLE_NAME);

        attributes.setFullyQualifiedTableNameAtRuntime(TEST_VALUE);
        assertThat(attributes.getFullyQualifiedTableNameAtRuntime()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testInsertSelectiveStatementId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getInsertSelectiveStatementId()).isEqualTo("insertSelective");

        attributes.setInsertSelectiveStatementId(TEST_VALUE);
        assertThat(attributes.getInsertSelectiveStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testInsertStatementId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getInsertStatementId()).isEqualTo("insert");

        attributes.setInsertStatementId(TEST_VALUE);
        assertThat(attributes.getInsertStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testKotlinRecordType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getKotlinRecordType()).isEqualTo("foo.bar.MyTable");

        attributes.setKotlinRecordType(TEST_VALUE);
        assertThat(attributes.getKotlinRecordType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3JavaMapperType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThatExceptionOfType(InternalException.class).isThrownBy(attributes::getMyBatis3JavaMapperType);

        attributes.setMyBatis3JavaMapperType(TEST_VALUE);
        assertThat(attributes.getMyBatis3JavaMapperType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3JavaMapperTypeInCompleteObject() {
        CodeGenerationAttributes attributes = generateCompleteTestObject();
        assertThat(attributes.getMyBatis3JavaMapperType()).isEqualTo("foo.bar.MyTableMapper");
    }

    @Test
    void testMyBatis3SqlMapNamespace() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThatExceptionOfType(InternalException.class).isThrownBy(attributes::getMyBatis3SqlMapNamespace);

        attributes.setMyBatis3SqlMapNamespace(TEST_VALUE);
        assertThat(attributes.getMyBatis3SqlMapNamespace()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3SqlMapNamespaceInCompleteObject() {
        CodeGenerationAttributes attributes = generateCompleteTestObject();
        assertThat(attributes.getMyBatis3SqlMapNamespace()).isEqualTo("foo.bar.MyTableMapper");
    }

    @Test
    void testMyBatis3SqlMapNamespaceInNoClientObject() {
        CodeGenerationAttributes attributes = generateNoClientTestObject();
        assertThat(attributes.getMyBatis3SqlMapNamespace()).isEqualTo("foo.bar.MyTableMapper");
    }

    @Test
    void testMyBatis3SqlProviderType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThatExceptionOfType(InternalException.class).isThrownBy(attributes::getMyBatis3SqlProviderType);

        attributes.setMyBatis3SqlProviderType(TEST_VALUE);
        assertThat(attributes.getMyBatis3SqlProviderType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3SqlProviderTypeInCompleteObject() {
        CodeGenerationAttributes attributes = generateCompleteTestObject();
        assertThat(attributes.getMyBatis3SqlProviderType()).isEqualTo("foo.bar.MyTableSqlProvider");
    }

    @Test
    void testMyBatis3UpdateByExampleWhereClauseId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getMyBatis3UpdateByExampleWhereClauseId()).isEqualTo("Update_By_Example_Where_Clause");

        attributes.setMyBatis3UpdateByExampleWhereClauseId(TEST_VALUE);
        assertThat(attributes.getMyBatis3UpdateByExampleWhereClauseId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3XmlMapperFileName() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getMyBatis3XmlMapperFileName()).isEqualTo("MyTableMapper.xml");

        attributes.setMyBatis3XmlMapperFileName(TEST_VALUE);
        assertThat(attributes.getMyBatis3XmlMapperFileName()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3XmlMapperPackage() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThatExceptionOfType(InternalException.class).isThrownBy(attributes::getMyBatis3XmlMapperPackage);

        attributes.setMyBatis3XmlMapperPackage(TEST_VALUE);
        assertThat(attributes.getMyBatis3XmlMapperPackage()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatis3XmlMapperPackageInCompleteObject() {
        CodeGenerationAttributes attributes = generateCompleteTestObject();
        assertThat(attributes.getMyBatis3XmlMapperPackage()).isEqualTo("foo.bar");
    }

    @Test
    void testMyBatisDynamicSqlSupportType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThatExceptionOfType(InternalException.class).isThrownBy(attributes::getMyBatisDynamicSqlSupportType);

        attributes.setMyBatisDynamicSqlSupportType(TEST_VALUE);
        assertThat(attributes.getMyBatisDynamicSqlSupportType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testMyBatisDynamicSqlSupportTypeInCompleteObject() {
        CodeGenerationAttributes attributes = generateCompleteTestObject();
        assertThat(attributes.getMyBatisDynamicSqlSupportType()).isEqualTo("foo.bar.MyTableDynamicSqlSupport");
    }

    @Test
    void testMyBatisDynamicSQLTableObjectName() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getMyBatisDynamicSQLTableObjectName()).isEqualTo("MyTable");

        attributes.setMyBatisDynamicSQLTableObjectName(TEST_VALUE);
        assertThat(attributes.getMyBatisDynamicSQLTableObjectName()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testPrimaryKeyType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getPrimaryKeyType()).isEqualTo("foo.bar.MyTableKey");

        attributes.setPrimaryKeyType(TEST_VALUE);
        assertThat(attributes.getPrimaryKeyType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testRecordWithBLOBsType() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getRecordWithBLOBsType()).isEqualTo("foo.bar.MyTableWithBLOBs");

        attributes.setRecordWithBLOBsType(TEST_VALUE);
        assertThat(attributes.getRecordWithBLOBsType()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testResultMapWithBLOBsId() {
        CodeGenerationAttributes attributes = generateMinimalTestObject();
        assertThat(attributes.getResultMapWithBLOBsId()).isEqualTo("ResultMapWithBLOBs");

        attributes.setResultMapWithBLOBsId(TEST_VALUE);
        assertThat(attributes.getResultMapWithBLOBsId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testRules() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getRules()).isNotNull().isInstanceOf(ConditionalModelRules.class);

        attributes.setRules(new FlatModelRules(attributes));
        assertThat(attributes.getRules()).isNotNull().isInstanceOf(FlatModelRules.class);
    }

    @Test
    void testSelectAllStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getSelectAllStatementId()).isEqualTo("selectAll");

        attributes.setSelectAllStatementId(TEST_VALUE);
        assertThat(attributes.getSelectAllStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testSelectByExampleStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getSelectByExampleStatementId()).isEqualTo("selectByExample");

        attributes.setSelectByExampleStatementId(TEST_VALUE);
        assertThat(attributes.getSelectByExampleStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testSelectByExampleWithBLOBsStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getSelectByExampleWithBLOBsStatementId()).isEqualTo("selectByExampleWithBLOBs");

        attributes.setSelectByExampleWithBLOBsStatementId(TEST_VALUE);
        assertThat(attributes.getSelectByExampleWithBLOBsStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testSelectByPrimaryKeyStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getSelectByPrimaryKeyStatementId()).isEqualTo("selectByPrimaryKey");

        attributes.setSelectByPrimaryKeyStatementId(TEST_VALUE);
        assertThat(attributes.getSelectByPrimaryKeyStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByExampleStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByExampleStatementId()).isEqualTo("updateByExample");

        attributes.setUpdateByExampleStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByExampleStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByExampleSelectiveStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByExampleSelectiveStatementId()).isEqualTo("updateByExampleSelective");

        attributes.setUpdateByExampleSelectiveStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByExampleSelectiveStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByExampleWithBLOBsStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByExampleWithBLOBsStatementId()).isEqualTo("updateByExampleWithBLOBs");

        attributes.setUpdateByExampleWithBLOBsStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByExampleWithBLOBsStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByPrimaryKeySelectiveStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByPrimaryKeySelectiveStatementId()).isEqualTo("updateByPrimaryKeySelective");

        attributes.setUpdateByPrimaryKeySelectiveStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByPrimaryKeySelectiveStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByPrimaryKeyStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByPrimaryKeyStatementId()).isEqualTo("updateByPrimaryKey");

        attributes.setUpdateByPrimaryKeyStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByPrimaryKeyStatementId()).isEqualTo(TEST_VALUE);
    }

    @Test
    void testUpdateByPrimaryKeyWithBLOBsStatementId() {
        IntrospectedTable attributes = generateMinimalTestObject();
        assertThat(attributes.getUpdateByPrimaryKeyWithBLOBsStatementId()).isEqualTo("updateByPrimaryKeyWithBLOBs");

        attributes.setUpdateByPrimaryKeyWithBLOBsStatementId(TEST_VALUE);
        assertThat(attributes.getUpdateByPrimaryKeyWithBLOBsStatementId()).isEqualTo(TEST_VALUE);
    }

    private IntrospectedTable generateMinimalTestObject() {
        // build a minimal IntrospectedTable so we can check for the optional attributes
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        Context context = new Context.Builder()
                .withId("test")
                .withDefaultModelType(ModelType.CONDITIONAL)
                .withJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration)
                .build();

        FullyQualifiedTable fullyQualifiedTable = new FullyQualifiedTable.Builder()
                // TODO - why does this need context?
                .withContext(context)
                .withIntrospectedTableName(TABLE_NAME)
                .build();

        TableConfiguration tableConfiguration = new TableConfiguration.Builder()
                .withTableName(TABLE_NAME)
                // TODO - why both here:
                .withModelType(ModelType.CONDITIONAL, "conditional")
                .build();

        return new IntrospectedTable.Builder()
                .withContext(context)
                .withPluginAggregator(new PluginAggregator())
                .withKnownRuntime(KnownRuntime.MYBATIS3)
                .withFullyQualifiedTable(fullyQualifiedTable)
                .withTableConfiguration(tableConfiguration)
                .build();
    }

    private IntrospectedTable generateNoClientTestObject() {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        Context context = new Context.Builder()
                .withId("test")
                .withDefaultModelType(ModelType.CONDITIONAL)
                .withJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration)
                .withSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration)
                .build();

        FullyQualifiedTable fullyQualifiedTable = new FullyQualifiedTable.Builder()
                // TODO - why does this need context?
                .withContext(context)
                .withIntrospectedTableName(TABLE_NAME)
                .build();

        TableConfiguration tableConfiguration = new TableConfiguration.Builder()
                .withTableName(TABLE_NAME)
                // TODO - why both here:
                .withModelType(ModelType.CONDITIONAL, "conditional")
                .build();

        return new IntrospectedTable.Builder()
                .withContext(context)
                .withPluginAggregator(new PluginAggregator())
                .withKnownRuntime(KnownRuntime.MYBATIS3)
                .withFullyQualifiedTable(fullyQualifiedTable)
                .withTableConfiguration(tableConfiguration)
                .build();
    }

    private IntrospectedTable generateCompleteTestObject() {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        Context context = new Context.Builder()
                .withId("test")
                .withDefaultModelType(ModelType.CONDITIONAL)
                .withJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration)
                .withSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration)
                .withJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration)
                .build();

        FullyQualifiedTable fullyQualifiedTable = new FullyQualifiedTable.Builder()
                // TODO - why does this need context?
                .withContext(context)
                .withIntrospectedTableName(TABLE_NAME)
                .build();

        TableConfiguration tableConfiguration = new TableConfiguration.Builder()
                .withTableName(TABLE_NAME)
                // TODO - why both here:
                .withModelType(ModelType.CONDITIONAL, "conditional")
                .build();

        return new IntrospectedTable.Builder()
                .withContext(context)
                .withPluginAggregator(new PluginAggregator())
                .withKnownRuntime(KnownRuntime.MYBATIS3)
                .withFullyQualifiedTable(fullyQualifiedTable)
                .withTableConfiguration(tableConfiguration)
                .build();
    }
}
