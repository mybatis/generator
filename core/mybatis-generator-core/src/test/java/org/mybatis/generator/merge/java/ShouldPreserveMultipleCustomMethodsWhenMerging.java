/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.merge.java;

public class ShouldPreserveMultipleCustomMethodsWhenMerging extends JavaMergeTestCase {
    public ShouldPreserveMultipleCustomMethodsWhenMerging() {
        addMergeConfiguration("MergeIntoNew", new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoNewLP", new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoOld", new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());

        addMergeConfiguration("MergeIntoOldLP", new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());
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
    public String expectedContentAfterMerge(String parameter, String id) {
        return switch (id) {
            case "MergeIntoNew" -> expectedMergeIntoNewContent();
            case "MergeIntoNewLP" -> expectedMergeIntoNewLPContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            case "MergeIntoOldLP" -> expectedMergeIntoOldLPContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedMergeIntoNewContent() {
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

    private String expectedMergeIntoNewLPContent() {
        // TODO - this is wrong. The comment in generatedMethod was moved
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
                   \s
                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }
                   \s
                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                }
                """;
    }

    private String expectedMergeIntoOldContent() {
        return """
                package com.example;

                public class TestMapper {

                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }

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
                }
                """;
    }

    private String expectedMergeIntoOldLPContent() {
        return """
                package com.example;

                public class TestMapper {


                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                   \s
                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        // Updated
                        return 1;
                    }
                   \s
                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 0;
                    }
                }
                """;
    }
}
