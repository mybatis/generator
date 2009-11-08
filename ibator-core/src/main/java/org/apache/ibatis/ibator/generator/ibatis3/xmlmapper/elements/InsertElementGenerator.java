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
package org.apache.ibatis.ibator.generator.ibatis3.xmlmapper.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.OutputUtilities;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.TextElement;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.config.GeneratedKey;
import org.apache.ibatis.ibator.generator.ibatis3.Ibatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 *
 */
public class InsertElementGenerator extends AbstractXmlElementGenerator {
    
    public InsertElementGenerator() {
        super();
    }
    
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", introspectedTable.getInsertStatementId())); //$NON-NLS-1$
        
        FullyQualifiedJavaType parameterType =
            introspectedTable.getRules().calculateAllFieldsClass();
        
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        ibatorContext.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey();

        if (gk != null && gk.isBeforeInsert()) {
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                // pre-generated key
                answer.addElement(getSelectKey(introspectedColumn, gk));
            }
        }

        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        insertClause.append("insert into "); //$NON-NLS-1$
        insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" ("); //$NON-NLS-1$

        valuesClause.append("values ("); //$NON-NLS-1$

        List<String> valuesClauses = new ArrayList<String>();
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }
            
            insertClause.append(Ibatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            valuesClause.append(Ibatis3FormattingUtilities.getParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                insertClause.append(", "); //$NON-NLS-1$
                valuesClause.append(", "); //$NON-NLS-1$
            }
            
            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);
                
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }
        
        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));
        
        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        
        for (String clause : valuesClauses) {
            answer.addElement(new TextElement(clause));
        }
        
        if (gk != null && !gk.isBeforeInsert()) {
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                // pre-generated key
                answer.addElement(getSelectKey(introspectedColumn, gk));
            }
        }

        if (ibatorContext.getPlugins().sqlMapInsertElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
