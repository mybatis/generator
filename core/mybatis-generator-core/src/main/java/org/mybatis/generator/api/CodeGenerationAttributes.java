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
package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.StringUtility.ifStringHasValueElse;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.mapStringValueOrElseGet;
import static org.mybatis.generator.internal.util.StringUtility.stringValueOrElseGet;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.config.ClientGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.ModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.internal.rules.Rules;

/**
 * This class holds calculated attributes for all code generator implementations. The class
 * is intended to be a super class of IntrospectedTable and is matched one-to-one with that
 * class (for purposes of preserving compatibility with the existing plugin methods). It is a separate class
 * because there are different responsibilities between IntrospectedTable and this class.
 *
 * @author Jeff Butler
 */
public abstract class CodeGenerationAttributes {
    protected final TableConfiguration tableConfiguration;
    protected final FullyQualifiedTable fullyQualifiedTable;
    protected final Context context;
    protected final KnownRuntime knownRuntime;

    /**
     * Plugins may use attributes to capture table-related state between
     * the different plugin calls.
     */
    protected final Map<String, Object> attributes = new HashMap<>();

    // these attributes are not final because we allow plugins to alter their values
    private String aliasedFullyQualifiedRuntimeTableName;
    private String baseColumnListId;
    private String baseRecordType;
    private String baseResultMapId;
    private String blobColumnListId;
    private String countByExampleStatementId;
    private String deleteByExampleStatementId;
    private String deleteByPrimaryKeyStatementId;
    private String exampleType;
    private String exampleWhereClauseId;
    private String fullyQualifiedTableNameAtRuntime;
    private String insertSelectiveStatementId;
    private String insertStatementId;
    private String kotlinRecordType;
    /** also used as XML Mapper namespace if a Java mapper is generated. */
    private @Nullable String myBatis3JavaMapperType;
    private @Nullable String myBatis3SqlMapNamespace;
    private @Nullable String myBatis3SqlProviderType;
    private String myBatis3UpdateByExampleWhereClauseId;
    private String myBatis3XmlMapperFileName;
    private @Nullable String myBatis3XmlMapperPackage;
    private String myBatisDynamicSQLTableObjectName;
    private @Nullable String myBatisDynamicSqlSupportType;
    private String primaryKeyType;
    private String recordWithBLOBsType;
    private String resultMapWithBLOBsId;
    private Rules rules;
    private String selectAllStatementId;
    private String selectByExampleStatementId;
    private String selectByExampleWithBLOBsStatementId;
    private String selectByPrimaryKeyStatementId;
    private String updateByExampleStatementId;
    private String updateByExampleSelectiveStatementId;
    private String updateByExampleWithBLOBsStatementId;
    private String updateByPrimaryKeySelectiveStatementId;
    private String updateByPrimaryKeyStatementId;
    private String updateByPrimaryKeyWithBLOBsStatementId;

    protected abstract Rules calculateRules();

