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
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Butler
 */
public class Parameter {
    private String name;
    private FullyQualifiedJavaType type;

    private List<String> annotations;

    public Parameter(FullyQualifiedJavaType type, String name) {
        super();
        this.name = name;
        this.type = type;
        annotations = new ArrayList<String>();
    }

    public Parameter(FullyQualifiedJavaType type, String name, String annotation) {
        this(type, name);
        addAnnotation(annotation);
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

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        for (String annotation : annotations) {
            sb.append(annotation);
            sb.append(' ');
        }

        sb.append(type.getShortName());
        sb.append(' ');
        sb.append(name);

        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedContent();
    }
}
