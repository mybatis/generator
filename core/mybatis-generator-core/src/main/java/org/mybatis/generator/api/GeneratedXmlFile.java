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
package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.xml.Document;

public class GeneratedXmlFile extends GeneratedFile {
    private final Document document;
    private final String fileName;
    private final String targetPackage;
    private boolean isMergeable;

    public GeneratedXmlFile(Document document, String fileName, String targetPackage, String targetProject,
                            boolean isMergeable) {
        super(targetProject);
        this.document = document;
        this.fileName = fileName;
        this.targetPackage = targetPackage;
        this.isMergeable = isMergeable;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public boolean isMergeable() {
        return isMergeable;
    }

    public Document getDocument() {
        return document;
    }

    public void setMergeable(boolean isMergeable) {
        this.isMergeable = isMergeable;
    }
}