    protected CodeGenerationAttributes(AbstractBuilder<?> builder) {
        this.knownRuntime = Objects.requireNonNull(builder.knownRuntime);
        this.tableConfiguration = Objects.requireNonNull(builder.tableConfiguration);
        this.fullyQualifiedTable = Objects.requireNonNull(builder.fullyQualifiedTable);
        this.context = Objects.requireNonNull(builder.context);

        String modelPackage = calculateModelPackage();

        aliasedFullyQualifiedRuntimeTableName = fullyQualifiedTable.getAliasedFullyQualifiedTableNameAtRuntime();
        baseColumnListId = "Base_Column_List"; //$NON-NLS-1$
        baseRecordType = calculateBaseRecordType(modelPackage);
        baseResultMapId = "BaseResultMap"; //$NON-NLS-1$
        blobColumnListId = "Blob_Column_List"; //$NON-NLS-1$
        countByExampleStatementId = "countByExample"; //$NON-NLS-1$
        deleteByExampleStatementId = "deleteByExample"; //$NON-NLS-1$
        deleteByPrimaryKeyStatementId = "deleteByPrimaryKey"; //$NON-NLS-1$
        exampleType = calculateExampleType(modelPackage);
        exampleWhereClauseId = "Example_Where_Clause"; //$NON-NLS-1$
        fullyQualifiedTableNameAtRuntime = fullyQualifiedTable.getFullyQualifiedTableNameAtRuntime();
        insertSelectiveStatementId = "insertSelective"; //$NON-NLS-1$
        insertStatementId = "insert"; //$NON-NLS-1$
        kotlinRecordType = calculateKotlinRecordType(modelPackage);
        myBatis3UpdateByExampleWhereClauseId = "Update_By_Example_Where_Clause"; //$NON-NLS-1$
        myBatis3XmlMapperFileName = calculateMyBatis3XmlMapperFileName();
        myBatisDynamicSQLTableObjectName = calculateMyBatisDynamicSQLTableObjectName();
        primaryKeyType = calculatePrimaryKeyType(modelPackage);
        recordWithBLOBsType = calculateRecordWithBLOBsType(modelPackage);
        resultMapWithBLOBsId = "ResultMapWithBLOBs"; //$NON-NLS-1$
        rules = calculateRules();
        selectAllStatementId = "selectAll"; //$NON-NLS-1$
        selectByExampleStatementId = "selectByExample"; //$NON-NLS-1$
        selectByExampleWithBLOBsStatementId = "selectByExampleWithBLOBs"; //$NON-NLS-1$
        selectByPrimaryKeyStatementId = "selectByPrimaryKey"; //$NON-NLS-1$
        updateByExampleStatementId = "updateByExample"; //$NON-NLS-1$
        updateByExampleSelectiveStatementId = "updateByExampleSelective"; //$NON-NLS-1$
        updateByExampleWithBLOBsStatementId = "updateByExampleWithBLOBs"; //$NON-NLS-1$
        updateByPrimaryKeySelectiveStatementId = "updateByPrimaryKeySelective"; //$NON-NLS-1$
        updateByPrimaryKeyStatementId = "updateByPrimaryKey"; //$NON-NLS-1$
        updateByPrimaryKeyWithBLOBsStatementId = "updateByPrimaryKeyWithBLOBs"; //$NON-NLS-1$

        context.getSqlMapGeneratorConfiguration().ifPresent(config ->
                myBatis3XmlMapperPackage = calculateSqlMapPackage(config));

        context.getClientGeneratorConfiguration().ifPresent(config -> {
            myBatis3JavaMapperType = calculateMyBatis3JavaMapperType(config);
            myBatis3SqlProviderType = calculateMyBatis3SqlProviderType(config);
            myBatisDynamicSqlSupportType = calculateMyBatisDynamicSqlSupportType(config);
        });

        myBatis3SqlMapNamespace = calculateMyBatis3SqlMapNamespace();
    }

    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }

    public Optional<GeneratedKey> getGeneratedKey() {
        return getTableConfiguration().getGeneratedKey();
    }

    public @Nullable String getTableConfigurationProperty(String property) {
        return getTableConfiguration().getProperty(property);
    }

    public @Nullable Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Object removeAttribute(String name) {
        return attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void setAliasedFullyQualifiedRuntimeTableName(String aliasedFullyQualifiedRuntimeTableName) {
        this.aliasedFullyQualifiedRuntimeTableName = aliasedFullyQualifiedRuntimeTableName;
    }

    public String getAliasedFullyQualifiedRuntimeTableName() {
        return aliasedFullyQualifiedRuntimeTableName;
    }

    public void setBaseColumnListId(String baseColumnListId) {
        this.baseColumnListId = baseColumnListId;
    }

    public String getBaseColumnListId() {
        return baseColumnListId;
    }

    public void setBaseRecordType(String baseRecordType) {
        this.baseRecordType = baseRecordType;
    }

    /**
     * Gets the base record type.
     *
     * @return the type for the record (the class that holds non-primary key and non-BLOB fields). Note that the value
     *         will be calculated regardless of whether the table has these columns or not.
     */
    public String getBaseRecordType() {
        return baseRecordType;
    }

    public void setBaseResultMapId(String baseResultMapId) {
        this.baseResultMapId = baseResultMapId;
    }

    public String getBaseResultMapId() {
        return baseResultMapId;
    }

    public void setBlobColumnListId(String blobColumnListId) {
        this.blobColumnListId = blobColumnListId;
    }

    public String getBlobColumnListId() {
        return blobColumnListId;
    }

    public void setCountByExampleStatementId(String countByExampleStatementId) {
        this.countByExampleStatementId = countByExampleStatementId;
    }

    public String getCountByExampleStatementId() {
        return countByExampleStatementId;
    }

    public void setDeleteByExampleStatementId(String deleteByExampleStatementId) {
        this.deleteByExampleStatementId = deleteByExampleStatementId;
    }

    public String getDeleteByExampleStatementId() {
        return deleteByExampleStatementId;
    }

    public void setDeleteByPrimaryKeyStatementId(String deleteByPrimaryKeyStatementId) {
        this.deleteByPrimaryKeyStatementId = deleteByPrimaryKeyStatementId;
    }

    public String getDeleteByPrimaryKeyStatementId() {
        return deleteByPrimaryKeyStatementId;
    }

    public void setExampleType(String exampleType) {
        this.exampleType = exampleType;
    }

    public String getExampleType() {
        return exampleType;
    }

    public void setExampleWhereClauseId(String exampleWhereClauseId) {
        this.exampleWhereClauseId = exampleWhereClauseId;
    }

    public String getExampleWhereClauseId() {
        return exampleWhereClauseId;
    }

    public void setFullyQualifiedTableNameAtRuntime(String fullyQualifiedTableNameAtRuntime) {
        this.fullyQualifiedTableNameAtRuntime = fullyQualifiedTableNameAtRuntime;
    }

    public String getFullyQualifiedTableNameAtRuntime() {
        return fullyQualifiedTableNameAtRuntime;
    }

    public void setInsertSelectiveStatementId(String insertSelectiveStatementId) {
        this.insertSelectiveStatementId = insertSelectiveStatementId;
    }

    public String getInsertSelectiveStatementId() {
        return insertSelectiveStatementId;
    }

    public void setInsertStatementId(String insertStatementId) {
        this.insertStatementId = insertStatementId;
    }

    public String getInsertStatementId() {
        return insertStatementId;
    }

    public void setKotlinRecordType(String kotlinRecordType) {
        this.kotlinRecordType = kotlinRecordType;
    }

    public String getKotlinRecordType() {
        return kotlinRecordType;
    }

    public void setMyBatis3JavaMapperType(String myBatis3JavaMapperType) {
        this.myBatis3JavaMapperType = myBatis3JavaMapperType;
    }

    public String getMyBatis3JavaMapperType() {
        return requireNonNullElseInternalError(myBatis3JavaMapperType,
                getString("RuntimeError.25", context.getId())); //$NON-NLS-1$
    }

    public void setMyBatis3SqlMapNamespace(String myBatis3SqlMapNamespace) {
        this.myBatis3SqlMapNamespace = myBatis3SqlMapNamespace;

    }

    public String getMyBatis3SqlMapNamespace() {
        return requireNonNullElseInternalError(myBatis3SqlMapNamespace,
                getString("RuntimeError.24", context.getId())); //$NON-NLS-1$
    }

    public void setMyBatis3SqlProviderType(String myBatis3SqlProviderType) {
        this.myBatis3SqlProviderType = myBatis3SqlProviderType;
    }

    public String getMyBatis3SqlProviderType() {
        return requireNonNullElseInternalError(myBatis3SqlProviderType,
                getString("RuntimeError.27", context.getId())); //$NON-NLS-1$
    }

    public void setMyBatis3UpdateByExampleWhereClauseId(String myBatis3UpdateByExampleWhereClauseId) {
        this.myBatis3UpdateByExampleWhereClauseId = myBatis3UpdateByExampleWhereClauseId;
    }

    public String getMyBatis3UpdateByExampleWhereClauseId() {
        return myBatis3UpdateByExampleWhereClauseId;
    }

    public void setMyBatis3XmlMapperFileName(String myBatis3XmlMapperFileName) {
        this.myBatis3XmlMapperFileName = myBatis3XmlMapperFileName;
    }

    public String getMyBatis3XmlMapperFileName() {
        return myBatis3XmlMapperFileName;
    }

    public void setMyBatis3XmlMapperPackage(String myBatis3XmlMapperPackage) {
        this.myBatis3XmlMapperPackage = myBatis3XmlMapperPackage;
    }

    public String getMyBatis3XmlMapperPackage() {
        return requireNonNullElseInternalError(myBatis3XmlMapperPackage,
                getString("RuntimeError.24", context.getId())); //$NON-NLS-1$

    }

    public void setMyBatisDynamicSqlSupportType(String myBatisDynamicSqlSupportType) {
        this.myBatisDynamicSqlSupportType = myBatisDynamicSqlSupportType;
    }

    public String getMyBatisDynamicSqlSupportType() {
        return requireNonNullElseInternalError(myBatisDynamicSqlSupportType,
                getString("RuntimeError.26", context.getId())); //$NON-NLS-1$
    }

    public void setMyBatisDynamicSQLTableObjectName(String myBatisDynamicSQLTableObjectName) {
        this.myBatisDynamicSQLTableObjectName = myBatisDynamicSQLTableObjectName;
    }

    public String getMyBatisDynamicSQLTableObjectName() {
        return myBatisDynamicSQLTableObjectName;
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setRecordWithBLOBsType(String recordWithBLOBsType) {
        this.recordWithBLOBsType = recordWithBLOBsType;
    }

    public String getRecordWithBLOBsType() {
        return recordWithBLOBsType;
    }

    public void setResultMapWithBLOBsId(String resultMapWithBLOBsId) {
        this.resultMapWithBLOBsId = resultMapWithBLOBsId;
    }

    public String getResultMapWithBLOBsId() {
        return resultMapWithBLOBsId;
    }

    /**
     * This method exists to give plugins the opportunity to replace the calculated rules if necessary.
     *
     * @param rules
     *            the new rules
     */
    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Rules getRules() {
        return Objects.requireNonNull(rules);
    }

    public void setSelectAllStatementId(String selectAllStatementId) {
        this.selectAllStatementId = selectAllStatementId;
    }

    public String getSelectAllStatementId() {
        return selectAllStatementId;
    }

    public void setSelectByExampleStatementId(String selectByExampleStatementId) {
        this.selectByExampleStatementId = selectByExampleStatementId;
    }

    public String getSelectByExampleStatementId() {
        return selectByExampleStatementId;
    }

    public void setSelectByExampleWithBLOBsStatementId(String selectByExampleWithBLOBsStatementId) {
        this.selectByExampleWithBLOBsStatementId = selectByExampleWithBLOBsStatementId;
    }

    public String getSelectByExampleWithBLOBsStatementId() {
        return selectByExampleWithBLOBsStatementId;
    }

    public void setSelectByPrimaryKeyStatementId(String selectByPrimaryKeyStatementId) {
        this.selectByPrimaryKeyStatementId = selectByPrimaryKeyStatementId;
    }

    public String getSelectByPrimaryKeyStatementId() {
        return selectByPrimaryKeyStatementId;
    }

    public void setUpdateByExampleStatementId(String updateByExampleStatementId) {
        this.updateByExampleStatementId = updateByExampleStatementId;
    }

    public String getUpdateByExampleStatementId() {
        return updateByExampleStatementId;
    }

    public void setUpdateByExampleSelectiveStatementId(String updateByExampleSelectiveStatementId) {
        this.updateByExampleSelectiveStatementId = updateByExampleSelectiveStatementId;
    }

    public String getUpdateByExampleSelectiveStatementId() {
        return updateByExampleSelectiveStatementId;
    }

    public void setUpdateByExampleWithBLOBsStatementId(String updateByExampleWithBLOBsStatementId) {
        this.updateByExampleWithBLOBsStatementId = updateByExampleWithBLOBsStatementId;
    }

    public String getUpdateByExampleWithBLOBsStatementId() {
        return updateByExampleWithBLOBsStatementId;
    }

    public void setUpdateByPrimaryKeySelectiveStatementId(String updateByPrimaryKeySelectiveStatementId) {
        this. updateByPrimaryKeySelectiveStatementId = updateByPrimaryKeySelectiveStatementId;
    }

    public String getUpdateByPrimaryKeySelectiveStatementId() {
        return updateByPrimaryKeySelectiveStatementId;
    }

    public void setUpdateByPrimaryKeyStatementId(String updateByPrimaryKeyStatementId) {
        this.updateByPrimaryKeyStatementId = updateByPrimaryKeyStatementId;
    }

    public String getUpdateByPrimaryKeyStatementId() {
        return updateByPrimaryKeyStatementId;
    }

    public void setUpdateByPrimaryKeyWithBLOBsStatementId(String updateByPrimaryKeyWithBLOBsStatementId) {
        this.updateByPrimaryKeyWithBLOBsStatementId = updateByPrimaryKeyWithBLOBsStatementId;
    }

    public String getUpdateByPrimaryKeyWithBLOBsStatementId() {
        return updateByPrimaryKeyWithBLOBsStatementId;
    }


    private boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
        return isTrue(propertyHolder.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
    }

    protected String calculateClientInterfacePackage(ClientGeneratorConfiguration config) {
        return config.getTargetPackage()
                + getFullyQualifiedTable().getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config));
    }

    protected String calculateDynamicSqlSupportPackage(ClientGeneratorConfiguration c) {
        return mapStringValueOrElseGet(c.getProperty(PropertyRegistry.CLIENT_DYNAMIC_SQL_SUPPORT_PACKAGE),
                s -> s + getFullyQualifiedTable().getSubPackageForClientOrSqlMap(isSubPackagesEnabled(c)),
                () -> calculateClientInterfacePackage(c));
    }

    private String calculateMyBatisDynamicSQLTableObjectName() {
        return stringValueOrElseGet(getTableConfiguration().getDynamicSqlTableObjectName(),
                () -> getFullyQualifiedTable().getDomainObjectName());
    }

    private String calculateMyBatis3JavaMapperType(ClientGeneratorConfiguration config) {
        StringBuilder sb = new StringBuilder();
        sb.append(calculateClientInterfacePackage(config));
        sb.append('.');
        ifStringHasValueElse(getTableConfiguration().getMapperName(), sb::append, () -> {
            getFullyQualifiedTable().getDomainObjectSubPackage().ifPresent(sp -> sb.append(sp).append('.'));
            sb.append(getFullyQualifiedTable().getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        });
        return sb.toString();
    }

    private String calculateMyBatis3SqlProviderType(ClientGeneratorConfiguration config) {
        StringBuilder sb = new StringBuilder();
        sb.append(calculateClientInterfacePackage(config));
        sb.append('.');
        ifStringHasValueElse(getTableConfiguration().getSqlProviderName(), sb::append, () -> {
            getFullyQualifiedTable().getDomainObjectSubPackage().ifPresent(sp -> sb.append(sp).append('.'));
            sb.append(getFullyQualifiedTable().getDomainObjectName());
            sb.append("SqlProvider"); //$NON-NLS-1$
        });
        return sb.toString();
    }

    private String calculateMyBatisDynamicSqlSupportType(ClientGeneratorConfiguration config) {
        StringBuilder sb = new StringBuilder();
        sb.append(calculateDynamicSqlSupportPackage(config));
        sb.append('.');
        ifStringHasValueElse(getTableConfiguration().getDynamicSqlSupportClassName(), sb::append, () -> {
            getFullyQualifiedTable().getDomainObjectSubPackage().ifPresent(sp -> sb.append(sp).append('.'));
            sb.append(getFullyQualifiedTable().getDomainObjectName());
            sb.append("DynamicSqlSupport"); //$NON-NLS-1$
        });
        return sb.toString();
    }

    private String calculateModelPackage() {
        ModelGeneratorConfiguration config = context.getModelGeneratorConfiguration();

        return config.getTargetPackage() + getFullyQualifiedTable().getSubPackageForModel(isSubPackagesEnabled(config));
    }

    private String calculatePrimaryKeyType(String modelPackage) {
        return modelPackage + '.' + getFullyQualifiedTable().getDomainObjectName() + "Key"; //$NON-NLS-1$
    }

    private String calculateBaseRecordType(String modelPackage) {
        return modelPackage + '.' + getFullyQualifiedTable().getDomainObjectName();
    }

    private String calculateKotlinRecordType(String modelPackage) {
        return modelPackage + '.' + getFullyQualifiedTable().getDomainObjectName();
    }

    private String calculateRecordWithBLOBsType(String modelPackage) {
        return modelPackage + '.' + getFullyQualifiedTable().getDomainObjectName() + "WithBLOBs"; //$NON-NLS-1$
    }

    private String calculateExampleType(String modelPackage) {
        return calculateModelExamplePackage(modelPackage) + '.'
                + getFullyQualifiedTable().getDomainObjectName() + "Example"; //$NON-NLS-1$
    }

    /**
     * If property exampleTargetPackage specified, for example, use the specified value, else
     * use the default value (targetPackage).
     *
     * @return the calculated package
     */
    protected String calculateModelExamplePackage(String modelPackage) {
        ModelGeneratorConfiguration config = context.getModelGeneratorConfiguration();
        return mapStringValueOrElseGet(config.getProperty(PropertyRegistry.MODEL_GENERATOR_EXAMPLE_PACKAGE),
                s -> s + getFullyQualifiedTable().getSubPackageForModel(isSubPackagesEnabled(config)),
                () -> modelPackage);
    }

    private String calculateSqlMapPackage(SqlMapGeneratorConfiguration config) {
        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(getFullyQualifiedTable().getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
        ifStringHasValueElse(getTableConfiguration().getMapperName(), mapperName -> {
            int ind = mapperName.lastIndexOf('.');
            if (ind != -1) {
                sb.append('.').append(mapperName, 0, ind);
            }
        }, () -> getFullyQualifiedTable().getDomainObjectSubPackage().ifPresent(sp -> sb.append('.').append(sp)));
        return sb.toString();
    }

    protected String calculateMyBatis3XmlMapperFileName() {
        return mapStringValueOrElseGet(getTableConfiguration().getMapperName(), mapperName -> {
            StringBuilder sb = new StringBuilder();
            int ind = mapperName.lastIndexOf('.');
            if (ind == -1) {
                sb.append(mapperName);
            } else {
                sb.append(mapperName.substring(ind + 1));
            }
            sb.append(".xml"); //$NON-NLS-1$
            return sb.toString();
        }, () -> getFullyQualifiedTable().getDomainObjectName() + "Mapper.xml"); //$NON-NLS-1$
    }

    // this method should be called after myBatis3JavaMapperType and myBatis3XmlMapperPackage values
    // are initialized
    private @Nullable String calculateMyBatis3SqlMapNamespace() {
        if (myBatis3JavaMapperType == null) {
            return calculateMyBatis3FallbackSqlMapNamespace();
        } else {
            return myBatis3JavaMapperType;
        }
    }

    private @Nullable String calculateMyBatis3FallbackSqlMapNamespace() {
        if (myBatis3XmlMapperPackage == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(myBatis3XmlMapperPackage);
        sb.append('.');
        ifStringHasValueElse(getTableConfiguration().getMapperName(), sb::append, () -> {
            sb.append(getFullyQualifiedTable().getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        });
        return sb.toString();
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public KnownRuntime getKnownRuntime() {
        return knownRuntime;
    }

    public boolean isImmutable() {
        return getTableConfiguration().isImmutable(context);
    }

    public boolean generateKotlinV1Model() {
        return getTableConfiguration().generateKotlinV1Model(context);
    }

    public boolean isConstructorBased() {
        if (isImmutable()) {
            return true;
        }

        Properties properties;

        if (getTableConfiguration().getProperties().containsKey(PropertyRegistry.ANY_CONSTRUCTOR_BASED)) {
            properties = getTableConfiguration().getProperties();
        } else {
            properties = context.getModelGeneratorConfiguration().getProperties();
        }

        return isTrue(properties.getProperty(PropertyRegistry.ANY_CONSTRUCTOR_BASED));
    }

    private <T> T requireNonNullElseInternalError(@Nullable T obj, String message) {
        if (obj == null) {
            throw new InternalException(message);
        }

        return obj;
    }

    public Context getContext() {
        return context;
    }

    public ModelType getModelType() {
        return tableConfiguration.getModelType().orElseGet(context::getDefaultModelType);
    }

    public boolean isRecordBased() {
        return getModelType() == ModelType.RECORD;
    }

    public Optional<String> findTableOrModelGeneratorProperty(String property) {
        return mapStringValueOrElseGet(getTableConfigurationProperty(property),
                Optional::of,
                () -> Optional.ofNullable(context.getModelGeneratorConfiguration().getProperty(property)));
    }

    public Optional<String> findTableOrClientGeneratorProperty(String property) {
        return mapStringValueOrElseGet(getTableConfigurationProperty(property),
                Optional::of,
                () -> context.getClientGeneratorConfiguration()
                        .map(c -> c.getProperty(property)));
    }

    public boolean findTableOrClientGeneratorPropertyAsBoolean(String property) {
        return findTableOrClientGeneratorProperty(property)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        private @Nullable KnownRuntime knownRuntime;
        private @Nullable TableConfiguration tableConfiguration;
        private @Nullable FullyQualifiedTable fullyQualifiedTable;
        private @Nullable Context context;

        public T withKnownRuntime(KnownRuntime knownRuntime) {
            this.knownRuntime = knownRuntime;
            return getThis();
        }

        public T withTableConfiguration(TableConfiguration tableConfiguration) {
            this.tableConfiguration = tableConfiguration;
            return getThis();
        }

        public T withFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
            this.fullyQualifiedTable = fullyQualifiedTable;
            return getThis();
        }

        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }

        protected abstract T getThis();
    }
}
