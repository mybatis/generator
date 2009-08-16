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

package org.apache.ibatis.abator.config;

import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * Typesafe enum of different model types
 * 
 * @author Jeff Butler
 */
public class ModelType {
    private String modelType;
    
    public static final ModelType HIERARCHICAL = new ModelType("hierarchical"); //$NON-NLS-1$
    public static final ModelType FLAT = new ModelType("flat"); //$NON-NLS-1$
    public static final ModelType CONDITIONAL = new ModelType("conditional"); //$NON-NLS-1$

    /**
     * 
     */
    private ModelType(String modelType) {
        super();
        this.modelType = modelType;
    }

    public String getModelType() {
        return modelType;
    }
    
    public static ModelType getModelType(String type) {
        if (HIERARCHICAL.getModelType().equalsIgnoreCase(type)) {
            return HIERARCHICAL;
        } else if (FLAT.getModelType().equalsIgnoreCase(type)) {
            return FLAT;
        } else if (CONDITIONAL.getModelType().equalsIgnoreCase(type)) {
            return CONDITIONAL;
        } else {
            throw new RuntimeException(Messages.getString("RuntimeError.13", type)); //$NON-NLS-1$
        }
    }
}
