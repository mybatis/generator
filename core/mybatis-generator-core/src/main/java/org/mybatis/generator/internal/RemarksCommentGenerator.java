package org.mybatis.generator.internal;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaElement;

import java.util.List;

/**
 * User: aermakov
 * Date: 25.09.2014
 * Time: 10:51
 */
public class RemarksCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        super.addFieldComment(field, introspectedTable, introspectedColumn);
        addRemarksToElement(field, introspectedColumn.getRemarks());
    }

    /**
     * This merhod adds javadoc comment for element based on remarks
     * @param el - java element
     * @param remarks - db remarks
     */
    protected void addRemarksToElement(JavaElement el, String remarks) {
        if (remarks != null && !remarks.trim().isEmpty()) {
            List<String> jdLines = el.getJavaDocLines();
            String[] lines = remarks.split(System.getProperty("line.separator"));
            int i = 1;
            for (String line : lines) {
                jdLines.add(i, " * " + line + "<br>");
                i++;
            }
            jdLines.add(i, " * <br>");
        }

    }
}
