package org.mybatis.generator.merge.java;

import java.util.Arrays;
import java.util.List;

import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveCustomMethodsWithAllSupportedJavadocTags
        implements MergeTestCase<ShouldPreserveCustomMethodsWithAllSupportedJavadocTags> {

    @Override
    public String existingContent(String parameter) {
        return String.format("""
                package com.example;

                public class TestMapper {

                    /**
                     * %s
                     */
                    public int oldGeneratedMethod() {
                        return 0;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public String newContent(String parameter) {
        return """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 1;
                    }
                }
                """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 1;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """;
    }

    @Override
    public ShouldPreserveCustomMethodsWithAllSupportedJavadocTags self() {
        return this;
    }

    @Override
    public List<String> parameterVariants() {
        return Arrays.stream(MergeConstants.getOldElementTags()).toList();
    }
}
