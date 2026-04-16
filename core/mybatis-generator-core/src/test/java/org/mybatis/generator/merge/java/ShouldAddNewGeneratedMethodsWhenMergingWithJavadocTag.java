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

import java.util.List;

public class ShouldAddNewGeneratedMethodsWhenMergingWithJavadocTag extends JavaMergeTestCase {
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
    public String expectedContentAfterMerge(String parameter, String id) {
        return switch (id) {
            case "Eclipse" -> expectedEclipseContent();
            case "LexicalPreserving" -> expectedLexicalPreservingContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedEclipseContent() {
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

    private String expectedLexicalPreservingContent() {
        return """
            package com.example;

            public class TestMapper {
                /**
                 * @mbg.generated
                 */
                public int insert(Object record) {
                    return 0;
                }
               \s
                public void customMethod() {
                    System.out.println("Custom method");
                }
            }
            """;
    }

    private String expectedMergeIntoOldContent() {
        return """
            package com.example;

            public class TestMapper {

                public void customMethod() {
                    System.out.println("Custom method");
                }

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
    public List<MergeConfigurationAndId> mergeConfigurations() {
        MergeConfiguration eclipse = new MergeConfiguration.Builder()
                .withImportSortType(MergeConfiguration.ImportSortType.ECLIPSE)
                .build();

        MergeConfiguration lexicalPreserving = new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .build();

        MergeConfiguration mergeIntoOld = new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build();

        return List.of(new MergeConfigurationAndId("Eclipse", eclipse),
                new MergeConfigurationAndId("LexicalPreserving", lexicalPreserving),
                new MergeConfigurationAndId("MergeIntoOld", mergeIntoOld));
    }
}
