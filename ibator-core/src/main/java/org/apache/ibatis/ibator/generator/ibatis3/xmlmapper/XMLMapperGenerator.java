/*
 *  Copyright 2009 The Apache Software Foundation
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
package org.apache.ibatis.ibator.generator.ibatis3.xmlmapper;

import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.Document;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.generator.AbstractXmlGenerator;
import org.apache.ibatis.ibator.generator.XmlConstants;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.CountByExampleElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.DeleteByExampleElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.ExampleWhereClauseElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.InsertElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.SelectByExampleWithBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByExampleSelectiveElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByExampleWithBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByExampleWithoutBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByPrimaryKeyWithBLOBsElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * 
 * @author Jeff Butler
 *
 */
public class XMLMapperGenerator extends AbstractXmlGenerator {

    public XMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(
                Messages.getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getIbatis3JavaMapperType();
        if (namespace == null) {
            // TODO - this is wierd for ibatis3
            namespace = introspectedTable.getIbatis2SqlMapNamespace();
        }
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        ibatorContext.getCommentGenerator().addRootComment(answer);
        
        addResultMapWithoutBLOBsElement(answer);
        addResultMapWithBLOBsElement(answer);
        addExampleWhereClauseElement(answer);
        addIbatis3UpdateByExampleWhereClauseElement(answer);
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
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addExampleWhereClauseElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSQLExampleWhereClause()) {
            AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addIbatis3UpdateByExampleWhereClauseElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateIbatis3UpdateByExampleWhereClause()) {
            AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addBlobColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBlobColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new SelectByExampleWithoutBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addSelectByExampleWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new SelectByExampleWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addDeleteByExampleElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteByExampleElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addInsertSelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractXmlElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addCountByExampleElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateCountByExample()) {
            AbstractXmlElementGenerator elementGenerator = new CountByExampleElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByExampleSelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByExampleSelectiveElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByExampleWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByExampleWithoutBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeySelectiveElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setIbatorContext(ibatorContext);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document(XmlConstants.IBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.IBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!ibatorContext.getPlugins().sqlMapDocumentGenerated(document, introspectedTable)) {
            document = null;
        }
        
        return document;
    }
}
