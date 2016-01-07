package org.mybatis.generator.api.dom.java;

import static org.junit.Assert.*;

import org.junit.Test;

public class NonImportTypeDeclarationTest {

    @Test
    public void testTopLevelClassImplementsInterface1() throws Exception {

        FullyQualifiedJavaType itf = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestInterface");
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestClass");

        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.addSuperInterface(itf);

        String fc = topLvlClass.getFormattedContent();
        assertTrue(fc, fc.contains("implements org.mybatis.generator.test1.TestInterface"));
    }

    @Test
    public void testTopLevelClassImplementsInterface2() throws Exception {

        FullyQualifiedJavaType itf = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestInterface");
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestClass");

        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.addImportedType(itf);
        topLvlClass.addSuperInterface(itf);

        String formattedContent = topLvlClass.getFormattedContent();
        assertFalse(formattedContent, formattedContent.contains("implements org.mybatis.generator.test1.TestInterface"));
        assertTrue(formattedContent, formattedContent.contains("implements TestInterface"));
    }

    @Test
    public void testTopLevelClassImplementsInterface3() throws Exception {
        
        FullyQualifiedJavaType itf = new FullyQualifiedJavaType("org.mybatis.generator.test.TestInterface");
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType("org.mybatis.generator.test.TestClass");
        
        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.addSuperInterface(itf);
        
        String fc = topLvlClass.getFormattedContent();
        assertTrue(fc, fc.contains("implements TestInterface"));
    }
    
    @Test
    public void testTopLevelClassExtendsSuperClass1() throws Exception {

        FullyQualifiedJavaType cls1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestClass");
        FullyQualifiedJavaType cls2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestClass");

        TopLevelClass topLvlClass = new TopLevelClass(cls1);
        topLvlClass.addImportedType(cls2);
        topLvlClass.setSuperClass(cls2);

        String fc = topLvlClass.getFormattedContent();
        assertTrue(fc, fc.contains("extends org.mybatis.generator.test2.TestClass"));
    }

    @Test
    public void testTopLevelClassExtendsSuperClass2() throws Exception {
        
        FullyQualifiedJavaType cls1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestClass1");
        FullyQualifiedJavaType cls2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestClass2");
        
        TopLevelClass topLvlClass = new TopLevelClass(cls1);
        topLvlClass.addImportedType(cls2);
        topLvlClass.setSuperClass(cls2);
        
        String fc = topLvlClass.getFormattedContent();
        assertFalse(fc, fc.contains("extends org.mybatis.generator.test2.TestClass2"));
        assertTrue(fc, fc.contains("extends TestClass2"));
    }

    @Test
    public void testTopLevelClassExtendsSuperClass3() throws Exception {
        
        FullyQualifiedJavaType cls1 = new FullyQualifiedJavaType("org.mybatis.generator.test.TestClass1");
        FullyQualifiedJavaType cls2 = new FullyQualifiedJavaType("org.mybatis.generator.test.TestClass2");
        
        TopLevelClass topLvlClass = new TopLevelClass(cls1);
        topLvlClass.setSuperClass(cls2);
        
        String fc = topLvlClass.getFormattedContent();
        assertFalse(fc, fc.contains("extends org.mybatis.generator.test.TestClass2"));
        assertTrue(fc, fc.contains("extends TestClass2"));
    }

    @Test
    public void testTopLevelClassExtendsSuperClass4() throws Exception {
        
        FullyQualifiedJavaType cls1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestClass1");
        FullyQualifiedJavaType cls2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestClass2");
        
        TopLevelClass topLvlClass = new TopLevelClass(cls1);
        topLvlClass.setSuperClass(cls2);
        
        String fc = topLvlClass.getFormattedContent();
        assertTrue(fc, fc.contains("extends org.mybatis.generator.test2.TestClass2"));
    }
    
    @Test
    public void testInterfaceExtendsInterface1() throws Exception {
        
        FullyQualifiedJavaType itf1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestInterface1");
        FullyQualifiedJavaType itf2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestInterface2");
        
        Interface itf = new Interface(itf1);
        itf.addSuperInterface(itf2);
        
        String fc = itf.getFormattedContent();
        assertTrue(fc, fc.contains("extends org.mybatis.generator.test2.TestInterface2"));
    }

    @Test
    public void testInterfaceExtendsInterface2() throws Exception {
        
        FullyQualifiedJavaType itf1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestInterface1");
        FullyQualifiedJavaType itf2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestInterface2");
        
        Interface itf = new Interface(itf1);
        itf.addImportedType(itf2);
        itf.addSuperInterface(itf2);
        
        String fc = itf.getFormattedContent();
        assertFalse(fc, fc.contains("extends org.mybatis.generator.test2.TestInterface2"));
        assertTrue(fc, fc.contains("extends TestInterface2"));
    }

    @Test
    public void testInterfaceExtendsInterface3() throws Exception {
        
        FullyQualifiedJavaType itf1 = new FullyQualifiedJavaType("org.mybatis.generator.test.TestInterface1");
        FullyQualifiedJavaType itf2 = new FullyQualifiedJavaType("org.mybatis.generator.test.TestInterface2");
        
        Interface itf = new Interface(itf1);
        itf.addSuperInterface(itf2);
        
        String fc = itf.getFormattedContent();
        assertFalse(fc, fc.contains("extends org.mybatis.generator.test2.TestInterface2"));
        assertTrue(fc, fc.contains("extends TestInterface2"));
    }

    @Test
    public void testInterfaceExtendsInterface4() throws Exception {
        
        FullyQualifiedJavaType itf1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.TestInterface");
        FullyQualifiedJavaType itf2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.TestInterface");
        
        Interface itf = new Interface(itf1);
        itf.addSuperInterface(itf2);
        
        String fc = itf.getFormattedContent();
        assertTrue(fc, fc.contains("extends org.mybatis.generator.test2.TestInterface"));
    }
    
    @Test
    public void testFieldType1() throws Exception {
        
        FullyQualifiedJavaType cls = new FullyQualifiedJavaType("org.mybatis.generator.test.TestClass");
        FullyQualifiedJavaType ft1 = new FullyQualifiedJavaType("org.mybatis.generator.test1.FieldType");
        FullyQualifiedJavaType ft2 = new FullyQualifiedJavaType("org.mybatis.generator.test2.FieldType");
        FullyQualifiedJavaType ft3 = new FullyQualifiedJavaType("org.mybatis.generator.test.FieldType");
        
        TopLevelClass topLvlClass = new TopLevelClass(cls);
        topLvlClass.addImportedType(ft1);
        
        Field field = new Field("test", ft2);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);
        
        field = new Field("test2", ft1);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);

        // Not imported but in the same package
        field = new Field("test3", ft3);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLvlClass.addField(field);
        
        String fc = topLvlClass.getFormattedContent();
        assertTrue(fc, fc.contains("private org.mybatis.generator.test2.FieldType test"));
        assertTrue(fc, fc.contains("private FieldType test2"));
        assertTrue(fc, fc.contains("private FieldType test3"));

        System.out.println(fc);
    }
}
