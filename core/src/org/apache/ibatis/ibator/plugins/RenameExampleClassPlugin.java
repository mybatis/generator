/*
 *  Copyright 2008 The Apache Software Foundation
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

package org.apache.ibatis.ibator.plugins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * This plugin demonstrates overriding the initialized() method 
 * to rename the generated example classes.  Instead of xxxExample,
 * the classes will be named xxxCriteria 
 * 
 * This plugin accepts two properties:
 * <ul>
 *   <li><tt>searchString</tt> (required) the regular expression of the name search.</li>
 *   <li><tt>replaceString</tt> (required) the replacement String.</li>
 * </ul>
 * 
 * For example, to change the name of the generated Example classes from
 * xxxExample to xxxCriteria, specify the following:
 * 
 * <dl>
 *   <dt>searchString</dt>
 *   <dd>Example$</dd>
 *   <dt>replaceString</dt>
 *   <dd>Criteria</dd>
 * </dl>
 *   
 * 
 * @author Jeff Butler
 *
 */
public class RenameExampleClassPlugin extends IbatorPluginAdapter {
    private String searchString;
    private String replaceString;
    private Pattern pattern;

    /**
     * 
     */
    public RenameExampleClassPlugin() {
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.ibator.api.IbatorPlugin#validate(java.util.List)
     */
    public boolean validate(List<String> warnings) {
        
        searchString = properties.getProperty("searchString");
        replaceString = properties.getProperty("replaceString");
        
        boolean valid = StringUtility.stringHasValue(searchString)
            && StringUtility.stringHasValue(replaceString);
        
        if (valid) {
            pattern = Pattern.compile(searchString);
        } else {
            if (!StringUtility.stringHasValue(searchString)) {
                warnings.add(Messages.getString("Warning.27"));
            }
            if (!StringUtility.stringHasValue(replaceString)) {
                warnings.add(Messages.getString("Warning.28"));
            }
        }
        
        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType oldType = (FullyQualifiedJavaType)
            introspectedTable.getAttribute(IntrospectedTable.ATTR_EXAMPLE_TYPE);
        
        String typeName = oldType.getFullyQualifiedName();
        Matcher matcher = pattern.matcher(typeName);
        typeName = matcher.replaceAll(replaceString);
        
        introspectedTable.setAttribute(IntrospectedTable.ATTR_EXAMPLE_TYPE,
                new FullyQualifiedJavaType(typeName));
    }
}
