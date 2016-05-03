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
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This plugin adds a cache element to generated sqlMaps.  This plugin
 * is for MyBatis3 targeted runtimes only.  The plugin accepts the
 * following properties (all are optional):
 * 
 * cache_eviction
 * cache_flushInterval
 * cache_size
 * cache_readOnly
 * cache_type
 * 
 * All properties correspond to properties of the MyBatis cache element and
 * are passed "as is" to the corresponding properties of the generated cache
 * element.  All properties can be specified at the table level, or on the
 * plugin element.  The property on the table element will override any
 * property on the plugin element.
 * 
 * @author Jason Bennett
 * @author Jeff Butler
 */
public class CachePlugin extends PluginAdapter {
    public enum CacheProperty {
        EVICTION("cache_eviction", "eviction"), //$NON-NLS-1$ //$NON-NLS-2$
        FLUSH_INTERVAL("cache_flushInterval", "flushInterval"), //$NON-NLS-1$ //$NON-NLS-2$
        READ_ONLY("cache_readOnly", "readOnly"), //$NON-NLS-1$ //$NON-NLS-2$
        SIZE("cache_size", "size"), //$NON-NLS-1$ //$NON-NLS-2$
        TYPE("cache_type", "type"); //$NON-NLS-1$ //$NON-NLS-2$
        
        private String propertyName;
        private String attributeName;
        
        CacheProperty(String propertyName, String attributeName) {
            this.propertyName = propertyName;
            this.attributeName = attributeName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getAttributeName() {
            return attributeName;
        }
    }
    
    public CachePlugin() {
        super();
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        XmlElement element = new XmlElement("cache"); //$NON-NLS-1$
        context.getCommentGenerator().addComment(element);

        for (CacheProperty cacheProperty : CacheProperty.values()) {
            addAttributeIfExists(element, introspectedTable, cacheProperty);
        }
        
        document.getRootElement().addElement(element);

        return true;
    }
    
    private void addAttributeIfExists(XmlElement element, IntrospectedTable introspectedTable,
            CacheProperty cacheProperty) {
        String property = introspectedTable.getTableConfigurationProperty(cacheProperty.getPropertyName());
        if (property == null) {
            property = properties.getProperty(cacheProperty.getPropertyName());
        }
        
        if (StringUtility.stringHasValue(property)) {
            element.addAttribute(new Attribute(cacheProperty.getAttributeName(), property));
        }
    }
}
