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
package org.azkwf.somali.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * このクラスは、コンフィグダイアログを実装する基底クラスです。
 * 
 * @author Kawakicchi
 */
public abstract class ConfigDialog<CONFIG> extends JDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = -2449312977427490099L;

	private List<ConfigDialogListener<CONFIG>> listeners;

	private JPanel pnlClient;
	private JPanel pnlControl;
	private JButton btnOk;
	private JButton btnCancel;

	public ConfigDialog(final JFrame parent) {
		super(parent, true);

		listeners = new ArrayList<ConfigDialogListener<CONFIG>>();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		pnlClient = new JPanel();
		pnlControl = new JPanel();

		add(BorderLayout.CENTER, pnlClient);
		add(BorderLayout.SOUTH, pnlControl);

		// Controll
		btnOk = new JButton("OK");
		btnCancel = new JButton("キャンセル");

		btnOk.setSize(120, 24);
		btnCancel.setSize(120, 24);

		// Controll
		pnlControl.setPreferredSize(new Dimension(0, 30));
		pnlControl.setLayout(null);
		pnlControl.add(btnOk);
		pnlControl.add(btnCancel);

		pnlControl.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = pnlControl.getInsets();
				int width = pnlControl.getWidth() - (insets.left + insets.right);
				int height = pnlControl.getHeight() - (insets.top + insets.bottom);
				btnOk.setLocation(width - (btnOk.getWidth()), 2);
				btnCancel.setLocation(width - (btnCancel.getWidth() + btnOk.getWidth()), 2);
			}
		});

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}
		});

		pnlClient.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Insets insets = pnlClient.getInsets();
				int width = pnlClient.getWidth() - (insets.left + insets.right);
				int height = pnlClient.getHeight() - (insets.top + insets.bottom);
				doResize(pnlClient);
			}
		});

		doInit(pnlClient);
	}

	protected abstract boolean doValidate();

	protected abstract CONFIG getConfig();

	protected abstract void doInit(final JPanel client);

	protected abstract void doResize(final JPanel client);

	public final synchronized void addConfigDialogListener(final ConfigDialogListener<CONFIG> listener) {
		listeners.add(listener);
	}

	protected final JPanel getClient() {
		return pnlClient;
	}

	private void close() {
		dispose();
	}

	private void doOk() {
		if (doValidate()) {

			CONFIG config = getConfig();

			synchronized (listeners) {
				for (ConfigDialogListener<CONFIG> l : listeners) {
					l.configDialogOk(config);
				}
			}

			close();
		}
	}

	private void doCancel() {
		close();
	}
}
