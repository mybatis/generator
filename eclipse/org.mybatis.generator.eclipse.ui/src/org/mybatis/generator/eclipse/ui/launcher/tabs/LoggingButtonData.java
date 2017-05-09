package org.mybatis.generator.eclipse.ui.launcher.tabs;

public enum LoggingButtonData {

    DEFAULT(true, "Default"),
    SLF4J(false, "SLF4J"),
    LOG4J2(false, "Log4j 2"),
    LOG4J(false, "Log4j"),
    JDK(false, "JDK Standard");
    
    private String displayText;
    private boolean isDefault;
    
    private LoggingButtonData(boolean isDefault, String displayText) {
        this.isDefault = isDefault;
        this.displayText = displayText;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public String displayText() {
        return displayText;
    }
}
