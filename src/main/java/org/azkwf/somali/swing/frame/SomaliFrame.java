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
package org.azkwf.somali.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;


import org.azkwf.somali.parser.LogParser;
import org.azkwf.somali.parser.LogParserConfig;
import org.azkwf.somali.parser.LogParserEvent;
import org.azkwf.somali.parser.LogParserListener;
import org.azkwf.somali.parser.SimpleLogParser;
import org.azkwf.somali.record.LogRecord;
import org.azkwf.somali.swing.component.LogListView;
import org.azkwf.somali.swing.component.LogListViewListener;
import org.azkwf.somali.swing.component.LogTextView;
import org.azkwf.somali.swing.component.StatusBar;
import org.azkwf.somali.swing.dialog.ConfigDialogEvent;
import org.azkwf.somali.swing.dialog.ConfigDialogListener;
import org.azkwf.somali.swing.dialog.LogReadConfig;
import org.azkwf.somali.swing.dialog.LogReadDialog;
import org.azkwf.somali.swing.dialog.MethodLogDialog;
import org.azkwf.somali.task.LogParseTask;
import org.azkwf.somali.task.Task;
import org.azkwf.somali.task.TaskManager;

public class SomaliFrame extends JFrame {

    /** serialVersionUID */
    private static final long serialVersionUID = 2391588458952219307L;

    private static final Pattern PTN_METHOD_START = Pattern.compile("^>>> (.*)$", Pattern.MULTILINE | Pattern.DOTALL);

    //
    private JMenuBar menubar;

    private JMenu menuFile;

    private JMenu menuEdit;

    private JMenu menuView;

    private JMenu menuHelp;

    private JMenuItem menuFileRead;

    private JMenuItem menuFileExit;

    private JMenuItem menuViewMethod;

    //
    private StatusBar statusbar;

    private JLabel lblStatusMessage;

    private JProgressBar progressbar;

    //
    private JSplitPane splitSub;

    private LogListView table;

    private LogTextView text;

    private List<LogRecord> logs;

    private Boolean exitFlag;

    public SomaliFrame() {
        setTitle("Somali");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        logs = new ArrayList<LogRecord>();
        exitFlag = Boolean.FALSE;

        table = new LogListView();
        text = new LogTextView();

        splitSub = new JSplitPane();
        splitSub.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitSub.setTopComponent(table);
        splitSub.setBottomComponent(text);
        add(BorderLayout.CENTER, splitSub);

        // Menu
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        menuFile = new JMenu("ファイル");
        menuEdit = new JMenu("編集");
        menuView = new JMenu("表示");
        menuHelp = new JMenu("ヘルプ");
        menubar.add(menuFile);
        menubar.add(menuEdit);
        menubar.add(menuView);
        menubar.add(menuHelp);
        menuFileRead = new JMenuItem("読込み");
        menuFileExit = new JMenuItem("終了");
        menuViewMethod = new JMenuItem("メソッドツリー");
        menuFile.add(menuFileRead);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);
        menuView.add(menuViewMethod);

        //
        statusbar = new StatusBar();
        add(BorderLayout.SOUTH, statusbar);
        statusbar.setLayout(new BorderLayout());

        lblStatusMessage = new JLabel();
        statusbar.add(BorderLayout.CENTER, lblStatusMessage);
        progressbar = new JProgressBar();
        progressbar.setPreferredSize(new Dimension(160, 0));
        progressbar.setStringPainted(true);
        progressbar.setString("");
        statusbar.add(BorderLayout.EAST, progressbar);

        init();

        addListener();

