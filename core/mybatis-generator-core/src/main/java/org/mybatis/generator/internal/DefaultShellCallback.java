/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.StringTokenizer;

import org.mybatis.generator.api.JavaFileMerger;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.exception.ShellException;

public class DefaultShellCallback implements ShellCallback {

    private final boolean overwrite;
    private JavaFileMerger javaFileMerger;

    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }

    public void setJavaFileMerger(JavaFileMerger javaFileMerger) {
        this.javaFileMerger = Objects.requireNonNull(javaFileMerger);
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        // targetProject is interpreted as a directory that must already exist
        //
        // targetPackage is interpreted as a subdirectory, but in package
        // format (with dots instead of slashes). The subdirectory will be
        // created if it does not already exist

        Path targetProjectDirectory = Path.of(targetProject);
        if (!Files.isDirectory(targetProjectDirectory)) {
            throw new ShellException(getString("Warning.9", //$NON-NLS-1$
                    targetProject));
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        Path directory = targetProjectDirectory.resolve(sb.toString());
        if (!Files.isDirectory(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                        directory.toFile().getAbsolutePath()));
            }
        }

        return directory.toFile();
    }

    @Override
    public boolean isMergeSupported() {
        return javaFileMerger != null;
    }

    @Override
    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    @Override
    public String mergeJavaFile(String newFileSource,
            File existingFile, String[] javadocTags, String fileEncoding)
            throws ShellException {
        if (javaFileMerger == null) {
            throw new UnsupportedOperationException();
        } else {
            return javaFileMerger.mergeJavaFile(newFileSource, existingFile, javadocTags, fileEncoding);
        }
    }
}
