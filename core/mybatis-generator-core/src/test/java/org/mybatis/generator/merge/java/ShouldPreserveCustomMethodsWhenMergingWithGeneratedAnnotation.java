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

public class ShouldPreserveCustomMethodsWhenMergingWithGeneratedAnnotation extends JavaMergeTestCase {
    @Override
    public String existingContent(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator")
                    public int annotationVariant1() {
                        return 0;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public int annotationVariant2() {
                        return 0;
                    }

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
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

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int insert(Object record) {
                        return 0;
                    }
                }
                """, parameter);
    }

    @Override
    public String expectedContentAfterMerge(String parameter, String id) {
        return switch (id) {
            case "Eclipse" -> expectedEclipseContent(parameter);
            case "LexicalPreserving" -> expectedLexicalPreservingContent(parameter);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedEclipseContent(String parameter) {
        return String.format("""
            package com.example;

            import %s;

            public class TestMapper {

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2026-01-30T16:13:03.730861-05:00", comments = "Source field: awful table.first name")
                public int deleteByPrimaryKey(Integer id) {
                    // Updated implementation
                    return 1;
                }

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2026-01-30T16:13:03.730861-05:00", comments = "Source field: awful table.first name")
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

    private String expectedLexicalPreservingContent(String parameter) {
        // TODO - this is wrong, the customMethod comment was dropped
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int insert(Object record) {
                        return 0;
                    }
                   \s
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public List<String> parameterVariants() {
        return List.of("javax.annotation.Generated", "jakarta.annotation.Generated");
    }

    @Override
    public List<MergeConfigurationAndId> mergeConfigurations() {
        MergeConfiguration eclipse = new MergeConfiguration.Builder()
                .withImportSortType(MergeConfiguration.ImportSortType.ECLIPSE)
                .build();

        MergeConfiguration lexicalPreserving = new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .build();

        return List.of(new MergeConfigurationAndId("Eclipse", eclipse),
                new MergeConfigurationAndId("LexicalPreserving", lexicalPreserving));
    }
}
