package org.mybatis.generator.maven;

import org.apache.maven.plugin.logging.Log;

public class MavenLogImpl implements org.mybatis.generator.logging.Log {
	private final Log mavenLog;

	MavenLogImpl(Log log) {
		mavenLog = log;
	}

	public boolean isDebugEnabled() {
		return mavenLog.isDebugEnabled();
	}

	public void error(String s, Throwable e) {
		mavenLog.error(s, e);
	}

	public void error(String s) {
		mavenLog.error(s);
	}

	public void debug(String s) {
		mavenLog.debug(s);
	}

	public void warn(String s) {
		mavenLog.warn(s);
	}

}
