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
import org.mybatis.generator.runtime.dynamic.sql.elements.FieldAndImports;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.BasicMultipleInsertHelperMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.BasicMultipleInsertMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.BasicSelectOneMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.DeleteByPrimaryKeyMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralCountMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralDeleteMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralSelectDistinctMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralSelectMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralSelectOneMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.GeneralUpdateMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.InsertMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.InsertMultipleMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.InsertSelectiveMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.SelectByPrimaryKeyMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.SelectListGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.UpdateAllColumnsMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.UpdateSelectiveColumnsMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.UpdateByPrimaryKeyMethodGeneratorV2;
import org.mybatis.generator.runtime.dynamic.sql.elements.v2.UpdateByPrimaryKeySelectiveMethodGeneratorV2;

public class DynamicSqlMapperGeneratorV2 extends AbstractDynamicSqlMapperGenerator {

    public DynamicSqlMapperGeneratorV2(String project) {
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
        addBasicInsertMultipleMethod(interfaze);
        if (introspectedTable.getGeneratedKey() != null) {
            addBasicInsertMultipleHelperMethod(interfaze);
        }
        addBasicSelectOneMethod(interfaze);
        addBasicSelectManyMethod(interfaze);
        addBasicUpdateMethod(interfaze);
        
        addCountByExampleMethod(interfaze);
        addDeleteByExampleMethod(interfaze);
        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertOneMethod(interfaze);
        addInsertMultipleMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectListField(interfaze);
        addSelectByExampleMethod(interfaze);
        addSelectDistinctByExampleMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addUpdateByExampleMethod(interfaze);
        addUpdateAllMethod(interfaze);
        addUpdateSelectiveMethod(interfaze);
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

    protected void addInsertOneMethod(Interface interfaze) {
        InsertMethodGeneratorV2 generator = new InsertMethodGeneratorV2.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }
    
    protected void addBasicInsertMultipleMethod(Interface interfaze) {
        BasicMultipleInsertMethodGenerator generator = new BasicMultipleInsertMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addBasicInsertMultipleHelperMethod(Interface interfaze) {
        BasicMultipleInsertHelperMethodGenerator generator = new BasicMultipleInsertHelperMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addInsertMultipleMethod(Interface interfaze) {
        InsertMultipleMethodGenerator generator = new InsertMultipleMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addCountByExampleMethod(Interface interfaze) {
        GeneralCountMethodGenerator generator = new GeneralCountMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addDeleteByExampleMethod(Interface interfaze) {
        GeneralDeleteMethodGenerator generator = new GeneralDeleteMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectListField(Interface interfaze) {
        SelectListGenerator generator = new SelectListGenerator.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .build();
        
        FieldAndImports fieldAndImports = generator.generateFieldAndImports();

        if (fieldAndImports != null && generator.callPlugins(fieldAndImports.getField(), interfaze)) {
            interfaze.addField(fieldAndImports.getField());
            interfaze.addImportedTypes(fieldAndImports.getImports());
            interfaze.addStaticImports(fieldAndImports.getStaticImports());
        }
    }
    
    protected void addSelectByExampleMethod(Interface interfaze) {
        addGeneralSelectOneMethod(interfaze);
        GeneralSelectMethodGenerator generator = new GeneralSelectMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectDistinctByExampleMethod(Interface interfaze) {
        GeneralSelectDistinctMethodGenerator generator = new GeneralSelectDistinctMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addGeneralSelectOneMethod(Interface interfaze) {
        GeneralSelectOneMethodGenerator generator = new GeneralSelectOneMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByExampleMethod(Interface interfaze) {
        GeneralUpdateMethodGenerator generator = new GeneralUpdateMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateAllMethod(Interface interfaze) {
        UpdateAllColumnsMethodGenerator generator = new UpdateAllColumnsMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateSelectiveMethod(Interface interfaze) {
        UpdateSelectiveColumnsMethodGenerator generator = new UpdateSelectiveColumnsMethodGenerator.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withFragmentGenerator(fragmentGenerator)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addBasicSelectOneMethod(Interface interfaze) {
        BasicSelectOneMethodGeneratorV2 generator = new BasicSelectOneMethodGeneratorV2.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .withResultMapId(resultMapId)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        DeleteByPrimaryKeyMethodGeneratorV2 generator = new DeleteByPrimaryKeyMethodGeneratorV2.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withFragmentGenerator(fragmentGenerator)
                .withTableFieldName(tableFieldName)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        InsertSelectiveMethodGeneratorV2 generator = new InsertSelectiveMethodGeneratorV2.Builder()
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        SelectByPrimaryKeyMethodGeneratorV2 generator = new SelectByPrimaryKeyMethodGeneratorV2.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        UpdateByPrimaryKeyMethodGeneratorV2 generator = new UpdateByPrimaryKeyMethodGeneratorV2.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        UpdateByPrimaryKeySelectiveMethodGeneratorV2 generator =
                new UpdateByPrimaryKeySelectiveMethodGeneratorV2.Builder()
                .withContext(context)
                .withFragmentGenerator(fragmentGenerator)
                .withIntrospectedTable(introspectedTable)
                .withTableFieldName(tableFieldName)
                .withRecordType(recordType)
                .build();
        
        generate(interfaze, generator);
    }
}
