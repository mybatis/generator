package org.mybatis.generator.merge.java;

import java.util.List;

import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveCustomMethodsWhenMergingWithGeneratedAnnotation
        implements MergeTestCase<ShouldPreserveCustomMethodsWhenMergingWithGeneratedAnnotation> {

    @Override
    public String existingContent(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public String newContent(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated("MyBatis Generator")
                    public int insert(Object record) {
                        return 0;
                    }
                }
                """, parameter);
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated("MyBatis Generator")
                    public int insert(Object record) {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public ShouldPreserveCustomMethodsWhenMergingWithGeneratedAnnotation self() {
        return this;
    }

    @Override
    public List<String> parameterVariants() {
        return List.of("javax.annotation.Generated", "jakarta.annotation.Generated");
    }
}
