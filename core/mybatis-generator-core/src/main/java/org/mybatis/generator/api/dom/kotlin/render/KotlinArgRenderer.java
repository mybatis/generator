/*
 *    Copyright 2006-2020 the original author or authors.
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
package org.mybatis.generator.api.dom.kotlin.render;

import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.internal.util.CustomCollectors;

public class KotlinArgRenderer {
    public String render(KotlinArg kotlinArg) {
        return renderAnnotations(kotlinArg)
                + kotlinArg.getName()
                + kotlinArg.getDataType().map(dt -> ": " + dt).orElse("") //$NON-NLS-1$ //$NON-NLS-2$
                + kotlinArg.getInitializationString().map(s -> " = " + s).orElse(""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private String renderAnnotations(KotlinArg kotlinArg) {
        return kotlinArg.getAnnotations().stream()
                .collect(CustomCollectors.joining(" ", "", " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
