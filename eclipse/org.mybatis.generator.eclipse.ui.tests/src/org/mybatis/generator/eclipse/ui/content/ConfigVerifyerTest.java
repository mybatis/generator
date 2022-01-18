package org.mybatis.generator.eclipse.ui.content;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

public class ConfigVerifyerTest {
    
    @Test
    public void testValidFile() {
        ConfigVerifyer cf = new ConfigVerifyer((File) null);
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/ui/content/GoodConfigFile.xml");
        
        assertThat(cf.isConfigFile(resource), is(true));
    }

    @Test
    public void testInvalidFile() {
        ConfigVerifyer cf = new ConfigVerifyer((File) null);
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/ui/content/NotAConfigFile.xml");
        
        assertThat(cf.isConfigFile(resource), is(false));
    }
}
