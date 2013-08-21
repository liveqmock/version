package com.win.knowledge.ui;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TreeTest extends JFrame {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1028914580550820581L;

	@Autowired
	public TreeTest(ProjectTree tree) {
		this.setSize(400, 600);
		getContentPane().add(tree);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
