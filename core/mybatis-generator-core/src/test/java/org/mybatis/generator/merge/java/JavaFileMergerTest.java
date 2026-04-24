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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.Printer;
import com.github.javaparser.printer.lexicalpreservation.DefaultLexicalPreservingPrinter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.exception.MergeException;

class JavaFileMergerTest {
    private static final boolean FORCE_DISABLED_TESTS = false;
    private static final Log log = LogFactory.getLog(JavaFileMergerTest.class);

    @ParameterizedTest
    @MethodSource("mergeTestCases")
    void mergeTestCases(JavaMergeTestCase testCase, String parameter,
                        JavaMergeTestCase.MergeConfigurationAndId mergeConfigurationAndId) throws Exception {
        if (mergeConfigurationAndId.enabled() || FORCE_DISABLED_TESTS) {
            JavaFileMerger javaFileMerger = JavaMergerFactory.getMerger(mergeConfigurationAndId.mergeConfiguration());
            var actual = javaFileMerger.getMergedSource(testCase.newContent(parameter),
                    testCase.existingContent(parameter));
            assertThat(actual).isEqualToNormalizingNewlines(
                    testCase.expectedContentAfterMerge(parameter, mergeConfigurationAndId.id()));
        } else {
            log.debug(String.format("Test disabled: %s (%s)", testCase.getClass().getSimpleName(), mergeConfigurationAndId.id()));
        }
    }

    static Stream<Arguments> mergeTestCases() {
        return JavaMergeTestCase.javaMergeTestCases("org.mybatis.generator.merge.java");
    }

    @Test
    void testBadExistingFile() {
        JavaFileMerger javaFileMerger = JavaMergerFactory.getMerger(MergeConfiguration.defaultMergeConfiguration());
        String badExistingFile = "some random text";

        assertThatExceptionOfType(MergeException.class).isThrownBy(() ->
                javaFileMerger.getMergedSource(badExistingFile, badExistingFile))
                .withMessage(getString("RuntimeError.28", "existing Java file"))
                .extracting(MergeException::getExtraMessages).asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(1);
    }

    @Test
    void testBadNewFile() {
        JavaFileMerger javaFileMerger = JavaMergerFactory.getMerger(MergeConfiguration.defaultMergeConfiguration());
        String existingFile = "public class Foo { public int i; }";
        String badNewFile = "some random text";

        assertThatExceptionOfType(MergeException.class).isThrownBy(() ->
                        javaFileMerger.getMergedSource(badNewFile, existingFile))
                .withMessage(getString("RuntimeError.28", "new Java file"))
                .extracting(MergeException::getExtraMessages).asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(1);
    }

    @Test
    void testNoTypeInExistingFile() {
        JavaFileMerger javaFileMerger = JavaMergerFactory.getMerger(MergeConfiguration.defaultMergeConfiguration());
        String existingFileNoTypes = "package foo.bar;";

        assertThatExceptionOfType(MergeException.class).isThrownBy(() ->
                        javaFileMerger.getMergedSource(existingFileNoTypes, existingFileNoTypes))
                .withMessage(getString("RuntimeError.29", "existing Java file"));
    }

    @Test
    @Disabled("This test is disabled because of bugs in the LexicalPreservingPrinter")
    void testCommentMergingWithLexicalPreservationEnabled() {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLexicalPreservationEnabled(true);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        var existingClassParseResults = javaParser.parse("""
                package foo;

                public class Bar {
                    private int bar;
                }
                """);

        existingClassParseResults.ifSuccessful(existingCu -> {
            var newClassParseResults = javaParser.parse("""
                package foo;

                public class Bar {
                    /**
                     * Javadoc Comment on field
                     */
                    int foo;

                    /**
                     * Javadoc Comment on method
                     */
                    public int getFoo() {
                        // some comment
                        return foo;
                    }
                }
                """);

            newClassParseResults.ifSuccessful(newCu -> {
                existingCu.getType(0).addMember(newCu.getType(0).getMember(0));
                existingCu.getType(0).addMember(newCu.getType(0).getMember(1));

                Printer pp = new DefaultLexicalPreservingPrinter();
                assertThat(pp.print(existingCu)).isEqualToNormalizingNewlines("""
                    package foo;

                    public class Bar {
                        private int bar;

                        /**
                         * Javadoc Comment on field
                         */
                        int foo;

                        /**
                         * Javadoc Comment on method
                         */
                        public int getFoo() {
                            // some comment
                            return foo;
                        }
                    }
                    """);
            });
        });
    }

    @Test
    void testCommentMergingWithoutLexicalPreservation() {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLexicalPreservationEnabled(false);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        var existingClassParseResults = javaParser.parse("""
                package foo;

                public class Bar {
                    private int bar;
                }
                """);

        existingClassParseResults.ifSuccessful(existingCu -> {
            var newClassParseResults = javaParser.parse("""
                package foo;

                public class Bar {
                    /**
                     * Javadoc Comment on field
                     */
                    int foo;

                    /**
                     * Javadoc Comment on method
                     */
                    public int getFoo() {
                        // some comment
                        return foo;
                    }
                }
                """);

            newClassParseResults.ifSuccessful(newCu -> {
                existingCu.getType(0).addMember(newCu.getType(0).getMember(0));
                existingCu.getType(0).addMember(newCu.getType(0).getMember(1));

                Printer pp = new DefaultPrettyPrinter();
                assertThat(pp.print(existingCu)).isEqualToNormalizingNewlines("""
                    package foo;

                    public class Bar {

                        private int bar;

                        /**
                         * Javadoc Comment on field
                         */
                        int foo;

                        /**
                         * Javadoc Comment on method
                         */
                        public int getFoo() {
                            // some comment
                            return foo;
                        }
                    }
                    """);
            });
        });
    }
}
