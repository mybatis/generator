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
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

public class JDBCConnectionConfiguration extends PropertyHolder {
    private final String driverClass;
    private final String connectionURL;
    private final @Nullable String userId;
    private final @Nullable String password;

    protected JDBCConnectionConfiguration(Builder builder) {
        super(builder);
        this.driverClass = Objects.requireNonNull(builder.driverClass);
        this.connectionURL = Objects.requireNonNull(builder.connectionURL);
        this.userId = builder.userId;
        this.password = builder.password;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getUserId() {
        return Optional.ofNullable(userId);
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void validate(List<String> errors) {
        if (!stringHasValue(driverClass)) {
            errors.add(getString("ValidationError.4")); //$NON-NLS-1$
        }

        if (!stringHasValue(connectionURL)) {
            errors.add(getString("ValidationError.5")); //$NON-NLS-1$
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private @Nullable String driverClass;
        private @Nullable String connectionURL;
        private @Nullable String userId;
        private @Nullable String password;

        public Builder withDriverClass(@Nullable String driverClass) {
            this.driverClass = driverClass;
            return this;
        }

        public Builder withConnectionURL(@Nullable String connectionURL) {
            this.connectionURL = connectionURL;
            return this;
        }

        public Builder withUserId(@Nullable String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withPassword(@Nullable String password) {
            this.password = password;
            return this;
        }

        public JDBCConnectionConfiguration build() {
            return new JDBCConnectionConfiguration(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
