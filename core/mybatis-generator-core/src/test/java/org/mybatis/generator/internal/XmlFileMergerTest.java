/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.xml.sax.InputSource;

/**
 * This test is related to issue #87 where XML files are slightly different
 * after running through the XML merger.
 * 
 * @author Jeff Butler
 *
 */
public class XmlFileMergerTest {

    @Test
    public void testThatFilesAreTheSameAfterMerge() throws Exception {
        DefaultXmlFormatter xmlFormatter = new DefaultXmlFormatter();
        Properties p = new Properties();
        p.setProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE, "true");
        CommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(p);

        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement(commentGenerator));

        GeneratedXmlFile generatedFile1 = new GeneratedXmlFile(document, "TestMapper.xml", "org.mybatis.test", "src",
                true, xmlFormatter);
        InputSource is1 = new InputSource(new StringReader(generatedFile1.getFormattedContent()));

        GeneratedXmlFile generatedFile2 = new GeneratedXmlFile(document, "TestMapper.xml", "org.mybatis.test", "src",
                true, xmlFormatter);
        InputSource is2 = new InputSource(new StringReader(generatedFile2.getFormattedContent()));

        String mergedSource = XmlFileMergerJaxp.getMergedSource(is1, is2, "TestMapper.xml");

        assertEquals(generatedFile1.getFormattedContent(), mergedSource);
    }

    private XmlElement getSqlMapElement(CommentGenerator commentGenerator) {

        XmlElement answer = new XmlElement("mapper");
        String namespace = "org.mybatis.test.TestMapper";
        answer.addAttribute(new Attribute("namespace", namespace));

        commentGenerator.addRootComment(answer);

        addInsertElement(commentGenerator, answer);
        addCdataNode1(commentGenerator, answer);
        addCdataNode2(commentGenerator, answer);

        return answer;
    }

    private void addInsertElement(CommentGenerator commentGenerator, XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute("id", "insert"));

        FullyQualifiedJavaType parameterType;
        parameterType = new FullyQualifiedJavaType("org.mybatis.test.TestRecord");

        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));

        commentGenerator.addComment(answer);

        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        insertClause.append("insert into ");
        insertClause.append("myschema.mytable");
        insertClause.append(" (id, description)");

        valuesClause.append("values (#{id}, #{description})");

        answer.addElement(new TextElement(insertClause.toString()));

        answer.addElement(new TextElement(valuesClause.toString()));

        parentElement.addElement(answer);
    }

    private void addCdataNode1(CommentGenerator commentGenerator, XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectWithCdata1"));
        commentGenerator.addComment(answer);

        answer.addElement(new TextElement("<![CDATA["));
        answer.addElement(new TextElement("select foo from bar where foo < 22"));
        answer.addElement(new TextElement("]]>"));

        parentElement.addElement(answer);
    }

    private void addCdataNode2(CommentGenerator commentGenerator, XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectWithCdata2"));
        commentGenerator.addComment(answer);

        answer.addElement(new TextElement("select foo from bar where foo <![CDATA[ < ]]> 22"));

        parentElement.addElement(answer);
    }
}
