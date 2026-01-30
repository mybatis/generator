/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.merge.xml;

import org.mybatis.generator.merge.MergeTestCase;

public class OldElementsShouldBeDeleted implements MergeTestCase<OldElementsShouldBeDeleted> {
    @Override
    public String existingContent(String parameter) {
        return """
               <?xml version="1.0" encoding="UTF-8"?>
               <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
               <mapper>
                 <select id="abatorgenerated_select" />
                 <select id="ibatorgenerated_select" />
                 <select id="oldway1">
                   <!-- @ibatorgenerated -->
                 </select>
                 <select id="oldway2">
                   <!-- @abatorgenerated -->
                 </select>
                 <select id="oldway3">
                   <!-- @mbggenerated -->
                 </select>
                 <select id="oldway4">
                   <!-- @mbg.generated -->
                 </select>
                 <select id="customSelect" />
               </mapper>
               """;
    }

    @Override
    public String newContent(String parameter) {
        return """
               <?xml version="1.0" encoding="UTF-8"?>
               <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
               <mapper>
                 <select id="newway">
                   <!-- @mbg.generated -->
                 </select>
               </mapper>
               """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return """
               <?xml version="1.0" encoding="UTF-8"?>
               <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
               <mapper>
                 <select id="newway">
                   <!-- @mbg.generated -->
                 </select>
                 <select id="customSelect" />
               </mapper>""";
    }

    @Override
    public OldElementsShouldBeDeleted self() {
        return this;
    }
}
