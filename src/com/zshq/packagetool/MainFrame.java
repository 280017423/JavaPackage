package com.zshq.packagetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author zou.sq
 * @version v1.0.0
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 7682167834179504188L;

	public static void main(String[] args) {
		int frameHeight = 250, frameWidth = 500;
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

		MainFrame mainFrame = new MainFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(frameWidth, frameHeight);
		mainFrame.setLocation((screenDimension.width - frameWidth) / 2, (screenDimension.height - frameHeight) / 2);
		mainFrame.setTitle("Aiikis Android打包工具");

		MainPanel mainPanel = new MainPanel();

		mainFrame.setBackground(Color.white);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(mainPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);

	}

}
