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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.MergeException;

public class JavaMergeUtilities {
    private JavaMergeUtilities() {
        // utility class, no instances
    }

    /**
     * Compare two compilation units and find imports that are in the source file but not in the target file.
     * We assume this means they are required for some custom method, so we will add them to the target
     * file if there are other items to merge. This may create unused imports in the target file if the
     * initial assumption is incorrect, but better safe than sorry.
     *
     * @param sourceCompilationUnit compilation unit representing the source file
     * @param targetCompilationUnit compilation unit representing the target file
     */
    public static void copyMissingImports(CompilationUnit sourceCompilationUnit,
                                          CompilationUnit targetCompilationUnit) {
        List<String> newFileImports = targetCompilationUnit.getImports().stream()
                .map(JavaMergeUtilities::stringify).toList();

        sourceCompilationUnit.getImports().stream()
                .filter(im -> !newFileImports.contains(stringify(im)))
                .forEach(targetCompilationUnit::addImport);
    }

    /**
     * Compare two members to see if they are "functionally equivalent". This is defined as:
     *
     * <ul>
     *     <li>Members are the same type</li>
     *     <li>Members have the same signature or basic declaration</li>
     * </ul>
     *
     * @param member1 the first member
     * @param member2 the second member
     * @return true if the members are functionally equivalent
     */
    private static boolean membersMatch(BodyDeclaration<?> member1, BodyDeclaration<?> member2) {
        if (member1.isTypeDeclaration() && member2.isTypeDeclaration()) {
            return member1.asTypeDeclaration().getNameAsString()
                    .equals(member2.asTypeDeclaration().getNameAsString());
        } else if (member1.isCallableDeclaration() && member2.isCallableDeclaration()) {
            return member1.asCallableDeclaration().getSignature().asString()
                    .equals(member2.asCallableDeclaration().getSignature().asString());
        } else if (member1.isFieldDeclaration() && member2.isFieldDeclaration()) {
            return stringify(member1.asFieldDeclaration()).equals(stringify(member2.asFieldDeclaration()));
        }

        return false;
    }

    public static void copyMissingSuperInterfaces(BodyDeclaration<?> sourceType, BodyDeclaration<?> targetType) {
        List<String> targetSuperInterfaces = findSuperInterfaces(targetType).stream()
                .map(NodeWithSimpleName::getNameAsString).toList();

        findSuperInterfaces(sourceType).stream()
                .filter(t -> !targetSuperInterfaces.contains(t.getNameAsString()))
                .forEach(t -> addSuperInterface(targetType, t));
    }

    private static List<ClassOrInterfaceType> findSuperInterfaces(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            return bodyDeclaration.asClassOrInterfaceDeclaration().getImplementedTypes();
        } else if (bodyDeclaration.isEnumDeclaration()) {
            return bodyDeclaration.asEnumDeclaration().getImplementedTypes();
        } else if (bodyDeclaration.isRecordDeclaration()) {
            return bodyDeclaration.asRecordDeclaration().getImplementedTypes();
        }

