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

package org.apache.ibatis.ibator.generator.ibatis3.javamapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.dom.java.CompilationUnit;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.generator.AbstractJavaGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.CountByExampleMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.DeleteByExampleMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.InsertMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.InsertSelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.SelectByExampleWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.SelectByExampleWithoutBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByExampleSelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByExampleWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByExampleWithoutBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.apache.ibatis.ibator.generator.ibatis3.javamapper.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

/**
 * @author Jeff Butler
 *
 */
public class JavaMapperGenerator extends AbstractJavaGenerator {

    /**
     * 
     */
    public JavaMapperGenerator() {
        super();
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.ibator.generator.AbstractJavaGenerator#getCompilationUnits()
     */
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask("Generating Mapper class");
        CommentGenerator commentGenerator = ibatorContext.getCommentGenerator();
        
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getIbatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        
        addCountByExampleMethod(interfaze);
        addDeleteByExampleMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectByExampleWithBLOBsMethod(interfaze);
        addSelectByExampleWithoutBLOBsMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addUpdateByExampleSelectiveMethod(interfaze);
        addUpdateByExampleWithBLOBsMethod(interfaze);
        addUpdateByExampleWithoutBLOBsMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);
        addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
        addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);
        
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (ibatorContext.getPlugins().daoInterfaceGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        return answer;
    }
    
    protected void addCountByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateCountByExample()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new CountByExampleMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addDeleteByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByExampleMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addInsertSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectByExampleWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectByExampleWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByExampleWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator, Interface interfaze) {
        methodGenerator.setIbatorContext(ibatorContext);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addInterfaceElements(interfaze);
    }
}
