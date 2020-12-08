/*
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.kotlin;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullyQualifiedKotlinType {

    private static final Set<String> AUTOMATIC_KOTLIN_PACKAGES = new HashSet<>();

    static {
        AUTOMATIC_KOTLIN_PACKAGES.add("kotlin"); //$NON-NLS-1$
        AUTOMATIC_KOTLIN_PACKAGES.add("kotlin.collections"); //$NON-NLS-1$
    }

    private String packageName;
    private final List<FullyQualifiedKotlinType> typeArguments = new ArrayList<>();
    private String shortNameWithoutTypeArguments;
    private boolean isExplicitlyImported;

    public FullyQualifiedKotlinType(String fullTypeSpecification) {
        parse(Objects.requireNonNull(fullTypeSpecification).trim());
    }

    public String getPackageName() {
        return packageName;
    }

    public String getShortNameWithoutTypeArguments() {
        return shortNameWithoutTypeArguments;
    }

    public String getShortNameWithTypeArguments() {
        if (typeArguments.isEmpty()) {
            return shortNameWithoutTypeArguments;
        }

        return typeArguments.stream().map(FullyQualifiedKotlinType::getShortNameWithTypeArguments)
                .collect(Collectors.joining(", ", shortNameWithoutTypeArguments //$NON-NLS-1$
                        + "<", ">")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public List<FullyQualifiedKotlinType> getTypeArguments() {
        return typeArguments;
    }

    public void addTypeArgument(FullyQualifiedKotlinType typeArgument) {
        typeArguments.add(typeArgument);
    }

    /**
     * Returns a list of Strings that are the fully qualified names of this type,
     * and any generic type argument associated with this type.
     *
     * @return the import list
     */
    public Set<String> getImportList() {
        Stream<String> thisImport;
        if (isExplicitlyImported) {
            thisImport = Stream.of(packageName + "." + shortNameWithoutTypeArguments); //$NON-NLS-1$
        } else {
            thisImport = Stream.empty();
        }

        Stream<String> ss = typeArguments.stream()
                .map(FullyQualifiedKotlinType::getImportList)
                .flatMap(Set::stream);

        return Stream.of(thisImport, ss)
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    private void parse(String fullTypeSpecification) {
        int index = fullTypeSpecification.indexOf('<');
        if (index == -1) {
            simpleParse(fullTypeSpecification);
        } else {
            simpleParse(fullTypeSpecification.substring(0, index));
            int endIndex = fullTypeSpecification.lastIndexOf('>');
            if (endIndex == -1) {
                throw new RuntimeException(getString("RuntimeError.22", fullTypeSpecification)); //$NON-NLS-1$
            }
            genericParse(fullTypeSpecification.substring(index, endIndex + 1));
        }
    }

    private void simpleParse(String typeSpecification) {
        String baseQualifiedName = typeSpecification.trim();
        if (baseQualifiedName.contains(".")) { //$NON-NLS-1$
            packageName = getPackage(baseQualifiedName);
            shortNameWithoutTypeArguments = baseQualifiedName.substring(packageName.length() + 1);
            isExplicitlyImported = !AUTOMATIC_KOTLIN_PACKAGES.contains(packageName);
        } else {
            shortNameWithoutTypeArguments = baseQualifiedName;
            isExplicitlyImported = false;
            packageName = ""; //$NON-NLS-1$
        }
    }

    private void genericParse(String genericSpecification) {
        int lastIndex = genericSpecification.lastIndexOf('>');
        if (lastIndex == -1) {
            // shouldn't happen - should be caught already, but just in case...
            throw new RuntimeException(getString("RuntimeError.22", genericSpecification)); //$NON-NLS-1$
        }
        String argumentString = genericSpecification.substring(1, lastIndex);
        // need to find "," outside of a <> bounds
        StringTokenizer st = new StringTokenizer(argumentString, ",<>", true); //$NON-NLS-1$
        int openCount = 0;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("<".equals(token)) { //$NON-NLS-1$
                sb.append(token);
                openCount++;
            } else if (">".equals(token)) { //$NON-NLS-1$
                sb.append(token);
                openCount--;
            } else if (",".equals(token)) { //$NON-NLS-1$
                if (openCount == 0) {
                    typeArguments.add(new FullyQualifiedKotlinType(sb.toString()));
                    sb.setLength(0);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }

        if (openCount != 0) {
            throw new RuntimeException(getString("RuntimeError.22", genericSpecification)); //$NON-NLS-1$
        }

        String finalType = sb.toString();
        if (stringHasValue(finalType)) {
            typeArguments.add(new FullyQualifiedKotlinType(finalType));
        }
    }

    /**
     * Returns the package name of a fully qualified type.
     *
     * <p>This method calculates the package as the part of the fully qualified name up
     * to, but not including, the last element. Therefore, it does not support fully
     * qualified inner classes. Not totally fool proof, but correct in most
     * instances.
     *
     * @param baseQualifiedName the base qualified name
     * @return the package
     */
    private static String getPackage(String baseQualifiedName) {
        int index = baseQualifiedName.lastIndexOf('.');
        return baseQualifiedName.substring(0, index);
    }
}
