package org.unical.neuralnetwork.gui.builder;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BuildingProgress extends JFrame {

	private static final long serialVersionUID = -1408673584927745269L;
	private JLabel label;
	private JProgressBar progress;
	private JPanel contentPane;
	
	public static void main(String[] args) {
		new BuildingProgress();
	}

	public void update(String status, int value) {
		label.setText(status);
		int p = progress.getValue() + value;
		progress.setValue(p);
		if(p >= 100)
			dispose();
	}

	public BuildingProgress() {
		setBounds(100, 200, 300, 60);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		contentPane = new JPanel();
		label = new JLabel(" ");
		progress = new JProgressBar(0, 100);
		progress.setValue(0);
		progress.setStringPainted(true);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(label);
		contentPane.add(progress);
		setContentPane(contentPane);
		setVisible(true);
	}
}
