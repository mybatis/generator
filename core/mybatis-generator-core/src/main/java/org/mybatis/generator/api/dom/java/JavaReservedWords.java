/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.api.dom.java;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a list of Java reserved words.
 * 
 * @author Jeff Butler
 * 
 */
public class JavaReservedWords {

    private static Set<String> RESERVED_WORDS;

    static {
        String[] words = { "abstract", //$NON-NLS-1$
                "assert", //$NON-NLS-1$
                "boolean", //$NON-NLS-1$
                "break", //$NON-NLS-1$
                "byte", //$NON-NLS-1$
                "case", //$NON-NLS-1$
                "catch", //$NON-NLS-1$
                "char", //$NON-NLS-1$
                "class", //$NON-NLS-1$
                "const", //$NON-NLS-1$
                "continue", //$NON-NLS-1$
                "default", //$NON-NLS-1$
                "do", //$NON-NLS-1$
                "double", //$NON-NLS-1$
                "else", //$NON-NLS-1$
                "enum", //$NON-NLS-1$
                "extends", //$NON-NLS-1$
                "final", //$NON-NLS-1$
                "finally", //$NON-NLS-1$
                "float", //$NON-NLS-1$
                "for", //$NON-NLS-1$
                "goto", //$NON-NLS-1$
                "if", //$NON-NLS-1$
                "implements", //$NON-NLS-1$
                "import", //$NON-NLS-1$
                "instanceof", //$NON-NLS-1$
                "int", //$NON-NLS-1$
                "interface", //$NON-NLS-1$
                "long", //$NON-NLS-1$
                "native", //$NON-NLS-1$
                "new", //$NON-NLS-1$
                "package", //$NON-NLS-1$
                "private", //$NON-NLS-1$
                "protected", //$NON-NLS-1$
                "public", //$NON-NLS-1$
                "return", //$NON-NLS-1$
                "short", //$NON-NLS-1$
                "static", //$NON-NLS-1$
                "strictfp", //$NON-NLS-1$
                "super", //$NON-NLS-1$
                "switch", //$NON-NLS-1$
                "synchronized", //$NON-NLS-1$
                "this", //$NON-NLS-1$
                "throw", //$NON-NLS-1$
                "throws", //$NON-NLS-1$
                "transient", //$NON-NLS-1$
                "try", //$NON-NLS-1$
                "void", //$NON-NLS-1$
                "volatile", //$NON-NLS-1$
                "while" //$NON-NLS-1$
        };

        RESERVED_WORDS = new HashSet<String>(words.length);

        for (String word : words) {
            RESERVED_WORDS.add(word);
        }
    }

    public static boolean containsWord(String word) {
        boolean rc;

        if (word == null) {
            rc = false;
        } else {
            rc = RESERVED_WORDS.contains(word);
        }

        return rc;
    }

    /**
     * Utility class - no instances allowed
     */
    private JavaReservedWords() {
    }
}
