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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;

/**
 * 
 * @author Jeff Butler
 */
public abstract class AbstractJavaProviderMethodGenerator extends
        AbstractGenerator {

    protected static final FullyQualifiedJavaType NEW_BUILDER_IMPORT = new FullyQualifiedJavaType("org.apache.ibatis.jdbc.SQL"); //$NON-NLS-1$
    protected boolean useLegacyBuilder;
    protected final String builderPrefix;
    
    public AbstractJavaProviderMethodGenerator(boolean useLegacyBuilder) {
        super();
        this.useLegacyBuilder = useLegacyBuilder;
        if (useLegacyBuilder) {
        	builderPrefix = ""; //$NON-NLS-1$
        } else {
        	builderPrefix = "sql."; //$NON-NLS-1$
        }
    }
    
    public abstract void addClassElements(TopLevelClass topLevelClass);
}
