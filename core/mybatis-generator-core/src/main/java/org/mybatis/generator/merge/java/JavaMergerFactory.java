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
import com.github.javaparser.printer.configuration.ImportOrderingStrategy;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.imports.DefaultImportOrderingStrategy;
import com.github.javaparser.printer.configuration.imports.EclipseImportOrderingStrategy;
import com.github.javaparser.printer.configuration.imports.IntelliJImportOrderingStrategy;
import com.github.javaparser.printer.lexicalpreservation.DefaultLexicalPreservingPrinter;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.config.JavaMergeConfiguration;

public class JavaMergerFactory {
    public static JavaFileMerger getMerger(JavaMergeConfiguration javaMergeConfiguration, Indenter indenter) {
        return switch (javaMergeConfiguration.mergeStrategy()) {
        case MERGE_INTO_NEW -> new MergeIntoNewJavaFileMerger(getPrinter(javaMergeConfiguration, indenter),
                    javaMergeConfiguration.isLexicalPreserving());
        case MERGE_INTO_EXISTING -> new MergeIntoExistingJavaFileMerger(getPrinter(javaMergeConfiguration, indenter),
                    javaMergeConfiguration.isLexicalPreserving());
        };
    }

    private static Printer getPrinter(JavaMergeConfiguration javaMergeConfiguration, Indenter indenter) {
        if (javaMergeConfiguration.isLexicalPreserving()) {
            return new DefaultLexicalPreservingPrinter();
        }

        return new DefaultPrettyPrinter(calculatePrinterConfiguration(javaMergeConfiguration, indenter));
    }

    private static CustomPrinterConfiguration calculatePrinterConfiguration(
            JavaMergeConfiguration javaMergeConfiguration, Indenter indenter) {
        ImportOrderingStrategy ios = switch (javaMergeConfiguration.importSortType()) {
        case ECLIPSE -> new EclipseImportOrderingStrategy();
        case INTELLIJ -> new IntelliJImportOrderingStrategy();
        case DEFAULT -> new DefaultImportOrderingStrategy();
        };
        ios.setSortImportsAlphabetically(true);

        Indentation indentation = switch (indenter.javaIndentType()) {
        case TABS -> new Indentation(Indentation.IndentType.TABS, indenter.javaIndentAmount());
        case SPACES -> new Indentation(Indentation.IndentType.SPACES, indenter.javaIndentAmount());
        };

        return new CustomPrinterConfiguration.Builder()
                .withImportOrderingStrategy(ios)
                .withIndentation(indentation)
                .build();
    }
}
