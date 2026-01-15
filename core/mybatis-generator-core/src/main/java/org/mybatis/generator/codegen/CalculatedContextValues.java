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
package org.mybatis.generator.codegen;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.Defaults;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.PluginAggregator;

/**
 * This class holds the intermediate results of the generator, as well as many pre-calculated objects.
 *
 */
public class CalculatedContextValues {
    private final Context context;
    private final List<IntrospectedTable> introspectedTables = new ArrayList<>();
    private final JavaFormatter javaFormatter;
    private final KotlinFormatter kotlinFormatter;
    private final XmlFormatter xmlFormatter;
    private final CommentGenerator commentGenerator;
    private final @Nullable String javaFileEncoding;
    private final @Nullable String kotlinFileEncoding;
    private final PluginAggregator pluginAggregator;
    private final KnownRuntime knownRuntime;
    private final String runtimeBuilderClassName;

    protected CalculatedContextValues(Builder builder) {
        context = Objects.requireNonNull(builder.context);
        javaFormatter = ObjectFactory.createJavaFormatter(context);
        kotlinFormatter = ObjectFactory.createKotlinFormatter(context);
        xmlFormatter = ObjectFactory.createXmlFormatter(context);
        commentGenerator = ObjectFactory.createCommentGenerator(context);
        javaFileEncoding = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING);
        kotlinFileEncoding = context.getProperty(PropertyRegistry.CONTEXT_KOTLIN_FILE_ENCODING);
        Objects.requireNonNull(builder.warnings);

        pluginAggregator = new PluginAggregator();
        context.pluginConfigurations().forEach(pluginConfiguration -> {
            Plugin plugin = ObjectFactory.createPlugin(context, pluginConfiguration, commentGenerator);
            if (plugin.validate(builder.warnings)) {
                pluginAggregator.addPlugin(plugin);
            } else {
                builder.warnings.add(getString("Warning.24", //$NON-NLS-1$
                        pluginConfiguration.getConfigurationType()
                                .orElse("Unknown Plugin Type"), context.getId())); //$NON-NLS-1$
            }
        });

        // this will either be the alias entered in the configuration, or the default if nothing was specified
        String builderAlias = context.getTargetRuntime().orElse(Defaults.DEFAULT_RUNTIME.getAlias());
        // this will either be a successful lookup by alias, or UNKNOWN
        knownRuntime = KnownRuntime.getByAlias(builderAlias);
        if (knownRuntime == KnownRuntime.UNKNOWN) {
            runtimeBuilderClassName = builderAlias;
        } else {
            runtimeBuilderClassName = knownRuntime.getBuilderClassName();
        }
    }

    public Context context() {
        return context;
    }

    public JavaFormatter javaFormatter() {
        return javaFormatter;
    }

    public KotlinFormatter kotlinFormatter() {
        return kotlinFormatter;
    }

    public XmlFormatter xmlFormatter() {
        return xmlFormatter;
    }

    public CommentGenerator commentGenerator() {
        return commentGenerator;
    }

    public @Nullable String javaFileEncoding() {
        return javaFileEncoding;
    }

    public @Nullable String kotlinFileEncoding() {
        return kotlinFileEncoding;
    }

    public PluginAggregator pluginAggregator() {
        return pluginAggregator;
    }

    public void addIntrospectedTables(List<IntrospectedTable> introspectedTables) {
        this.introspectedTables.addAll(introspectedTables);
    }

    public KnownRuntime knownRuntime() {
        return knownRuntime;
    }

    public String runtimeBuilderClassName() {
        return runtimeBuilderClassName;
    }

    public List<IntrospectedTable> introspectedTables() {
        return introspectedTables;
    }

    public static class Builder {
        private @Nullable Context context;
        private @Nullable List<String> warnings;

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        public CalculatedContextValues build() {
            return new CalculatedContextValues(this);
        }
    }
}
