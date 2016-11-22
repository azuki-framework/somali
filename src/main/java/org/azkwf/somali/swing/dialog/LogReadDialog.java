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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Kawakicchi
 */
public class LogReadDialog extends ConfigDialog<LogReadConfig> {

    /** serialVersionUID */
    private static final long serialVersionUID = -3769460374849864027L;

    private static final int MARGIN = 12;

    private static final int SPACE = 4;

    private static final int HEIGHT = 24;

    private JLabel lblLogFile;

    private JTextField txtLogFile;

    private JButton btnLogFile;

    private JLabel lblDate;

    private JTextField txtDateFrom;

    private JLabel lblDateFromTo;

    private JTextField txtDateTo;

    private JLabel lblLevel;

    private JTextField txtLevel;

    private JLabel lblLogger;

    private JTextField txtLogger;

    private JLabel lblMessage;

    private JTextField txtMessage;

    public LogReadDialog(final JFrame parent, final LogReadConfig config) {
        super(parent);
        setTitle("ログファイル読込み");

        if (null != config) {
            txtLogFile.setText(config.getLogFile());
            txtDateFrom.setText(LogReadConfig.toString(config.getDateFrom()));
            txtDateTo.setText(LogReadConfig.toString(config.getDateTo()));
            txtLevel.setText(config.getLevel());
            txtLogger.setText(config.getLogger());
            txtMessage.setText(config.getMessage());
        }

        setSize(600, 240);
        setLocation(parent.getX() + (parent.getWidth() / 2)
            - (this.getWidth() / 2), parent.getY() + (parent.getHeight() / 2)
            - (this.getHeight() / 2));
    }

    public void close() {
        dispose();
    }

    private void doLogFile() {
        JFileChooser filechooser = new JFileChooser();

        int selected = filechooser.showOpenDialog(this);
        if (selected == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            txtLogFile.setText(file.getAbsolutePath());
        } else if (selected == JFileChooser.CANCEL_OPTION) {
        } else if (selected == JFileChooser.ERROR_OPTION) {
        }
    }

    protected boolean doValidate() {
        return true;
    }

    protected LogReadConfig getConfig() {
        LogReadConfig config = new LogReadConfig();
        config.setLogFile(txtLogFile.getText());
        config.setDateFrom(LogReadConfig.toDate(txtDateFrom.getText()));
        config.setDateTo(LogReadConfig.toDate(txtDateTo.getText()));
        config.setLevel(txtLevel.getText());
        config.setLogger(txtLogger.getText());
        config.setMessage(txtMessage.getText());
        return config;
    }

    @Override
    protected void doInit(final JPanel client,
        final LogReadConfig config) {
        client.setLayout(null);
        // LogFile
        lblLogFile = new JLabel("LogFile");
        txtLogFile = new JTextField();
        btnLogFile = new JButton("Select");
        // Date
        lblDate = new JLabel("Date");
        txtDateFrom = new JTextField();
        lblDateFromTo = new JLabel("〜");
        txtDateTo = new JTextField();
        // Level
        lblLevel = new JLabel("Level");
        txtLevel = new JTextField();
        // Logger
        lblLogger = new JLabel("Logger");
        txtLogger = new JTextField();
        // Message
        lblMessage = new JLabel("Message");
        txtMessage = new JTextField();

        // LogFile
        client.add(lblLogFile);
        client.add(txtLogFile);
        client.add(btnLogFile);
        // Date
        client.add(lblDate);
        client.add(txtDateFrom);
        client.add(lblDateFromTo);
        client.add(txtDateTo);
        // Level
        client.add(lblLevel);
        client.add(txtLevel);
        // Logger
        client.add(lblLogger);
        client.add(txtLogger);
        // Message
        client.add(lblMessage);
        client.add(txtMessage);

        int y = MARGIN;
        // LogFile
        lblLogFile.setLocation(MARGIN, y);
        lblLogFile.setSize(80, HEIGHT);
        txtLogFile.setLocation(MARGIN + lblLogFile.getWidth()
            + SPACE, y);
        btnLogFile.setSize(100, HEIGHT);
        // Date
        y += HEIGHT + SPACE
            + 10;
        lblDate.setLocation(MARGIN, y);
        lblDate.setSize(80, HEIGHT);
        txtDateFrom.setLocation(lblDate.getX() + lblDate.getWidth()
            + SPACE, y);
        txtDateFrom.setSize(160, HEIGHT);
        lblDateFromTo.setLocation(txtDateFrom.getX() + txtDateFrom.getWidth(), y);
        lblDateFromTo.setSize(16, HEIGHT);
        txtDateTo.setLocation(lblDateFromTo.getX() + lblDateFromTo.getWidth(), y);
        txtDateTo.setSize(160, HEIGHT);
        // Level
        y += HEIGHT + SPACE;
        lblLevel.setLocation(MARGIN, y);
        lblLevel.setSize(80, HEIGHT);
        txtLevel.setLocation(lblLevel.getX() + lblLevel.getWidth()
            + SPACE, y);
        txtLevel.setSize(100, HEIGHT);
        // Logger
        y += HEIGHT + SPACE;
        lblLogger.setLocation(MARGIN, y);
        lblLogger.setSize(80, HEIGHT);
        txtLogger.setLocation(lblLogger.getX() + lblLogger.getWidth()
            + SPACE, y);
        txtLogger.setSize(300, HEIGHT);
        // Message
        y += HEIGHT + SPACE;
        lblMessage.setLocation(MARGIN, y);
        lblMessage.setSize(80, HEIGHT);
        txtMessage.setLocation(lblMessage.getX() + lblMessage.getWidth()
            + SPACE, y);

        btnLogFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogFile();
            }
        });
    }

    @Override
    protected void doResize(final JPanel client) {
        Insets insets = client.getInsets();
        int width = client.getWidth() - (insets.left + insets.right);
        int height = client.getHeight() - (insets.top + insets.bottom);

        int y = MARGIN;
        txtLogFile.setSize(width - (MARGIN + lblLogFile.getWidth()
            + SPACE
            + SPACE
            + btnLogFile.getWidth() + MARGIN), HEIGHT);
        btnLogFile.setLocation(width - (btnLogFile.getWidth() + MARGIN), y);

        txtMessage.setSize(width - (txtMessage.getX() + MARGIN), HEIGHT);
    }
}
