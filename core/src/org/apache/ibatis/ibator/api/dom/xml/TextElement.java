/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.ibatis.ibator.api.dom.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.ibator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class TextElement extends Element {
    private String content;

    /**
     *  
     */
    public TextElement(String content) {
        super();
        this.content = content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.ibator.api.dom.xml.Element#getFormattedContent(int)
     */
    @Override
    public String getFormattedContent(int indentLevel) {
        return formatLongString(content, 100, indentLevel);
    }

    /**
     * Utility method. Takes a long string and breaks it into multiple lines of
     * whose width is no longer that the specified maximum line length.
     * 
     * @param s
     *            the String to be formatted
     * @param maxLineLength
     *            the maximum line length
     * @param indentLevel
     *            the required indent level of all lines
     * @return the formatted String
     */
    private static String formatLongString(String s, int maxLineLength,
            int indentLevel) {

        StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(s);
        if (sb.length() <= maxLineLength || s.indexOf(' ') == -1) {
            // line is short, or has no spaces - return as is
            return sb.toString();
        }
        
        List<String> lines = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s, " "); //$NON-NLS-1$
        
        sb.setLength(0);
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(st.nextToken());
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (sb.length() + token.length() + 1 > maxLineLength) {
                lines.add(sb.toString());
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, indentLevel + 1);
                sb.append(token);
            } else {
                sb.append(' ');
                sb.append(token);
            }
        }

        if (sb.toString().trim().length() > 0) {
            lines.add(sb.toString());
        }

        sb.setLength(0);
        Iterator<String> iter = lines.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        return sb.toString();
    }
}
