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
package org.mybatis.generator.ant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PropertySet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.exception.XMLParserException;

class GeneratorAntTaskTest {

    @TempDir
    Path tempDir;

    @Test
    void testGettersAndSetters() {
        GeneratorAntTask task = new GeneratorAntTask();
        task.setConfigfile("foo.xml");
        assertThat(task.getConfigfile()).isEqualTo("foo.xml");

        task.setOverwrite(true);
        assertThat(task.isOverwrite()).isTrue();

        task.setVerbose(true);
        assertThat(task.isVerbose()).isTrue();

        task.setContextIds("c1,c2");
        assertThat(task.getContextIds()).isEqualTo("c1,c2");

        task.setFullyQualifiedTableNames("t1,t2");
        assertThat(task.getFullyQualifiedTableNames()).isEqualTo("t1,t2");

        task.setJavaMergeEnabled(true);
        assertThat(task.isJavaMergeEnabled()).isTrue();
    }

    @Test
    void testCreatePropertySet() {
        GeneratorAntTask task = new GeneratorAntTask();
        PropertySet ps = task.createPropertyset();
        assertThat(ps).isNotNull();
        assertThat(ps.getProject()).isEqualTo(task.getProject());
    }

    @Test
    void testCreatePropertySetTwice() {
        GeneratorAntTask task = new GeneratorAntTask();
        PropertySet ps = task.createPropertyset();
        PropertySet ps2 = task.createPropertyset();
        assertThat(ps).isEqualTo(ps2);
    }

    @Test
    void testExecuteWithNoConfigFile() {
        GeneratorAntTask task = new GeneratorAntTask();
        assertThatExceptionOfType(BuildException.class).isThrownBy(task::execute);
    }

    @Test
    void testExecuteWithMissingConfigFile() {
        GeneratorAntTask task = new GeneratorAntTask();
        task.setConfigfile("non_existent.xml");
        assertThatExceptionOfType(BuildException.class).isThrownBy(task::execute);
    }

    @Test
    void testExecuteWithInvalidXml() throws Exception {
        XmlElement context = new XmlElement("context");
        context.addAttribute(new Attribute("id", "foo"));
        XmlElement root = new XmlElement("generatorConfiguration");
        root.addElement(context);
        Document doc = new Document(root);
        XmlFormatter formatter = new DefaultXmlFormatter();
        formatter.setIndenter(Indenter.defaultIndenter());

        Path configFile = tempDir.resolve("generatorConfig.xml");
        Files.writeString(configFile, formatter.getFormattedContent(doc));

        TestBuildListener listener = new TestBuildListener();
        Project project = new Project();
        project.addBuildListener(listener);
        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(project);
        task.setConfigfile(configFile.toString());

        // This should throw BuildException because the XML is missing required elements like a doctype
        assertThatExceptionOfType(BuildException.class).isThrownBy(task::execute)
                .withCauseInstanceOf(XMLParserException.class);
        assertThat(listener.events).hasSize(2);
    }

    @Test
    void testExecuteWithInvalidJdbcDriver() throws Exception {
        XmlElement root = new XmlElement("generatorConfiguration");

        XmlElement context = new XmlElement("context");
        context.addAttribute(new Attribute("id", "foo"));
        root.addElement(context);

        XmlElement jdbcConnection = new XmlElement("jdbcConnection");
        jdbcConnection.addAttribute(new Attribute("driverClass", "foo.Bar"));
        jdbcConnection.addAttribute(new Attribute("connectionURL", "jdbc:foo:bar"));
        context.addElement(jdbcConnection);

        XmlElement modelGenerator = new XmlElement("modelGenerator");
        modelGenerator.addAttribute(new Attribute("targetPackage", "model"));
        modelGenerator.addAttribute(new Attribute("targetProject", "src/main/java"));
        context.addElement(modelGenerator);

        XmlElement table = new XmlElement("table");
        table.addAttribute(new Attribute("tableName", "foo"));
        context.addElement(table);

        Document doc = new Document(XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID, root);
        XmlFormatter formatter = new DefaultXmlFormatter();
        formatter.setIndenter(Indenter.defaultIndenter());

        Path configFile = tempDir.resolve("generatorConfig.xml");
        Files.writeString(configFile, formatter.getFormattedContent(doc));

        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(new Project());
        task.setConfigfile(configFile.toString());

        // This should throw InternalExcetpion because the JDBC driver class is missing
        assertThatExceptionOfType(BuildException.class).isThrownBy(task::execute)
                .withCauseInstanceOf(InternalException.class);
    }

