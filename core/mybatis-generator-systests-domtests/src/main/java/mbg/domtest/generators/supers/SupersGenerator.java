package mbg.domtest.generators.supers;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import mbg.domtest.CompilationUnitGenerator;

public class SupersGenerator implements CompilationUnitGenerator {

    private static final String BASE_PACKAGE = "mbg.domtest.generators.supers";
    
    @Override
    public List<CompilationUnit> generate() {
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        
        TopLevelClass baseClass = getBaseClass();
        TopLevelClass superClass = getSuperClass();
        baseClass.setSuperClass(superClass.getType());
        
        Interface baseInterface = getBaseInterface();
        Interface superInterface = getSuperInterface();
        baseInterface.addSuperInterface(superInterface.getType());
        baseClass.addSuperInterface(superInterface.getType());

        answer.add(baseClass);
        answer.add(superClass);
        answer.add(baseInterface);
        answer.add(superInterface);
        
        return answer;
    }
    
    private TopLevelClass getSuperClass() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub.SuperClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        return tlc;
    }
    
    private Interface getSuperInterface() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".sub.SuperInterface");
        Interface ifc = new Interface(fqjt);
        ifc.setVisibility(JavaVisibility.PUBLIC);
        
        return ifc;
    }
    
    private TopLevelClass getBaseClass() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".BaseClass");
        TopLevelClass tlc = new TopLevelClass(fqjt);
        tlc.setVisibility(JavaVisibility.PUBLIC);
        
        return tlc;
    }

    private Interface getBaseInterface() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(BASE_PACKAGE + ".BaseInterface");
        Interface ifc = new Interface(fqjt);
        ifc.setVisibility(JavaVisibility.PUBLIC);
        
        return ifc;
    }
    
}
