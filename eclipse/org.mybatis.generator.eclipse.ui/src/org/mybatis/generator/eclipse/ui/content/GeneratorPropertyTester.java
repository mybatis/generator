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
package org.mybatis.generator.eclipse.ui.content;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;

public class GeneratorPropertyTester extends PropertyTester {

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (!(receiver instanceof IFile)) {
            return false;
        }
        
        IFile file = (IFile) receiver;
        
        if ("isGeneratorConfigurationFile".equals(property)) { //$NON-NLS-1$
            ConfigVerifyer verifyer = new ConfigVerifyer(file);
            return verifyer.isConfigurationFile();
        }
        
        return false;
    }
}
