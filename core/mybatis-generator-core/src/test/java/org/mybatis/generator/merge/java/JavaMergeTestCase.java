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
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.mybatis.generator.merge.MergeTestCase;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public abstract class JavaMergeTestCase extends MergeTestCase {
    private static final Log LOG = LogFactory.getLog(JavaMergeTestCase.class);

    public abstract List<JavaMergerFactory.PrinterConfiguration> printerConfigurations();
    public abstract String expectedContentAfterMerge(String parameter, JavaMergerFactory.PrinterConfiguration printerConfiguration);

    public static Stream<Arguments> javaMergeTestCases(String searchedPackage) {
        // need to add the name filter as well as the search package
        // seems to be a bug in reflections: https://github.com/ronmamo/reflections/issues/457
        return new Reflections(new ConfigurationBuilder()
                .forPackages(searchedPackage)
                .filterInputsBy(n -> n.startsWith(searchedPackage))
                .addScanners(Scanners.SubTypes))
                .getSubTypesOf(JavaMergeTestCase.class)
                .stream()
                .filter(JavaMergeTestCase::isClassConcrete)
                .flatMap(JavaMergeTestCase::testCaseVariants);
    }

    private static boolean isClassConcrete(Class<?> clazz) {
        return !clazz.isInterface() && (clazz.getModifiers() & java.lang.reflect.Modifier.ABSTRACT) == 0;
    }

    private static Stream<Arguments> testCaseVariants(Class<? extends JavaMergeTestCase> clazz) {
        try {
            JavaMergeTestCase instance = clazz.getDeclaredConstructor().newInstance();
            return instance.variants();
        } catch (Exception e) {
            LOG.error("Failed to instantiate test case " + clazz.getName(), e);
            return Stream.empty();
        }
    }

    // variant of parameters and print configurations
    private Stream<Arguments> variants() {
        String name = "testCase = " + getClass().getSimpleName() + ", parameter = %s, printerConfiguration = %s";

        if (parameterVariants().isEmpty()) {
            return printerConfigurations().stream()
                    .map(pc -> Arguments.argumentSet(String.format(name, "null", pc), this, null, pc));
        } else {
            return parameterVariants().stream()
                    .flatMap(pv -> printerConfigurations().stream()
                            .map(pc -> Arguments.argumentSet(String.format(name, pv, pc), this, pv, pc)));
        }
    }
}
