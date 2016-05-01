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
package org.mybatis.generator.api.dom.java;

public class JavaDomUtils {
    /**
     * Calculates type names for writing into generated Java.  We try to
     * use short names wherever possible.  If the type requires an import,
     * but has not been imported, then we need to use the fully qualified
     * type name.
     * 
     * @param compilationUnit the compilation unit being written
     * @param fqjt the type in question
     */
    public static String calculateTypeName(CompilationUnit compilationUnit, FullyQualifiedJavaType fqjt) {
        if(compilationUnit == null
                || typeDoesNotRequireImport(fqjt)
                || typeIsInSamePackage(compilationUnit, fqjt) 
                || typeIsAlreadyImported(compilationUnit, fqjt)) {
            return fqjt.getShortName();
        } else {
            return fqjt.getFullyQualifiedName();
        }
    }
    
    private static boolean typeDoesNotRequireImport(FullyQualifiedJavaType fullyQualifiedJavaType) {
        return fullyQualifiedJavaType.isPrimitive()
                || !fullyQualifiedJavaType.isExplicitlyImported();
    }
    
    private static boolean typeIsInSamePackage(CompilationUnit compilationUnit, FullyQualifiedJavaType fullyQualifiedJavaType) {
        return fullyQualifiedJavaType.getPackageName().equals(compilationUnit.getType().getPackageName());
    }
    
    private static boolean typeIsAlreadyImported(CompilationUnit compilationUnit, FullyQualifiedJavaType fullyQualifiedJavaType) {
        FullyQualifiedJavaType nonGenericType = new FullyQualifiedJavaType(fullyQualifiedJavaType.getFullyQualifiedNameWithoutTypeParameters());
        return compilationUnit.getImportedTypes().contains(nonGenericType);
    }
}
