package org.azkwf.somali;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SomaliFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = 2391588458952219307L;

	public static void main(final String[] args) {
		SomaliFrame frm = new SomaliFrame();
		frm.setVisible(true);
	}

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuFileExit;
	
	private LogPanel pnlLog;

	public SomaliFrame() {
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		pnlLog = new LogPanel();
		pnlLog.setLocation(0, 0);
		add(pnlLog);
		
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
				pnlLog.setSize(width, height - menuBar.getHeight());
			}
		});
	
		setBounds(10, 10, 600, 400);
	}
	
}
