package org.unical.neuralnetwork.gui.pathselector;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.unical.neuralnetwork.gui.MainView;

public class PathSelector extends JFrame {

	private static final long serialVersionUID = 867013563921717763L;
	private MainView mv;
	private TableModelAdapter model;
	private JPanel containerPanel;
	private JLabel preable;
	private JTable pathTable;
	private JButton selectButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PathSelector(null, null);
			}
		});
	}

	public PathSelector(MainView mv, TableModelAdapter model) {
		super();
		this.mv = mv;
		this.model = model;
		setVisible(true);
		setContentPane(getContainerPanel());
		setResizable(false);
		setBounds(new Rectangle(100, 200, 500, 400));
		if (mv != null) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	public JPanel getContainerPanel() {
		if (containerPanel == null) {
			containerPanel = new JPanel();
			containerPanel.setBorder(BorderFactory.createEmptyBorder());
			containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
			containerPanel.add(getPreable());
			containerPanel.add(new JScrollPane(getPathTable()));
			containerPanel.add(getSelectButton());
		}
		return containerPanel;
	}

	public JLabel getPreable() {
		if (preable == null) {
			preable = new JLabel("Selezionare la traccia da visualizzare:");
			//preable.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return preable;
	}

	public JTable getPathTable() {
		if (pathTable == null) {
			if (model != null) {
				pathTable = new JTable(model);
			} else {
				pathTable = new JTable(new DefaultTableModel());
			}
			pathTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					selectButton.setEnabled(true);
				}
			});
		}
		return pathTable;
	}

	public JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton("Seleziona");
			selectButton.setEnabled(false);
			//selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			selectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(mv != null){
						mv.lightPath(pathTable.getSelectedRow());
					}
					dispose();
				}
			});
		}
		return selectButton;
	}
}
