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
package org.mybatis.generator.config.xml;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.parseNullableBoolean;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.StringUtility.trimToNull;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.ColumnRenamingRule;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.DomainObjectRenamingRule;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.IgnoredColumn;
import org.mybatis.generator.config.IgnoredColumnException;
import org.mybatis.generator.config.IgnoredColumnPattern;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.Property;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class parses configuration files into the new Configuration API.
 *
 * @author Jeff Butler
 */
public class MyBatisGeneratorConfigurationParser {
    private final Properties extraProperties;
    private final Properties configurationProperties;

    public MyBatisGeneratorConfigurationParser(@Nullable Properties extraProperties) {
        this.extraProperties = Objects.requireNonNullElseGet(extraProperties, Properties::new);
        configurationProperties = new Properties();
    }

    public Configuration parseConfiguration(Element rootNode)
            throws XMLParserException {

        Configuration configuration = new Configuration();

        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (childNode.getNodeName()) {
            case "properties" ->  //$NON-NLS-1$
                    parsePropertiesElement(childNode);
            case "classPathEntry" ->  //$NON-NLS-1$
                    configuration.addClasspathEntry(parseClassPathEntry(childNode));
            case "context" ->  //$NON-NLS-1$
                    configuration.addContext(parseContext(childNode));
            default -> { }
            }
        }

