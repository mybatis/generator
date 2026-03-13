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

import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.imports.EclipseImportOrderingStrategy;

/**
 * Printer configuration that orders imports in "Eclipse" order (statics first, etc.).
 */
public class EclipseOrderedPrinterConfiguration extends DefaultPrinterConfiguration {
    public EclipseOrderedPrinterConfiguration() {
        addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.ORDER_IMPORTS, true));
        addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.SORT_IMPORTS_STRATEGY,
                        new EclipseImportOrderingStrategy()));
        addOption(new DefaultConfigurationOption(ConfigOption.PRINT_COMMENTS, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(ConfigOption.PRINT_JAVADOC, Boolean.TRUE));
        addOption(new DefaultConfigurationOption(ConfigOption.SPACE_AROUND_OPERATORS, Boolean.TRUE));

        addOption(new DefaultConfigurationOption(ConfigOption.INDENT_CASE_IN_SWITCH, false));
        addOption(new DefaultConfigurationOption(ConfigOption.MAX_ENUM_CONSTANTS_TO_ALIGN_HORIZONTALLY, 999));
        addOption(new DefaultConfigurationOption(ConfigOption.INDENTATION,
                new Indentation(Indentation.IndentType.SPACES, 4)));
        addOption(new DefaultConfigurationOption(ConfigOption.INDENT_PRINT_ARRAYS_OF_ANNOTATIONS, true));
    }
}
