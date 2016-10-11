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
import java.awt.Component;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.azkwf.somali.method.MethodCall;
import org.azkwf.somali.util.SomaliUtil;

public class MethodTreeView extends JPanel {

    /** serialVersionUID */
    private static final long serialVersionUID = 563777696996408432L;

    private JTree tree;

    private DefaultMutableTreeNode root;

    private DefaultTreeModel model;

    private JScrollPane scroll;

    private ImageIcon iconSuccess;

    private ImageIcon iconError;

    public MethodTreeView() {
        setLayout(new BorderLayout());

        root = new DefaultMutableTreeNode();
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        scroll = new JScrollPane(tree);

        tree.setRootVisible(false);

        iconSuccess = new ImageIcon(this.getClass().getResource("/success.png"));
        iconError = new ImageIcon(this.getClass().getResource("/error.png"));
        TreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                Object value,
                boolean selected,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (null == c) {
                    c = new JLabel(value.toString());
                }

                JLabel label = (JLabel) c;
                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    MethodCall method = (MethodCall) node.getUserObject();
                    if (null != method) {
                        if (SomaliUtil.isNotEmpty(method.getException())) {
                            label.setIcon(iconError);
                        } else {
                            label.setIcon(iconSuccess);
                        }
                    } else {
                        label.setIcon(iconError);
                    }
                }
                return label;
            }
        };
        tree.setCellRenderer(renderer);

        add(BorderLayout.CENTER, scroll);
    }

    public JTree getTree() {
        return tree;
    }

    public void setMethodCall(final MethodCall mc) {
        addMethodCall(root, mc.getMethodCalls());
        model.reload();
    }

    private void addMethodCall(final MutableTreeNode parent,
        List<MethodCall> children) {

        for (MethodCall mc : children) {

            MutableTreeNode node = new DefaultMutableTreeNode(mc);

            parent.insert(node, parent.getChildCount());

            addMethodCall(node, mc.getMethodCalls());

        }
    }

    public void expandAll() {
        expandAll(tree);
    }

    private void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    private void collapseAll(JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    private void visitAll(JTree tree,
        TreePath parent,
        boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (!node.isLeaf() && node.getChildCount() >= 0) {
            Enumeration e = node.children();
            while (e.hasMoreElements()) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                visitAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
