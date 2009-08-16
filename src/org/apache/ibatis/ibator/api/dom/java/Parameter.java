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
package org.apache.ibatis.ibator.api.dom.java;


/**
 * @author Jeff Butler
 */
public class Parameter {
    private String name;
    private FullyQualifiedJavaType type;

    public Parameter(FullyQualifiedJavaType type, String name) {
        super();
        this.name = name;
        this.type = type;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }
}
