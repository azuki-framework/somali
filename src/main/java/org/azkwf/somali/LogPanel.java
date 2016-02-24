package org.azkwf.somali;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class LogPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 7261770600290219865L;

	private JSplitPane pnlSplit;

	public LogPanel() {
		pnlSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		
		add(pnlSplit);
		
	}
}
