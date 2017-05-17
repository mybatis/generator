/**
 *    Copyright 2009-2015 the original author or authors.
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
/**
 * Classes in this package, and the logging sub packages, are virtual copies of their counterpart classes
 * in the base MyBatis Generator code.  We need to duplicate them in the Eclipse plugin Ant runner
 * because of some complexities in plugin class loading.
 * <p>
 * We don't want the optional logging frameworks to always be on the plugin classpath.  And the plugin
 * classpath cannot be altered at runtime.  And the plugin classloader is a parent of the Ant classloader.
 * All of this combines to mean that we need the logging implementations that have optional dependencies
 * to be loaded by the Ant classloader, rather than the plugin classloader - so we need them in this source
 * JAR rather than the main plugin JAR.
 * <p>
 * At least this is my best understanding of the issue at this time :)   
 */
package org.mybatis.generator.eclipse.ui.ant.logging;
