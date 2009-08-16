package org.apache.ibatis.abator.ui.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class ConfigFileEditor extends TextEditor {

	private ColorManager colorManager;

	public ConfigFileEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
