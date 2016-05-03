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
package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

/**
 * This plugin demonstrates adding methods to the example class to enable
 * case-insensitive LIKE searches. It shows hows to construct new methods and
 * add them to an existing class.
 * 
 * This plugin only adds methods for String fields mapped to a JDBC character
 * type (CHAR, VARCHAR, etc.)
 * 
 * @author Jeff Butler
 * 
 */
public class CaseInsensitiveLikePlugin extends PluginAdapter {

    /**
     * 
     */
    public CaseInsensitiveLikePlugin() {
        super();
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {

        InnerClass criteria = null;
        // first, find the Criteria inner class
        for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
            if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) { //$NON-NLS-1$
                criteria = innerClass;
                break;
            }
        }

        if (criteria == null) {
            // can't find the inner class for some reason, bail out.
            return true;
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonBLOBColumns()) {
            if (!introspectedColumn.isJdbcCharacterColumn()
                    || !introspectedColumn.isStringColumn()) {
                continue;
            }

            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(new Parameter(introspectedColumn
                    .getFullyQualifiedJavaType(), "value")); //$NON-NLS-1$

            StringBuilder sb = new StringBuilder();
            sb.append(introspectedColumn.getJavaProperty());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            sb.insert(0, "and"); //$NON-NLS-1$
            sb.append("LikeInsensitive"); //$NON-NLS-1$
            method.setName(sb.toString());
            method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

            sb.setLength(0);
            sb.append("addCriterion(\"upper("); //$NON-NLS-1$
            sb.append(Ibatis2FormattingUtilities
                    .getAliasedActualColumnName(introspectedColumn));
            sb.append(") like\", value.toUpperCase(), \""); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("\");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
            method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

            criteria.addMethod(method);
        }

        return true;
    }
}
