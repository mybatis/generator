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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mybatis.generator.eclipse.core.merge.EclipseDomUtils.getCompilationUnitFromSource;
import static org.mybatis.generator.eclipse.tests.harness.Utilities.getCompilationUnitSummaryFromSource;
import static org.mybatis.generator.eclipse.tests.harness.Utilities.getResourceAsString;
import static org.mybatis.generator.eclipse.tests.harness.matchers.Matchers.*;

import java.io.InputStream;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.eclipse.core.merge.ExistingJavaFileVisitor;
import org.mybatis.generator.eclipse.tests.harness.summary.CompilationUnitSummary;

public class ExistingJavaFileVisitorTest {

    @Test
    public void testRegularClass() throws Exception {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTable.java.src");
        String source = getResourceAsString(resource);
        IDocument document = new Document(source);
        CompilationUnit cu = getCompilationUnitFromSource(source);
                
        cu.recordModifications();
        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor(MergeConstants.OLD_ELEMENT_TAGS);
        
        // delete all the old generated stuff
        cu.accept(visitor);
        assertThat(visitor.getTypeDeclaration(), is(notNullValue()));
        assertThat(visitor.getTypeDeclaration().isInterface(), is(false));
        assertThat(visitor.getTypeDeclaration().getName().getFullyQualifiedName(),
                is("AwfulTable"));
        
        // generate a new compilation unit that is stripped of old stuff
        TextEdit textEdit = cu.rewrite(document, null);
        textEdit.apply(document);
        
        CompilationUnitSummary summary = getCompilationUnitSummaryFromSource(document.get());
        assertThat(summary, hasImportCount(1));
        assertThat(summary, hasClass("AwfulTable", withSuperClass("AwfulTableKey")));
        assertThat(summary, hasClass("AwfulTable", withSuperInterfaceCount(0)));
        assertThat(summary, hasClass("AwfulTable", withMethodCount(0)));
        assertThat(summary, hasClass("AwfulTable", withFieldCount(0)));
    }
    
    @Test
    public void testComplexClass() throws Exception {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableExample.java.src");
        String source = getResourceAsString(resource);
        IDocument document = new Document(source);
        CompilationUnit cu = getCompilationUnitFromSource(source);
        cu.recordModifications();
        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor(MergeConstants.OLD_ELEMENT_TAGS);
        
        // delete all the old generated stuff
        cu.accept(visitor);
        assertThat(visitor.getTypeDeclaration(), is(notNullValue()));
        assertThat(visitor.getTypeDeclaration().isInterface(), is(false));
        assertThat(visitor.getTypeDeclaration().getName().getFullyQualifiedName(),
                is("AwfulTableExample"));
        assertThat(visitor.containsInnerClass("Criteria"), is(true));
        
        // generate a new compilation unit that is stripped of old stuff
        TextEdit textEdit = cu.rewrite(document, null);
        textEdit.apply(document);

        CompilationUnitSummary summary = getCompilationUnitSummaryFromSource(document.get());
        assertThat(summary, hasImportCount(2));
        assertThat(summary, hasClass("AwfulTableExample", withSuperClass(null)));
        assertThat(summary, hasClass("AwfulTableExample", withSuperInterfaceCount(0)));
        assertThat(summary, hasClass("AwfulTableExample", withMethodCount(0)));
        assertThat(summary, hasClass("AwfulTableExample", withFieldCount(0)));
        assertThat(summary, hasClass("AwfulTableExample", withClass("Criteria")));
    }
    
    @Test
    public void testRegularInterface() throws Exception {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/core/tests/merge/resources/AwfulTableMapper.java.src");
        String source = getResourceAsString(resource);
        IDocument document = new Document(source);
        CompilationUnit cu = getCompilationUnitFromSource(source);
        cu.recordModifications();
        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor(MergeConstants.OLD_ELEMENT_TAGS);
        
        // delete all the old generated stuff
        cu.accept(visitor);
        assertThat(visitor.getTypeDeclaration(), is(notNullValue()));
        assertThat(visitor.getTypeDeclaration().isInterface(), is(true));
        assertThat(visitor.getTypeDeclaration().getName().getFullyQualifiedName(),
                is("AwfulTableMapper"));
        
        // generate a new compilation unit that is stripped of old stuff
        TextEdit textEdit = cu.rewrite(document, null);
        textEdit.apply(document);

        CompilationUnitSummary summary = getCompilationUnitSummaryFromSource(document.get());
        assertThat(summary, hasImportCount(11));
        assertThat(summary, hasInterface("AwfulTableMapper"));
        assertThat(summary, hasInterface("AwfulTableMapper", withSuperInterfaceCount(0)));
        assertThat(summary, hasInterface("AwfulTableMapper", withMethodCount(0)));
        assertThat(summary, hasInterface("AwfulTableMapper", withFieldCount(0)));
    }
}
