/*
 *    Copyright 2006-2023 the original author or authors.
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
 *
 */
/**
 * Classes in this package, and the logging sub packages, are virtual copies of their counterpart classes
 * in the base MyBatis Generator code.  We need to duplicate them in the Eclipse plugin Ant runner
 * because of some complexities in plugin class loading.
 * <p>
 * Classes in the antsupport JAR are loaded by the Ant classloader - which is a child of the plugin classloader.
 * 
 * Only the Ant classloader will have the logging implementations that are added to the launch configuration
 * classpath.
 * 
 * Any class that uses one of the optional logging implementations must also be loaded by the Ant classloader.
 * If we use the versions from the base library (loaded by the plugin classloader), they will not be able to see
 * the implementation JARs.
 * 
 * All of this combines to mean that we need the logging implementations that have optional dependencies
 * to be loaded by the Ant classloader, rather than the plugin classloader - so we need them in this source
 * JAR rather than the main plugin JAR.
 * <p>
 * At least this is my best understanding of the issue at this time :)   
 */
package org.mybatis.generator.eclipse.ui.ant.logging;
