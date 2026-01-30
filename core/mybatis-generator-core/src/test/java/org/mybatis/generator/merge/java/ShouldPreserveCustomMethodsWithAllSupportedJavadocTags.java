package org.mybatis.generator.merge.java;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveCustomMethodsWithAllSupportedJavadocTags implements MergeTestCase {
    @Override
    public Stream<Arguments> variants() {
        return Arrays.stream(MergeConstants.getOldElementTags()).map(et ->
            Arguments.of(this, et)
        );
    }

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
}