        setSize(900, 600);
        center();
    }

    private void openReadLogFile() {

        LogReadConfig condition = new LogReadConfig();
        try {
            condition = LogReadConfig.load(new File("condition.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        LogReadDialog dlg = new LogReadDialog(this, condition);
        dlg.addConfigDialogListener(new ConfigDialogListener<LogReadConfig>() {

            @Override
            public void configDialogOk(LogReadConfig config,
                ConfigDialogEvent<LogReadConfig> e) {
                try {
                    LogReadConfig.store(new File("condition.properties"), config);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                parse(config);
            }
        });
        dlg.setVisible(true);
    }

    private void openMethodTree() {
        int index = table.getSelectIndex();
        if (0 <= index) {
            LogRecord log = logs.get(index);

            if (PTN_METHOD_START.matcher(log.getMessage()).find()) {
                MethodLogDialog dlg = new MethodLogDialog(this);
                dlg.refrash(logs, index);
                dlg.setVisible(true);
            }
        } else {

        }
    }

    private void parse(final LogReadConfig config) {
        LogParserConfig configParser = null;
        try {
            configParser = LogParserConfig.load(new File("somali.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        File file = new File(config.getLogFile());

        table.clear();
        logs.clear();

        LogParser parser = new SimpleLogParser(file, configParser);
        parser.addLogParserListener(new LogParserListener() {
            long start = 0;

            long cntTotal = 0;

            long cntHit = 0;

            @Override
            public void logParserLogRecord(final LogRecord record,
                LogParserEvent e) {
                cntTotal++;
                if (null != config.getDateFrom()) {
                    if (record.getDate().getTime() < config.getDateFrom().getTime()) {
                        return;
                    }
                }
                if (null != config.getDateTo()) {
                    if (record.getDate().getTime() > config.getDateTo().getTime()) {
                        return;
                    }
                }
                if (0 < config.getLevel().length()) {
                    if (!record.getLevel().equals(config.getLevel())) {
                        return;
                    }
                }
                if (0 < config.getLogger().length()) {
                    if (-1 == record.getLogger().indexOf(config.getLogger())) {
                        return;
                    }
                }
                if (0 < config.getMessage().length()) {
                    if (-1 == record.getMessage().indexOf(config.getMessage())) {
                        return;
                    }
                }

                cntHit++;
                logs.add(record);

                if (100000 < cntHit) {
                    cntHit--;
                    logs.remove(0);
                }

            }

            @Override
            public void logParserStarted(final LogParserEvent e) {
                start = System.nanoTime();
                setProgressStart("読込み中");
                lblStatusMessage.setText("");
            }

            @Override
            public void logParserFinished(final LogParserEvent e) {
                for (LogRecord log : logs) {
                    table.addLog(log);
                }
                setProgressStop("");
                long end = System.nanoTime();
                String msg = String.format("%d/%d (%.3fsec)", cntHit, cntTotal, ((double) (end - start)) / 1000000000f);
                lblStatusMessage.setText(msg);
            }
        });

        Task task = new LogParseTask(parser);
        TaskManager.getInstance().queue(task);
    }

    private void setProgressStart(final String message) {
        progressbar.setString(message);
        progressbar.setIndeterminate(true);
    }

    private void setProgressStop(final String message) {
        progressbar.setString(message);
        progressbar.setIndeterminate(false);
    }

    private void close() {
        synchronized (exitFlag) {
            if (!exitFlag) {
                exitFlag = Boolean.TRUE;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TaskManager.getInstance().stopWait();
                        dispose();
                    }
                });
                thread.start();
            }
        }
    }

    private void init() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                TaskManager.getInstance().start();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }
        });

        table.addLogListViewListener(new LogListViewListener() {
            @Override
            public void logListViewSelectChanged(List<LogRecord> logs) {
                StringBuffer s = new StringBuffer();
                for (LogRecord log : logs) {
                    s.append(log.getMessage());
                    s.append("\n");
                }
                text.setText(s.toString());
            }
        });

    }

    private void addListener() {
        menuFileRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReadLogFile();
            }
        });
        menuFileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        menuViewMethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMethodTree();
            }
        });
    }

    private void center() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect = env.getMaximumWindowBounds();

        setLocation(rect.x + (rect.width / 2)
            - (getWidth() / 2), rect.y + (rect.height / 2)
            - (getHeight() / 2));
    }
}
