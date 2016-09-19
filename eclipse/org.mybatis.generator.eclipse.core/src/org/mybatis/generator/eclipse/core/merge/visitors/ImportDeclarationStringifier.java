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
package org.mybatis.generator.eclipse.core.merge.visitors;

import org.eclipse.jdt.core.dom.ImportDeclaration;

public class ImportDeclarationStringifier extends TypeStringifier {

    @Override
    public boolean visit(ImportDeclaration node) {
        buffer.append("import ");//$NON-NLS-1$
        if (node.isStatic()) {
            buffer.append("static ");//$NON-NLS-1$
        }
        
        node.getName().accept(this);
        
        if (node.isOnDemand()) {
            buffer.append(".*");//$NON-NLS-1$
        }

        return false;
    }
}
