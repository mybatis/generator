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
package org.mybatis.generator.internal.util;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class holds methods useful for constructing custom classloaders.
 * 
 * @author Jeff Butler
 * 
 */
public class ClassloaderUtility {

    /**
     * Utility Class - No Instances.
     */
    private ClassloaderUtility() {
    }

    public static ClassLoader getCustomClassloader(Collection<String> entries) {
        List<URL> urls = new ArrayList<>();
        File file;

        if (entries != null) {
            for (String classPathEntry : entries) {
                file = new File(classPathEntry);
                if (!file.exists()) {
                    throw new RuntimeException(getString(
                            "RuntimeError.9", classPathEntry)); //$NON-NLS-1$
                }

                try {
                    urls.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    // this shouldn't happen, but just in case...
                    throw new RuntimeException(getString(
                            "RuntimeError.9", classPathEntry)); //$NON-NLS-1$
                }
            }
        }

        ClassLoader parent = Thread.currentThread().getContextClassLoader();

        URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[urls
                .size()]), parent);

        return ucl;
    }
}
