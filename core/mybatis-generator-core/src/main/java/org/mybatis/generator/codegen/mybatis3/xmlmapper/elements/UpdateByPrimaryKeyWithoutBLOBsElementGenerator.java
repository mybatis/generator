/*
 *    Copyright 2006-2021 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class UpdateByPrimaryKeyWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

    private final boolean isSimple;

    public UpdateByPrimaryKeyWithoutBLOBsElementGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        List<IntrospectedColumn> columns;
        if (isSimple) {
            columns = introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = introspectedTable.getBaseColumns();
        }

        XmlElement answer = buildUpdateByPrimaryKeyElement(introspectedTable.getUpdateByPrimaryKeyStatementId(),
                introspectedTable.getBaseRecordType(),
                columns);

        if (context.getPlugins().sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