        return List.of();
    }

    private static void addSuperInterface(BodyDeclaration<?> bodyDeclaration, ClassOrInterfaceType superInterface) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            bodyDeclaration.asClassOrInterfaceDeclaration().addImplementedType(superInterface);
        } else if (bodyDeclaration.isEnumDeclaration()) {
            bodyDeclaration.asEnumDeclaration().addImplementedType(superInterface);
        } else if (bodyDeclaration.isRecordDeclaration()) {
            bodyDeclaration.asRecordDeclaration().addImplementedType(superInterface);
        }
    }

    /**
     * Create a string representation of an import that we can use to find matches.
     *
     * @param importDeclaration the import declaration to stringify
     * @return string representation of the import (not a full import statement)
     */
    private static String stringify(ImportDeclaration importDeclaration) {
        StringBuilder sb = new StringBuilder();
        if (importDeclaration.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }
        if (importDeclaration.isModule()) {
            sb.append("module "); //$NON-NLS-1$
        }
        sb.append(importDeclaration.getNameAsString());
        if (importDeclaration.isAsterisk()) {
            sb.append(".*"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    private static String stringify(FieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getVariables().stream()
                .map(JavaMergeUtilities::stringify)
                .collect(Collectors.joining(",")); //$NON-NLS-1$
    }

    private static String stringify(VariableDeclarator variableDeclarator) {
        return variableDeclarator.getType().toString()
                + " " //$NON-NLS-1$
                + variableDeclarator.getName().toString();
    }

    public static GeneratedType checkForGeneratedAnnotation(BodyDeclaration<?> member) {
        return member.getAnnotations().stream()
                .filter(JavaMergeUtilities::isOurGeneratedAnnotation)
                .findFirst()
                .map(a -> {
                    if (hasDoNotDeleteComment(a)) {
                        return GeneratedType.GENERATED_KEEP;
                    } else {
                        return GeneratedType.GENERATED_REMOVE;
                    }
                })
                .orElse(GeneratedType.NOT_GENERATED);
    }

    private static boolean isOurGeneratedAnnotation(AnnotationExpr annotationExpr) {
        if (!isGeneratedAnnotation(annotationExpr)) {
            return false;
        }

        if (annotationExpr.isSingleMemberAnnotationExpr()) {
            Expression value = annotationExpr.asSingleMemberAnnotationExpr().getMemberValue();
            if (value.isStringLiteralExpr()) {
                return annotationValueMatchesMyBatisGenerator(value.asStringLiteralExpr());
            }
        } else if (annotationExpr.isNormalAnnotationExpr()) {
            return annotationExpr.asNormalAnnotationExpr().getPairs().stream()
                    .filter(JavaMergeUtilities::isValuePair)
                    .map(MemberValuePair::getValue)
                    .filter(Expression::isStringLiteralExpr)
                    .map(Expression::asStringLiteralExpr)
                    .findFirst()
                    .map(JavaMergeUtilities::annotationValueMatchesMyBatisGenerator)
                    .orElse(false);
        }

        return false;
    }

    private static boolean hasDoNotDeleteComment(AnnotationExpr annotationExpr) {
        // check the comments value for the do_not_delete marker string
        if (annotationExpr.isSingleMemberAnnotationExpr()) {
            // no comments in a single member annotation - only the single "value" member"
            return false;
        } else if (annotationExpr.isNormalAnnotationExpr()) {
            return annotationExpr.asNormalAnnotationExpr().getPairs().stream()
                    .filter(JavaMergeUtilities::isCommentsPair)
                    .map(MemberValuePair::getValue)
                    .filter(Expression::isStringLiteralExpr)
                    .map(Expression::asStringLiteralExpr)
                    .findFirst()
                    .map(StringLiteralExpr::asString)
                    .map(s -> s.contains(MergeConstants.DO_NOT_DELETE_DURING_MERGE))
                    .orElse(false);
        }

        return false;
    }

    private static boolean isGeneratedAnnotation(AnnotationExpr annotationExpr) {
        String annotationName = annotationExpr.getNameAsString();
        // Check for @Generated annotation (both javax and jakarta packages)
        return "Generated".equals(annotationName) //$NON-NLS-1$
                || "javax.annotation.Generated".equals(annotationName) //$NON-NLS-1$
                || "jakarta.annotation.Generated".equals(annotationName); //$NON-NLS-1$
    }

    private static boolean isValuePair(MemberValuePair pair) {
        return pair.getName().asString().equals("value"); //$NON-NLS-1$
    }

    private static boolean isCommentsPair(MemberValuePair pair) {
        return pair.getName().asString().equals("comments"); //$NON-NLS-1$
    }

    private static boolean annotationValueMatchesMyBatisGenerator(StringLiteralExpr expr) {
        return expr.asString().equals(MyBatisGenerator.class.getName());
    }

    public static GeneratedType checkForGeneratedJavadocTag(BodyDeclaration<?> member) {
        return member.getComment()
                .map(Comment::getContent)
                .map(JavaMergeUtilities::checkJavadocTag)
                .orElse(GeneratedType.NOT_GENERATED);
    }

    // Check if the comment contains any of the javadoc tags
    private static GeneratedType checkJavadocTag(String comment) {
        for (String tag : MergeConstants.getOldElementTags()) {
            if (comment.contains(tag)) {
                if (comment.contains(MergeConstants.DO_NOT_DELETE_DURING_MERGE)) {
                    return GeneratedType.GENERATED_KEEP;
                } else {
                    return GeneratedType.GENERATED_REMOVE;
                }
            }
        }
        return GeneratedType.NOT_GENERATED;
    }

    private static TypeDeclaration<?> findMainTypeDeclaration(CompilationUnit compilationUnit,
                                                              MergeFileType mergeFileType) throws MergeException {
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
        if (firstType == null) {
            throw new MergeException(getString("RuntimeError.29", mergeFileType.toString())); //$NON-NLS-1$
        }
        return firstType;
    }

    public static ParseResults parseAndFindMainTypeDeclaration(JavaParser javaParser, String source,
                                                               MergeFileType mergeFileType) throws MergeException {
        ParseResult<CompilationUnit> parseResult = javaParser.parse(source);

        // little hack to pull the result out of the lambda. This allows us to avoid "orElseThrow()" later on
        @Nullable CompilationUnit[] compilationUnits = new CompilationUnit [1];
        parseResult.ifSuccessful(cu -> compilationUnits[0] = cu);

        if (compilationUnits[0] == null) {
            List<String> details = parseResult.getProblems().stream()
                    .map(Problem::toString)
                    .toList();
            throw new MergeException(getString("RuntimeError.28", mergeFileType.toString()), details); //$NON-NLS-1$
        }

        return new ParseResults(compilationUnits[0], findMainTypeDeclaration(compilationUnits[0], mergeFileType));
    }

    public static void deleteDuplicateMemberIfExists(TypeDeclaration<?> newTypeDeclaration, BodyDeclaration<?> member) {
        newTypeDeclaration.getMembers().stream()
                .filter(td -> membersMatch(td, member))
                .findFirst()
                .ifPresent(newTypeDeclaration::remove);
    }
}
