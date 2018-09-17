/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.eclipse.core.tests.merge.support;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class TestResourceGenerator {

    private static TestCommentGenerator commentGenerator = new TestCommentGenerator();
    
    public static String simpleClassWithAllGeneratedItems() {
        TopLevelClass tlc = new TopLevelClass(new FullyQualifiedJavaType("org.mybatis.test.SimpleClass"));
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Field field = new Field("id", FullyQualifiedJavaType.getIntInstance());
        field.setVisibility(JavaVisibility.PRIVATE);
        commentGenerator.addFieldComment(field);
        tlc.addField(field);

        Method method = new Method("getId");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine("return id;");
        method.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addMethodComment(method);
        tlc.addMethod(method);
        
        method = new Method("setId");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "id"));
        method.addBodyLine("this.id = id;");
        method.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addMethodComment(method);
        tlc.addMethod(method);

        return new DefaultJavaFormatter().getFormattedContent(tlc);
    }

    public static String simpleClassWithGeneratedAndCustomItems() {
        TopLevelClass tlc = new TopLevelClass(new FullyQualifiedJavaType("org.mybatis.test.SimpleClass"));
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Field field = new Field("description", FullyQualifiedJavaType.getStringInstance());
        field.setVisibility(JavaVisibility.PRIVATE);
        commentGenerator.addFieldComment(field);
        tlc.addField(field);

        Method method = new Method("getDescription");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addBodyLine("return description;");
        method.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addMethodComment(method);
        tlc.addMethod(method);
        
        method = new Method("setDescription");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "description"));
        method.addBodyLine("this.description = description;");
        method.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addMethodComment(method);
        tlc.addMethod(method);

        // no comments on these items - they should survive a merge
        FullyQualifiedJavaType bigDecimal = new FullyQualifiedJavaType("java.math.BigDecimal");
        tlc.addImportedType(bigDecimal);
        field = new Field("amount", bigDecimal);
        field.setVisibility(JavaVisibility.PRIVATE);
        tlc.addField(field);

        method = new Method("getAmount");
        method.setReturnType(bigDecimal);
        method.addBodyLine("return amount;");
        method.setVisibility(JavaVisibility.PUBLIC);
        tlc.addMethod(method);
        
        method = new Method("setAmount");
        method.addParameter(new Parameter(bigDecimal, "amount"));
        method.addBodyLine("this.amount = amount;");
        method.setVisibility(JavaVisibility.PUBLIC);
        tlc.addMethod(method);

        return new DefaultJavaFormatter().getFormattedContent(tlc);
    }

    public static String simpleInterfaceWithAllGeneratedItems() {
        Interface itf = new Interface(new FullyQualifiedJavaType("org.mybatis.test.SimpleInterface"));
        itf.setVisibility(JavaVisibility.PUBLIC);
        
        Method method = new Method("count");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        commentGenerator.addMethodComment(method);
        itf.addMethod(method);
        
        method = new Method("add");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        commentGenerator.addMethodComment(method);
        itf.addMethod(method);

        return new DefaultJavaFormatter().getFormattedContent(itf);
    }

    public static String simpleInterfaceWithGeneratedAndCustomItems() {
        Interface itf = new Interface(new FullyQualifiedJavaType("org.mybatis.test.SimpleInterface"));
        itf.setVisibility(JavaVisibility.PUBLIC);
        
        Method method = new Method("subtract");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        commentGenerator.addMethodComment(method);
        itf.addMethod(method);

        method = new Method("divide");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addAnnotation("@Generated(value=\"" + MyBatisGenerator.class.getName() + "\")");
        itf.addMethod(method);

        method = new Method("multiply");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addAnnotation("@Generated(\"" + MyBatisGenerator.class.getName() + "\")");
        itf.addMethod(method);

        method = new Method("add");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addAnnotation("@javax.annotation.Generated(\"" + MyBatisGenerator.class.getName() + "\")");
        itf.addMethod(method);

        method = new Method("add2");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "a"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "b"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addAnnotation("@javax.annotation.Generated(value=\"" + MyBatisGenerator.class.getName() + "\")");
        itf.addMethod(method);

        method = new Method("nonGeneratedMethod");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        itf.addMethod(method);

        return new DefaultJavaFormatter().getFormattedContent(itf);
    }
}
