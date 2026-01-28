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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

public class ConnectionFactoryConfiguration extends TypedPropertyHolder {

    protected ConnectionFactoryConfiguration(Builder builder) {
        super(builder);
    }

    public void validate(List<String> errors) {
        if (configurationType == null || "DEFAULT".equals(configurationType)) { //$NON-NLS-1$
            if (!stringHasValue(getProperty("driverClass"))) { //$NON-NLS-1$
                errors.add(getString("ValidationError.18", //$NON-NLS-1$
                        "connectionFactory", //$NON-NLS-1$
                        "driverClass")); //$NON-NLS-1$
            }

            if (!stringHasValue(getProperty("connectionURL"))) { //$NON-NLS-1$
                errors.add(getString("ValidationError.18", //$NON-NLS-1$
                        "connectionFactory", //$NON-NLS-1$
                        "connectionURL")); //$NON-NLS-1$
            }
        }
    }

    public String getImplementationType() {
        if (configurationType == null || "DEFAULT".equals(configurationType)) { //$NON-NLS-1$
            return Defaults.DEFAULT_GENERIC_CONNECTION_FACTORY;
        } else {
            return configurationType;
        }
    }

    public static class Builder extends TypedBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        public ConnectionFactoryConfiguration build() {
            return new ConnectionFactoryConfiguration(this);
        }
    }
}
