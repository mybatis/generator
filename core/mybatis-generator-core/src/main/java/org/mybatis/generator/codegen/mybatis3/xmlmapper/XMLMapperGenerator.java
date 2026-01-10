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
package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.CountByExampleElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByExampleElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ExampleWhereClauseElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleSelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

public class XMLMapperGenerator extends AbstractXmlGenerator {

    protected XMLMapperGenerator(Builder builder) {
        super(builder);
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace)); //$NON-NLS-1$

        context.getCommentGenerator().addRootComment(answer);

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
        if (introspectedTable.getRules().generateBaseResultMap()) {
            var builder = new ResultMapWithoutBLOBsElementGenerator.Builder().isSimple(false);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            var builder = new ResultMapWithBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addExampleWhereClauseElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSQLExampleWhereClause()) {
            var builder = new ExampleWhereClauseElementGenerator.Builder().isForUpdateByExample(false);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addMyBatis3UpdateByExampleWhereClauseElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateMyBatis3UpdateByExampleWhereClause()) {
            var builder = new ExampleWhereClauseElementGenerator.Builder().isForUpdateByExample(true);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            var builder = new BaseColumnListElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBlobColumnList()) {
            var builder = new BlobColumnListElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            var builder = new SelectByExampleWithoutBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addSelectByExampleWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            var builder = new SelectByExampleWithBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            var builder = new SelectByPrimaryKeyElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addDeleteByExampleElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            var builder = new DeleteByExampleElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            var builder = new DeleteByPrimaryKeyElementGenerator.Builder().isSimple(false);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            var builder = new InsertElementGenerator.Builder().isSimple(false);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addInsertSelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            var builder = new InsertSelectiveElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addCountByExampleElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateCountByExample()) {
            var builder = new CountByExampleElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByExampleSelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            var builder = new UpdateByExampleSelectiveElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            var builder = new UpdateByExampleWithBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            var builder = new UpdateByExampleWithoutBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            var builder = new UpdateByPrimaryKeySelectiveElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            var builder = new UpdateByPrimaryKeyWithBLOBsElementGenerator.Builder();
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            var builder = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator.Builder().isSimple(false);
            initializeAndExecuteGenerator(builder, parentElement);
        }
    }

    protected <T extends AbstractXmlElementGenerator.AbstractXmlElementGeneratorBuilder<T>>
            void initializeAndExecuteGenerator(T builder, XmlElement parentElement) {
        initializeSubBuilder(builder).build().addElements(parentElement);
    }

    @Override
    public @Nullable Document getDocument() {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID, getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document, introspectedTable)) {
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
