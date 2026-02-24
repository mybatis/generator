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
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.CountByExampleElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.DeleteByExampleElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.ExampleWhereClauseElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.SelectByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByExampleSelectiveElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithBLOBsElementGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

public class XMLMapperGenerator extends AbstractXmlGenerator {
    protected XMLMapperGenerator(Builder builder) {
        super(builder);
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("namespace", introspectedTable.getMyBatis3SqlMapNamespace())); //$NON-NLS-1$

        commentGenerator.addRootComment(answer);

        addResultMapWithoutBLOBsElement(answer);
        addResultMapWithBLOBsElement(answer);
        addExampleWhereClauseElement(answer);
        addMyBatis3UpdateByExampleWhereClauseElement(answer);
        addBaseColumnListElement(answer);
        addBlobColumnListElement(answer);
        addSelectByExampleWithBLOBsElement(answer);
        addSelectByExampleWithoutBLOBsElement(answer);
        addSelectByPrimaryKeyElement(answer);
        addDeleteByPrimaryKeyElement(answer);
        addDeleteByExampleElement(answer);
        addInsertElement(answer);
        addInsertSelectiveElement(answer);
        addCountByExampleElement(answer);
        addUpdateByExampleSelectiveElement(answer);
        addUpdateByExampleWithBLOBsElement(answer);
        addUpdateByExampleWithoutBLOBsElement(answer);
        addUpdateByPrimaryKeySelectiveElement(answer);
        addUpdateByPrimaryKeyWithBLOBsElement(answer);
        addUpdateByPrimaryKeyWithoutBLOBsElement(answer);

        return answer;
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new ResultMapWithoutBLOBsElementGenerator.Builder())
                .isSimple(false)
                .build()
                .execute(parentElement);
    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new ResultMapWithBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addExampleWhereClauseElement(XmlElement parentElement) {
        initializeSubBuilder(new ExampleWhereClauseElementGenerator.Builder())
                .isForUpdateByExample(false)
                .build()
                .execute(parentElement);
    }

    protected void addMyBatis3UpdateByExampleWhereClauseElement(XmlElement parentElement) {
        initializeSubBuilder(new ExampleWhereClauseElementGenerator.Builder())
                .isForUpdateByExample(true)
                .build()
                .execute(parentElement);
    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        initializeSubBuilder(new BaseColumnListElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        initializeSubBuilder(new BlobColumnListElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new SelectByExampleWithoutBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addSelectByExampleWithBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new SelectByExampleWithBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        initializeSubBuilder(new SelectByPrimaryKeyElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addDeleteByExampleElement(XmlElement parentElement) {
        initializeSubBuilder(new DeleteByExampleElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        initializeSubBuilder(new DeleteByPrimaryKeyElementGenerator.Builder())
                .isSimple(false)
                .build()
                .execute(parentElement);
    }

    protected void addInsertElement(XmlElement parentElement) {
        initializeSubBuilder(new InsertElementGenerator.Builder())
                .isSimple(false)
                .build()
                .execute(parentElement);
    }

    protected void addInsertSelectiveElement(XmlElement parentElement) {
        initializeSubBuilder(new InsertSelectiveElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addCountByExampleElement(XmlElement parentElement) {
        initializeSubBuilder(new CountByExampleElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByExampleSelectiveElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByExampleSelectiveElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByExampleWithBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByExampleWithoutBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByPrimaryKeySelectiveElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByPrimaryKeyWithBLOBsElementGenerator.Builder())
                .build()
                .execute(parentElement);
    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
        initializeSubBuilder(new UpdateByPrimaryKeyWithoutBLOBsElementGenerator.Builder())
                .isSimple(false)
                .build()
                .execute(parentElement);
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
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AbstractXmlGenerator build() {
            return new XMLMapperGenerator(this);
        }
    }
}
