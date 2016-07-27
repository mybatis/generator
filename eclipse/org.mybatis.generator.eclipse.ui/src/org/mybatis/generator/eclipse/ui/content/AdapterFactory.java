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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * This class provides a factory for different adapters used in the plugin.
 * Namely, this class decides if any particular file is a MyBatis
 * Generator configuration file or not.
 * 
 * @author Jeff Butler
 */
public class AdapterFactory implements IAdapterFactory {

    public AdapterFactory() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
        if (adapterType == ConfigurationFileAdapter.class &&
                adaptableObject instanceof IFile) {
            IFile file = (IFile) adaptableObject;
            
            ConfigVerifyer verifyer = new ConfigVerifyer(file);
            if (verifyer.isConfigurationFile()) {
                return (T) new ConfigurationFileAdapter(file);
            }
        }
        
        return null;
    }

    @Override
    public Class<?>[] getAdapterList() {
        return new Class<?>[] { ConfigurationFileAdapter.class };
    }
}
