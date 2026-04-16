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
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.ORDER_IMPORTS, true));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.SORT_IMPORTS_STRATEGY, Objects.requireNonNull(builder.ios)));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.PRINT_JAVADOC, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.SPACE_AROUND_OPERATORS, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.INDENT_CASE_IN_SWITCH, false));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.MAX_ENUM_CONSTANTS_TO_ALIGN_HORIZONTALLY, -1));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.INDENTATION, Objects.requireNonNull(builder.indentation)));
        addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.INDENT_PRINT_ARRAYS_OF_ANNOTATIONS, true));
    }

    public static class Builder {
        private @Nullable ImportOrderingStrategy ios;
        private @Nullable Indentation indentation;

        public Builder withImportOrderingStrategy(ImportOrderingStrategy ios) {
            this.ios = ios;
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
