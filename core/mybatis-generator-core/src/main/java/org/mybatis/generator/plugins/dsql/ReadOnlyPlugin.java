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
package org.mybatis.generator.plugins.dsql;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.CompositePlugin;
import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.Indenter;
import org.mybatis.generator.config.Context;

/**
 * Disables delete, insert, delete, and update methods in the MyBatisDynamicSQLV2 runtime.
 *
 * @author Jeff Butler
 */
public class ReadOnlyPlugin extends CompositePlugin {
    private @Nullable Context context;
    private final Properties properties = new Properties();
    private @Nullable CommentGenerator commentGenerator;
    private @Nullable KnownRuntime knownRuntime;
    private @Nullable Indenter indenter;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    @Override
    public void setCommentGenerator(CommentGenerator commentGenerator) {
        this.commentGenerator = commentGenerator;
    }

    @Override
    public void setKnownRuntime(KnownRuntime knownRuntime) {
        this.knownRuntime = knownRuntime;
    }

    @Override
    public void setIndenter(Indenter indenter) {
        this.indenter = indenter;
    }

    @Override
    public boolean validate(List<String> warnings) {
        addPlugin(initializePlugin(new DisableDeletePlugin()));
        addPlugin(initializePlugin(new DisableInsertPlugin()));
        addPlugin(initializePlugin(new DisableUpdatePlugin()));

        return true;
    }

    private Plugin initializePlugin(Plugin plugin) {
        plugin.setContext(Objects.requireNonNull(context));
        plugin.setProperties(properties);
        plugin.setKnownRuntime(Objects.requireNonNull(knownRuntime));
        plugin.setCommentGenerator(Objects.requireNonNull(commentGenerator));
        plugin.setIndenter(Objects.requireNonNull(indenter));
        return plugin;
    }
}
