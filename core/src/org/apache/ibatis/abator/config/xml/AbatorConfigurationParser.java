/*
 *  Copyright 2005, 2006 The Apache Software Foundation
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
package org.apache.ibatis.abator.config.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ibatis.abator.config.AbatorConfiguration;
import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.config.ColumnOverride;
import org.apache.ibatis.abator.config.ColumnRenamingRule;
import org.apache.ibatis.abator.config.CommentGeneratorConfiguration;
import org.apache.ibatis.abator.config.DAOGeneratorConfiguration;
import org.apache.ibatis.abator.config.GeneratedKey;
import org.apache.ibatis.abator.config.IgnoredColumn;
import org.apache.ibatis.abator.config.JDBCConnectionConfiguration;
import org.apache.ibatis.abator.config.JavaModelGeneratorConfiguration;
import org.apache.ibatis.abator.config.JavaTypeResolverConfiguration;
import org.apache.ibatis.abator.config.ModelType;
import org.apache.ibatis.abator.config.PropertyHolder;
import org.apache.ibatis.abator.config.SqlMapGeneratorConfiguration;
import org.apache.ibatis.abator.config.TableConfiguration;
import org.apache.ibatis.abator.exception.XMLParserException;
import org.apache.ibatis.abator.internal.util.StringUtility;
import org.apache.ibatis.abator.internal.util.messages.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Jeff Butler
 */
public class AbatorConfigurationParser {
    private List warnings;

    private List parseErrors;

    private Properties properties;

    /**
     * 
     */
    public AbatorConfigurationParser(List warnings) {
        this(null, warnings);
    }

    public AbatorConfigurationParser(Properties properties, List warnings) {
        super();
        if (properties == null) {
            this.properties = System.getProperties();
        } else {
            this.properties = properties;
        }
        
        if (warnings == null) {
            this.warnings = new ArrayList();
        } else {
            this.warnings = warnings;
        }
        
        parseErrors = new ArrayList();
    }
    
    public AbatorConfiguration parseAbatorConfiguration(File inputFile)
            throws IOException, XMLParserException {

        FileReader fr = new FileReader(inputFile);

        return parseAbatorConfiguration(fr);
    }

    public AbatorConfiguration parseAbatorConfiguration(Reader reader)
            throws IOException, XMLParserException {

        InputSource is = new InputSource(reader);

        return parseAbatorConfiguration(is);
    }

    public AbatorConfiguration parseAbatorConfiguration(InputStream inputStream)
            throws IOException, XMLParserException {

        InputSource is = new InputSource(inputStream);

        return parseAbatorConfiguration(is);
    }

    private AbatorConfiguration parseAbatorConfiguration(InputSource inputSource)
            throws IOException, XMLParserException {
        parseErrors.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new ParserEntityResolver());

            ParserErrorHandler handler = new ParserErrorHandler(warnings,
                    parseErrors);
            builder.setErrorHandler(handler);

