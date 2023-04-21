/*
 *    Copyright 2006-2023 the original author or authors.
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
package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.kotlin.KotlinFile;

public class GeneratedKotlinFile extends GeneratedFile {

    private final KotlinFile kotlinFile;

    private final String fileEncoding;

    private final KotlinFormatter kotlinFormatter;

    public GeneratedKotlinFile(KotlinFile kotlinFile,
            String targetProject,
            String fileEncoding,
            KotlinFormatter kotlinFormatter) {
        super(targetProject);
        this.kotlinFile = kotlinFile;
        this.fileEncoding = fileEncoding;
        this.kotlinFormatter = kotlinFormatter;
    }

    @Override
    public String getFormattedContent() {
        return kotlinFormatter.getFormattedContent(kotlinFile);
    }

    @Override
    public String getFileName() {
        return kotlinFile.getFileName();
    }

    @Override
    public String getTargetPackage() {
        return kotlinFile.getPackage().orElse(""); //$NON-NLS-1$
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

    @Override
    public String getFileEncoding() {
        return fileEncoding;
    }
}
