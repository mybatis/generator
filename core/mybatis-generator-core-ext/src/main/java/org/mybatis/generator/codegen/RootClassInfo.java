/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.codegen;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * Holds information about a class (uses the JavaBeans Introspector to find properties).
 * @author Jeff Butler
 * 
 */
public class RootClassInfo {

    private static Map<String, RootClassInfo> rootClassInfoMap;

    static {
        rootClassInfoMap = Collections
                .synchronizedMap(new HashMap<String, RootClassInfo>());
    }

    public static RootClassInfo getInstance(String className,
            List<String> warnings) {
        RootClassInfo classInfo = rootClassInfoMap.get(className);
        if (classInfo == null) {
            classInfo = new RootClassInfo(className, warnings);
            rootClassInfoMap.put(className, classInfo);
        }

        return classInfo;
    }

    /**
     * Clears the internal map containing root class info.  This method should be called at the beginning of
     * a generation run to clear the cached root class info in case there has been a change.
     * For example, when using the eclipse launcher, the cache would be kept until eclipse
     * was restarted.
     * 
     */
    public static void reset() {
        rootClassInfoMap.clear();
    }

    private PropertyDescriptor[] propertyDescriptors;
    private String className;
    private List<String> warnings;
    private boolean genericMode = false;

    private RootClassInfo(String className, List<String> warnings) {
        super();
        this.className = className;
        this.warnings = warnings;

        if (className == null) {
            return;
        }

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(className);
        String nameWithoutGenerics = fqjt.getFullyQualifiedNameWithoutTypeParameters();
        if (!nameWithoutGenerics.equals(className)) {
            genericMode = true;
        }

        try {
            Class<?> clazz = ObjectFactory.externalClassForName(nameWithoutGenerics);
            BeanInfo bi = Introspector.getBeanInfo(clazz);
            propertyDescriptors = bi.getPropertyDescriptors();
        } catch (Exception e) {
            propertyDescriptors = null;
            warnings.add(getString("Warning.20", className)); //$NON-NLS-1$
        }
    }

    public boolean containsProperty(IntrospectedColumn introspectedColumn) {
        if (propertyDescriptors == null) {
            return false;
        }

        boolean found = false;
        String propertyName = introspectedColumn.getJavaProperty();
        String propertyType = introspectedColumn.getFullyQualifiedJavaType()
                .getFullyQualifiedName();

        // get method names from class and check against this column definition.
        // better yet, have a map of method Names. check against it.
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];

            if (propertyDescriptor.getName().equals(propertyName)) {
                // property name is in the rootClass...

                // Is it the proper type?
                String introspectedPropertyType = propertyDescriptor.getPropertyType().getName();
                if (genericMode && introspectedPropertyType.equals("java.lang.Object")) { //$NON-NLS-1$
                    // OK - but add a warning
                    warnings.add(getString("Warning.28", //$NON-NLS-1$
                            propertyName, className));
                } else if (!introspectedPropertyType.equals(propertyType)) {
                    warnings.add(getString("Warning.21", //$NON-NLS-1$
                            propertyName, className, propertyType));
                    break;
                }

                // Does it have a getter?
                if (propertyDescriptor.getReadMethod() == null) {
                    warnings.add(getString("Warning.22", //$NON-NLS-1$
                            propertyName, className));
                    break;
                }

                // Does it have a setter?
                if (propertyDescriptor.getWriteMethod() == null) {
                    warnings.add(getString("Warning.23", //$NON-NLS-1$
                            propertyName, className));
                    break;
                }

                found = true;
                break;
            }
        }

        return found;
    }
}