            Document document = null;
            try {
                document = builder.parse(inputSource);
            } catch (SAXParseException e) {
                throw new XMLParserException(parseErrors);
            } catch (SAXException e) {
                if (e.getException() == null) {
                    parseErrors.add(e.getMessage());
                } else {
                    parseErrors.add(e.getException().getMessage());
                }
            }

            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }

            AbatorConfiguration ac;
            Element rootNode = document.getDocumentElement();
            if (rootNode.getNodeType() == 1
                    && "abatorConfiguration".equals(rootNode.getNodeName())) { //$NON-NLS-1$
                ac = parseAbatorConfiguration(rootNode);
            } else {
                throw new XMLParserException(Messages.getString("RuntimeError.5")); //$NON-NLS-1$
            }
            
            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }

            return ac;
        } catch (ParserConfigurationException e) {
            parseErrors.add(e.getMessage());
            throw new XMLParserException(parseErrors);
        }
    }

    private AbatorConfiguration parseAbatorConfiguration(Element node) throws XMLParserException {

        AbatorConfiguration abatorConfiguration = new AbatorConfiguration();

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("properties".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperties(abatorConfiguration, childNode);
            } else if ("abatorContext".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseAbatorContext(abatorConfiguration, childNode);
            }
        }

        return abatorConfiguration;
    }

    private void parseProperties(AbatorConfiguration abatorConfiguration,
            Node node) throws XMLParserException {
        Properties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource"); //$NON-NLS-1$
        String url = attributes.getProperty("url"); //$NON-NLS-1$

        if (!StringUtility.stringHasValue(resource)
                && !StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14")); //$NON-NLS-1$
        }

        if (StringUtility.stringHasValue(resource)
                && StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14")); //$NON-NLS-1$
        }

        URL resourceUrl;

        try {
            if (StringUtility.stringHasValue(resource)) {
                resourceUrl = Thread.currentThread().getContextClassLoader()
                        .getResource(resource);
                if (resourceUrl == null) {
                    throw new XMLParserException(Messages.getString("RuntimeError.15", resource)); //$NON-NLS-1$
                }
            } else {
                resourceUrl = new URL(url);
            }

            InputStream inputStream = resourceUrl.openConnection()
                    .getInputStream();

            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            if (StringUtility.stringHasValue(resource)) {
                throw new XMLParserException(Messages.getString("RuntimeError.16", resource)); //$NON-NLS-1$
            } else {
                throw new XMLParserException(Messages.getString("RuntimeError.17", url)); //$NON-NLS-1$
            }
        }
    }

    private void parseAbatorContext(AbatorConfiguration abatorConfiguration,
            Node node) {

        Properties attributes = parseAttributes(node);
        String generatorSet = attributes.getProperty("generatorSet"); //$NON-NLS-1$
        String defaultModelType = attributes.getProperty("defaultModelType"); //$NON-NLS-1$
        String id = attributes.getProperty("id"); //$NON-NLS-1$

        ModelType mt = defaultModelType == null ? null : ModelType
                .getModelType(defaultModelType);

        AbatorContext abatorContext = new AbatorContext(generatorSet, mt);
        abatorContext.setId(id);

        abatorConfiguration.addAbatorContext(abatorContext);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(abatorContext, childNode);
            } else if ("commentGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseCommentGenerator(abatorContext, childNode);
            } else if ("jdbcConnection".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJdbcConnection(abatorContext, childNode);
            } else if ("javaModelGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaModelGenerator(abatorContext, childNode);
            } else if ("javaTypeResolver".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaTypeResolver(abatorContext, childNode);
            } else if ("sqlMapGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseSqlMapGenerator(abatorContext, childNode);
            } else if ("daoGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseDaoGenerator(abatorContext, childNode);
            } else if ("table".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseTable(abatorContext, childNode);
            }
        }
    }

    private void parseSqlMapGenerator(AbatorContext abatorContext, Node node) {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();

        abatorContext
                .setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$

        if (StringUtility.stringHasValue(type)) {
            sqlMapGeneratorConfiguration.setConfigurationType(type);
        }

        sqlMapGeneratorConfiguration.setTargetPackage(targetPackage);
        sqlMapGeneratorConfiguration.setTargetProject(targetProject);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(sqlMapGeneratorConfiguration, childNode);
            }
        }
    }

    private void parseTable(AbatorContext abatorContext, Node node) {
        TableConfiguration tc = new TableConfiguration(abatorContext);
        abatorContext.addTableConfiguration(tc);

        Properties attributes = parseAttributes(node);
        String catalog = attributes.getProperty("catalog"); //$NON-NLS-1$
        String schema = attributes.getProperty("schema"); //$NON-NLS-1$
        String tableName = attributes.getProperty("tableName"); //$NON-NLS-1$
        String domainObjectName = attributes.getProperty("domainObjectName"); //$NON-NLS-1$
        String alias = attributes.getProperty("alias"); //$NON-NLS-1$
        String enableInsert = attributes.getProperty("enableInsert"); //$NON-NLS-1$
        String enableSelectByPrimaryKey = attributes
                .getProperty("enableSelectByPrimaryKey"); //$NON-NLS-1$
        String enableSelectByExample = attributes
                .getProperty("enableSelectByExample"); //$NON-NLS-1$
        String enableUpdateByPrimaryKey = attributes
                .getProperty("enableUpdateByPrimaryKey"); //$NON-NLS-1$
        String enableDeleteByPrimaryKey = attributes
                .getProperty("enableDeleteByPrimaryKey"); //$NON-NLS-1$
        String enableDeleteByExample = attributes
                .getProperty("enableDeleteByExample"); //$NON-NLS-1$
        String enableCountByExample = attributes
            .getProperty("enableCountByExample"); //$NON-NLS-1$
        String enableUpdateByExample = attributes
            .getProperty("enableUpdateByExample"); //$NON-NLS-1$
        String selectByPrimaryKeyQueryId = attributes
                .getProperty("selectByPrimaryKeyQueryId"); //$NON-NLS-1$
        String selectByExampleQueryId = attributes
                .getProperty("selectByExampleQueryId"); //$NON-NLS-1$
        String modelType = attributes.getProperty("modelType"); //$NON-NLS-1$
        String escapeWildcards = attributes.getProperty("escapeWildcards"); //$NON-NLS-1$
        String delmitIdentifiers = attributes.getProperty("delimitIdentifiers"); //$NON-NLS-1$

        if (StringUtility.stringHasValue(catalog)) {
            tc.setCatalog(catalog);
        }

        if (StringUtility.stringHasValue(schema)) {
            tc.setSchema(schema);
        }

        if (StringUtility.stringHasValue(tableName)) {
            tc.setTableName(tableName);
        }

        if (StringUtility.stringHasValue(domainObjectName)) {
            tc.setDomainObjectName(domainObjectName);
        }

        if (StringUtility.stringHasValue(alias)) {
            tc.setAlias(alias);
        }

        if (StringUtility.stringHasValue(enableInsert)) {
            tc.setInsertStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableInsert));
        }

        if (StringUtility.stringHasValue(enableSelectByPrimaryKey)) {
            tc.setSelectByPrimaryKeyStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableSelectByPrimaryKey));
        }

        if (StringUtility.stringHasValue(enableSelectByExample)) {
            tc.setSelectByExampleStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableSelectByExample));
        }

        if (StringUtility.stringHasValue(enableUpdateByPrimaryKey)) {
            tc.setUpdateByPrimaryKeyStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableUpdateByPrimaryKey));
        }

        if (StringUtility.stringHasValue(enableDeleteByPrimaryKey)) {
            tc.setDeleteByPrimaryKeyStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableDeleteByPrimaryKey));
        }

        if (StringUtility.stringHasValue(enableDeleteByExample)) {
            tc.setDeleteByExampleStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableDeleteByExample));
        }

        if (StringUtility.stringHasValue(enableCountByExample)) {
            tc.setCountByExampleStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableCountByExample));
        }

        if (StringUtility.stringHasValue(enableUpdateByExample)) {
            tc.setUpdateByExampleStatementEnabled("true" //$NON-NLS-1$
                    .equals(enableUpdateByExample));
        }

        if (StringUtility.stringHasValue(selectByPrimaryKeyQueryId)) {
            tc.setSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId);
        }

        if (StringUtility.stringHasValue(selectByExampleQueryId)) {
            tc.setSelectByExampleQueryId(selectByExampleQueryId);
        }

        if (StringUtility.stringHasValue(modelType)) {
            tc.setConfiguredModelType(modelType);
        }

        if (StringUtility.stringHasValue(escapeWildcards)) {
            tc.setWildcardEscapingEnabled("true" //$NON-NLS-1$
                    .equals(escapeWildcards));
        }

        if (StringUtility.stringHasValue(delmitIdentifiers)) {
            tc.setDelimitIdentifiers("true" //$NON-NLS-1$
                    .equals(delmitIdentifiers));
        }
        
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(tc, childNode);
            } else if ("columnOverride".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseColumnOverride(tc, childNode);
            } else if ("ignoreColumn".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseIgnoreColumn(tc, childNode);
            } else if ("generatedKey".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseGeneratedKey(tc, childNode);
            } else if ("columnRenamingRule".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseColumnRenamingRule(tc, childNode);
            }
        }
    }

    private void parseColumnOverride(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String property = attributes.getProperty("property"); //$NON-NLS-1$
        String javaType = attributes.getProperty("javaType"); //$NON-NLS-1$
        String jdbcType = attributes.getProperty("jdbcType"); //$NON-NLS-1$
        String typeHandler = attributes.getProperty("typeHandler"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$

        ColumnOverride co = new ColumnOverride(column);

        if (StringUtility.stringHasValue(property)) {
            co.setJavaProperty(property);
        }

        if (StringUtility.stringHasValue(javaType)) {
            co.setJavaType(javaType);
        }

        if (StringUtility.stringHasValue(jdbcType)) {
            co.setJdbcType(jdbcType);
        }

        if (StringUtility.stringHasValue(typeHandler)) {
            co.setTypeHandler(typeHandler);
        }

        if (StringUtility.stringHasValue(delimitedColumnName)) {
            co.setColumnNameDelimited("true".equalsIgnoreCase(delimitedColumnName)); //$NON-NLS-1$
        }
        
        tc.addColumnOverride(co);
    }

    private void parseGeneratedKey(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);

        String column = attributes.getProperty("column"); //$NON-NLS-1$
        boolean identity = "true".equals(attributes.getProperty("identity")); //$NON-NLS-1$ //$NON-NLS-2$
        String sqlStatement = attributes.getProperty("sqlStatement"); //$NON-NLS-1$
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        GeneratedKey gk = new GeneratedKey(column, sqlStatement, identity, type);

        tc.setGeneratedKey(gk);
    }

    private void parseIgnoreColumn(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$
        
        IgnoredColumn ic = new IgnoredColumn(column);
        
        if (StringUtility.stringHasValue(delimitedColumnName)) {
            ic.setColumnNameDelimited("true".equalsIgnoreCase(delimitedColumnName)); //$NON-NLS-1$
        }

        tc.addIgnoredColumn(ic);
    }

    private void parseColumnRenamingRule(TableConfiguration tc, Node node) {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$
        
        ColumnRenamingRule crr = new ColumnRenamingRule();
        
        crr.setSearchString(searchString);
        
        if (StringUtility.stringHasValue(replaceString)) {
            crr.setReplaceString(replaceString);
        }

        tc.setColumnRenamingRule(crr);
    }
    
    private void parseJavaTypeResolver(AbatorContext abatorContext, Node node) {
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();

        abatorContext
                .setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        if (StringUtility.stringHasValue(type)) {
            javaTypeResolverConfiguration.setConfigurationType(type);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaTypeResolverConfiguration, childNode);
            }
        }
    }

    private void parseJavaModelGenerator(AbatorContext abatorContext, Node node) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();

        abatorContext
                .setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$

        if (StringUtility.stringHasValue(type)) {
            javaModelGeneratorConfiguration.setConfigurationType(type);
        }

        javaModelGeneratorConfiguration.setTargetPackage(targetPackage);
        javaModelGeneratorConfiguration.setTargetProject(targetProject);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaModelGeneratorConfiguration, childNode);
            }
        }
    }

    private void parseDaoGenerator(AbatorContext abatorContext, Node node) {
        DAOGeneratorConfiguration daoGeneratorConfiguration = new DAOGeneratorConfiguration();

        abatorContext.setDaoGeneratorConfiguration(daoGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$

        daoGeneratorConfiguration.setConfigurationType(type);
        daoGeneratorConfiguration.setTargetPackage(targetPackage);
        daoGeneratorConfiguration.setTargetProject(targetProject);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(daoGeneratorConfiguration, childNode);
            }
        }
    }

    private void parseJdbcConnection(AbatorContext abatorContext, Node node) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();

        abatorContext
                .setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        Properties attributes = parseAttributes(node);
        String driverClass = attributes.getProperty("driverClass"); //$NON-NLS-1$
        String connectionURL = attributes.getProperty("connectionURL"); //$NON-NLS-1$
        String userId = attributes.getProperty("userId"); //$NON-NLS-1$
        String password = attributes.getProperty("password"); //$NON-NLS-1$

        jdbcConnectionConfiguration.setDriverClass(driverClass);
        jdbcConnectionConfiguration.setConnectionURL(connectionURL);

        if (StringUtility.stringHasValue(userId)) {
            jdbcConnectionConfiguration.setUserId(userId);
        }

        if (StringUtility.stringHasValue(password)) {
            jdbcConnectionConfiguration.setPassword(password);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("classPathEntry".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseClassPathEntry(jdbcConnectionConfiguration, childNode);
            } else if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(jdbcConnectionConfiguration, childNode);
            }
        }
    }

    private void parseClassPathEntry(
            JDBCConnectionConfiguration jdbcConnectionConfiguration, Node node) {
        Properties attributes = parseAttributes(node);

        jdbcConnectionConfiguration.addClasspathEntry(attributes
                .getProperty("location")); //$NON-NLS-1$
    }

    private void parseProperty(PropertyHolder propertyHolder, Node node) {
        Properties attributes = parseAttributes(node);

        String name = attributes.getProperty("name"); //$NON-NLS-1$
        String value = attributes.getProperty("value"); //$NON-NLS-1$

        propertyHolder.addProperty(name, value);
    }

    private Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    private String parsePropertyTokens(String string) {
        final String OPEN = "${"; //$NON-NLS-1$
        final String CLOSE = "}"; //$NON-NLS-1$

        String newString = string;
        if (newString != null) {
            int start = newString.indexOf(OPEN);
            int end = newString.indexOf(CLOSE);

            while (start > -1 && end > start) {
                String prepend = newString.substring(0, start);
                String append = newString.substring(end + CLOSE.length());
                String propName = newString.substring(start + OPEN.length(),
                        end);
                String propValue = properties.getProperty(propName);
                if (propValue != null) {
                    newString = prepend + propValue + append;
                }

                start = newString.indexOf(OPEN,end);
                end = newString.indexOf(CLOSE,end);
            }
        }

        return newString;
    }

    private void parseCommentGenerator(AbatorContext abatorContext, Node node) {
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();

        abatorContext.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        if (StringUtility.stringHasValue(type)) {
            commentGeneratorConfiguration.setConfigurationType(type);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != 1) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(commentGeneratorConfiguration, childNode);
            }
        }
    }
}
