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
 *
 */
package org.mybatis.generator.eclipse.core.tests.merge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mybatis.generator.eclipse.core.merge.EclipseDomUtils.getCompilationUnitFromSource;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleType;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.eclipse.core.merge.NewJavaFileVisitor;
import org.mybatis.generator.eclipse.core.tests.merge.support.TestResourceGenerator;

public class NewJavaFileVisitorTest {

    @Test
    public void testSimpleClass() {
        CompilationUnit cu = getCompilationUnitFromSource(TestResourceGenerator.simpleClassWithAllGeneratedItems());
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports()).isEmpty();
        assertThat(visitor.getNewNodes()).hasSize(3);
        assertThat(visitor.getSuperclass()).isNull();
        assertThat(visitor.getSuperInterfaceTypes()).isEmpty();
        assertThat(visitor.isInterface()).isFalse();
    }

    @Test
    public void testRegularClass() throws IOException {
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTable.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports()).hasSize(1);
        assertThat(visitor.getNewNodes()).hasSize(50);
        assertThat(visitor.getSuperclass().isSimpleType()).isTrue();
        assertThat(((SimpleType) visitor.getSuperclass()).getName().getFullyQualifiedName()).isEqualTo("AwfulTableKey");
        assertThat(visitor.getSuperInterfaceTypes()).isEmpty();
        assertThat(visitor.isInterface()).isFalse();
    }

    @Test
    public void testComplexClass() throws IOException {
        InputStream resource = getClass().getResourceAsStream(
                "/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableExample.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports()).hasSize(2);
        assertThat(visitor.getNewNodes()).hasSize(17);
        assertThat(visitor.getSuperclass()).isNull();
        assertThat(visitor.getSuperInterfaceTypes()).isEmpty();
        assertThat(visitor.isInterface()).isFalse();
    }

    @Test
    public void testRegularInterface() throws IOException {
        InputStream resource = getClass().getResourceAsStream(
                "/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableMapper.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports()).hasSize(11);
        assertThat(visitor.getNewNodes()).hasSize(11);
        assertThat(visitor.getSuperclass()).isNull();
        assertThat(visitor.getSuperInterfaceTypes()).isEmpty();
        assertThat(visitor.isInterface()).isTrue();
    }

    public static CompilationUnit getCompilationUnitFromResource(InputStream resource) throws IOException {
        String javaSource = ExistingJavaFileVisitorTest.getResourceAsString(resource);
        return getCompilationUnitFromSource(javaSource);
    }
}
