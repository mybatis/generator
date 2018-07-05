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
package org.mybatis.generator.codegen;

/**
 * Constants for MyBatis XML IDs.
 * 
 * @author Jeff Butler
 */
public class XmlConstants {

    /**
     * Utility Class, no instances.
     */
    private XmlConstants() {
        super();
    }

    public static final String MYBATIS3_MAPPER_SYSTEM_ID =
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd"; //$NON-NLS-1$

    public static final String MYBATIS3_MAPPER_PUBLIC_ID =
            "-//mybatis.org//DTD Mapper 3.0//EN"; //$NON-NLS-1$

    public static final String MYBATIS3_MAPPER_CONFIG_SYSTEM_ID =
            "http://mybatis.org/dtd/mybatis-3-config.dtd"; //$NON-NLS-1$

    public static final String MYBATIS3_MAPPER_CONFIG_PUBLIC_ID =
            "-//mybatis.org//DTD Config 3.0//EN"; //$NON-NLS-1$

    public static final String MYBATIS_GENERATOR_CONFIG_SYSTEM_ID =
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd"; //$NON-NLS-1$

    public static final String MYBATIS_GENERATOR_CONFIG_PUBLIC_ID =
            "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"; //$NON-NLS-1$
}
