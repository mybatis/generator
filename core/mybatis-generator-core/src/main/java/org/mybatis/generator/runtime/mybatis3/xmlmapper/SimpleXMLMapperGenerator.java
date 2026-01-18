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
package org.mybatis.generator.runtime.mybatis3.xmlmapper;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.SimpleSelectAllElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.SimpleSelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

import java.util.Objects;

public class SimpleXMLMapperGenerator extends AbstractXmlGenerator {
    private final String myBatis3SqlMapNamespace;

    protected SimpleXMLMapperGenerator(Builder builder) {
        super(builder);
        myBatis3SqlMapNamespace = Objects.requireNonNull(builder.myBatis3SqlMapNamespace);
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("namespace", myBatis3SqlMapNamespace)); //$NON-NLS-1$

        commentGenerator.addRootComment(answer);

        addResultMapElement(answer);
        addDeleteByPrimaryKeyElement(answer);
        addInsertElement(answer);
        addUpdateByPrimaryKeyElement(answer);
        addSelectByPrimaryKeyElement(answer);
        addSelectAllElement(answer);

        return answer;
    }

    protected void addResultMapElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            initializeSubBuilder(new ResultMapWithoutBLOBsElementGenerator.Builder().isSimple(true))
                    .build().addElements(parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            initializeSubBuilder(new SimpleSelectByPrimaryKeyElementGenerator.Builder())
                    .build().addElements(parentElement);
        }
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        initializeSubBuilder(new SimpleSelectAllElementGenerator.Builder())
                .build().addElements(parentElement);
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            initializeSubBuilder(new DeleteByPrimaryKeyElementGenerator.Builder().isSimple(true))
                    .build().addElements(parentElement);
        }
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            initializeSubBuilder(new InsertElementGenerator.Builder().isSimple(true))
                    .build().addElements(parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            initializeSubBuilder(new UpdateByPrimaryKeyWithoutBLOBsElementGenerator.Builder().isSimple(true))
                    .build().addElements(parentElement);
        }
    }

    @Override
    public @Nullable Document getDocument() {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, getSqlMapElement());

        if (!pluginAggregator.sqlMapDocumentGenerated(document, introspectedTable)) {
            document = null;
        }

        return document;
    }

    public static class Builder extends AbstractXmlGeneratorBuilder<Builder> {
        private @Nullable String myBatis3SqlMapNamespace;

        public Builder withMyBatis3SqlMapNamespace(String myBatis3SqlMapNamespace) {
            this.myBatis3SqlMapNamespace = myBatis3SqlMapNamespace;
            return this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public SimpleXMLMapperGenerator build() {
            return new SimpleXMLMapperGenerator(this);
        }
    }
}
