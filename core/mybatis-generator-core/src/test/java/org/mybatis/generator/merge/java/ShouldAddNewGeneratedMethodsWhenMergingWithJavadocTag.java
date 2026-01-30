package org.mybatis.generator.merge.java;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mybatis.generator.merge.MergeTestCase;

public class ShouldAddNewGeneratedMethodsWhenMergingWithJavadocTag implements MergeTestCase {
    public Stream<Arguments> variants() {
        return Stream.of(Arguments.of(this, null));
    }

    @Override
    public String existingContent(String parameter) {
        return """
                package com.example;

                public class TestMapper {
                    public void customMethod() {
                        System.out.println("Custom method");
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
                    public int insert(Object record) {
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
                    public int insert(Object record) {
                        return 0;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """;
    }
}
