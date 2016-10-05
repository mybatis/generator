/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.eclipse.core.tests.merge;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mybatis.generator.eclipse.core.merge.EclipseDomUtils.getCompilationUnitFromSource;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleType;
import org.junit.Test;
import org.mybatis.generator.eclipse.core.merge.NewJavaFileVisitor;
import org.mybatis.generator.eclipse.core.tests.merge.support.TestResourceGenerator;
import org.mybatis.generator.eclipse.tests.harness.Utilities;

public class NewJavaFileVisitorTest {

    @Test
    public void testSimpleClass() {
        CompilationUnit cu = getCompilationUnitFromSource(TestResourceGenerator.simpleClassWithAllGeneratedItems());
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports().size(), is(0));
        assertThat(visitor.getNewNodes().size(), is(3));
        assertThat(visitor.getSuperclass(), is(nullValue()));
        assertThat(visitor.getSuperInterfaceTypes().size(), is(0));
        assertThat(visitor.isInterface(), is(false));
    }

    @Test
    public void testRegularClass() throws IOException {
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTable.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports().size(), is(1));
        assertThat(visitor.getNewNodes().size(), is(50));
        assertThat(visitor.getSuperclass().isSimpleType(), is(true));
        assertThat(((SimpleType) visitor.getSuperclass()).getName().getFullyQualifiedName(), is("AwfulTableKey"));
        assertThat(visitor.getSuperInterfaceTypes().size(), is(0));
        assertThat(visitor.isInterface(), is(false));
    }

    @Test
    public void testComplexClass() throws IOException {
        InputStream resource = getClass().getResourceAsStream(
                "/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableExample.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports().size(), is(2));
        assertThat(visitor.getNewNodes().size(), is(17));
        assertThat(visitor.getSuperclass(), is(nullValue()));
        assertThat(visitor.getSuperInterfaceTypes().size(), is(0));
        assertThat(visitor.isInterface(), is(false));
    }

    @Test
    public void testRegularInterface() throws IOException {
        InputStream resource = getClass().getResourceAsStream(
                "/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableMapper.java.src");
        CompilationUnit cu = getCompilationUnitFromResource(resource);
        NewJavaFileVisitor visitor = new NewJavaFileVisitor();
        cu.accept(visitor);
        assertThat(visitor.getImports().size(), is(11));
        assertThat(visitor.getNewNodes().size(), is(11));
        assertThat(visitor.getSuperclass(), is(nullValue()));
        assertThat(visitor.getSuperInterfaceTypes().size(), is(0));
        assertThat(visitor.isInterface(), is(true));
    }

    public static CompilationUnit getCompilationUnitFromResource(InputStream resource) throws IOException {
        String javaSource = Utilities.getResourceAsString(resource);
        return getCompilationUnitFromSource(javaSource);
    }
}
