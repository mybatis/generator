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
package org.apache.ibatis.ibator.generator.ibatis2.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IbatorPlugin;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.java.CompilationUnit;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.generator.AbstractJavaGenerator;
import org.apache.ibatis.ibator.generator.RootClassInfo;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * 
 * @author Jeff Butler
 *
 */
public class RecordWithBLOBsGenerator extends AbstractJavaGenerator {

    public RecordWithBLOBsGenerator() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.ibator.generator.JavaGenerator#getCompilationUnits()
     */
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(
                Messages.getString("Progress.9", table.toString())); //$NON-NLS-1$
        IbatorPlugin plugins = ibatorContext.getPlugins();
        CommentGenerator commentGenerator = ibatorContext.getCommentGenerator();
        
        TopLevelClass topLevelClass = new TopLevelClass(introspectedTable.getRecordWithBLOBsType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        if (introspectedTable.getRules().generateBaseRecordClass()) {
            topLevelClass.setSuperClass(introspectedTable.getBaseRecordType());
        } else {
            topLevelClass.setSuperClass(introspectedTable.getPrimaryKeyType());
        }
        
        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getBLOBColumns()) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            
            Field field = getJavaBeansField(introspectedColumn);
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn,
                    introspectedTable, IbatorPlugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
            
            Method method = getJavaBeansGetter(introspectedColumn);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn,
                    introspectedTable, IbatorPlugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addMethod(method);
            }
            
            method = getJavaBeansSetter(introspectedColumn);
            if (plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn,
                    introspectedTable, IbatorPlugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addMethod(method);
            }
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (ibatorContext.getPlugins().modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
}
