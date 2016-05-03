/*
 *  Copyright 2006 The Apache Software Foundation
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
package org.mybatis.generator.eclipse.ui.content;

import org.eclipse.core.resources.IFile;

/**
 * This is the adapter class for files that are generator configuration files.
 * 
 * @author Jeff Butler
 */
public class ConfigurationFileAdapter {

    private IFile baseFile;
    
    public ConfigurationFileAdapter(IFile baseFile) {
        super();
        this.baseFile = baseFile;
    }

    public IFile getBaseFile() {
        return baseFile;
    }
}
