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
package org.mybatis.generator.internal.db;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;

/**
 * This class holds the actual catalog, schema, and table name returned from the
 * database introspection.
 * 
 * @author Jeff Butler
 * 
 */
public class ActualTableName {

    private String tableName;
    private String catalog;
    private String schema;
    private String fullName;

    public ActualTableName(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        fullName = composeFullyQualifiedTableName(catalog,
                schema, tableName, '.');
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ActualTableName)) {
            return false;
        }

        return obj.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public String toString() {
        return fullName;
    }
}
