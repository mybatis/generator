/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

/**
 * This class is used to specify a renaming rule for table's domain object name.
 * If domainObjectName is not configured, we'll build the domain object named
 * based on the tableName or runtimeTableName. And then we use the domain object
 * renaming rule to generate the final domain object name.
 * 
 * <p>For example, if some tables are named:
 * 
 * <ul>
 * <li>SYS_USER</li>
 * <li>SYS_ROLE</li>
 * <li>SYS_FUNCTIONS</li>
 * </ul>
 * 
 * <p>it might be annoying to have the generated domain name all containing the SYS
 * prefix. This class can be used to remove the prefix by specifying
 * 
 * <ul>
 * <li>searchString="^Sys"</li>
 * <li>replaceString=""</li>
 * </ul>
 * 
 * <p>Note that internally, the generator uses the
 * <code>java.util.regex.Matcher.replaceAll</code> method for this function. See
 * the documentation of that method for example of the regular expression
 * language used in Java.
 * 
 * @author liuzh
 * 
 */
public class DomainObjectRenamingRule {
    private String searchString;
    private String replaceString;

    public String getReplaceString() {
        return replaceString;
    }

    public void setReplaceString(String replaceString) {
        this.replaceString = replaceString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(searchString)) {
            errors.add(getString("ValidationError.28", tableName)); //$NON-NLS-1$
        }
    }
}
