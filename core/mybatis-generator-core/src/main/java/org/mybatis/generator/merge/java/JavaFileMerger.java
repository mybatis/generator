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

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.exception.ShellException;

@FunctionalInterface
public interface JavaFileMerger {

    /**
     * Merge a newly generated Java file with an existing Java file.
     *
     * @param newFileContent the content of the newly generated Java file
     * @param existingFile the existing Java file
     * @param fileEncoding the file encoding for reading existing Java files
     * @return the merged source, properly formatted
     * @throws ShellException if the file cannot be merged for some reason
     */
    default String getMergedSource(String newFileContent, File existingFile,
                                  @Nullable String fileEncoding) throws ShellException {
        try {
            String existingFileContent = readFileContent(existingFile, fileEncoding);
            return getMergedSource(newFileContent, existingFileContent);
        } catch (IOException e) {
            throw new ShellException(getString("Warning.32", existingFile.getName()), e); //$NON-NLS-1$
        }
    }

    /**
     * Merge a newly generated Java file with existing Java file content.
     *
     * @param newFileContent the content of the newly generated Java file
     * @param existingFileContent the content of the existing Java file
     * @return the merged source, properly formatted
     * @throws ShellException if the file cannot be merged for some reason
     */
    String getMergedSource(String newFileContent, String existingFileContent) throws ShellException;

    private String readFileContent(File file, @Nullable String fileEncoding) throws IOException {
        if (fileEncoding != null) {
            return Files.readString(file.toPath(), Charset.forName(fileEncoding));
        } else {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        }
    }
}
