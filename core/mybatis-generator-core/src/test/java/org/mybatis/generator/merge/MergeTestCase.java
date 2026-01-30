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
package org.mybatis.generator.merge;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public interface MergeTestCase<T extends MergeTestCase<T>> {
    Log log = LogFactory.getLog(MergeTestCase.class);

    String existingContent(String parameter);
    String newContent(String parameter);
    String expectedContentAfterMerge(String parameter);
    T self();

    /**
     * Provide a list of parameter variants. If empty, then the test case will be run one time with null passed as a
     * parameter to the content methods. If not empty, the test case will be run multiple times - once for each value
     * in the list, and the value will be passed to the content methods.
     *
     * @return a list of parameter variants. Empty by default.
     */
    default List<String> parameterVariants() {
        return Collections.emptyList();
    }

    default Stream<Arguments> variants() {
        T self = self();
        String name = "testCase = " + self.getClass().getSimpleName() + ", parameter = ";
        if (parameterVariants().isEmpty()) {
            return Stream.of(Arguments.argumentSet(name + "null", self, null));
        } else {
            return parameterVariants().stream().map(v -> Arguments.argumentSet(name + v, self, v));
        }
    }

    static Stream<Arguments> findTestCases(String searchedPackage) {
        // seems to be a bug in reflections: https://github.com/ronmamo/reflections/issues/457
        return new Reflections(new ConfigurationBuilder()
                .forPackages(searchedPackage)
                .filterInputsBy(n -> n.startsWith(searchedPackage))
                .addScanners(Scanners.SubTypes))
                .getSubTypesOf(MergeTestCase.class).stream()
                .flatMap(MergeTestCase::testCaseVariants);
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
