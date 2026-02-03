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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.merge.MergeTestCase;

class JavaFileMergerTest {
    @ParameterizedTest
    @MethodSource("mergeTestCases")
    void mergeTestCases(MergeTestCase testCase, String parameter) throws Exception {
        JavaFileMerger javaFileMerger = new JavaFileMerger(new EclipseOrderedPrinterConfiguration());
        var actual = javaFileMerger.getMergedSource(testCase.newContent(parameter),
                testCase.existingContent(parameter));
        assertThat(actual).isEqualToNormalizingNewlines(testCase.expectedContentAfterMerge(parameter));
    }

    static Stream<Arguments> mergeTestCases() {
        return MergeTestCase.findTestCases("org.mybatis.generator.merge.java");
    }

    @Test
    void testBadExistingFile() {
        JavaFileMerger javaFileMerger = new JavaFileMerger(new EclipseOrderedPrinterConfiguration());
        String badExistingFile = "some random text";

        assertThatExceptionOfType(ShellException.class).isThrownBy(() ->
                javaFileMerger.getMergedSource(badExistingFile, badExistingFile))
                .withMessageStartingWith("Failed to parse existing file");
    }

    @Test
    void testBadNewFile() {
        JavaFileMerger javaFileMerger = new JavaFileMerger(new EclipseOrderedPrinterConfiguration());
        String existingFile = "public class Foo { public int i; }";
        String badNewFile = "some random text";

        assertThatExceptionOfType(ShellException.class).isThrownBy(() ->
                        javaFileMerger.getMergedSource(badNewFile, existingFile))
                .withMessageStartingWith("Failed to parse new file");
    }

    @Test
    void testNoTypeInExistingFile() {
        JavaFileMerger javaFileMerger = new JavaFileMerger(new EclipseOrderedPrinterConfiguration());
        String existingFileNoTypes = "package foo.bar;";

        assertThatExceptionOfType(ShellException.class).isThrownBy(() ->
                        javaFileMerger.getMergedSource(existingFileNoTypes, existingFileNoTypes))
                .withMessage("Failed to find main type declaration in existing file");
    }
}
