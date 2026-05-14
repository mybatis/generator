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
package org.mybatis.generator.merge.java;

import java.util.Objects;

import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.ImportOrderingStrategy;
import com.github.javaparser.printer.configuration.Indentation;
import org.jspecify.annotations.Nullable;

public class CustomPrinterConfiguration extends DefaultPrinterConfiguration {
    private CustomPrinterConfiguration(Builder builder) {
        addOption(new DefaultConfigurationOption(ConfigOption.ORDER_IMPORTS, true));
        addOption(new DefaultConfigurationOption(ConfigOption.SORT_IMPORTS_STRATEGY,
                Objects.requireNonNull(builder.importOrderingStrategy)));
        addOption(new DefaultConfigurationOption(ConfigOption.PRINT_COMMENTS, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(ConfigOption.PRINT_JAVADOC, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(ConfigOption.SPACE_AROUND_OPERATORS, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(ConfigOption.INDENT_CASE_IN_SWITCH, false));
        addOption(new DefaultConfigurationOption(ConfigOption.MAX_ENUM_CONSTANTS_TO_ALIGN_HORIZONTALLY, -1));
        addOption(new DefaultConfigurationOption(ConfigOption.INDENTATION,
                Objects.requireNonNull(builder.indentation)));
        addOption(new DefaultConfigurationOption(ConfigOption.INDENT_PRINT_ARRAYS_OF_ANNOTATIONS, true));
        addOption(new DefaultConfigurationOption(ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, true));
    }

    public static class Builder {
        private @Nullable ImportOrderingStrategy importOrderingStrategy;
        private @Nullable Indentation indentation;

        public Builder withImportOrderingStrategy(ImportOrderingStrategy importOrderingStrategy) {
            this.importOrderingStrategy = importOrderingStrategy;
            return this;
        }

        public Builder withIndentation(Indentation indentation) {
            this.indentation = indentation;
            return this;
        }

        public CustomPrinterConfiguration build() {
            return new CustomPrinterConfiguration(this);
        }
    }
}
