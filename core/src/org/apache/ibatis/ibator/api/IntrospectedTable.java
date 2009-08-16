/*
 *  Copyright 2006 The Apache Software Foundation
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

package org.apache.ibatis.ibator.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.config.DAOGeneratorConfiguration;
import org.apache.ibatis.ibator.config.GeneratedKey;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.config.JavaModelGeneratorConfiguration;
import org.apache.ibatis.ibator.config.ModelType;
import org.apache.ibatis.ibator.config.PropertyRegistry;
import org.apache.ibatis.ibator.config.SqlMapGeneratorConfiguration;
import org.apache.ibatis.ibator.config.TableConfiguration;
import org.apache.ibatis.ibator.internal.rules.ConditionalModelRules;
import org.apache.ibatis.ibator.internal.rules.FlatModelRules;
import org.apache.ibatis.ibator.internal.rules.HierarchicalModelRules;
import org.apache.ibatis.ibator.internal.rules.IbatorRules;
import org.apache.ibatis.ibator.internal.util.StringUtility;

/**
 * Base class for all code generator implementations.
 * This class provides many of the ibator housekeeping methods needed
 * to implement a code generator, with only the actual code generation
 * methods left unimplemented.
 * 
 * @author Jeff Butler
 *
 */
public abstract class IntrospectedTable {
    protected TableConfiguration tableConfiguration;
    protected FullyQualifiedTable fullyQualifiedTable;
    protected IbatorContext ibatorContext;
    protected IbatorRules rules;
    protected List<IntrospectedColumn> primaryKeyColumns;
    protected List<IntrospectedColumn> baseColumns;
    protected List<IntrospectedColumn> blobColumns;
    protected boolean hasJDBCDateColumns;
    protected boolean hasJDBCTimeColumns;
    
