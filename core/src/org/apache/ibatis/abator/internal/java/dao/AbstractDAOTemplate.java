/*
 *  Copyright 2006 The Apache Software Foundation
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
package org.apache.ibatis.abator.internal.java.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.abator.api.CommentGenerator;
import org.apache.ibatis.abator.api.FullyQualifiedTable;
import org.apache.ibatis.abator.api.dom.java.Field;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.api.dom.java.Method;
import org.apache.ibatis.abator.api.dom.java.Parameter;

/**
 * @author Jeff Butler
 */
public abstract class AbstractDAOTemplate {
    private List interfaceImports;

    private List implementationImports;

    private FullyQualifiedJavaType superClass;

    private List checkedExceptions;

    private List fields;

    private List methods;

    private Method constructorTemplate;

    private String deleteMethodTemplate;

    private String insertMethodTemplate;

    private String updateMethodTemplate;

    private String queryForObjectMethodTemplate;

    private String queryForListMethodTemplate;

    /**
     *  
     */
    public AbstractDAOTemplate() {
        super();
        interfaceImports = new ArrayList();
        implementationImports = new ArrayList();
        fields = new ArrayList();
        methods = new ArrayList();
        checkedExceptions = new ArrayList();
    }

    public Method getConstructorClone(CommentGenerator commentGenerator, FullyQualifiedJavaType type, FullyQualifiedTable table) {
        Method answer = new Method();
        answer.setConstructor(true);
        answer.setName(type.getShortName());
        answer.setVisibility(constructorTemplate.getVisibility());
        Iterator iter = constructorTemplate.getParameters().iterator();
        while (iter.hasNext()) {
            answer.addParameter((Parameter) iter.next());
        }
        iter = constructorTemplate.getBodyLines().iterator();
        while (iter.hasNext()) {
            answer.addBodyLine((String) iter.next());
        }
        iter = constructorTemplate.getExceptions().iterator();
        while (iter.hasNext()) {
            answer.addException((FullyQualifiedJavaType) iter.next());
        }
        
        commentGenerator.addGeneralMethodComment(answer, table);
        
        return answer;
    }

    public String getDeleteMethod(String sqlMapNamespace, String statementId,
            String parameter) {
        String answer = MessageFormat.format(deleteMethodTemplate,
                new String[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    public List getInterfaceImports() {
        return interfaceImports;
    }

    public List getImplementationImports() {
        return implementationImports;
    }

    public String getInsertMethod(String sqlMapNamespace, String statementId,
            String parameter) {
        String answer = MessageFormat.format(insertMethodTemplate,
                new String[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    public String getQueryForListMethod(String sqlMapNamespace, String statementId,
            String parameter) {
        String answer = MessageFormat.format(queryForListMethodTemplate,
                new String[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    public String getQueryForObjectMethod(String sqlMapNamespace, String statementId,
            String parameter) {
        String answer = MessageFormat.format(queryForObjectMethodTemplate,
                new String[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    public FullyQualifiedJavaType getSuperClass() {
        return superClass;
    }

    public String getUpdateMethod(String sqlMapNamespace, String statementId,
            String parameter) {
        String answer = MessageFormat.format(updateMethodTemplate,
                new String[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    public List getCheckedExceptions() {
        return checkedExceptions;
    }

    public Iterator getFieldClones(CommentGenerator commentGenerator, FullyQualifiedTable table) {
        ArrayList answer = new ArrayList();
        Iterator iter = fields.iterator();
        while (iter.hasNext()) {
            Field field = new Field();
            Field oldField = (Field) iter.next();
            
            field.setInitializationString(oldField.getInitializationString());
            field.setModifierFinal(oldField.isModifierFinal());
            field.setModifierStatic(oldField.isModifierStatic());
            field.setName(oldField.getName());
            field.setType(oldField.getType());
            field.setVisibility(oldField.getVisibility());
            commentGenerator.addFieldComment(field, table);
            answer.add(field);
        }
        
        return answer.iterator();
    }

    public Iterator getMethodClones(CommentGenerator commentGenerator, FullyQualifiedTable table) {
        ArrayList answer = new ArrayList();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            Method method = new Method();
            Method oldMethod = (Method) iter.next();

            Iterator iter2 = oldMethod.getBodyLines().iterator();
            while (iter2.hasNext()) {
                method.addBodyLine((String) iter2.next());
            }
            
            iter2 = oldMethod.getExceptions().iterator();
            while (iter2.hasNext()) {
                method.addException((FullyQualifiedJavaType) iter2.next());
            }
            
            iter2 = oldMethod.getParameters().iterator();
            while (iter2.hasNext()) {
                method.addParameter((Parameter) iter2.next());
            }
            
            method.setConstructor(oldMethod.isConstructor());
            method.setModifierFinal(oldMethod.isModifierFinal());
            method.setModifierStatic(oldMethod.isModifierStatic());
            method.setName(oldMethod.getName());
            method.setReturnType(oldMethod.getReturnType());
            method.setVisibility(oldMethod.getVisibility());
            
            commentGenerator.addGeneralMethodComment(method, table);
            
            answer.add(method);
        }
        
        return answer.iterator();
    }

    protected void setConstructorTemplate(Method constructorTemplate) {
        this.constructorTemplate = constructorTemplate;
    }

    protected void setDeleteMethodTemplate(String deleteMethodTemplate) {
        this.deleteMethodTemplate = deleteMethodTemplate;
    }

    protected void addField(Field field) {
        fields.add(field);
    }

    protected void setInsertMethodTemplate(String insertMethodTemplate) {
        this.insertMethodTemplate = insertMethodTemplate;
    }

    protected void addMethod(Method method) {
        methods.add(method);
    }

    protected void setQueryForListMethodTemplate(String queryForListMethodTemplate) {
        this.queryForListMethodTemplate = queryForListMethodTemplate;
    }

    protected void setQueryForObjectMethodTemplate(String queryForObjectMethodTemplate) {
        this.queryForObjectMethodTemplate = queryForObjectMethodTemplate;
    }

    protected void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    protected void setUpdateMethodTemplate(String updateMethodTemplate) {
        this.updateMethodTemplate = updateMethodTemplate;
    }

    protected void addInterfaceImport(FullyQualifiedJavaType type) {
        interfaceImports.add(type);
    }

    protected void addImplementationImport(FullyQualifiedJavaType type) {
        implementationImports.add(type);
    }

    protected void addCheckedException(FullyQualifiedJavaType type) {
        checkedExceptions.add(type);
    }
}
