package org.mybatis.generator.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 *
 * @author Mindia Chubinidze
 * @email mi.chubinidze@gmail.com
 *
 * 
 *  This plugin generated Super Class for database objects, 
 *  We can use supper class for additional fields which will not lost by next generation
 * 
 * 
 *  There are two parameters for this plugin in configuration xml, please remember 'baseClassesDir' is important
 * 
 * 
 *   <property name="baseClassesDir" value="path/to/domain/objects"/>
 *   <property name="baseClassSuffix" value="Super or Parent or ..."/>
 */
public class SuperClassPlugin extends PluginAdapter {

    private final String defailtBaseClassSuffix = "Super";
    private final FullyQualifiedJavaType serializable;
    private String baseClassesDir;
    private String baseClassSuffix;

    public SuperClassPlugin() {
        super();
        serializable = new FullyQualifiedJavaType("java.io.Serializable");
    }

    public boolean validate(List<String> warnings) {

        baseClassesDir = context.getProperty("baseClassesDir");
        baseClassSuffix = context.getProperty("baseClassSuffix");

        if (!isset(baseClassesDir)) {
            warnings.add("Parameter 'baseClassSuffix' must not be empty");
            return false;
        }
        File f = new File(baseClassesDir);
        if (!f.isDirectory()) {
            warnings.add(baseClassesDir + " is not directory");
            return false;
        }
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateSupperClass(topLevelClass);
        return true;
    }

    private boolean isset(String s) {
        return s != null && s.trim().length() > 0;
    }

    private void generateSupperClass(TopLevelClass topLevelClass) {

        String fullName = topLevelClass.getType().getFullyQualifiedName();
        String shortName = topLevelClass.getType().getShortName();

        if (!isset(baseClassSuffix)) {
            baseClassSuffix = defailtBaseClassSuffix;
        }

        String superClassFullName = fullName + baseClassSuffix;
        String superClassShortName = shortName + baseClassSuffix;

        topLevelClass.setSuperClass(superClassFullName);

        File f = new File(baseClassesDir + "/" + superClassShortName + ".java");

        if (!f.exists()) {
            try {

                f.createNewFile();

                TopLevelClass superClassTemplate = new TopLevelClass(superClassFullName);
                superClassTemplate.setVisibility(JavaVisibility.PUBLIC);

                superClassTemplate.addSuperInterface(serializable);
                superClassTemplate.addImportedType(serializable);

                FileWriter fileWriter = new FileWriter(f);
                fileWriter.write(superClassTemplate.getFormattedContent());
                fileWriter.close();

            } catch (IOException ex) {
                System.err.println("ERORR: Unable to generate super class \n" + ex);
            }
        }
    }
}
