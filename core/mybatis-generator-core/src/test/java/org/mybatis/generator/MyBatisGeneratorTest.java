package org.mybatis.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisGeneratorTest {

    @Test
    public void testGenerateIbatis2Java2() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(
                this.getClass().getClassLoader().getResourceAsStream("CreateDbIbatis2Java2.sql"),
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
        
        scriptRunner.executeScript();
        
        File file = new File("target/generated-sources/mbgenerator/ibatis2/java2");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("ibatorConfigIbatis2Java2.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            
        myBatisGenerator.generate(null, null, null);
    }

    @Test
    public void testGenerateIbatis2Java5() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(
                this.getClass().getClassLoader().getResourceAsStream("CreateDbIbatis2Java5.sql"),
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
        
        scriptRunner.executeScript();
        
        File file = new File("target/generated-sources/mbgenerator/ibatis2/java5");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("ibatorConfigIbatis2Java5.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            
        myBatisGenerator.generate(null, null, null);
    }
    
    @Test
    public void testGenerateMyBatis3() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(
                this.getClass().getClassLoader().getResourceAsStream("CreateDbMyBatis3.sql"),
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
        
        scriptRunner.executeScript();
        
        File file = new File("target/generated-sources/mbgenerator/mybatis3");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigMyBatis3.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            
        myBatisGenerator.generate(null, null, null);
    }
}
