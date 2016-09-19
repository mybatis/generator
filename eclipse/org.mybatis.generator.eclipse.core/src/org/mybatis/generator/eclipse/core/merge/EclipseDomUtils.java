/*
 *  Copyright 2012 The MyBatis.org Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.eclipse.core.merge;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.mybatis.generator.eclipse.core.merge.visitors.ImportDeclarationStringifier;
import org.mybatis.generator.eclipse.core.merge.visitors.TypeStringifier;

/**
 * Convenience methods for working with Eclipse DOM
 * 
 * @author Jeff Butler
 * 
 */
public class EclipseDomUtils {

    public static boolean importDeclarationsMatch(ImportDeclaration import1,
            ImportDeclaration import2) {
        if (import1 == null || import2 == null) {
            return import1 == null && import2 == null;
        }
        
        ImportDeclarationStringifier ids1 = new ImportDeclarationStringifier();
        import1.accept(ids1);

        ImportDeclarationStringifier ids2 = new ImportDeclarationStringifier();
        import2.accept(ids2);
        
        return ids1.toString().equals(ids2.toString());
    }

    public static boolean typesMatch(Type type1, Type type2) {

        if (type1 == null || type2 == null) {
            return type1 == null && type2 == null;
        }
        
        TypeStringifier ts1 = new TypeStringifier();
        type1.accept(ts1);

        TypeStringifier ts2 = new TypeStringifier();
        type2.accept(ts2);

        return ts1.toString().equals(ts2.toString());
    }
    
    public static CompilationUnit getCompilationUnitFromSource(String javaSource) {
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        Map<?,?> options = JavaCore.getDefaultOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);
        astParser.setSource(javaSource.toCharArray());
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        return cu;
    }
}
