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

import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.Printer;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.ImportOrderingStrategy;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.imports.DefaultImportOrderingStrategy;
import com.github.javaparser.printer.configuration.imports.EclipseImportOrderingStrategy;
import com.github.javaparser.printer.configuration.imports.IntelliJImportOrderingStrategy;
import com.github.javaparser.printer.lexicalpreservation.DefaultLexicalPreservingPrinter;

public class JavaMergerFactory {
    public static JavaFileMerger getMerger(MergeConfiguration mergeConfiguration) {
        return switch (mergeConfiguration.mergeStrategy()) {
            case MERGE_INTO_NEW -> new MergeIntoNewJavaFileMerger(getPrinter(mergeConfiguration));
            case MERGE_INTO_EXISTING -> new MergeIntoExistingJavaFileMerger(getPrinter(mergeConfiguration));
        };
    }

    private static Printer getPrinter(MergeConfiguration mergeConfiguration) {
        if (mergeConfiguration.isLexicalPreserving()) {
            return new DefaultLexicalPreservingPrinter();
        }

        ImportOrderingStrategy ios = switch (mergeConfiguration.importSortType()) {
            case ECLIPSE -> new EclipseImportOrderingStrategy();
            case INTELLIJ -> new IntelliJImportOrderingStrategy();
            default -> new DefaultImportOrderingStrategy();
        };
        ios.setSortImportsAlphabetically(true);

        Indentation indentation = switch (mergeConfiguration.indentType()) {
            case TAB -> new Indentation(Indentation.IndentType.TABS, mergeConfiguration.indentSize());
            case SPACE -> new Indentation(Indentation.IndentType.SPACES, mergeConfiguration.indentSize());
        };

        return new DefaultPrettyPrinter(config(ios, indentation));
    }

    private static DefaultPrinterConfiguration config(ImportOrderingStrategy ios, Indentation indentation) {
        DefaultPrinterConfiguration config = new DefaultPrinterConfiguration();
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.ORDER_IMPORTS, true));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.SORT_IMPORTS_STRATEGY,
                ios));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS, Boolean.TRUE));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_JAVADOC, Boolean.TRUE));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.SPACE_AROUND_OPERATORS, Boolean.TRUE));

        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENT_CASE_IN_SWITCH, false));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.MAX_ENUM_CONSTANTS_TO_ALIGN_HORIZONTALLY, -1));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, indentation));
        config.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENT_PRINT_ARRAYS_OF_ANNOTATIONS, true));
        return config;
    }
}
