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
import static org.mybatis.generator.internal.util.StringUtility.trimToNull;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.mybatis.generator.config.NullableProperties;
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

    public Configuration parseConfiguration(Element rootNode) throws XMLParserException {
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
            default -> {
                // Ignore unrecognized elements
            }
            }
        }

        return configuration;
    }

    protected void parsePropertiesElement(Node node) throws XMLParserException {
        NullableProperties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource"); //$NON-NLS-1$
        String url = attributes.getProperty("url"); //$NON-NLS-1$

        if (resource == null && url == null) {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        if (resource != null && url != null) {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        if (resource != null) {
            loadPropertiesFromResource(resource);
        } else {
            loadPropertiesFromURL(url);
        }
    }

    private void loadPropertiesFromResource(String resource) throws XMLParserException {
        try {
            URL resourceUrl = ObjectFactory.getResource(resource)
                    .orElseThrow(() -> new XMLParserException(getString("RuntimeError.15", resource)));
            InputStream inputStream = resourceUrl.openConnection().getInputStream();
            configurationProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new XMLParserException(getString("RuntimeError.16", resource)); //$NON-NLS-1$
        }
    }

    private void loadPropertiesFromURL(String url) throws XMLParserException {
        try {
            URL resourceUrl = new URL(url);
            InputStream inputStream = resourceUrl.openConnection().getInputStream();
            configurationProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new XMLParserException(getString("RuntimeError.17", url)); //$NON-NLS-1$
        }
    }

    private Context parseContext(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String defaultModelType = attributes.getProperty("defaultModelType"); //$NON-NLS-1$
        String targetRuntime = attributes.getProperty("targetRuntime"); //$NON-NLS-1$
        String introspectedColumnImpl = attributes.getProperty("introspectedColumnImpl"); //$NON-NLS-1$
        String id = attributes.getProperty("id"); //$NON-NLS-1$
        ModelType dmt =
                defaultModelType == null ? null : ModelType.getModelType(defaultModelType);

        Context.Builder builder = new Context.Builder()
                .withId(id)
                .withDefaultModelType(dmt)
                .withIntrospectedColumnImpl(introspectedColumnImpl)
                .withTargetRuntime(targetRuntime);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (childNode.getNodeName()) {
            case "property" ->  //$NON-NLS-1$
                    parseProperty(childNode).ifPresent(builder::withProperty);
            case "plugin" ->  //$NON-NLS-1$
                    builder.withPluginConfiguration(parsePlugin(childNode));
            case "commentGenerator" ->  //$NON-NLS-1$
                    builder.withCommentGeneratorConfiguration(parseCommentGenerator(childNode));
            case "jdbcConnection" ->  //$NON-NLS-1$
                    builder.withJdbcConnectionConfiguration(parseJdbcConnection(childNode));
            case "connectionFactory" ->  //$NON-NLS-1$
                    builder.withConnectionFactoryConfiguration(parseConnectionFactory(childNode));
            case "javaModelGenerator" ->  //$NON-NLS-1$
                    builder.withJavaModelGeneratorConfiguration(parseJavaModelGenerator(childNode));
            case "javaTypeResolver" ->  //$NON-NLS-1$
                    builder.withJavaTypeResolverConfiguration(parseJavaTypeResolver(childNode));
            case "sqlMapGenerator" ->  //$NON-NLS-1$
                    builder.withSqlMapGeneratorConfiguration(parseSqlMapGenerator(childNode));
            case "javaClientGenerator" ->  //$NON-NLS-1$
                    builder.withJavaClientGeneratorConfiguration(parseJavaClientGenerator(childNode));
            case "table" ->  //$NON-NLS-1$
                    builder.withTableConfiguration(parseTable(childNode));
            default -> {
                // Ignore unrecognized elements
            }
            }
        }

        return builder.build();
    }

    protected SqlMapGeneratorConfiguration parseSqlMapGenerator(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new SqlMapGeneratorConfiguration.Builder()
                .withTargetPackage(targetPackage)
                .withTargetProject(targetProject)
                .withProperties(properties)
                .build();
    }

    protected TableConfiguration parseTable(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String catalog = attributes.getProperty("catalog"); //$NON-NLS-1$
        String schema = attributes.getProperty("schema"); //$NON-NLS-1$
        String tableName = attributes.getProperty("tableName"); //$NON-NLS-1$
        String domainObjectName = attributes.getProperty("domainObjectName"); //$NON-NLS-1$
        String alias = attributes.getProperty("alias"); //$NON-NLS-1$
        String selectByPrimaryKeyQueryId = attributes.getProperty("selectByPrimaryKeyQueryId"); //$NON-NLS-1$
        String selectByExampleQueryId = attributes.getProperty("selectByExampleQueryId"); //$NON-NLS-1$
        String modelType = attributes.getProperty("modelType"); //$NON-NLS-1$
        String mapperName = attributes.getProperty("mapperName"); //$NON-NLS-1$
        String sqlProviderName = attributes.getProperty("sqlProviderName"); //$NON-NLS-1$

        TableConfiguration.Builder builder = new TableConfiguration.Builder()
                .withModelType(modelType)
                .withCatalog(catalog)
                .withSchema(schema)
                .withTableName(tableName)
                .withDomainObjectName(domainObjectName)
                .withAlias(alias)
                .withSelectByExampleQueryId(selectByExampleQueryId)
                .withSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId)
                .withMapperName(mapperName)
                .withSqlProviderName(sqlProviderName);

        String enableInsert = attributes.getProperty("enableInsert"); //$NON-NLS-1$
        if (enableInsert != null) {
            builder.withInsertStatementEnabled(isTrue(enableInsert));
        }

        String enableSelectByPrimaryKey = attributes.getProperty("enableSelectByPrimaryKey"); //$NON-NLS-1$
        if (enableSelectByPrimaryKey != null) {
            builder.withSelectByPrimaryKeyStatementEnabled(isTrue(enableSelectByPrimaryKey));
        }

        String enableSelectByExample = attributes.getProperty("enableSelectByExample"); //$NON-NLS-1$
        if (enableSelectByExample != null) {
            builder.withSelectByExampleStatementEnabled(isTrue(enableSelectByExample));
        }

        String enableUpdateByPrimaryKey = attributes.getProperty("enableUpdateByPrimaryKey"); //$NON-NLS-1$
        if (enableUpdateByPrimaryKey != null) {
            builder.withUpdateByPrimaryKeyStatementEnabled(isTrue(enableUpdateByPrimaryKey));
        }

        String enableDeleteByPrimaryKey = attributes.getProperty("enableDeleteByPrimaryKey"); //$NON-NLS-1$
        if (enableDeleteByPrimaryKey != null) {
            builder.withDeleteByPrimaryKeyStatementEnabled(isTrue(enableDeleteByPrimaryKey));
        }

        String enableDeleteByExample = attributes.getProperty("enableDeleteByExample"); //$NON-NLS-1$
        if (enableDeleteByExample != null) {
            builder.withDeleteByExampleStatementEnabled(isTrue(enableDeleteByExample));
        }

        String enableCountByExample = attributes.getProperty("enableCountByExample"); //$NON-NLS-1$
        if (enableCountByExample != null) {
            builder.withCountByExampleStatementEnabled(isTrue(enableCountByExample));
        }

        String enableUpdateByExample = attributes.getProperty("enableUpdateByExample"); //$NON-NLS-1$
        if (enableUpdateByExample != null) {
            builder.withUpdateByExampleStatementEnabled(isTrue(enableUpdateByExample));
        }

        String escapeWildcards = attributes.getProperty("escapeWildcards"); //$NON-NLS-1$
        if (escapeWildcards != null) {
            builder.withWildcardEscapingEnabled(isTrue(escapeWildcards));
        }

        String delimitIdentifiers = attributes.getProperty("delimitIdentifiers"); //$NON-NLS-1$
        if (delimitIdentifiers != null) {
            builder.withDelimitIdentifiers(isTrue(delimitIdentifiers));
        }

        String delimitAllColumns = attributes.getProperty("delimitAllColumns"); //$NON-NLS-1$
        if (delimitAllColumns != null) {
            builder.withAllColumnDelimitingEnabled(isTrue(delimitAllColumns));
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (childNode.getNodeName()) {
            case "property" ->  //$NON-NLS-1$
                    parseProperty(childNode).ifPresent(builder::withProperty);
            case "columnOverride" ->  //$NON-NLS-1$
                    builder.withColumnOverride(parseColumnOverride(childNode));
            case "ignoreColumn" ->  //$NON-NLS-1$
                    builder.withIgnoredColumn(parseIgnoreColumn(childNode));
            case "ignoreColumnsByRegex" ->  //$NON-NLS-1$
                    builder.withIgnoredColumnPattern(parseIgnoreColumnByRegex(childNode));
            case "generatedKey" ->  //$NON-NLS-1$
                    builder.withGeneratedKey(parseGeneratedKey(childNode));
            case "domainObjectRenamingRule" ->  //$NON-NLS-1$
                    builder.withDomainObjectRenamingRule(parseDomainObjectRenamingRule(childNode));
            case "columnRenamingRule" ->  //$NON-NLS-1$
                    builder.withColumnRenamingRule(parseColumnRenamingRule(childNode));
            default -> {
                // Ignore unrecognized elements
            }
            }
        }

        return builder.build();
    }

    private ColumnOverride parseColumnOverride(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String javaProperty = attributes.getProperty("property"); //$NON-NLS-1$
        String javaType = attributes.getProperty("javaType"); //$NON-NLS-1$
        String jdbcType = attributes.getProperty("jdbcType"); //$NON-NLS-1$
        String typeHandler = attributes.getProperty("typeHandler"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$
        String isGeneratedAlways = attributes.getProperty("isGeneratedAlways"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());

        return new ColumnOverride.Builder()
                .withColumnName(column)
                .withJavaProperty(javaProperty)
                .withJavaType(javaType)
                .withJdbcType(jdbcType)
                .withTypeHandler(typeHandler)
                .withColumnNameDelimited(parseNullableBoolean(delimitedColumnName))
                .withGeneratedAlways(isTrue(isGeneratedAlways))
                .withProperties(properties)
                .build();
    }

    private GeneratedKey parseGeneratedKey(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        boolean identity = isTrue(attributes.getProperty("identity")); //$NON-NLS-1$
        String sqlStatement = attributes.getProperty("sqlStatement"); //$NON-NLS-1$
        return new GeneratedKey(column, sqlStatement, identity);
    }

    private IgnoredColumn parseIgnoreColumn(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$
        return new IgnoredColumn(column, isTrue(delimitedColumnName));
    }

    private IgnoredColumnPattern parseIgnoreColumnByRegex(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String pattern = attributes.getProperty("pattern"); //$NON-NLS-1$

        IgnoredColumnPattern.Builder builder = new IgnoredColumnPattern.Builder().withPattern(pattern);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("except".equals(childNode.getNodeName())) {
                builder.addException(parseException(childNode));
            }
        }

        return builder.build();
    }

    private IgnoredColumnException parseException(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$
        return new IgnoredColumnException(column, isTrue(delimitedColumnName));
    }

    private DomainObjectRenamingRule parseDomainObjectRenamingRule(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$
        return new DomainObjectRenamingRule(searchString, replaceString);
    }

    private ColumnRenamingRule parseColumnRenamingRule(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$
        return new ColumnRenamingRule(searchString, replaceString);
    }

    protected JavaTypeResolverConfiguration parseJavaTypeResolver(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new JavaTypeResolverConfiguration.Builder()
                .withConfigurationType(type)
                .withProperties(properties)
                .build();
    }

    private PluginConfiguration parsePlugin(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new PluginConfiguration.Builder()
                .withConfigurationType(type)
                .withProperties(properties)
                .build();
    }

    protected JavaModelGeneratorConfiguration parseJavaModelGenerator(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new JavaModelGeneratorConfiguration.Builder()
                .withTargetPackage(targetPackage)
                .withTargetProject(targetProject)
                .withProperties(properties)
                .build();
    }

    private JavaClientGeneratorConfiguration parseJavaClientGenerator(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new JavaClientGeneratorConfiguration.Builder()
                .withConfigurationType(type)
                .withTargetPackage(targetPackage)
                .withTargetProject(targetProject)
                .withProperties(properties)
                .build();
    }

    protected JDBCConnectionConfiguration parseJdbcConnection(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String driverClass = attributes.getProperty("driverClass"); //$NON-NLS-1$
        String connectionURL = attributes.getProperty("connectionURL"); //$NON-NLS-1$
        String userId = attributes.getProperty("userId"); //$NON-NLS-1$
        String password = attributes.getProperty("password"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new JDBCConnectionConfiguration.Builder()
                .withDriverClass(driverClass)
                .withConnectionURL(connectionURL)
                .withUserId(userId)
                .withPassword(password)
                .withProperties(properties)
                .build();
    }

    protected @Nullable String parseClassPathEntry(Node node) {
        NullableProperties attributes = parseAttributes(node);
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
                parseProperty(childNode).ifPresent(p -> properties.setProperty(p.name(), p.value()));
            }
        }
        return properties;
    }

    protected Optional<Property> parseProperty(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String name = attributes.getProperty("name"); //$NON-NLS-1$
        String value = attributes.getProperty("value"); //$NON-NLS-1$

        if (name == null || value == null) {
            return Optional.empty();
        } else {
            return Optional.of(new Property(name, value));
        }
    }

    /**
     * Parses node attributes.
     *
     * <p>Any attribute with an empty value (defined as missing, or blank string) will be dropped.
     *
     * @param node the node
     * @return properties containing all non-empty attributes
     */
    protected NullableProperties parseAttributes(Node node) {
        NullableProperties attributes = new NullableProperties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), trimToNull(value));
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
        NullableProperties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new CommentGeneratorConfiguration.Builder()
                .withConfigurationType(type)
                .withProperties(properties)
                .build();
    }

    protected ConnectionFactoryConfiguration parseConnectionFactory(Node node) {
        NullableProperties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        Properties properties = parseProperties(node.getChildNodes());
        return new ConnectionFactoryConfiguration.Builder()
                .withConfigurationType(type)
                .withProperties(properties)
                .build();
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
