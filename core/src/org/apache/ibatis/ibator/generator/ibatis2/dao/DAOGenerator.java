/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.apache.ibatis.ibator.generator.ibatis2.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.dom.java.CompilationUnit;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.PropertyRegistry;
import org.apache.ibatis.ibator.generator.AbstractJavaGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.AbstractDAOElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.CountByExampleMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.DeleteByExampleMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.DeleteByPrimaryKeyMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.InsertMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.InsertSelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.SelectByExampleWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.SelectByExampleWithoutBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.SelectByPrimaryKeyMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByExampleParmsInnerclassGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByExampleSelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByExampleWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByExampleWithoutBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.dao.templates.AbstractDAOTemplate;
import org.apache.ibatis.ibator.internal.rules.IbatorRules;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * 
 * @author Jeff Butler
 *
 */
public class DAOGenerator extends AbstractJavaGenerator {
    
    private AbstractDAOTemplate daoTemplate;
    private boolean generateForJava5;

    public DAOGenerator(AbstractDAOTemplate daoTemplate, boolean generateForJava5) {
        super();
        this.daoTemplate = daoTemplate;
        this.generateForJava5 = generateForJava5;
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(
                Messages.getString("Progress.14", table.toString())); //$NON-NLS-1$
        TopLevelClass topLevelClass = getTopLevelClassShell();
        Interface interfaze = getInterfaceShell();
        
        addCountByExampleMethod(topLevelClass, interfaze);
        addDeleteByExampleMethod(topLevelClass, interfaze);
        addDeleteByPrimaryKeyMethod(topLevelClass, interfaze);
        addInsertMethod(topLevelClass, interfaze);
        addInsertSelectiveMethod(topLevelClass, interfaze);
        addSelectByExampleWithBLOBsMethod(topLevelClass, interfaze);
        addSelectByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
        addSelectByPrimaryKeyMethod(topLevelClass, interfaze);
        addUpdateByExampleParmsInnerclass(topLevelClass, interfaze);
        addUpdateByExampleSelectiveMethod(topLevelClass, interfaze);
        addUpdateByExampleWithBLOBsMethod(topLevelClass, interfaze);
        addUpdateByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
        addUpdateByPrimaryKeySelectiveMethod(topLevelClass, interfaze);
        addUpdateByPrimaryKeyWithBLOBsMethod(topLevelClass, interfaze);
        addUpdateByPrimaryKeyWithoutBLOBsMethod(topLevelClass, interfaze);
        
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (ibatorContext.getPlugins().daoImplementationGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        
        if (ibatorContext.getPlugins().daoInterfaceGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        return answer;
    }
    
    protected TopLevelClass getTopLevelClassShell() {
        FullyQualifiedJavaType interfaceType = new FullyQualifiedJavaType(introspectedTable.getDAOInterfaceType());
        FullyQualifiedJavaType implementationType = new FullyQualifiedJavaType(introspectedTable.getDAOImplementationType());
        
        CommentGenerator commentGenerator = ibatorContext.getCommentGenerator();
        
        TopLevelClass answer = new TopLevelClass(implementationType);
        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setSuperClass(daoTemplate.getSuperClass());
        answer.addImportedType(daoTemplate.getSuperClass());
        answer.addSuperInterface(interfaceType);
        answer.addImportedType(interfaceType);

        for (FullyQualifiedJavaType fqjt : daoTemplate.getImplementationImports()) {
            answer.addImportedType(fqjt);
        }
        
        commentGenerator.addJavaFileComment(answer);

        // add constructor from the template
        answer.addMethod(daoTemplate.getConstructorClone(commentGenerator,
                implementationType, introspectedTable));

        // add any fields from the template
        for (Field field : daoTemplate.getFieldClones(commentGenerator, introspectedTable)) {
            answer.addField(field);
        }

        // add any methods from the template
        for (Method method : daoTemplate.getMethodClones(commentGenerator, introspectedTable)) {
            answer.addMethod(method);
        }
        
        return answer;
    }
    
    protected Interface getInterfaceShell() {
        Interface answer = new Interface(new FullyQualifiedJavaType(introspectedTable.getDAOInterfaceType()));
        answer.setVisibility(JavaVisibility.PUBLIC);

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (rootInterface == null) {
            rootInterface = ibatorContext.getDaoGeneratorConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }
        
        if (StringUtility.stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            answer.addSuperInterface(fqjt);
            answer.addImportedType(fqjt);
        }

        for (FullyQualifiedJavaType fqjt : daoTemplate.getInterfaceImports()) {
            answer.addImportedType(fqjt);
        }

        ibatorContext.getCommentGenerator().addJavaFileComment(answer);

        return answer;
    }
    
    protected void addCountByExampleMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateCountByExample()) {
            AbstractDAOElementGenerator methodGenerator = new CountByExampleMethodGenerator(generateForJava5);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addDeleteByExampleMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            AbstractDAOElementGenerator methodGenerator = new DeleteByExampleMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addDeleteByPrimaryKeyMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractDAOElementGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addInsertMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractDAOElementGenerator methodGenerator = new InsertMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addInsertSelectiveMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractDAOElementGenerator methodGenerator = new InsertSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByExampleWithBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new SelectByExampleWithBLOBsMethodGenerator(generateForJava5);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new SelectByExampleWithoutBLOBsMethodGenerator(generateForJava5);
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addSelectByPrimaryKeyMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractDAOElementGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleParmsInnerclass(TopLevelClass topLevelClass, Interface interfaze) {
        IbatorRules ibatorRules = introspectedTable.getRules();
        if (ibatorRules.generateUpdateByExampleSelective()
                || ibatorRules.generateUpdateByExampleWithBLOBs()
                || ibatorRules.generateUpdateByExampleWithoutBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByExampleParmsInnerclassGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleSelectiveMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByExampleSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByExampleWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByExampleWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(TopLevelClass topLevelClass, Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            AbstractDAOElementGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
        }
    }
    
    protected void initializeAndExecuteGenerator(AbstractDAOElementGenerator methodGenerator, TopLevelClass topLevelClass, Interface interfaze) {
        methodGenerator.setDAOTemplate(daoTemplate);
        methodGenerator.setIbatorContext(ibatorContext);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addImplementationElements(topLevelClass);
        methodGenerator.addInterfaceElements(interfaze);
    }
}
