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
package mbg.domtest.generators.fieldtype1;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import mbg.domtest.CompilationUnitGenerator;

/**
 * This class generates a hierarchy with multiple classes that have the same name in
 * different packages.  It tests the ability of the generator to use fully qualified names
 * in code generation when the type is not explicitly imported.
 * 
 */
//@IgnoreDomTest("Ignore until changes for issue #63 are committed")
public class Test1Generator implements CompilationUnitGenerator {
    
    private static final String BASE_PACKAGE = "mbg.domtest.generators.fieldtype1.output";

    public List<CompilationUnit> generate() {
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType(BASE_PACKAGE + ".SomeClass");
        
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        TopLevelClass tlcMain = generateFieldTypeMain();
        TopLevelClass tlcSub1 = generateFieldTypeSub1();
        TopLevelClass tlcTcSub1 = generateTestClassSub1();
        TopLevelClass tlcSub2 = generateFieldTypeSub2();
        
        answer.add(tlcMain);
        answer.add(tlcSub1);
        answer.add(tlcTcSub1);
        answer.add(tlcSub2);
        
        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.setVisibility(JavaVisibility.PUBLIC);
        topLvlClass.addImportedType(tlcTcSub1.getType());
        
        Field field = new Field("main", tlcMain.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);
        
        field = new Field("tcSub1", tlcTcSub1.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        field = new Field("sub1", tlcSub1.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        field = new Field("sub2", tlcSub2.getType());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);
        
        Method m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("executeMain");
        m.addBodyLine("main.mainMethod();");
        topLvlClass.addMethod(m);
        
        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("setMain");
        m.addParameter(new Parameter(tlcMain.getType(), "main"));
        m.addBodyLine("this.main = main;");
        topLvlClass.addMethod(m);
        
        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("getMain");
        m.setReturnType(tlcMain.getType());
        m.addBodyLine("return main;");
        topLvlClass.addMethod(m);

        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("executeSub1");
        m.addBodyLine("sub1.sub1Method();");
        topLvlClass.addMethod(m);

        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("setSub1");
        m.addParameter(new Parameter(tlcSub1.getType(), "sub1"));
        m.addBodyLine("this.sub1 = sub1;");
        topLvlClass.addMethod(m);
        
        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("getSub1");
        m.setReturnType(tlcSub1.getType());
        m.addBodyLine("return sub1;");
        topLvlClass.addMethod(m);

        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("executeSub2");
        m.addBodyLine("sub2.sub2Method();");
        topLvlClass.addMethod(m);
        
        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("setSub2");
        m.addParameter(new Parameter(tlcSub2.getType(), "sub2"));
        m.addBodyLine("this.sub2 = sub2;");
        topLvlClass.addMethod(m);
        
        m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("getSub2");
        m.setReturnType(tlcSub2.getType());
        m.addBodyLine("return sub2;");
        topLvlClass.addMethod(m);

        answer.add(topLvlClass);
        
        return answer;
    }
    
    private TopLevelClass generateFieldTypeMain() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Method m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("mainMethod");
        m.addBodyLine("System.out.println(\"main method\");");
        tlc.addMethod(m);
        
        return tlc;
    }

    private TopLevelClass generateFieldTypeSub1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub1.FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Method m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("sub1Method");
        m.addBodyLine("System.out.println(\"sub1 method\");");
        tlc.addMethod(m);
        
        return tlc;
    }

    private TopLevelClass generateTestClassSub1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub1.SomeClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Method m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("testClassMethod");
        m.addBodyLine("System.out.println(\"testClass sub1 method\");");
        tlc.addMethod(m);
        
        return tlc;
    }

    private TopLevelClass generateFieldTypeSub2() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub2.FieldType");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        Method m = new Method();
        m.setVisibility(JavaVisibility.PUBLIC);
        m.setName("sub2Method");
        m.addBodyLine("System.out.println(\"sub2 method\");");
        tlc.addMethod(m);
        
        return tlc;
    }
}
