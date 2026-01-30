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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.merge.MergeTestCase;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

class JavaFileMergerTest {

    private static final Log log = LogFactory.getLog(JavaFileMergerTest.class);

    @ParameterizedTest
    @MethodSource("testCases")
    void allTestCases(MergeTestCase<?> testCase, String parameter) throws Exception {
        var javadocTags = MergeConstants.getOldElementTags();
        var actual = JavaFileMerger.getMergedSource(testCase.newContent(parameter),
                testCase.existingContent(parameter), javadocTags);
        assertThat(actual).isEqualTo(testCase.expectedContentAfterMerge(parameter));
    }

    static Stream<Arguments> testCases() {
        return new Reflections(new ConfigurationBuilder()
                .forPackages("org.mybatis.generator.merge.java")
                .addScanners(Scanners.SubTypes))
                .getSubTypesOf(MergeTestCase.class).stream()
                .flatMap(JavaFileMergerTest::testCaseVariants);
    }

    private static <T extends MergeTestCase<?>> Stream<Arguments> testCaseVariants(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            return instance.variants();
        } catch (Exception e) {
            log.error("Failed to instantiate test case " + clazz.getName(), e);
            return Stream.empty();
        }
    }
}