    @Test
    void testExecuteWithBadConnectionURL() throws Exception {
        XmlElement root = new XmlElement("generatorConfiguration");

        XmlElement context = new XmlElement("context");
        context.addAttribute(new Attribute("id", "foo"));
        root.addElement(context);

        XmlElement jdbcConnection = new XmlElement("jdbcConnection");
        jdbcConnection.addAttribute(new Attribute("driverClass", "org.hsqldb.jdbcDriver"));
        jdbcConnection.addAttribute(new Attribute("connectionURL", "jdbc:hsqldbddd:mem:foo"));
        jdbcConnection.addAttribute(new Attribute("userId", "fred"));
        context.addElement(jdbcConnection);

        XmlElement modelGenerator = new XmlElement("modelGenerator");
        modelGenerator.addAttribute(new Attribute("targetPackage", "model"));
        modelGenerator.addAttribute(new Attribute("targetProject", "src/main/java"));
        context.addElement(modelGenerator);

        XmlElement table = new XmlElement("table");
        table.addAttribute(new Attribute("tableName", "foo"));
        context.addElement(table);

        Document doc = new Document(XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID, root);
        XmlFormatter formatter = new DefaultXmlFormatter();
        formatter.setIndenter(Indenter.defaultIndenter());

        Path configFile = tempDir.resolve("generatorConfig.xml");
        Files.writeString(configFile, formatter.getFormattedContent(doc));

        TestBuildListener listener = new TestBuildListener();
        Project project = new Project();
        project.addBuildListener(listener);
        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(project);
        task.setConfigfile(configFile.toString());

        // This should throw InternalExcetpion because the JDBC driver class is missing
        assertThatExceptionOfType(BuildException.class).isThrownBy(task::execute)
                .withCauseInstanceOf(SQLException.class);
        assertThat(listener.events).isEmpty();
    }

    @Test
    void testExecuteHappyPath() throws Exception {
        XmlElement root = new XmlElement("generatorConfiguration");

        XmlElement context = new XmlElement("context");
        context.addAttribute(new Attribute("id", "foo"));
        root.addElement(context);

        XmlElement jdbcConnection = new XmlElement("jdbcConnection");
        jdbcConnection.addAttribute(new Attribute("driverClass", "org.hsqldb.jdbcDriver"));
        jdbcConnection.addAttribute(new Attribute("connectionURL", "jdbc:hsqldb:mem:foo"));
        jdbcConnection.addAttribute(new Attribute("userId", "fred"));
        context.addElement(jdbcConnection);

        XmlElement modelGenerator = new XmlElement("modelGenerator");
        modelGenerator.addAttribute(new Attribute("targetPackage", "model"));
        modelGenerator.addAttribute(new Attribute("targetProject", "src/main/java"));
        context.addElement(modelGenerator);

        XmlElement table = new XmlElement("table");
        table.addAttribute(new Attribute("tableName", "foo"));
        context.addElement(table);

        Document doc = new Document(XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID, root);
        XmlFormatter formatter = new DefaultXmlFormatter();
        formatter.setIndenter(Indenter.defaultIndenter());

        Path configFile = tempDir.resolve("generatorConfig.xml");
        Files.writeString(configFile, formatter.getFormattedContent(doc));

        TestBuildListener listener = new TestBuildListener();
        Project project = new Project();
        project.addBuildListener(listener);
        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(project);
        task.setConfigfile(configFile.toString());

        // no exception expected
        task.execute();
        assertThat(listener.events).hasSize(1);
    }

    @Test
    void testAntProgressCallback() {
        TestBuildListener listener = new TestBuildListener();
        Project project = new Project();
        project.addBuildListener(listener);
        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(project);
        ProgressCallback callback = new AntProgressCallback(task, true);
        callback.startTask("foo");
        assertThat(listener.events).hasSize(1);
    }

    @Test
    void testAntProgressCallbackNotVerbose() {
        TestBuildListener listener = new TestBuildListener();
        Project project = new Project();
        project.addBuildListener(listener);
        GeneratorAntTask task = new GeneratorAntTask();
        task.setProject(project);
        ProgressCallback callback = new AntProgressCallback(task, false);
        callback.startTask("foo");
        assertThat(listener.events).isEmpty();
    }

    private static class TestBuildListener implements BuildListener {
        List<BuildEvent> events = new ArrayList<>();

        @Override
        public void buildStarted(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void buildFinished(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void targetStarted(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void targetFinished(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void taskStarted(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void taskFinished(BuildEvent event) {
            events.add(event);
        }

        @Override
        public void messageLogged(BuildEvent event) {
            events.add(event);
        }
    }
}
