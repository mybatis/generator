/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.runtime.dynamic.sql;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.BasicSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.CountByExampleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.DeleteByExampleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.InsertMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.InsertSelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.SelectByExampleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.SelectDistinctByExampleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.UpdateByExampleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.UpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.UpdateByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v1.UpdateByPrimaryKeySelectiveMethodGenerator;

public class DynamicSqlMapperGeneratorV1 extends AbstractDynamicSqlMapperGenerator {
    public DynamicSqlMapperGeneratorV1(String project) {
        super(project);
    }
    
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        preCalculate();
        
        Interface interfaze = createBasicInterface();

        TopLevelClass supportClass = getSupportClass();
        String staticImportString =
                supportClass.getType().getFullyQualifiedNameWithoutTypeParameters() + ".*"; //$NON-NLS-1$
        interfaze.addStaticImport(staticImportString);

        addBasicCountMethod(interfaze);
        addBasicDeleteMethod(interfaze);
        addBasicInsertMethod(interfaze);
        addBasicSelectOneMethod(interfaze);
        addBasicSelectManyMethod(interfaze);
        addBasicUpdateMethod(interfaze);
        
        addCountByExampleMethod(interfaze);
        addDeleteByExampleMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectByExampleMethod(interfaze);
        addSelectDistinctByExampleMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addUpdateByExampleMethod(interfaze);
        addUpdateByExampleSelectiveMethod(interfaze);
        addUpdateByPrimaryKeyMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);
        
        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().clientGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        if (context.getPlugins().dynamicSqlSupportGenerated(supportClass, introspectedTable)) {
            answer.add(supportClass);
        }

        return answer;
    }

    protected void addBasicSelectOneMethod(Interface interfaze) {
        BasicSelectOneMethodGenerator generator = new BasicSelectOneMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .withResultMapId(resultMapId)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addCountByExampleMethod(Interface interfaze) {
        CountByExampleMethodGenerator generator = new CountByExampleMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }
    
    protected void addDeleteByExampleMethod(Interface interfaze) {
        DeleteByExampleMethodGenerator generator = new DeleteByExampleMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        DeleteByPrimaryKeyMethodGenerator generator = new DeleteByPrimaryKeyMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addInsertMethod(Interface interfaze) {
        InsertMethodGenerator generator = new InsertMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        InsertSelectiveMethodGenerator generator = new InsertSelectiveMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectByExampleMethod(Interface interfaze) {
        SelectByExampleMethodGenerator generator = new SelectByExampleMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectDistinctByExampleMethod(Interface interfaze) {
        SelectDistinctByExampleMethodGenerator generator = new SelectDistinctByExampleMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        SelectByPrimaryKeyMethodGenerator generator = new SelectByPrimaryKeyMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByExampleMethod(Interface interfaze) {
        UpdateByExampleMethodGenerator generator = new UpdateByExampleMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        UpdateByExampleSelectiveMethodGenerator generator = new UpdateByExampleSelectiveMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        UpdateByPrimaryKeyMethodGenerator generator = new UpdateByPrimaryKeyMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        UpdateByPrimaryKeySelectiveMethodGenerator generator = new UpdateByPrimaryKeySelectiveMethodGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }
}
