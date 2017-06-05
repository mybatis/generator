/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.codegen.ibatis2.dao.elements;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Generates the Update By Example parameters inner class.
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateByExampleParmsInnerclassGenerator extends
        AbstractDAOElementGenerator {

    public UpdateByExampleParmsInnerclassGenerator() {
        super();
    }

    @Override
    public void addImplementationElements(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(
                introspectedTable.getExampleType()));

        InnerClass innerClass = new InnerClass(new FullyQualifiedJavaType(
                "UpdateByExampleParms")); //$NON-NLS-1$
        innerClass.setVisibility(JavaVisibility.PROTECTED);
        innerClass.setStatic(true);
        innerClass.setSuperClass(introspectedTable.getExampleType());
        context.getCommentGenerator().addClassComment(innerClass,
                introspectedTable);

        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(innerClass.getType().getShortName());
        method.addParameter(new Parameter(FullyQualifiedJavaType
                .getObjectInstance(), "record")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType(
                introspectedTable.getExampleType()), "example")); //$NON-NLS-1$
        method.addBodyLine("super(example);"); //$NON-NLS-1$
        method.addBodyLine("this.record = record;"); //$NON-NLS-1$
        innerClass.addMethod(method);

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(FullyQualifiedJavaType.getObjectInstance());
        field.setName("record"); //$NON-NLS-1$
        innerClass.addField(field);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getObjectInstance());
        method.setName("getRecord"); //$NON-NLS-1$
        method.addBodyLine("return record;"); //$NON-NLS-1$
        innerClass.addMethod(method);

        topLevelClass.addInnerClass(innerClass);
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        // nothing to add to the interface
    }
}
