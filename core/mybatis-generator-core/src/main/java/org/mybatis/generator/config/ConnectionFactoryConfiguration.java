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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.internal.util.StringUtility;

public class ConnectionFactoryConfiguration extends TypedPropertyHolder {

    public ConnectionFactoryConfiguration() {
        super();
    }

    public void validate(List<String> errors) {
        if (getConfigurationType() == null || "DEFAULT".equals(getConfigurationType())) { //$NON-NLS-1$
            if (!StringUtility.stringHasValue(getProperty("driverClass"))) { //$NON-NLS-1$
                errors.add(getString("ValidationError.18", "connectionFactory", "driverClass")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            if (!StringUtility.stringHasValue(getProperty("connectionURL"))) { //$NON-NLS-1$
                errors.add(getString("ValidationError.18", "connectionFactory", "connectionURL")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
    }
}
