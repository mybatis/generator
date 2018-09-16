/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.api.dom.java.render;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.InitializationBlock;

public class InitializationBlockRenderer {

    private BodyLineRenderer bodyLineRenderer = new BodyLineRenderer();
    
    public List<String> render(InitializationBlock initializationBlock) {
        List<String> lines = new ArrayList<>();

        lines.addAll(initializationBlock.getJavaDocLines());
        lines.add(renderFirstLine(initializationBlock));
        lines.addAll(bodyLineRenderer.render(initializationBlock.getBodyLines()));
        lines.add("}"); //$NON-NLS-1$
        
        return lines;
    }
    
    private String renderFirstLine(InitializationBlock initializationBlock) {
        if (initializationBlock.isStatic()) {
            return "static {"; //$NON-NLS-1$
        } else {
            return "{"; //$NON-NLS-1$
        }
    }
}
