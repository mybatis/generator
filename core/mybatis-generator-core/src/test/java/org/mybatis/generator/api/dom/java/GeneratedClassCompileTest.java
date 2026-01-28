/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.api.dom.java;

import static org.assertj.core.api.Assertions.assertThat;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GeneratedClassCompileTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedClassCompileTest.class);
    private final DefaultJavaFormatter javaFormatter = new DefaultJavaFormatter();

    @ParameterizedTest
    @MethodSource("testVariations")
    void testCompile(List<CompilationUnit> testClasses) throws IOException {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(Path.of("target").toFile()));

        List<StringBasedJavaFileObject> files = testClasses.stream()
                .map(this::toJavaFileObject).toList();

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, files);
        boolean success = task.call();

        if (!diagnostics.getDiagnostics().isEmpty()) {
            for (var diagnostic : diagnostics.getDiagnostics()) {
                LOGGER.error(diagnostic.toString());
            }
        }

        assertThat(success).isTrue();
        assertThat(diagnostics.getDiagnostics()).isEmpty();
    }

    private static Stream<Arguments> testVariations() {
        return Stream.of(
                Arguments.argumentSet("Complex Hierarchy", ComplexHierarchyGenerator.generateTestClasses()),
                Arguments.argumentSet("Simple Interface", SimpleInterfaceGenerator.generateTestClasses()),
                Arguments.argumentSet("Supers", SupersGenerator.generateTestClasses()),
                Arguments.argumentSet("Records", RecordGenerator.generateTestClasses())
        );
    }

    private StringBasedJavaFileObject toJavaFileObject(CompilationUnit compilationUnit) {
        String source = javaFormatter.getFormattedContent(compilationUnit);
        return new StringBasedJavaFileObject(compilationUnit.getType().getFullyQualifiedNameWithoutTypeParameters(), source);
    }

    public static class StringBasedJavaFileObject extends SimpleJavaFileObject {
        final String renderedContent;

        public StringBasedJavaFileObject(String name, String renderedContent) {
            super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.renderedContent = renderedContent;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return renderedContent;
        }
    }
}
