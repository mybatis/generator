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

import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndentationTest {
    @ParameterizedTest
    @MethodSource("indentationTestDataProvider")
    void indentationTest(Indentation indentation, String expectedResult) {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        ParseResult<CompilationUnit> parseResult = javaParser.parse("""
            public class TestClass {
                public int sumIt() {
                    return LongStream.ofRange(1, 10)
                    .filter(i -> i % 2 == 0)
                    .map(i -> i * 3)
                    .sum();
                }
            }
            """);
        assertThat(parseResult.isSuccessful()).isTrue();

        DefaultPrinterConfiguration dpc = new DefaultPrinterConfiguration();
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, indentation));
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, Boolean.TRUE));

        DefaultPrettyPrinter dpp = new DefaultPrettyPrinter(dpc);

        String source = dpp.print(parseResult.getResult().orElseThrow());

        assertThat(source).isEqualToNormalizingNewlines(expectedResult);
    }

    public static Stream<Arguments> indentationTestDataProvider() {
        return Stream.of(oneSpace());
    }

    private static Arguments.ArgumentSet oneSpace() {
        return Arguments.argumentSet("One Space",
                new Indentation(Indentation.IndentType.SPACES, 1),
                """
                public class TestClass {

                 public int sumIt() {
                  return LongStream.ofRange(1, 10)
                                   .filter(i -> i % 2 == 0)
                                   .map(i -> i * 3)
                                   .sum();
                 }
                }
                """
        );
    }

    @Test
    void spacesTest2() {
        Indentation indentation = new Indentation(Indentation.IndentType.SPACES);
        assertThat(indentation.getIndent()).isEqualTo("    ");
    }

    @Test
    void tabsTest1() {
        Indentation indentation = new Indentation(Indentation.IndentType.TABS, 1);

        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        ParseResult<CompilationUnit> parseResult = javaParser.parse("""
            public class TestClass {
                public int sumIt() {
                    return LongStream.ofRange(1, 10)
                    .filter(i -> i % 2 == 0)
                    .map(i -> i * 3)
                    .sum();
                }
            }
            """);
        assertThat(parseResult.isSuccessful()).isTrue();

        DefaultPrinterConfiguration dpc = new DefaultPrinterConfiguration();
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, indentation));
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, Boolean.TRUE));

        DefaultPrettyPrinter dpp = new DefaultPrettyPrinter(dpc);

        String source = dpp.print(parseResult.getResult().orElseThrow());

        assertThat(source).isEqualToNormalizingNewlines("""
            public class TestClass {

            \tpublic int sumIt() {
            \t\treturn LongStream.ofRange(1, 10)
            \t\t\t\t\t\t .filter(i -> i % 2 == 0)
            \t\t\t\t\t\t .map(i -> i * 3)
            \t\t\t\t\t\t .sum();
            \t}
            }
            """);
    }

    @Test
    void tabsTest2() {
        Indentation indentation = new Indentation(Indentation.IndentType.TABS);
        assertThat(indentation.getIndent()).isEqualTo("\t\t\t\t");
    }

    @Test
    void tabsTest3() {
        Indentation indentation = new Indentation(Indentation.IndentType.TABS, 2);
        assertThat(indentation.getIndent()).isEqualTo("\t\t");
    }

    @Test
    void tabsTestWithSpaceAlign1() {
        Indentation indentation = new Indentation(Indentation.IndentType.TABS_WITH_SPACE_ALIGN, 1);
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        ParseResult<CompilationUnit> parseResult = javaParser.parse("""
            public class TestClass {
                public int sumIt() {
                    return LongStream.ofRange(1, 10)
                    .filter(i -> i % 2 == 0)
                    .map(i -> i * 3)
                    .sum();
                }
            }
            """);
        assertThat(parseResult.isSuccessful()).isTrue();

        DefaultPrinterConfiguration dpc = new DefaultPrinterConfiguration();
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, indentation));
        dpc.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, Boolean.TRUE));

        DefaultPrettyPrinter dpp = new DefaultPrettyPrinter(dpc);

        String source = dpp.print(parseResult.getResult().orElseThrow());

        assertThat(source).isEqualToNormalizingNewlines("""
            public class TestClass {

            \tpublic int sumIt() {
            \t\treturn LongStream.ofRange(1, 10)
            \t\t                 .filter(i -> i % 2 == 0)
            \t\t                 .map(i -> i * 3)
            \t\t                 .sum();
            \t}
            }
            """);
    }

    @Test
    void tabsTestWithSpaceAlign2() {
        Indentation indentation = new Indentation(Indentation.IndentType.TABS_WITH_SPACE_ALIGN, 2);
        assertThat(indentation.getIndent()).isEqualTo("\t\t");
    }
}
