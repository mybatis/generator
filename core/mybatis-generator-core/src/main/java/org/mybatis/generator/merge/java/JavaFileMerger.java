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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.jspecify.annotations.Nullable;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.ShellException;

/**
 * This class handles the task of merging changes into an existing Java file using JavaParser.
 * It supports merging by removing methods and fields that have specific JavaDoc tags or annotations.
 *
 * @author Freeman
 */
public class JavaFileMerger {
    private JavaFileMerger() {
    }

    /**
     * Merge a newly generated Java file with an existing Java file.
     *
     * @param newFileSource the source of the newly generated Java file
     * @param existingFile  the existing Java file
     * @param javadocTags   the JavaDoc tags that denote which methods and fields in the old file to delete
     * @param fileEncoding  the file encoding for reading existing Java files
     * @return the merged source, properly formatted
     * @throws ShellException if the file cannot be merged for some reason
     */
    public static String getMergedSource(String newFileSource, File existingFile,
                                         String[] javadocTags, @Nullable String fileEncoding) throws ShellException {
        try {
            String existingFileContent = readFileContent(existingFile, fileEncoding);
            return getMergedSource(newFileSource, existingFileContent, javadocTags);
        } catch (IOException e) {
            throw new ShellException(getString("Warning.13", existingFile.getName()), e);
        }
    }

    /**
     * Merge a newly generated Java file with existing Java file content.
     *
     * @param newFileSource       the source of the newly generated Java file
     * @param existingFileContent the content of the existing Java file
     * @param javadocTags         the JavaDoc tags that denote which methods and fields in the old file to delete
     * @return the merged source, properly formatted
     * @throws ShellException if the file cannot be merged for some reason
     */
    public static String getMergedSource(String newFileSource, String existingFileContent,
                                         String[] javadocTags) throws ShellException {
        try {
            JavaParser javaParser = new JavaParser();

            // Parse the new file
            ParseResult<CompilationUnit> newParseResult = javaParser.parse(newFileSource);
            if (!newParseResult.isSuccessful()) {
                throw new ShellException("Failed to parse new Java file: " + newParseResult.getProblems());
            }
            CompilationUnit newCompilationUnit = newParseResult.getResult().orElseThrow();

            // Parse the existing file
            ParseResult<CompilationUnit> existingParseResult = javaParser.parse(existingFileContent);
            if (!existingParseResult.isSuccessful()) {
                throw new ShellException("Failed to parse existing Java file: " + existingParseResult.getProblems());
            }
            CompilationUnit existingCompilationUnit = existingParseResult.getResult().orElseThrow();

            // Perform the merge
            CompilationUnit mergedCompilationUnit =
                    performMerge(newCompilationUnit, existingCompilationUnit, javadocTags);

            return mergedCompilationUnit.toString();
        } catch (Exception e) {
            throw new ShellException("Error merging Java files: " + e.getMessage(), e);
        }
    }

    private static CompilationUnit performMerge(CompilationUnit newCompilationUnit,
                                                CompilationUnit existingCompilationUnit,
                                                String[] javadocTags) {
        // Start with the new compilation unit as the base (to get new generated elements first)
        CompilationUnit mergedCompilationUnit = newCompilationUnit.clone();

        // Merge imports
        mergeImports(existingCompilationUnit, mergedCompilationUnit);

        // Add preserved (non-generated) elements from existing file at the end
        addPreservedElements(existingCompilationUnit, mergedCompilationUnit, javadocTags);

        return mergedCompilationUnit;
    }

    private static boolean isGeneratedElement(BodyDeclaration<?> member, String[] javadocTags) {
        return hasGeneratedAnnotation(member) || hasGeneratedJavadocTag(member, javadocTags);
    }

