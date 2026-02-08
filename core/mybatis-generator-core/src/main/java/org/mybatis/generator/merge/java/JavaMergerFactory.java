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

import com.github.javaparser.printer.configuration.PrinterConfiguration;
import org.jspecify.annotations.Nullable;
import org.mybatis.generator.internal.util.messages.Messages;

public class JavaMergerFactory {
    public static JavaFileMerger defaultMerger(@Nullable Object printerConfiguration) {
        if (printerConfiguration == null) {
            return new JavaFileMergerJavaParserImpl(new EclipseOrderedPrinterConfiguration());
        } else if (printerConfiguration instanceof PrinterConfiguration pc) {
            return new JavaFileMergerJavaParserImpl(pc);
        } else {
            throw new IllegalArgumentException(Messages.getString("RuntimeError.30")); //$NON-NLS-1$
        }
    }
}
