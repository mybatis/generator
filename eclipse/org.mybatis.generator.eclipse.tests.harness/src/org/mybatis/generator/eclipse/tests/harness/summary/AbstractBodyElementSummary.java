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
package org.mybatis.generator.eclipse.tests.harness.summary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a base class for Annotations, Classes, Enums, and Interfaces.
 * They all have a name and fields.  They can also contain inner Annotations,
 * Classes, Enums, and Interfaces.
 * 
 * @author Jeff Butler
 *
 */
public abstract class AbstractBodyElementSummary extends AbstractSummary {

    private Map<String, FieldSummary> fields = new HashMap<String, FieldSummary>();
    private String name;

    public FieldSummary getField(String fieldName) {
        return fields.get(fieldName);
    }

    public int getFieldCount() {
        return fields.size();
    }

    public String getName() {
        return name;
    }
    
    protected abstract static class AbstractBodyElementSummaryBuilder<T extends AbstractBodyElementSummaryBuilder<T>> extends AbstractSummaryBuilder<T> {

        protected T withFields(List<FieldSummary> fields) {
            for (FieldSummary field : fields) {
                summary().fields.put(field.getName(), field);
            }
            return getThis();
        }

        protected T withName(String name) {
            summary().name = name;
            return getThis();
        }

        @Override
        protected abstract AbstractBodyElementSummary summary();
    }
}
