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
package org.mybatis.generator.api.dom;

import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.render.KotlinFileRenderer;
import org.mybatis.generator.config.Context;

/**
 * This class is the default formatter for generated Kotlin.  This class will use the
 * built in DOM renderers.
 *
 * @author Jeff Butler
 *
 */
public class DefaultKotlinFormatter implements KotlinFormatter {
    protected Context context;

    @Override
    public String getFormattedContent(KotlinFile kotlinFile) {
        return new KotlinFileRenderer().render(kotlinFile);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
