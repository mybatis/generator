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

public class ShouldMergeRecords extends JavaMergeTestCase {
    public ShouldMergeRecords() {
        addMergeConfiguration("Eclipse", new MergeConfiguration.Builder()
                .withImportSortType(MergeConfiguration.ImportSortType.ECLIPSE)
                .build());

        addMergeConfiguration("LexicalPreserving", new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .build());

        addMergeConfiguration("MergeIntoOld", new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

    @Override
    public String existingContent(String parameter) {
        return
                """
                package foo;

                import java.io.Serializable;

                public record Name(int id, String firstName, String lastName) implements Serializable {
                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return
                """
                package foo;

                public record Name(int id, String firstName, String lastName, String middleName) {}
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
                package foo;

                import java.io.Serializable;

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {

                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }

    private String expectedLexicalPreservingContent() {
        return """
                package foo;
                import java.io.Serializable;


                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {
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

                public record Name(int id, String firstName, String lastName, String middleName) implements Serializable {

                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }
}
