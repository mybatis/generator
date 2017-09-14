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

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class InsertReturnIdPlugin extends PluginAdapter {

    public boolean validate(List<String> warnings) {
        return true;
    }
    
    private void enableRetrunId(XmlElement element, IntrospectedTable introspectedTable)
    {
		element.addAttribute(new Attribute("useGeneratedKeys", "true"));
		element.addAttribute(new Attribute("keyProperty", 
                introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty()));
    }

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		enableRetrunId(element, introspectedTable);
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		enableRetrunId(element, introspectedTable);
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}

    
}
