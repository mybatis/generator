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
