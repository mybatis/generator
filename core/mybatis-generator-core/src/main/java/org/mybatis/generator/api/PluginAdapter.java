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
package org.mybatis.generator.api;

import java.util.Objects;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.config.Context;

/**
 * This class is a convenient base class for implementing plugins.
 *
 * <p>This adapter does not implement the <code>validate</code> method - all plugins
 * must perform validation.
 *
 * @author Jeff Butler
 */
public abstract class PluginAdapter implements Plugin {
    protected @Nullable Context context;
    protected final Properties properties = new Properties();
    protected @Nullable CommentGenerator commentGenerator;
    protected @Nullable KnownRuntime knownRuntime;
    protected @Nullable Indenter indenter;

    protected PluginAdapter() {
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return Objects.requireNonNull(context);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    @Override
    public void setCommentGenerator(CommentGenerator commentGenerator) {
        this.commentGenerator = commentGenerator;
    }

    protected CommentGenerator getCommentGenerator() {
        return Objects.requireNonNull(commentGenerator);
    }

    @Override
    public void setKnownRuntime(KnownRuntime knownRuntime) {
        this.knownRuntime = knownRuntime;
    }

    protected KnownRuntime getKnownRuntime() {
        return Objects.requireNonNull(knownRuntime);
    }

    @Override
    public void setIndenter(Indenter indenter) {
        this.indenter = indenter;
    }

    protected Indenter getIndenter() {
        return Objects.requireNonNull(indenter);
    }
}