        return configuration;
    }

    protected void parsePropertiesElement(Node node) throws XMLParserException {
        Properties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource"); //$NON-NLS-1$
        String url = attributes.getProperty("url"); //$NON-NLS-1$

        if (!stringHasValue(resource) && !stringHasValue(url)) {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        if (stringHasValue(resource) && stringHasValue(url)) {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        URL resourceUrl;

        try {
            if (stringHasValue(resource)) {
                resourceUrl = ObjectFactory.getResource(resource)
                        .orElseThrow(() -> new XMLParserException(getString("RuntimeError.15", resource)));
            } else {
                resourceUrl = new URL(url);
            }

            InputStream inputStream = resourceUrl.openConnection().getInputStream();

            configurationProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            if (stringHasValue(resource)) {
                throw new XMLParserException(getString("RuntimeError.16", resource)); //$NON-NLS-1$
            } else {
                throw new XMLParserException(getString("RuntimeError.17", url)); //$NON-NLS-1$
            }
        }
    }

    private Context parseContext(Node node) {
        Properties attributes = parseAttributes(node);
        String defaultModelType = attributes.getProperty("defaultModelType"); //$NON-NLS-1$
        String targetRuntime = attributes.getProperty("targetRuntime"); //$NON-NLS-1$
        String introspectedColumnImpl = attributes.getProperty("introspectedColumnImpl"); //$NON-NLS-1$
        String id = attributes.getProperty("id"); //$NON-NLS-1$

        ModelType mt = defaultModelType == null ? null : ModelType.getModelType(defaultModelType);

        Context context = new Context(id, mt);
        if (stringHasValue(introspectedColumnImpl)) {
            context.setIntrospectedColumnImpl(introspectedColumnImpl);
        }
        if (stringHasValue(targetRuntime)) {
            context.setTargetRuntime(targetRuntime);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (childNode.getNodeName()) {
            case "property" ->  //$NON-NLS-1$
                    context.addProperty(parseProperty(childNode));
            case "plugin" ->  //$NON-NLS-1$
                    context.addPluginConfiguration(parsePlugin(childNode));
            case "commentGenerator" ->  //$NON-NLS-1$
                    context.setCommentGeneratorConfiguration(parseCommentGenerator(childNode));
            case "jdbcConnection" ->  //$NON-NLS-1$
                    context.setJdbcConnectionConfiguration(parseJdbcConnection(childNode));
            case "connectionFactory" ->  //$NON-NLS-1$
                    context.setConnectionFactoryConfiguration(parseConnectionFactory(childNode));
            case "javaModelGenerator" ->  //$NON-NLS-1$
                    context.setJavaModelGeneratorConfiguration(parseJavaModelGenerator(childNode));
            case "javaTypeResolver" ->  //$NON-NLS-1$
                    context.setJavaTypeResolverConfiguration(parseJavaTypeResolver(childNode));
            case "sqlMapGenerator" ->  //$NON-NLS-1$
                    context.setSqlMapGeneratorConfiguration(parseSqlMapGenerator(childNode));
            case "javaClientGenerator" ->  //$NON-NLS-1$
                    context.setJavaClientGeneratorConfiguration(parseJavaClientGenerator(childNode));
            case "table" ->  //$NON-NLS-1$
                    context.addTableConfiguration(parseTable(context, childNode));
            default -> { }
            }
        }

        return context;
    }

    protected SqlMapGeneratorConfiguration parseSqlMapGenerator(Node node) {
        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration =
                new SqlMapGeneratorConfiguration(targetPackage, targetProject);
        Properties p = parseProperties(node.getChildNodes());
        sqlMapGeneratorConfiguration.addProperties(p);
        return sqlMapGeneratorConfiguration;
    }

    protected TableConfiguration parseTable(Context context, Node node) {
        TableConfiguration tc = new TableConfiguration(context);

        Properties attributes = parseAttributes(node);

        String catalog = attributes.getProperty("catalog"); //$NON-NLS-1$
        if (stringHasValue(catalog)) {
            tc.setCatalog(catalog);
        }

        String schema = attributes.getProperty("schema"); //$NON-NLS-1$
        if (stringHasValue(schema)) {
            tc.setSchema(schema);
        }

        String tableName = attributes.getProperty("tableName"); //$NON-NLS-1$
        if (stringHasValue(tableName)) {
            tc.setTableName(tableName);
        }

        String domainObjectName = attributes.getProperty("domainObjectName"); //$NON-NLS-1$
        if (stringHasValue(domainObjectName)) {
            tc.setDomainObjectName(domainObjectName);
        }

        String alias = attributes.getProperty("alias"); //$NON-NLS-1$
        if (stringHasValue(alias)) {
            tc.setAlias(alias);
        }

        String enableInsert = attributes.getProperty("enableInsert"); //$NON-NLS-1$
        if (stringHasValue(enableInsert)) {
            tc.setInsertStatementEnabled(isTrue(enableInsert));
        }

        String enableSelectByPrimaryKey = attributes.getProperty("enableSelectByPrimaryKey"); //$NON-NLS-1$
        if (stringHasValue(enableSelectByPrimaryKey)) {
            tc.setSelectByPrimaryKeyStatementEnabled(isTrue(enableSelectByPrimaryKey));
        }

        String enableSelectByExample = attributes.getProperty("enableSelectByExample"); //$NON-NLS-1$
        if (stringHasValue(enableSelectByExample)) {
            tc.setSelectByExampleStatementEnabled(isTrue(enableSelectByExample));
        }

        String enableUpdateByPrimaryKey = attributes.getProperty("enableUpdateByPrimaryKey"); //$NON-NLS-1$
        if (stringHasValue(enableUpdateByPrimaryKey)) {
            tc.setUpdateByPrimaryKeyStatementEnabled(isTrue(enableUpdateByPrimaryKey));
        }

        String enableDeleteByPrimaryKey = attributes.getProperty("enableDeleteByPrimaryKey"); //$NON-NLS-1$
        if (stringHasValue(enableDeleteByPrimaryKey)) {
            tc.setDeleteByPrimaryKeyStatementEnabled(isTrue(enableDeleteByPrimaryKey));
        }

        String enableDeleteByExample = attributes.getProperty("enableDeleteByExample"); //$NON-NLS-1$
        if (stringHasValue(enableDeleteByExample)) {
            tc.setDeleteByExampleStatementEnabled(isTrue(enableDeleteByExample));
        }

        String enableCountByExample = attributes.getProperty("enableCountByExample"); //$NON-NLS-1$
        if (stringHasValue(enableCountByExample)) {
            tc.setCountByExampleStatementEnabled(isTrue(enableCountByExample));
        }

        String enableUpdateByExample = attributes.getProperty("enableUpdateByExample"); //$NON-NLS-1$
        if (stringHasValue(enableUpdateByExample)) {
            tc.setUpdateByExampleStatementEnabled(isTrue(enableUpdateByExample));
        }

        String selectByPrimaryKeyQueryId = attributes.getProperty("selectByPrimaryKeyQueryId"); //$NON-NLS-1$
        if (stringHasValue(selectByPrimaryKeyQueryId)) {
            tc.setSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId);
        }

        String selectByExampleQueryId = attributes.getProperty("selectByExampleQueryId"); //$NON-NLS-1$
        if (stringHasValue(selectByExampleQueryId)) {tc.setSelectByExampleQueryId(selectByExampleQueryId);
        }

        String modelType = attributes.getProperty("modelType"); //$NON-NLS-1$
        if (stringHasValue(modelType)) {
            tc.setConfiguredModelType(modelType);
        }

        String escapeWildcards = attributes.getProperty("escapeWildcards"); //$NON-NLS-1$
        if (stringHasValue(escapeWildcards)) {
            tc.setWildcardEscapingEnabled(isTrue(escapeWildcards));
        }

        String delimitIdentifiers = attributes.getProperty("delimitIdentifiers"); //$NON-NLS-1$
        if (stringHasValue(delimitIdentifiers)) {
            tc.setDelimitIdentifiers(isTrue(delimitIdentifiers));
        }

        String delimitAllColumns = attributes.getProperty("delimitAllColumns"); //$NON-NLS-1$
        if (stringHasValue(delimitAllColumns)) {
            tc.setAllColumnDelimitingEnabled(isTrue(delimitAllColumns));
        }

        String mapperName = attributes.getProperty("mapperName"); //$NON-NLS-1$
        if (stringHasValue(mapperName)) {
            tc.setMapperName(mapperName);
        }

        String sqlProviderName = attributes.getProperty("sqlProviderName"); //$NON-NLS-1$
        if (stringHasValue(sqlProviderName)) {
            tc.setSqlProviderName(sqlProviderName);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (childNode.getNodeName()) {
            case "property" ->  //$NON-NLS-1$
                    tc.addProperty(parseProperty(childNode));
            case "columnOverride" ->  //$NON-NLS-1$
                    tc.addColumnOverride(parseColumnOverride(childNode));
            case "ignoreColumn" ->  //$NON-NLS-1$
                    tc.addIgnoredColumn(parseIgnoreColumn(childNode));
            case "ignoreColumnsByRegex" ->  //$NON-NLS-1$
                    tc.addIgnoredColumnPattern(parseIgnoreColumnByRegex(childNode));
            case "generatedKey" ->  //$NON-NLS-1$
                    tc.setGeneratedKey(parseGeneratedKey(childNode));
            case "domainObjectRenamingRule" ->  //$NON-NLS-1$
                    tc.setDomainObjectRenamingRule(parseDomainObjectRenamingRule(childNode));
            case "columnRenamingRule" ->  //$NON-NLS-1$
                    tc.setColumnRenamingRule(parseColumnRenamingRule(childNode));
            default -> { }
            }
        }

        return tc;
    }

    private ColumnOverride parseColumnOverride(Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String javaProperty = attributes.getProperty("property"); //$NON-NLS-1$
        String javaType = attributes.getProperty("javaType"); //$NON-NLS-1$
        String jdbcType = attributes.getProperty("jdbcType"); //$NON-NLS-1$
        String typeHandler = attributes.getProperty("typeHandler"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$

        String isGeneratedAlways = attributes.getProperty("isGeneratedAlways"); //$NON-NLS-1$

        ColumnOverride co = new ColumnOverride.Builder()
                .withColumnName(column)
                .withJavaProperty(javaProperty)
                .withJavaType(javaType)
                .withJdbcType(jdbcType)
                .withTypeHandler(typeHandler)
                .withColumnNameDelimited(parseNullableBoolean(delimitedColumnName))
                .withGeneratedAlways(isTrue(isGeneratedAlways))
                .build();

        Properties properties = parseProperties(node.getChildNodes());
        co.addProperties(properties);
        return co;
    }

    private GeneratedKey parseGeneratedKey(Node node) {
        Properties attributes = parseAttributes(node);

        String column = attributes.getProperty("column"); //$NON-NLS-1$
        boolean identity = isTrue(attributes
                .getProperty("identity")); //$NON-NLS-1$
        String sqlStatement = attributes.getProperty("sqlStatement"); //$NON-NLS-1$
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        return new GeneratedKey(column, sqlStatement, identity, type);
    }

    private IgnoredColumn parseIgnoreColumn(Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$
        return new IgnoredColumn(column, isTrue(delimitedColumnName));
    }

    private IgnoredColumnPattern parseIgnoreColumnByRegex(Node node) {
        Properties attributes = parseAttributes(node);
        String pattern = attributes.getProperty("pattern"); //$NON-NLS-1$

        IgnoredColumnPattern icPattern = new IgnoredColumnPattern(pattern);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("except".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseException(icPattern, childNode);
            }
        }

        return icPattern;
    }

    private void parseException(IgnoredColumnPattern icPattern, Node node) {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$

        IgnoredColumnException exception = new IgnoredColumnException(column, isTrue(delimitedColumnName));

        icPattern.addException(exception);
    }

    private DomainObjectRenamingRule parseDomainObjectRenamingRule(Node node) {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$

        return new DomainObjectRenamingRule(searchString, replaceString);
    }

    private ColumnRenamingRule parseColumnRenamingRule(Node node) {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$
        return new ColumnRenamingRule(searchString, replaceString);
    }

    protected JavaTypeResolverConfiguration parseJavaTypeResolver(Node node) {
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        JavaTypeResolverConfiguration javaTypeResolverConfiguration =
                new JavaTypeResolverConfiguration(trimToNull(type));
        Properties properties = parseProperties(node.getChildNodes());
        javaTypeResolverConfiguration.addProperties(properties);
        return javaTypeResolverConfiguration;
    }

    private PluginConfiguration parsePlugin(Node node) {
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        PluginConfiguration pluginConfiguration = new PluginConfiguration(trimToNull(type));
        Properties properties = parseProperties(node.getChildNodes());
        pluginConfiguration.addProperties(properties);
        return pluginConfiguration;
    }

    protected JavaModelGeneratorConfiguration parseJavaModelGenerator(Node node) {
        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration =
                new JavaModelGeneratorConfiguration(targetPackage, targetProject);
        Properties properties = parseProperties(node.getChildNodes());
        javaModelGeneratorConfiguration.addProperties(properties);
        return javaModelGeneratorConfiguration;
    }

    private JavaClientGeneratorConfiguration parseJavaClientGenerator(Node node) {
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration =
                new JavaClientGeneratorConfiguration(trimToNull(type), targetPackage, targetProject);
        Properties properties = parseProperties(node.getChildNodes());
        javaClientGeneratorConfiguration.addProperties(properties);
        return javaClientGeneratorConfiguration;
    }

    protected JDBCConnectionConfiguration parseJdbcConnection(Node node) {
        Properties attributes = parseAttributes(node);
        String driverClass = attributes.getProperty("driverClass"); //$NON-NLS-1$
        String connectionURL = attributes.getProperty("connectionURL"); //$NON-NLS-1$
        String userId = attributes.getProperty("userId"); //$NON-NLS-1$
        String password = attributes.getProperty("password"); //$NON-NLS-1$
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration(driverClass,
                connectionURL, trimToNull(userId), trimToNull(password));
        Properties properties = parseProperties(node.getChildNodes());
        jdbcConnectionConfiguration.addProperties(properties);
        return jdbcConnectionConfiguration;
    }

    protected String parseClassPathEntry(Node node) {
        Properties attributes = parseAttributes(node);
        return attributes.getProperty("location"); //$NON-NLS-1$
    }

    protected Properties parseProperties(NodeList nodeList) {
        Properties properties = new Properties();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                Property property = parseProperty(childNode);
                properties.setProperty(property.name(), property.value());
            }
        }
        return properties;
    }

    protected Property parseProperty(Node node) {
        Properties attributes = parseAttributes(node);

        String name = attributes.getProperty("name"); //$NON-NLS-1$
        String value = attributes.getProperty("value"); //$NON-NLS-1$
        return new Property(name, value);
    }

    /**
     * Parses node attributes.
     *
     * <p>Any attribut with an empty value (defined as missing, or blank string) will be dropped.
     *
     * @param node the node
     * @return properties containing all non-empty attributes
     */
    protected Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            value = trimToNull(value);
            if (value != null) {
                attributes.put(attribute.getNodeName(), value);
            }
        }

        return attributes;
    }

    String parsePropertyTokens(String s) {
        final String OPEN = "${"; //$NON-NLS-1$
        final String CLOSE = "}"; //$NON-NLS-1$
        int currentIndex = 0;

        List<String> answer = new ArrayList<>();

        int markerStartIndex = s.indexOf(OPEN);
        if (markerStartIndex < 0) {
            // no parameter markers
            answer.add(s);
            currentIndex = s.length();
        }

        while (markerStartIndex > -1) {
            if (markerStartIndex > currentIndex) {
                // add the characters before the next parameter marker
                answer.add(s.substring(currentIndex, markerStartIndex));
                currentIndex = markerStartIndex;
            }

            int markerEndIndex = s.indexOf(CLOSE, currentIndex);
            int nestedStartIndex = s.indexOf(OPEN, markerStartIndex + OPEN.length());
            while (nestedStartIndex > -1 && markerEndIndex > -1 && nestedStartIndex < markerEndIndex) {
                nestedStartIndex = s.indexOf(OPEN, nestedStartIndex + OPEN.length());
                markerEndIndex = s.indexOf(CLOSE, markerEndIndex + CLOSE.length());
            }

            if (markerEndIndex < 0) {
                // no closing delimiter, just move to the end of the string
                answer.add(s.substring(markerStartIndex));
                currentIndex = s.length();
                break;
            }

            // we have a valid property marker...
            String property = s.substring(markerStartIndex + OPEN.length(), markerEndIndex);
            String propertyValue = resolveProperty(parsePropertyTokens(property));
            if (propertyValue == null) {
                // add the property marker back into the stream
                answer.add(s.substring(markerStartIndex, markerEndIndex + 1));
            } else {
                answer.add(propertyValue);
            }

            currentIndex = markerEndIndex + CLOSE.length();
            markerStartIndex = s.indexOf(OPEN, currentIndex);
        }

        if (currentIndex < s.length()) {
            answer.add(s.substring(currentIndex));
        }

        return String.join("", answer);
    }

    protected CommentGeneratorConfiguration parseCommentGenerator(Node node) {
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        CommentGeneratorConfiguration commentGeneratorConfiguration =
                new CommentGeneratorConfiguration(trimToNull(type));
        Properties properties = parseProperties(node.getChildNodes());
        commentGeneratorConfiguration.addProperties(properties);
        return commentGeneratorConfiguration;
    }

    protected ConnectionFactoryConfiguration parseConnectionFactory(Node node) {
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        ConnectionFactoryConfiguration connectionFactoryConfiguration =
                new ConnectionFactoryConfiguration(trimToNull(type));
        Properties properties = parseProperties(node.getChildNodes());
        connectionFactoryConfiguration.addProperties(properties);
        return connectionFactoryConfiguration;
    }

    /**
     * This method resolves a property from one of the three sources: system properties,
     * properties loaded from the &lt;properties&gt; configuration element, and
     * "extra" properties that may be supplied by the Maven or Ant environments.
     *
     * <p>If there is a name collision, system properties take precedence, followed by
     * configuration properties, followed by extra properties.
     *
     * @param key property key
     * @return the resolved property.  This method will return null if the property is
     *     undefined in any of the sources.
     */
    private @Nullable String resolveProperty(String key) {
        String property = System.getProperty(key);

        if (property == null) {
            property = configurationProperties.getProperty(key);
        }

        if (property == null) {
            property = extraProperties.getProperty(key);
        }

        return property;
    }
}