    private static boolean hasGeneratedAnnotation(BodyDeclaration<?> member) {
        for (AnnotationExpr annotation : member.getAnnotations()) {
            // TODO - only check the generated annotations from us!
            String annotationName = annotation.getNameAsString();
            // Check for @Generated annotation (both javax and jakarta packages)
            if ("Generated".equals(annotationName)
                    || "javax.annotation.Generated".equals(annotationName)
                    || "jakarta.annotation.Generated".equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasGeneratedJavadocTag(BodyDeclaration<?> member, String[] javadocTags) {
        // Check if the member has a comment and if it contains any of the javadoc tags
        if (member.getComment().isPresent()) {
            String commentContent = member.getComment().orElseThrow().getContent();
            for (String tag : javadocTags) {
                if (commentContent.contains(tag)) {
                    return !commentContent.contains(MergeConstants.DO_NOT_DELETE_DURING_MERGE);
                }
            }
        }
        return false;
    }

    private static void mergeImports(CompilationUnit existingCompilationUnit,
                                     CompilationUnit mergedCompilationUnit) {
        record ImportInfo(String name, boolean isStatic, boolean isAsterisk) implements Comparable<ImportInfo> {
            @Override
            public int compareTo(ImportInfo other) {
                // Static imports come last
                if (this.isStatic != other.isStatic) {
                    return this.isStatic ? 1 : -1;
                }

                // Within the same category (static or non-static), sort by import order priority
                int priorityThis = getImportPriority(this.name);
                int priorityOther = getImportPriority(other.name);

                if (priorityThis != priorityOther) {
                    return Integer.compare(priorityThis, priorityOther);
                }

                // Within the same priority, use natural ordering (case-insensitive)
                return String.CASE_INSENSITIVE_ORDER.compare(this.name, other.name);
            }
        }

        // Collect all imports from both compilation units
        Set<ImportInfo> allImports = new LinkedHashSet<>();

        // Add imports from new file
        for (ImportDeclaration importDecl : mergedCompilationUnit.getImports()) {
            allImports.add(
                    new ImportInfo(importDecl.getNameAsString(), importDecl.isStatic(), importDecl.isAsterisk()));
        }

        // Add imports from existing file (avoiding duplicates)
        for (ImportDeclaration importDecl : existingCompilationUnit.getImports()) {
            allImports.add(
                    new ImportInfo(importDecl.getNameAsString(), importDecl.isStatic(), importDecl.isAsterisk()));
        }

        // Clear existing imports and add sorted imports
        mergedCompilationUnit.getImports().clear();

        // Sort imports according to best practices and add them back
        allImports.stream()
                .sorted()
                .forEach(importInfo -> mergedCompilationUnit.addImport(
                        importInfo.name(), importInfo.isStatic(), importInfo.isAsterisk()));
    }

    private static int getImportPriority(String importName) {
        if (importName.startsWith("java.")) {
            return 10;
        } else if (importName.startsWith("javax.")) {
            return 20;
        } else if (importName.startsWith("jakarta.")) {
            return 30;
        } else {
            return 40; // Third-party and project imports
        }
    }

    private static void addPreservedElements(CompilationUnit existingCompilationUnit,
                                             CompilationUnit mergedCompilationUnit, String[] javadocTags) {
        // Find the main type declarations
        TypeDeclaration<?> existingTypeDeclaration = findMainTypeDeclaration(existingCompilationUnit);
        TypeDeclaration<?> mergedTypeDeclaration = findMainTypeDeclaration(mergedCompilationUnit);

        if (existingTypeDeclaration instanceof ClassOrInterfaceDeclaration existingClassDeclaration
                && mergedTypeDeclaration instanceof ClassOrInterfaceDeclaration mergedClassDeclaration) {

            // Add only non-generated members from the existing class to the end of merged class
            for (BodyDeclaration<?> member : existingClassDeclaration.getMembers()) {
                if (!isGeneratedElement(member, javadocTags)) {
                    // If there is a member in the merged type that matches an existing member, we need to delete it.
                    // Some generated elements could survive if they have the do_not_delete_during_merge text
                    deleteDuplicateMemberIfExists(mergedClassDeclaration, member);
                    mergedClassDeclaration.addMember(member.clone());
                }
            }
        }
    }

    private static void deleteDuplicateMemberIfExists(TypeDeclaration<?> mergedTypeDeclaration,
                                                      BodyDeclaration<?> member) {
        mergedTypeDeclaration.getMembers().stream()
                .filter(Objects::nonNull)
                .filter(td -> membersMatch(td, member))
                .findFirst()
                .ifPresent(mergedTypeDeclaration::remove);
    }

    private static boolean membersMatch(BodyDeclaration<?> member1, BodyDeclaration<?> member2) {
        if (member1.isTypeDeclaration() && member2.isTypeDeclaration()) {
            return member1.asTypeDeclaration().getNameAsString()
                    .equals(member2.asTypeDeclaration().getNameAsString());
        } else if (member1.isCallableDeclaration() && member2.isCallableDeclaration()) {
            return member1.asCallableDeclaration().getSignature().asString()
                    .equals(member2.asCallableDeclaration().getSignature().asString());
        } else if (member1.isFieldDeclaration() && member2.isFieldDeclaration()) {
            return member1.asFieldDeclaration().toString()
                    .equals(member2.asFieldDeclaration().toString());
        }

        return false;
    }

    private static @Nullable TypeDeclaration<?> findMainTypeDeclaration(CompilationUnit compilationUnit) {
        // Return the first public type declaration, or the first type declaration if no public one exists
        TypeDeclaration<?> firstType = null;
        for (TypeDeclaration<?> typeDeclaration : compilationUnit.getTypes()) {
            if (firstType == null) {
                firstType = typeDeclaration;
            }
            if (typeDeclaration.isPublic()) {
                return typeDeclaration;
            }
        }
        return firstType;
    }

    private static String readFileContent(File file, @Nullable String fileEncoding) throws IOException {
        if (fileEncoding != null) {
            return Files.readString(file.toPath(), Charset.forName(fileEncoding));
        } else {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        }
    }
}
