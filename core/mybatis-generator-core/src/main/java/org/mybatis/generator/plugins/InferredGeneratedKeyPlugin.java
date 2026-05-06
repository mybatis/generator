package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.GeneratedKey;

public class InferredGeneratedKeyPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        // if there is already a generated key, do not override it
        if (introspectedTable.getGeneratedKey().isPresent()) {
            return;
        }

        // we only do this for single primary keys
        if (introspectedTable.getPrimaryKeyColumns().size() != 1) {
            return;
        }

        var column = introspectedTable.getPrimaryKeyColumns().get(0);

        if (!column.isGeneratedColumn()) {
            return;
        }

        GeneratedKey generatedKey = new GeneratedKey(column.getActualColumnName(), "JDBC", true);

//        introspectedTable.setGeneratedKey(generatedKey);
    }
}
