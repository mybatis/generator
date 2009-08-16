/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.apache.ibatis.ibator.build;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * This class is used in the Ibator build to force emma to dump
 * coverage information to disk.  This is needed because emma
 * doesn't normally dump until a JVM exit and there are several
 * places where emma is run during the build - and most data is
 * lost if not forced to dump.  BTW - this seems like a bug in emma!
 * <p>
 * This class is not intended to be used by clients.  The class uses
 * reflection to dump emma so that the build is not dependent on emma.
 * 
 * @author Jeff Butler
 *
 */
public class EmmaDumperTask extends Task {
    
    private String fileName;

    @Override
    public void execute() throws BuildException {
        try {
            File file = new File(fileName);
            
            //com.vladium.emma.rt.RT.dumpCoverageData(file, true, false);
            Class<?> clazz = Class.forName("com.vladium.emma.rt.RT"); //$NON-NLS-1$
            Class<?>[] parameters = new Class<?>[3];
            parameters[0] = File.class;
            parameters[1] = boolean.class;
            parameters[2] = boolean.class;
            
            Method method = clazz.getMethod("dumpCoverageData", parameters); //$NON-NLS-1$
            
            Object[] actualParms = new Object[3];
            actualParms[0] = file;
            actualParms[1] = true;
            actualParms[2] = false;
            method.invoke(null, actualParms);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
