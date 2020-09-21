/*
 *    Copyright 2006-2020 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.Collections;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinModifier;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;

public class KotlinDataClassGenerator extends AbstractKotlinGenerator {

    public KotlinDataClassGenerator(String project) {
        super(project);
    }

    @Override
    public List<KotlinFile> getKotlinFiles() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();
        FullyQualifiedKotlinType type = new FullyQualifiedKotlinType(
                introspectedTable.getKotlinRecordType());

        KotlinFile kf = new KotlinFile(type.getShortNameWithoutTypeArguments());
        kf.setPackage(type.getPackageName());

        KotlinType dataClass = KotlinType.newClass(type.getShortNameWithoutTypeArguments())
                .withModifier(KotlinModifier.DATA)
                .build();
        kf.addNamedItem(dataClass);

        commentGenerator.addFileComment(kf);

        commentGenerator.addModelClassComment(dataClass, introspectedTable);

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            FullyQualifiedKotlinType kotlinType =
                    JavaToKotlinTypeConverter.convert(introspectedColumn.getFullyQualifiedJavaType());

            KotlinProperty kp = KotlinProperty.newVar(introspectedColumn.getJavaProperty())
                    .withDataType(kotlinType.getShortNameWithTypeArguments() + "?") //$NON-NLS-1$
                    .withInitializationString("null") //$NON-NLS-1$
                    .build();

            dataClass.addConstructorProperty(kp);

            kf.addImports(kotlinType.getImportList());
        }

        if (context.getPlugins().kotlinDataClassGenerated(kf, dataClass, introspectedTable)) {
            return listOf(kf);
        } else {
            return Collections.emptyList();
        }
    }
}
