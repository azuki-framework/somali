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
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.azkwf.somali.method.MethodCall;
import org.azkwf.somali.record.LogRecord;
import org.azkwf.somali.swing.component.MethodTreeView;

public class MethodLogDialog extends JDialog {

    /** serialVersionUID */
    private static final long serialVersionUID = 2334085603000353145L;

    private static final Pattern PTN_METHOD_START = Pattern.compile("^>>> (.*)$");

    private static final Pattern PTN_METHOD_END = Pattern.compile("^<<< (.*)$");

    private static final Pattern PTN_ARGUMENT = Pattern.compile("^ argument\\[([0-9]+)\\] = (.*)$",
                                                                Pattern.MULTILINE | Pattern.DOTALL);

    private static final Pattern PTN_RETURN = Pattern.compile("^ return = (.*)$", Pattern.MULTILINE | Pattern.DOTALL);

    private static final Pattern PTN_THROW = Pattern.compile("^ throw (.*)$", Pattern.MULTILINE | Pattern.DOTALL);

    private MethodTreeView tree;

    private JSplitPane splitMain;

    private JSplitPane splitSub;

    private List<LogRecord> logs;

    private JTextPane txtLog;

    public MethodLogDialog(final Frame parent) {
        super(parent, true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtLog = new JTextPane();
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLog.setEditable(false);

        tree = new MethodTreeView();

        splitSub = new JSplitPane();
        splitSub.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitSub.setDividerLocation(200);
        splitSub.setTopComponent(new JLabel());
        splitSub.setBottomComponent(new JScrollPane(txtLog));

        splitMain = new JSplitPane();
        splitMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        splitMain.setLeftComponent(tree);
        splitMain.setRightComponent(splitSub);
        splitMain.setDividerLocation(320);
        add(BorderLayout.CENTER, splitMain);

        tree.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = tree.getTree().getSelectionPath();
                if (path != null) {
                    int count = path.getPathCount();
                    Object[] data = path.getPath();

                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data[count - 1];

                    MethodCall method = (MethodCall) node.getUserObject();

                    StringBuffer s = new StringBuffer();
                    for (int i = method.getIndexStart(); i <= method.getIndexEnd(); i++) {
                        LogRecord log = logs.get(i);

                        if (0 < s.length()) {
                            s.append("\n");
                        }

                        s.append(log.toString());
                    }
                    txtLog.setText(s.toString());
                    txtLog.setCaretPosition(0);

                }

            }
        });

        setSize(860, 600);
        center();
    }

    public void refrash(final List<LogRecord> logs) {
        refrash(logs, 0);
    }

    public void refrash(final List<LogRecord> logs,
        final int index) {
        this.logs = logs;

        tree.setMethodCall(create(logs, index));
        tree.expandAll();
    }

    private MethodCall create(final List<LogRecord> logs) {
        return create(logs, 0);
    }

    private MethodCall create(final List<LogRecord> logs,
        int index) {
        MethodCall method = new MethodCall();
        create(method, logs, index);

        return method;
    }

    private int create(final MethodCall parent,
        final List<LogRecord> logs,
        final int index) {
        int indexBuf = index;
        Matcher m = null;

        // >>> method
        m = PTN_METHOD_START.matcher(logs.get(indexBuf).getMessage());
        if (!m.find()) {
            return indexBuf;
        }
        Date start = logs.get(indexBuf).getDate();
        MethodCall method = new MethodCall(m.group(1));
        method.setIndexStart(indexBuf);
        parent.addMethodCall(method);

        indexBuf++;

        // argument[9] =
        while (indexBuf < logs.size()) {
            m = PTN_ARGUMENT.matcher(logs.get(indexBuf).getMessage());
            if (m.find()) {
                method.addArgument(m.group(2));
                indexBuf++;
            } else {
                break;
            }
        }

        while (indexBuf < logs.size()) {
            m = PTN_METHOD_START.matcher(logs.get(indexBuf).getMessage());
            if (m.find()) {
                // >>>
                indexBuf = create(method, logs, indexBuf);
            } else {
                // <<< method
                m = PTN_METHOD_END.matcher(logs.get(indexBuf).getMessage());
                if (m.find()) {
                    Date end = logs.get(indexBuf).getDate();
                    method.setTime(end.getTime() - start.getTime());
                    indexBuf++;
                    break;
                } else {
                    // info log?
                    indexBuf++;
                }
            }
        }

        m = PTN_RETURN.matcher(logs.get(indexBuf).getMessage());
        if (m.find()) {
            method.setResult(m.group(1));
            method.setIndexEnd(indexBuf);
            return indexBuf + 1;
        }
        m = PTN_THROW.matcher(logs.get(indexBuf).getMessage());
        if (m.find()) {
            method.setException(m.group(1));
            method.setIndexEnd(indexBuf);
            return indexBuf + 1;
        }

        return indexBuf + 1;
    }

    private void center() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect = env.getMaximumWindowBounds();

        setLocation(rect.x + (rect.width / 2)
            - (getWidth() / 2), rect.y + (rect.height / 2)
            - (getHeight() / 2));
    }
}
