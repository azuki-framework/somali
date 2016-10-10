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
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.azkwf.somali.method.MethodCall;

public class MethodTreeView extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 563777696996408432L;

	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	private JScrollPane scroll;

	public MethodTreeView() {
		setLayout(new BorderLayout());

		root = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(root);
		tree = new JTree(model);
		scroll = new JScrollPane(tree);

		tree.setRootVisible(false);

		add(BorderLayout.CENTER, scroll);
	}

	public void setMethodCall(final MethodCall mc) {
		MutableTreeNode node = new DefaultMutableTreeNode(mc);

		root.add(node);

		addMethodCall(node, mc.getMethodCalls());

		model.reload();
	}

	private void addMethodCall(final MutableTreeNode parent, List<MethodCall> children) {

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

	private void visitAll(JTree tree, TreePath parent, boolean expand) {
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
