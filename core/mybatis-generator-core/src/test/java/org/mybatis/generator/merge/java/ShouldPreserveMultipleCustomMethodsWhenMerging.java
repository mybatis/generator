package org.mybatis.generator.merge.java;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveMultipleCustomMethodsWhenMerging implements MergeTestCase {

    @Override
    public Stream<Arguments> variants() {
        return Stream.of(Arguments.of(this, null));
    }

    @Override
    public String existingContent(String parameter) {
        return  """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        return 0;
                    }

                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        return 1; // Updated
                    }

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 0;
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
                    public int generatedMethod() {
                        // Updated
                        return 1;
                    }

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 0;
                    }

                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                }
                """;
    }
}
