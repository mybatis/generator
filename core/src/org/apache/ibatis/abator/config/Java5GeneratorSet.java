/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.abator.config;

import org.apache.ibatis.abator.internal.java.dao.GenericCIJava5DAOGenerator;
import org.apache.ibatis.abator.internal.java.dao.GenericSIJava5DAOGenerator;
import org.apache.ibatis.abator.internal.java.dao.IbatisJava5DAOGenerator;
import org.apache.ibatis.abator.internal.java.dao.SpringJava5DAOGenerator;
import org.apache.ibatis.abator.internal.java.model.JavaModelGeneratorJava5Impl;
import org.apache.ibatis.abator.internal.sqlmap.SqlMapGeneratorIterateImpl;
import org.apache.ibatis.abator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * @author Jeff Butler
 *
 */
public class Java5GeneratorSet extends GeneratorSet {
    
    /**
     * 
     */
    public Java5GeneratorSet() {
        super();
        super.javaModelGeneratorType = JavaModelGeneratorJava5Impl.class.getName();
        super.javaTypeResolverType = JavaTypeResolverDefaultImpl.class.getName();
        super.sqlMapGeneratorType = SqlMapGeneratorIterateImpl.class.getName();
    }

    public String translateDAOGeneratorType(String configurationType) {
        String answer;
        
        if ("IBATIS".equalsIgnoreCase(configurationType)) { //$NON-NLS-1$
            answer = IbatisJava5DAOGenerator.class.getName();
        } else if ("SPRING".equalsIgnoreCase(configurationType)) { //$NON-NLS-1$
            answer = SpringJava5DAOGenerator.class.getName();
        } else if ("GENERIC-CI".equalsIgnoreCase(configurationType)) { //$NON-NLS-1$
            answer = GenericCIJava5DAOGenerator.class.getName();
        } else if ("GENERIC-SI".equalsIgnoreCase(configurationType)) { //$NON-NLS-1$
            answer = GenericSIJava5DAOGenerator.class.getName();
        } else {
            answer = configurationType;
        }
        
        return answer;
    }
}
