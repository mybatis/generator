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
package org.mybatis.generator.runtime.common;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelRecord;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

/**
 * This model generator builds a record-based model.
 * Records are inherently immutable and constructor-based, so specifying those attributes will have no effect.
 *
 * @author Jeff Butler
 */
public class RecordModelGenerator extends AbstractJavaGenerator {

    public RecordModelGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.8", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        TopLevelRecord topLevelRecord = new TopLevelRecord(type);
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelRecord);
        commentGenerator.addRecordAnnotation(topLevelRecord, introspectedTable, topLevelRecord.getImportedTypes());

        addParameters(topLevelRecord);

        List<CompilationUnit> answer = new ArrayList<>();
        if (pluginAggregator.modelRecordGenerated(topLevelRecord, introspectedTable)) {
            answer.add(topLevelRecord);
        }
        return answer;
    }

    private void addParameters(TopLevelRecord topLevelRecord) {
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            FullyQualifiedJavaType parameterType = introspectedColumn.getFullyQualifiedJavaType();
            topLevelRecord.addImportedType(parameterType);
            topLevelRecord.addParameter(new Parameter(parameterType, introspectedColumn.getJavaProperty()));
        }
    }

    public static class Builder extends AbstractJavaGeneratorBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public RecordModelGenerator build() {
            return new RecordModelGenerator(this);
        }
    }
}
