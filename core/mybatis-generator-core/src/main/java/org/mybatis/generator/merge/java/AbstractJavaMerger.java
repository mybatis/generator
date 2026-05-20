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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.printer.Printer;
import org.mybatis.generator.exception.MergeException;

public abstract class AbstractJavaMerger implements JavaFileMerger {
    protected final Printer printer;
    private final boolean isLexicalPreserving;

    protected  AbstractJavaMerger(Printer printer, boolean isLexicalPreserving) {
        this.printer = printer;
        this.isLexicalPreserving = isLexicalPreserving;
    }

    /**
     * Merge a newly generated Java file with existing Java file content.
     *
     * @param newFileContent the content of the newly generated Java file
     * @param existingFileContent the content of the existing Java file
     * @return the merged source, properly formatted
     * @throws MergeException if the file cannot be merged for some reason
     */
    @Override
    public String getMergedSource(String newFileContent, String existingFileContent) throws MergeException {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
        if (isLexicalPreserving) {
            parserConfiguration.setLexicalPreservationEnabled(true);
        }
        JavaParser javaParser = new JavaParser(parserConfiguration);

        ParseResults existingFileParseResults = JavaMergeUtilities.parseAndFindMainTypeDeclaration(javaParser,
                existingFileContent, MergeFileType.EXISTING_FILE);

        // Gather custom members from the existing file. If none, just return the new file as is
        CustomMemberGatherer customMemberGatherer =
                new CustomMemberGatherer(existingFileParseResults.typeDeclaration());
        if (!customMemberGatherer.hasAnyMembersToMerge()) {
            return newFileContent;
        }

        // Custom members exist, need to merge...
        ParseResults newFileParseResults = JavaMergeUtilities.parseAndFindMainTypeDeclaration(javaParser,
                newFileContent, MergeFileType.NEW_FILE);

        return performMerge(customMemberGatherer, existingFileParseResults, newFileParseResults);
    }

    protected abstract String performMerge(CustomMemberGatherer customMemberGatherer,
                                           ParseResults existingFileParseResults, ParseResults newFileParseResults);
}
