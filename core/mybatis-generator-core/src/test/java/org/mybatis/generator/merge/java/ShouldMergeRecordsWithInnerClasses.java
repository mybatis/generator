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

import org.mybatis.generator.config.JavaMergeConfiguration;
import org.mybatis.generator.config.MergeStrategy;

public class ShouldMergeRecordsWithInnerClasses extends JavaMergeTestCase {
    public ShouldMergeRecordsWithInnerClasses() {
        addMergeConfiguration("MergeIntoNew", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoNewLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoOld", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());

        addMergeConfiguration("MergeIntoOldLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

    @Override
    public String existingContent(String parameter) {
        return
                """
                package foo;

                import java.io.Serializable;
                import javax.annotation.Generated;

                public record Name(int id, String firstName, String lastName) implements Serializable {
                  private static final long serialVersionUID = 1L;

                  public String fullName() {
                    return firstName + " " + lastName;
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public static class SomeClass {
                    public int method1() {
                      return 3;
                    }
                  }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return
                """
                package foo;

                import java.util.stream.IntStream;
                import javax.annotation.Generated;

                public record Name(int id, String firstName, String lastName, String middleName) {

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public static class SomeClass {
                    public int method2() {
                      return IntStream.range(0, 10)
                          .filter(i -> i % 2 == 0)
                          .map(i -> i * 2)
                          .reduce(Integer::sum);
                    }
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
                package foo;

                import java.io.Serializable;
                import java.util.stream.IntStream;

                import javax.annotation.Generated;

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public static class SomeClass {

                        public int method2() {
                            return IntStream.range(0, 10)
                                            .filter(i -> i % 2 == 0)
                                            .map(i -> i * 2)
                                            .reduce(Integer::sum);
                        }
                    }

                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }

    private String expectedMergeIntoNewLPContent() {
        return """
                package foo;

                import java.util.stream.IntStream;
                import javax.annotation.Generated;
                import java.io.Serializable;

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public static class SomeClass {
                    public int method2() {
                      return IntStream.range(0, 10)
                          .filter(i -> i % 2 == 0)
                          .map(i -> i * 2)
                          .reduce(Integer::sum);
                    }
                  }
                 \s
                  private static final long serialVersionUID = 1L;
                 \s
                  public String fullName() {
                    return firstName + " " + lastName;
                  }
                }
                """;
    }

    private String expectedMergeIntoOldContent() {
        return """
                package foo;

                import java.io.Serializable;
                import java.util.stream.IntStream;

                import javax.annotation.Generated;

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {

                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public static class SomeClass {

                        public int method2() {
                            return IntStream.range(0, 10)
                                            .filter(i -> i % 2 == 0)
                                            .map(i -> i * 2)
                                            .reduce(Integer::sum);
                        }
                    }
                }
                """;
    }

    private String expectedMergeIntoOldLPContent() {
        return """
                package foo;

                import java.io.Serializable;
                import javax.annotation.Generated;
                import java.util.stream.IntStream;

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {
                  private static final long serialVersionUID = 1L;

                  public String fullName() {
                    return firstName + " " + lastName;
                  }
                 \s
                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public static class SomeClass {
                    public int method2() {
                      return IntStream.range(0, 10)
                          .filter(i -> i % 2 == 0)
                          .map(i -> i * 2)
                          .reduce(Integer::sum);
                    }
                  }
                }
                """;
    }
}
