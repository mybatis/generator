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

import org.mybatis.generator.api.IndentType;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.config.ImportSortType;
import org.mybatis.generator.config.JavaMergeConfiguration;
import org.mybatis.generator.config.MergeStrategy;

public class ShouldSortImportsCorrectly extends JavaMergeTestCase {
    public ShouldSortImportsCorrectly() {
        addMergeConfiguration("MergeIntoNewEclipseSort", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .withImportSortType(ImportSortType.ECLIPSE)
                .build());

        addMergeConfiguration("MergeIntoNewIntelliJSort",
                new JavaMergeConfiguration.Builder()
                        .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                        .withImportSortType(ImportSortType.INTELLIJ)
                        .build(),
                new Indenter.Builder()
                        .withJavaIndentAmount(2)
                        .build()
        );

        addMergeConfiguration("MergeIntoNewDefaultSort",
                new JavaMergeConfiguration.Builder()
                        .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                        .withImportSortType(ImportSortType.DEFAULT)
                        .build(),
                new Indenter.Builder()
                        .withJavaIndentType(IndentType.TABS)
                        .build()
        );

        addMergeConfiguration("MergeIntoNewLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoOld", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());

        // disabled because of an issue with the lexical preserving printer - JavaDoc comments are lost
        addMergeConfiguration(false, "MergeIntoOldLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

    @Override
    public String existingContent(String parameter) {
        return """
                package com.example;

                import foo.Bar;
                import java.util.Set;
                import java.util.Date;
                import bar.Foo;
                import java.sql.Connection;

                import static java.util.Collections.emptySet;

                public class TestMapper {
                    public void customMethod() {}
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return """
                package com.example;

                import java.util.List;
                import java.util.Map;
                import static java.util.Collections.sort;
                import java.util.Date;
                import java.sql.PreparedStatement;

                public class TestMapper {
                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }
                }
                """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter, String id) {
        return switch (id) {
            case "MergeIntoNewEclipseSort" -> expectedMergeIntoNewEclipseSortContent();
            case "MergeIntoNewIntelliJSort" -> expectedMergeIntoNewIntelliJSortContent();
            case "MergeIntoNewDefaultSort" -> expectedMergeIntoNewDefaultSortContent();
            case "MergeIntoNewLP" -> expectedMergeIntoNewLPContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            case "MergeIntoOldLP" -> expectedMergeIntoOldLPContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedMergeIntoNewEclipseSortContent() {
        return """
                package com.example;

                import static java.util.Collections.emptySet;
                import static java.util.Collections.sort;

                import java.sql.Connection;
                import java.sql.PreparedStatement;
                import java.util.Date;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                import bar.Foo;
                import foo.Bar;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }

                    public void customMethod() {
                    }
                }
                """;
    }

    private String expectedMergeIntoNewIntelliJSortContent() {
        return """
                package com.example;

                import bar.Foo;
                import foo.Bar;

                import java.sql.Connection;
                import java.sql.PreparedStatement;
                import java.util.Date;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                import static java.util.Collections.emptySet;
                import static java.util.Collections.sort;

                public class TestMapper {

                  /**
                   * @mbg.generated
                   */
                  public Map<String, Object> getMap() {
                    return null;
                  }

                  public void customMethod() {
                  }
                }
                """;
    }

    private String expectedMergeIntoNewDefaultSortContent() {
        return """
                package com.example;

                import static java.util.Collections.emptySet;
                import static java.util.Collections.sort;
                import bar.Foo;
                import foo.Bar;
                import java.sql.Connection;
                import java.sql.PreparedStatement;
                import java.util.Date;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                public class TestMapper {

                \t/**
                \t * @mbg.generated
                \t */
                \tpublic Map<String, Object> getMap() {
                \t\treturn null;
                \t}

                \tpublic void customMethod() {
                \t}
                }
                """;
    }

    private String expectedMergeIntoNewLPContent() {
        // the lexical preserving printer does not sort imports
        return """
                package com.example;

                import java.util.List;
                import java.util.Map;
                import static java.util.Collections.sort;
                import java.util.Date;
                import java.sql.PreparedStatement;
                import foo.Bar;
                import java.util.Set;
                import bar.Foo;
                import java.sql.Connection;
                import static java.util.Collections.emptySet;

                public class TestMapper {
                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }
                   \s
                    public void customMethod() {}
                }
                """;
    }

    private String expectedMergeIntoOldContent() {
        return """
                package com.example;

                import static java.util.Collections.emptySet;
                import static java.util.Collections.sort;

                import java.sql.Connection;
                import java.sql.PreparedStatement;
                import java.util.Date;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                import bar.Foo;
                import foo.Bar;

                public class TestMapper {

                    public void customMethod() {
                    }

                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }
                }
                """;
    }

    private String expectedMergeIntoOldLPContent() {
        return """
                package com.example;

                import foo.Bar;
                import java.util.Set;
                import java.util.Date;
                import bar.Foo;
                import java.sql.Connection;

                import static java.util.Collections.emptySet;
                import java.util.List;
                import java.util.Map;
                import static java.util.Collections.sort;
                import java.sql.PreparedStatement;

                public class TestMapper {
                    public void customMethod() {}
                   \s
                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }
                }
                """;
    }
}
