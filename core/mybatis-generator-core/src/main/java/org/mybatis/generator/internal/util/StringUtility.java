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
package org.mybatis.generator.internal.util;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.jspecify.annotations.Nullable;

public class StringUtility {

    /**
     * Utility class. No instances allowed
     */
    private StringUtility() {
        super();
    }

    public static @Nullable String trimToNull(@Nullable String s) {
        if (s == null) {
            return null;
        } else {
            var d = s.trim();
            return d.isEmpty() ? null : d;
        }
    }

    public static @Nullable Boolean parseNullableBoolean(@Nullable String s) {
        return s == null ? null : Boolean.valueOf(s);
    }

    public static boolean stringHasValue(@Nullable String s) {
        return s != null && !s.isEmpty();
    }

    public static String composeFullyQualifiedTableName(@Nullable String catalog,
            @Nullable String schema, String tableName, char separator) {
        StringBuilder sb = new StringBuilder();

        if (stringHasValue(catalog)) {
            sb.append(catalog);
            sb.append(separator);
        }

        if (stringHasValue(schema)) {
            sb.append(schema);
            sb.append(separator);
        } else {
            if (!sb.isEmpty()) {
                sb.append(separator);
            }
        }

        sb.append(tableName);

        return sb.toString();
    }

    public static boolean stringContainsSpace(@Nullable String s) {
        return s != null && s.indexOf(' ') != -1;
    }

    public static String escapeStringForJava(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"", true); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) { //$NON-NLS-1$
                sb.append("\\\""); //$NON-NLS-1$
            } else {
                sb.append(token);
            }
        }

        return sb.toString();
    }

    public static String escapeStringForKotlin(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"$", true); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) { //$NON-NLS-1$
                sb.append("\\\""); //$NON-NLS-1$
            } else if ("$".equals(token)) { //$NON-NLS-1$
                sb.append("\\$"); //$NON-NLS-1$
            } else {
                sb.append(token);
            }
        }

        return sb.toString();
    }

    public static boolean isTrue(@Nullable String s) {
        return Boolean.parseBoolean(s);
    }

    public static boolean stringContainsSQLWildcard(@Nullable String s) {
        if (s == null) {
            return false;
        }

        return s.indexOf('%') != -1 || s.indexOf('_') != -1;
    }

    /**
     * Given an input string, tokenize on the commas and trim all token. Returns an empty set if the input string is
     * null.
     *
     * @param in
     *            strong to tokenize.
     *
     * @return Set of tokens
     */
    public static Set<String> tokenize(String in) {
        Set<String> answer = new HashSet<>();
        if (StringUtility.stringHasValue(in)) {
            StringTokenizer st = new StringTokenizer(in, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (!s.isEmpty()) {
                    answer.add(s);
                }
            }
        }
        return answer;
    }

    public static String convertCamelCaseToSnakeCase(String in) {
        if (in.chars().anyMatch(Character::isLowerCase)) {
            return in
                    .replaceAll("([A-Z])(?=[A-Z])", "$1_") //$NON-NLS-1$ //$NON-NLS-2$
                    .replaceAll("([a-z])([A-Z])", "$1_$2") //$NON-NLS-1$ //$NON-NLS-2$
                    .toUpperCase();
        } else {
            // if all upper case, then return the string as is
            return in;
        }
    }
}
