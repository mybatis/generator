/**
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
package org.mybatis.generator.javamerger;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.ShellException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.github.javaparser.ast.Modifier.createModifierList;
import static org.junit.jupiter.api.Assertions.fail;

public class JavaParserJavaFileMergerTest {

    private final JavadocComment mbgGeneratedComment = new JavadocComment(MergeConstants.NEW_ELEMENT_TAG);

    private final String PUBLIC_CLASS_NAME = "Test";


    @Test
    public void testMergeJavaFile() throws Exception {
        JavaParserJavaFileMerger javaFileMerger = new JavaParserJavaFileMerger();
        String oldFile = createExistingFile();
        String newFile = createNewFile();
        String result = javaFileMerger.mergeJavaFile(newFile, new ByteArrayInputStream(oldFile.getBytes(StandardCharsets.UTF_8))
                , PUBLIC_CLASS_NAME + ".java"
                , "UTF-8");
        assert result.equals(createExpectFile());
    }

    @ParameterizedTest
    @MethodSource("org.mybatis.generator.JavaCodeGenerationTest#generateJavaFiles")
    public void testThatFilesAreTheSameAfterMerge(GeneratedJavaFile generatedJavaFile) {

        JavaParserJavaFileMerger javaFileMerger = new JavaParserJavaFileMerger();
        DefaultJavaFormatter formatter = new DefaultJavaFormatter();
        String originFile = formatter.getFormattedContent(generatedJavaFile.getCompilationUnit());

        try {
            String result = javaFileMerger.mergeJavaFile(originFile, new ByteArrayInputStream(originFile.getBytes(StandardCharsets.UTF_8))
                    , generatedJavaFile.getFileName()
                    , generatedJavaFile.getFileEncoding());
            assert result.equals(originFile);
        } catch (ShellException e) {
            fail("merge Java File error: not same after merge." + generatedJavaFile.getFileName(), e);
        }
    }


    private String createExistingFile() {
        CompilationUnit existingUnit = new CompilationUnit();
        existingUnit.setPackageDeclaration("org.mybatis.generator");
        existingUnit.addImport("org.mybatis.t1");
        existingUnit.addImport("org.mybatis.t2");

        existingUnit.addClass(PUBLIC_CLASS_NAME, Modifier.Keyword.PUBLIC)
                .addMember(createFieldMember("id", true))
                .addMember(createFieldMember("extraField", false))
                .addMember(createMethodMember("insert", true))
                .addMember(createMethodMember("select", false))
                .addMember(createClassMember("Criteria", true))
                .addMember(createClassMember("MyInnerClass", false))
                .addMember(createConstructor(PUBLIC_CLASS_NAME, true));

        assert existingUnit.getClassByName(PUBLIC_CLASS_NAME).isPresent();
        ClassOrInterfaceDeclaration publicClass = existingUnit.getClassByName(PUBLIC_CLASS_NAME).get();
        publicClass.addAnnotation("Mapper");
        publicClass.addAnnotation("CustomAnnotation");

        return prettyPrinter(existingUnit);
    }


    private String createNewFile() {
        CompilationUnit existingUnit = new CompilationUnit();
        existingUnit.setPackageDeclaration("org.mybatis.generator");
        //new import
        existingUnit.addImport("java.util.Date");
        existingUnit.addImport("java.util.HashMap");

        existingUnit.addClass(PUBLIC_CLASS_NAME, Modifier.Keyword.PUBLIC)
                .addMember(createFieldMember("autoGeneratedField", true))
                .addMember(createMethodMember("autoGeneratedMethod", true))
                .addMember(createConstructor(PUBLIC_CLASS_NAME, true))
                .addMember(createClassMember("AutoGeneratedClass", true));
        assert existingUnit.getClassByName(PUBLIC_CLASS_NAME).isPresent();
        ClassOrInterfaceDeclaration publicClass = existingUnit.getClassByName(PUBLIC_CLASS_NAME).get();
        publicClass.addAnnotation("Mapper");
        publicClass.addAnnotation("Component");
        return prettyPrinter(existingUnit);
    }


    private String createExpectFile() {
        CompilationUnit existingUnit = new CompilationUnit();
        existingUnit.setPackageDeclaration("org.mybatis.generator");
        existingUnit.addImport("org.mybatis.t1");
        existingUnit.addImport("org.mybatis.t2");
        existingUnit.addImport("java.util.Date");
        existingUnit.addImport("java.util.HashMap");

        existingUnit.addClass(PUBLIC_CLASS_NAME, Modifier.Keyword.PUBLIC)
                .addMember(createFieldMember("autoGeneratedField", true))
                .addMember(createFieldMember("extraField", false))
                .addMember(createConstructor(PUBLIC_CLASS_NAME, true))
                .addMember(createMethodMember("autoGeneratedMethod", true))
                .addMember(createMethodMember("select", false))
                .addMember(createClassMember("AutoGeneratedClass", true))
                .addMember(createClassMember("MyInnerClass", false));
        assert existingUnit.getClassByName(PUBLIC_CLASS_NAME).isPresent();
        ClassOrInterfaceDeclaration publicClass = existingUnit.getClassByName(PUBLIC_CLASS_NAME).get();
        publicClass.addAnnotation("Mapper");
        publicClass.addAnnotation("CustomAnnotation");
        publicClass.addAnnotation("Component");
        return prettyPrinter(existingUnit);
    }

    public MethodDeclaration createMethodMember(String name, boolean isGenerated) {
        MethodDeclaration generatedMethod = new MethodDeclaration(createModifierList(Modifier.Keyword.PUBLIC, Modifier.Keyword.ABSTRACT), PrimitiveType.intType(), name);
        if (isGenerated) {
            generatedMethod.setComment(mbgGeneratedComment);
        }
        generatedMethod.setBody(null);
        return generatedMethod;
    }

    public FieldDeclaration createFieldMember(String name, boolean isGenerated) {
        FieldDeclaration extraField = new FieldDeclaration(createModifierList(Modifier.Keyword.PRIVATE), PrimitiveType.longType().toBoxedType(), name);
        if (isGenerated) {
            extraField.setComment(mbgGeneratedComment);
        }
        return extraField;
    }

    public ClassOrInterfaceDeclaration createClassMember(String name, boolean isGenerated) {
        ClassOrInterfaceDeclaration extraInnerClass = new ClassOrInterfaceDeclaration(createModifierList(Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC),
                false, name);
        if (isGenerated) {
            extraInnerClass.setComment(mbgGeneratedComment);
        }
        return extraInnerClass;
    }

    public ConstructorDeclaration createConstructor(String publicClassName, boolean isGenerated) {
        ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(createModifierList(Modifier.Keyword.PUBLIC), publicClassName);
        if (isGenerated) {
            constructorDeclaration.setComment(mbgGeneratedComment);
        }
        return constructorDeclaration;
    }

    public String prettyPrinter(CompilationUnit unit) {
        PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
        PrettyPrinter prettyPrinter = new PrettyPrinter(conf);
        return prettyPrinter.print(unit);
    }


}