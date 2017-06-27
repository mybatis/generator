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
package org.mybatis.generator.codegen.ibatis2;

/**
 * Introspected table implementation for iBatis targeting Java versions 1.5 or later.
 * 
 * @author Jeff Butler
 * 
 */
public class IntrospectedTableIbatis2Java5Impl extends
        IntrospectedTableIbatis2Java2Impl {
    @Override
    public boolean isJava5Targeted() {
        return true;
    }
}
