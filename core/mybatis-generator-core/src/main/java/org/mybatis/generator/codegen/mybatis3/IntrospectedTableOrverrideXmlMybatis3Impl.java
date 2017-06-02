package com.bambo.plugin.mybatis;



import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展mybatis generator , 以覆盖xml文件的生成.
 * @author songzj
 * @date 16/4/18-11:22
 */
public class IntrospectedTableOrverrideXmlMybatis3Impl extends IntrospectedTableMyBatis3Impl {

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        ArrayList answer = new ArrayList();
        if(this.xmlMapperGenerator != null) {
            Document document = this.xmlMapperGenerator.getDocument();
            String tmp = context.getProperty("mergeable");
            boolean mergeable = false;
            if("true".equalsIgnoreCase(tmp)){
                mergeable = true;
            }
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.getMyBatis3XmlMapperFileName(), this.getMyBatis3XmlMapperPackage(), this.context.getSqlMapGeneratorConfiguration().getTargetProject(), mergeable, this.context.getXmlFormatter());
            if(this.context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        return answer;
    }
}
