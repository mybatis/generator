/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.config;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class JavaTypeResolverConfiguration extends TypedPropertyHolder {

    public JavaTypeResolverConfiguration(@Nullable String configurationType) {
        super(configurationType);
    }

    public String getImplementationType() {
        if (configurationType == null || "DEFAULT".equals(configurationType)) { //$NON-NLS-1$
            return JavaTypeResolverDefaultImpl.class.getName();
        } else {
            return configurationType;
        }
    }
}