    public IntrospectedTable() {
        super();
        primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        baseColumns = new ArrayList<IntrospectedColumn>();
        blobColumns = new ArrayList<IntrospectedColumn>();
    }
    
    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }
    
    public String getSelectByExampleQueryId() {
        return tableConfiguration.getSelectByExampleQueryId();
    }
    
    public String getSelectByPrimaryKeyQueryId() {
        return tableConfiguration.getSelectByPrimaryKeyQueryId();
    }

    public GeneratedKey getGeneratedKey() {
        return tableConfiguration.getGeneratedKey();
    }

    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            // search primary key columns
            for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            
            // search base columns
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search blob columns
            for (IntrospectedColumn introspectedColumn : blobColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            
            return null;
        }
    }
    
    /**
     * Returns true if any of the columns in the table are JDBC Dates
     * (as opposed to timestamps).
     * 
     * @return true if the table contains DATE columns
     */
    public boolean hasJDBCDateColumns() {
        return hasJDBCDateColumns;
    }

    /**
     * Returns true if any of the columns in the table are JDBC Times
     * (as opposed to timestamps).
     * 
     * @return true if the table contains TIME columns
     */
    public boolean hasJDBCTimeColumns() {
        return hasJDBCTimeColumns;
    }
    
    /**
     * Returns the columns in the primary key.  If the
     * generatePrimaryKeyClass() method returns false, then these
     * columns will be iterated as the parameters of the 
     * selectByPrimaryKay and deleteByPrimaryKey methods
     * 
     * @return a List of ColumnDefinition objects for
     *   columns in the primary key
     */
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }
    
    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }
    
    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }
    
    /**
     * Returns all columns in the table (for use by the select by
     * primary key and select by example with BLOBs methods)
     * 
     * @return a List of ColumnDefinition objects for
     *   all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);
        
        return answer;
    }
    
    /**
     * Returns all columns except BLOBs (for use by the select by
     * example without BLOBs method)
     * 
     * @return a List of ColumnDefinition objects for
     *   columns in the table that are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        
        return answer;
    }
    
    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size()
            + baseColumns.size();
    }
    
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);
        
        return answer;
    }
    
    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }
    
    public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
    }
    
    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }
    
    public IbatorRules getRules() {
        if (rules == null) {
            if (tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
                rules = new HierarchicalModelRules(tableConfiguration, this);
            } else if (tableConfiguration.getModelType() == ModelType.FLAT) {
                rules = new FlatModelRules(tableConfiguration, this);
            } else {
                rules = new ConditionalModelRules(tableConfiguration, this);
            }
        }
        
        return rules;
    }
    
    public String getTableConfigurationProperty(String property) {
        return tableConfiguration.getProperty(property);
    }

    public FullyQualifiedJavaType getPrimaryKeyType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getJavaModelPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Key"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    /**
     * 
     * @return the type for the record (the class that holds non-primary
     *  key and non-BLOB fields).  Note that
     *  the value will be calculated regardless of whether the table has these columns or not.
     */
    public FullyQualifiedJavaType getBaseRecordType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getJavaModelPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    /**
     * 
     * @return the type for the example class.
     */
    public FullyQualifiedJavaType getExampleType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getJavaModelPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Example"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    /**
     * 
     * @return the type for the record with BLOBs class.  Note that
     *  the value will be calculated regardless of whether the table has BLOB columns or not.
     */
    public FullyQualifiedJavaType getRecordWithBLOBsType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getJavaModelPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    /**
     * Calculates an SQL Map file name for the table. Typically the name is
     * "XXXX_SqlMap.xml" where XXXX is the fully qualified table name (delimited
     * with underscores).
     * 
     * @return the name of the SqlMap file
     */
    public String getSqlMapFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getSqlMapNamespace());
        sb.append("_SqlMap.xml"); //$NON-NLS-1$

        return sb.toString();
    }

    /**
     * Calculates the package for the current table.
     * 
     * @return the package for the SqlMap for the current table
     */
    public String getSqlMapPackage() {
        SqlMapGeneratorConfiguration config = ibatorContext.getSqlMapGeneratorConfiguration();
        
        StringBuilder sb = new StringBuilder(config.getTargetPackage());
        if (StringUtility.isTrue(config.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES))) {
            sb.append(fullyQualifiedTable.getSubPackage());
        }
            
        return sb.toString();
    }
    
    public FullyQualifiedJavaType getDAOImplementationType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDAOImplementationPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAOImpl"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    public FullyQualifiedJavaType getDAOInterfaceType() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDAOInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAO"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(sb.toString());

        return fqjt;
    }

    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0
            || baseColumns.size() > 0
            || blobColumns.size() > 0;
    }
    
    protected String getDAOInterfacePackage() {
        DAOGeneratorConfiguration config = ibatorContext.getDaoGeneratorConfiguration();
        
        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        if (StringUtility.isTrue(config.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES))) {
            sb.append(fullyQualifiedTable.getSubPackage());
        }
        
        return sb.toString();
    }
    
    protected String getDAOImplementationPackage() {
        DAOGeneratorConfiguration config = ibatorContext.getDaoGeneratorConfiguration();
        
        StringBuilder sb = new StringBuilder();
        if (StringUtility.stringHasValue(config.getImplementationPackage())) {
            sb.append(config.getImplementationPackage());
        } else {
            sb.append(config.getTargetPackage());
        }
        if (StringUtility.isTrue(config.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES))) {
            sb.append(fullyQualifiedTable.getSubPackage());
        }
        
        return sb.toString();
    }
    
    protected String getJavaModelPackage() {
        JavaModelGeneratorConfiguration config = ibatorContext.getJavaModelGeneratorConfiguration();

        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        if (StringUtility.isTrue(config.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES))) {
            sb.append(fullyQualifiedTable.getSubPackage());
        }
        
        return sb.toString();
    }
    
    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }
    
    public void setIbatorContext(IbatorContext ibatorContext) {
        this.ibatorContext = ibatorContext;
    }

    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }
        
        if (introspectedColumn.isJDBCDateColumn()) {
            hasJDBCDateColumns = true;
        }
        
        if (introspectedColumn.isJDBCTimeColumn()) {
            hasJDBCTimeColumns = true;
        }
        
        introspectedColumn.setIntrospectedTable(this);
    }
    
    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }
        
        // search blob columns in the weird event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }
    
    /**
     * This method can be used to initialize the generators before they
     * will be called. 
     * 
     * This method is called after all the setX methods, but before
     * getNumberOfSubtasks(), getGeneratedJavaFiles, and getGeneratedXmlFiles.
     * 
     * @param warnings
     * @param progressCallback
     */
    public abstract void calculateGenerators(List<String> warnings, ProgressCallback progressCallback);
    
    /**
     * This method should return a list of generated Java files related to
     * this table.  This list could include various types of model classes,
     * as well as DAO classes.
     * 
     * @return the list of generated Java files for this table
     */
    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();
    
    /**
     * This method should return a list of generated XML files related to
     * this table.  Most implementations will only return one file - 
     * the generated SqlMap file.
     * 
     * @return the list of generated XML files for this table
     */
    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();
    
    /**
     * Denotes whether generated code is targeted for Java version 5.0
     * or higher.
     *   
     * @return true if the generated code makes use of Java5 features
     */
    public abstract boolean isJava5Targeted();
    
    /**
     * This method should return the number of progress messages that
     * will be send during the generation phase.
     * 
     * @return the number of progress messages
     */
    public abstract int getGenerationSteps();
}
