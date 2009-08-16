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

package org.apache.ibatis.abator.internal.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.abator.config.JDBCConnectionConfiguration;
import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * This class holds methods useful for constructing custom 
 * classloaders in Abator.
 * 
 * @author Jeff Butler
 *
 */
public class ClassloaderUtility {

    /**
     * Utility Class - No Instances 
     */
    private ClassloaderUtility() {
    }

    /**
     * Builds a classloader and adds the specified entries to the
     * classpath.
     * 
     * @param entries a semicolon delimited string of class path entries
     * @return
     */
    public static ClassLoader getCustomClassloader(String entries) {
        if (entries == null) {
            return getCustomClassloader((List) null);
        }
        
        List entryList = new ArrayList();
        StringTokenizer st = new StringTokenizer(entries, ";");
        while (st.hasMoreTokens()) {
            entryList.add(st.nextToken());
        }
        
        return getCustomClassloader(entryList);
    }
    
    public static ClassLoader getCustomClassloader(
            JDBCConnectionConfiguration connectionInformation) {
        return getCustomClassloader(connectionInformation.getClassPathEntries());
    }

    public static ClassLoader getCustomClassloader(List entries) {
        ArrayList urls = new ArrayList();
        File file;

        if (entries != null) {
            Iterator iter = entries.iterator();
            while (iter.hasNext()) {
                String classPathEntry = (String) iter.next();
                file = new File(classPathEntry);
                if (!file.exists()) {
                    throw new RuntimeException(
                        Messages.getString("RuntimeError.9", classPathEntry)); //$NON-NLS-1$
                }

                try {
                    urls.add(file.toURL());
                } catch (MalformedURLException e) {
                    // this shouldn't happen, but just in case...
                    throw new RuntimeException(
                        Messages.getString("RuntimeError.9", classPathEntry)); //$NON-NLS-1$
                }
            }
        }

        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URLClassLoader ucl = new URLClassLoader((URL[]) urls
                .toArray(new URL[urls.size()]), parent);

        return ucl;
    }
}
