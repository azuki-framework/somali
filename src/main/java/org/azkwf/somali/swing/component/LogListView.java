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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.azkwf.somali.record.LogRecord;

/**
 * このクラスは、ログ情報を表示するリストビュークラスです。
 * 
 * @author Kawakicchi
 */
public class LogListView extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 6875088439896991496L;

	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scroll;

	private final List<LogListViewListener> listeners;

	private final List<LogRecord> logs;

	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	public LogListView() {
		setLayout(new BorderLayout());

		listeners = new ArrayList<LogListViewListener>();
		logs = new ArrayList<LogRecord>();

		model = new DefaultTableModel() {
			/** serialVersionUID */
			private static final long serialVersionUID = -370990479152285062L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		scroll = new JScrollPane(table);

		add(BorderLayout.CENTER, scroll);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}

				List<LogRecord> list = new ArrayList<LogRecord>();
				int sc = table.getSelectedRowCount();
				if (0 < sc) {
					for (int row : table.getSelectedRows()) {
						int index = table.convertRowIndexToModel(row);
						list.add(logs.get(index));
					}
				}
				synchronized (listeners) {
					for (LogListViewListener l : listeners) {
						l.logListViewSelectChanged(list);
					}
				}
			}
		});

		model.addColumn("Date");
		model.addColumn("Level");
		model.addColumn("Logger");
		model.addColumn("Message");

		TableCellRenderer cellRenderer = new HorizontalAlignmentTableRenderer();
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(172);
		columnModel.getColumn(1).setPreferredWidth(52);
		columnModel.getColumn(2).setPreferredWidth(240);
		columnModel.getColumn(3).setPreferredWidth(400);

		columnModel.getColumn(0).setCellRenderer(cellRenderer);
		columnModel.getColumn(1).setCellRenderer(cellRenderer);
		columnModel.getColumn(2).setCellRenderer(cellRenderer);
		columnModel.getColumn(3).setCellRenderer(cellRenderer);
	}

	public synchronized void addLogListViewListener(final LogListViewListener listener) {
		listeners.add(listener);
	}

	public int getSelectIndex() {
		return table.getSelectedRow();
	}

	public void clear() {
		logs.clear();

		model.setRowCount(0);
	}

	public void addLog(final LogRecord log) {
		logs.add(log);

		Object[] cells = new Object[4];
		cells[0] = FORMAT_DATE.format(log.getDate());
		cells[1] = log.getLevel();
		cells[2] = log.getLogger();
		cells[3] = log.getMessage();

		model.addRow(cells);
	}

	private static class HorizontalAlignmentTableRenderer extends DefaultTableCellRenderer {
		private static final Color COLOR_ERROR = new Color(242, 222, 222);
		private static final Color COLOR_WARN = new Color(252, 248, 227);
		private static final Color COLOR_INFO = new Color(217, 237, 247);

		// 223,240,216

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (c instanceof JLabel) {
				JLabel label = (JLabel) c;
				int orgColumn = table.convertColumnIndexToModel(column);

				if (1 == orgColumn) {
					label.setHorizontalAlignment(SwingConstants.CENTER);
				} else {
					label.setHorizontalAlignment(SwingConstants.LEFT);
				}

				if (!isSelected) {

					String v = table.getModel().getValueAt(row, 1).toString();

					if ("ERROR".equals(v)) {
						label.setBackground(COLOR_ERROR);
					} else if ("WARN".equals(v)) {
						label.setBackground(COLOR_WARN);
					} else if ("INFO".equals(v)) {
						label.setBackground(COLOR_INFO);
					} else {
						label.setBackground(null);
					}
				}
			}

			return c;
		}

	}
}
