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
package org.mybatis.generator.merge.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.merge.MergeTestCase;
import org.xml.sax.InputSource;

class XmlFileMergerTest {
    @ParameterizedTest
    @MethodSource("testCases")
    void allTestCases(MergeTestCase<?> testCase, String parameter) throws Exception {
        InputSource existingFile = new InputSource(new StringReader(testCase.existingContent(parameter)));
        InputSource newFile = new InputSource(new StringReader(testCase.newContent(parameter)));
        var actual = XmlFileMergerJaxp.getMergedSource(newFile, existingFile, "Fred");
        assertThat(actual).isEqualToNormalizingNewlines(testCase.expectedContentAfterMerge(parameter));
    }

    static Stream<Arguments> testCases() {
        return MergeTestCase.findTestCases("org.mybatis.generator.merge.xml");
    }
}
