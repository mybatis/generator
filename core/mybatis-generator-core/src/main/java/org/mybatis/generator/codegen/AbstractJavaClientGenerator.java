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
package org.mybatis.generator.codegen;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * This class exists to that Java client generators can specify whether
 * an XML generator is required to match the methods in the
 * Java client.  For example, a Java client built entirely with
 * annotations does not need matching XML.
 *
 * @author Jeff Butler
 */
public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator {

    private final boolean requiresXMLGenerator;

    protected AbstractJavaClientGenerator(String project, boolean requiresXMLGenerator) {
        super(project);
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    /**
     * Returns true is a matching XML generator is required.
     *
     * @return true if matching XML is generator required
     */
    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }

    /**
     * Returns an instance of the XML generator associated
     * with this client generator.
     *
     * @return the matched XML generator.  May return null if no
     *     XML is required by this generator
     */
    public abstract AbstractXmlGenerator getMatchedXMLGenerator();

    /**
     * Processes the rootInterface string to replace generic type placeholders with the actual record type.
     * <p>
     * If the rootInterface contains a generic placeholder like {@code <T>}, {@code <E>}, etc., it will be
     * replaced with the actual record type from the introspected table. For example:
     * <ul>
     *   <li>{@code "BaseMapper<T>"} becomes {@code "BaseMapper<com.example.User>"}</li>
     *   <li>{@code "BaseMapper"} remains {@code "BaseMapper"} (no change for backward compatibility)</li>
     *   <li>{@code "BaseMapper<com.example.User>"} remains unchanged (already fully qualified)</li>
     * </ul>
     *
     * @param rootInterface
     *            the rootInterface string from configuration, may contain generic placeholders
     * @param recordType
     *            the actual record type (fully qualified) to use as replacement
     * @return the processed rootInterface string with placeholders replaced, or the original string if no
     *         replacement is needed
     */
    protected String processRootInterfaceWithGenerics(String rootInterface, String recordType) {
        if (!stringHasValue(rootInterface) || !stringHasValue(recordType)) {
            return rootInterface;
        }

        // Check if rootInterface contains generic brackets
        int openBracketIndex = rootInterface.indexOf('<');
        if (openBracketIndex == -1) {
            // No generic brackets, return as-is for backward compatibility
            return rootInterface;
        }

        int closeBracketIndex = rootInterface.lastIndexOf('>');
        if (closeBracketIndex == -1 || closeBracketIndex <= openBracketIndex) {
            // Malformed generic brackets, return as-is
            return rootInterface;
        }

        String baseInterface = rootInterface.substring(0, openBracketIndex);
        String genericContent = rootInterface.substring(openBracketIndex + 1, closeBracketIndex).trim();

        // Check if the generic content is a placeholder (single letter or single letter with extends clause)
        // Common Java generic type parameter names: T, E, K, V, N, U, R, S
        if (isGenericPlaceholder(genericContent)) {
            // Replace placeholder with actual record type
            return baseInterface + "<" + recordType + ">";
        }

        // Already has a concrete type (fully qualified or not), return as-is
        return rootInterface;
    }

    /**
     * Checks if a generic content string represents a placeholder that should be replaced.
     * Placeholders are typically single-letter type parameters like T, E, K, V, etc.,
     * optionally with an "extends" clause like "T extends Entity".
     *
     * @param genericContent
     *            the content between angle brackets
     * @return true if this looks like a placeholder that should be replaced
     */
    private boolean isGenericPlaceholder(String genericContent) {
        if (genericContent.isEmpty()) {
            return false;
        }

        String trimmed = genericContent.trim();
        
        // Simple case: just a single uppercase letter (T, E, K, V, etc.)
        if (trimmed.length() == 1 && Character.isUpperCase(trimmed.charAt(0))) {
            return true;
        }

        // Case with bounds: "T extends SomeClass" or "T super SomeClass"
        // Pattern: single uppercase letter followed by " extends " or " super "
        int extendsIndex = trimmed.indexOf(" extends ");
        int superIndex = trimmed.indexOf(" super ");
        
        if (extendsIndex > 0 || superIndex > 0) {
            int boundIndex = extendsIndex > 0 ? extendsIndex : superIndex;
            // First part should be a single uppercase letter
            String typeParam = trimmed.substring(0, boundIndex).trim();
            if (typeParam.length() == 1 && Character.isUpperCase(typeParam.charAt(0))) {
                return true;
            }
        }

        return false;
    }
}
