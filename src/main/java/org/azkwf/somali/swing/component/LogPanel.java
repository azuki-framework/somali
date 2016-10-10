/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkwf.somali.swing.component;

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
