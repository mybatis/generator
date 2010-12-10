/*
 *  Copyright 2010 The MyBatis Team
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
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.mybatis.generator.api.dom.OutputUtilities;

public class InitializationBlock {

    private boolean isStatic;
    private List<String> bodyLines;
    private List<String> javaDocLines;

    public InitializationBlock() {
        this(false);
    }

    public InitializationBlock(boolean isStatic) {
        this.isStatic = isStatic;
        bodyLines = new ArrayList<String>();
        javaDocLines = new ArrayList<String>();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }

    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }
    
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        for (String javaDocLine : javaDocLines) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }

        OutputUtilities.javaIndent(sb, indentLevel);

        if (isStatic) {
            sb.append("static "); //$NON-NLS-1$
        }

        sb.append('{');
        indentLevel++;

        ListIterator<String> listIter = bodyLines.listIterator();
        while (listIter.hasNext()) {
            String line = listIter.next();
            if (line.startsWith("}")) { //$NON-NLS-1$
                indentLevel--;
            }

            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(line);

            if ((line.endsWith("{") && !line.startsWith("switch")) //$NON-NLS-1$ //$NON-NLS-2$
                    || line.endsWith(":")) { //$NON-NLS-1$
                indentLevel++;
            }

            if (line.startsWith("break")) { //$NON-NLS-1$
                // if the next line is '}', then don't outdent
                if (listIter.hasNext()) {
                    String nextLine = listIter.next();
                    if (nextLine.startsWith("}")) { //$NON-NLS-1$
                        indentLevel++;
                    }

                    // set back to the previous element
                    listIter.previous();
                }
                indentLevel--;
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }
}
