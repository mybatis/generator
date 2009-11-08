package org.apache.ibatis.ibator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.Ibator;
import org.apache.ibatis.ibator.api.ProgressCallback;
import org.apache.ibatis.ibator.api.VerboseProgressCallback;
import org.apache.ibatis.ibator.config.IbatorConfiguration;
import org.apache.ibatis.ibator.config.xml.IbatorConfigurationParser;
import org.apache.ibatis.ibator.internal.DefaultShellCallback;
import org.junit.Test;

public class IbatisGenerationTest {

    @Test
    public void testGenerateIbatis2Java2() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(
                this.getClass().getClassLoader().getResourceAsStream("CreateDbIbatis2Java2.sql"),
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
        
        scriptRunner.executeScript();
        
        File file = new File("target/generated-sources/ibator/ibatis2/java2");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        IbatorConfigurationParser cp = new IbatorConfigurationParser(
                warnings);
        IbatorConfiguration config = cp.parseIbatorConfiguration(this.getClass().getClassLoader().getResourceAsStream("ibatorConfigIbatis2Java2.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        Ibator ibator = new Ibator(config, shellCallback, warnings);
            
        ProgressCallback progressCallback = new VerboseProgressCallback();
            
        ibator.generate(progressCallback, null, null);
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
        
        File file = new File("target/generated-sources/ibator/ibatis2/java5");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        IbatorConfigurationParser cp = new IbatorConfigurationParser(
                warnings);
        IbatorConfiguration config = cp.parseIbatorConfiguration(this.getClass().getClassLoader().getResourceAsStream("ibatorConfigIbatis2Java5.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        Ibator ibator = new Ibator(config, shellCallback, warnings);
            
        ProgressCallback progressCallback = new VerboseProgressCallback();
            
        ibator.generate(progressCallback, null, null);
    }
    
    @Test
    public void testGenerateIbatis3() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(
                this.getClass().getClassLoader().getResourceAsStream("CreateDbIbatis3.sql"),
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
        
        scriptRunner.executeScript();
        
        File file = new File("target/generated-sources/ibator/ibatis3");
        file.mkdirs();
        
        List<String> warnings = new ArrayList<String>();
        IbatorConfigurationParser cp = new IbatorConfigurationParser(
                warnings);
        IbatorConfiguration config = cp.parseIbatorConfiguration(this.getClass().getClassLoader().getResourceAsStream("ibatorConfigIbatis3.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            
        Ibator ibator = new Ibator(config, shellCallback, warnings);
            
        ProgressCallback progressCallback = new VerboseProgressCallback();
            
        ibator.generate(progressCallback, null, null);
    }
}
