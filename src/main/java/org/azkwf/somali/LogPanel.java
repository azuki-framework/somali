package org.azkwf.somali;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LogPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 7261770600290219865L;

	private JSplitPane pnlSplit;

	private DefaultTableModel model;
	private JTable tblLog;
	private JScrollPane sclLog;

	public LogPanel() {
		setLayout(null);

		model = new DefaultTableModel();
		tblLog = new JTable(model);
		sclLog = new JScrollPane(tblLog);

		pnlSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pnlSplit.setLocation(0, 0);
		pnlSplit.setDividerLocation(240);
		pnlSplit.setTopComponent(sclLog);
		pnlSplit.setBottomComponent(new JPanel());
		add(pnlSplit);
		
		model.addColumn("Date");
		model.addColumn("User");
		model.addColumn("Data");

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				doResize();
			}

			public void componentResized(ComponentEvent e) {
				doResize();
			}

			private void doResize() {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				pnlSplit.setSize(width, height);
			}
		});
	}
	
	public void add() {
	}
}
