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
package mbg.domtest.generators.simple.interfaze;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import mbg.domtest.CompilationUnitGenerator;

/**
 * This generator generates a simple interface and implementing class in different packages.
 * 
 */
public class SimpleInterfaceGenerator implements CompilationUnitGenerator {
    
    private static final String BASE_PACKAGE = "mbg.domtest.generators.simple.interfaze.output";

    @Override
    public List<CompilationUnit> generate() {
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        
        Interface interfaze = generateInterface();
        answer.add(interfaze);
        answer.add(generateClass(interfaze));

        return answer;
    }
    
    private Interface generateInterface() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub1.SimpleInterface");
        Interface interfaze = new Interface(fqjt);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        return interfaze;
    }

    private TopLevelClass generateClass(Interface interfaze) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + "SimpleClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        tlc.addSuperInterface(interfaze.getType());
        tlc.addImportedType(interfaze.getType());
        return tlc;
    }
}
