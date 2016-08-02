package org.unical.neuralnetwork.gui.builder;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.unical.neuralnetwork.gui.MainView;
import org.unical.neuralnetwork.logic.ModelBuilder;

public class ModelBuilderView extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ModelBuilderView(null);
			}
		});
	}

	private static final long serialVersionUID = 7631890618259826270L;
	private MainView mainView;
	private JLabel preamble;
	private JPanel contentPane;
	private JPanel actionsPane;
	// per la form
	private JLabel mapLabel;
	private JLabel trainingLabel;
	private JLabel typeLabel;
	private JLabel simulationLabel;
	private JTextField mapTxf;
	private JTextField trainingTxf;
	private JTextField simulationTxf;
	private JComboBox<ModelBuilder.NNType> typeCb;
	private JButton find1;
	private JButton find2;
	private JButton find3;
	private JButton finish;
	// logica
	private ModelBuilder modelBuilder = new ModelBuilder();

	public ModelBuilderView(MainView mw) {
		mainView = mw;
		setBounds(new Rectangle(100, 200, 500, 200));
		if (mw == null) {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(getActionsPane());
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(contentPane);
		setResizable(false);
		setVisible(true);
	}

	public JLabel getPreamble() {
		if (preamble == null) {
			preamble = new JLabel("Proprietà del progetto");
		}
		return preamble;
	}

	public JPanel getActionsPane() {
		if (actionsPane == null) {
			actionsPane = new JPanel();
			actionsPane.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 0;
			actionsPane.add(getPreamble(), gbc);
			
			JLabel tmp1 = getMapLabel();
			JComponent tmp2 = getMapTxf();
			JButton tmp3 = getFind1();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.LINE_END;
			actionsPane.add(tmp1, gbc);
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp2, gbc);
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp3, gbc);
			
			tmp1 = getTrainingLabel();
			tmp2 = getTrainingTxf();
			tmp3 = getFind2();
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.LINE_END;
			actionsPane.add(tmp1, gbc);
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp2, gbc);
			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp3, gbc);
			
			tmp1 = getTypeLabel();
			tmp2 = getTypeCb();
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.LINE_END;
			actionsPane.add(tmp1, gbc);
			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp2, gbc);
			
			tmp1 = getSimuLationLabel();
			tmp2 = getSimulationTxf();
			tmp3 = getFind3();
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.LINE_END;
			actionsPane.add(tmp1, gbc);
			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp2, gbc);
			gbc.gridx = 2;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.LINE_START;
			actionsPane.add(tmp3, gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 5;
			actionsPane.add(getFinish(), gbc);
		}
		return actionsPane;
	}

	private JButton getFind3() {
		if (find3 == null) {
			find3 = new JButton("Sfoglia");
			ModelBuilderView mbv = this;
			find3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter ff = new FileNameExtensionFilter("SUMO tcl", "tcl");
					fc.setFileFilter(ff);
					fc.showOpenDialog(mbv);
					File f = fc.getSelectedFile();
					modelBuilder.setSs(f);
					if (f != null) {
						simulationTxf.setText(f.getAbsolutePath());
					} else {
						simulationTxf.setText("");
					}
					updateFinish();
				}
			});
		}
		return find3;
	}

	private JComponent getSimulationTxf() {
		if (simulationTxf == null) {
			simulationTxf = new JTextField(20);
			simulationTxf.setEditable(false);
		}
		return simulationTxf;
	}

	private JLabel getSimuLationLabel() {
		if(simulationLabel == null){
			simulationLabel = new JLabel("Simulation Set:");
		}
		return simulationLabel;
	}

	public JLabel getMapLabel() {
		if (mapLabel == null) {
			mapLabel = new JLabel("File mappa:");
		}
		return mapLabel;
	}

	public JLabel getTrainingLabel() {
		if (trainingLabel == null) {
			trainingLabel = new JLabel("Training Set:");
		}
		return trainingLabel;
	}

	public JLabel getTypeLabel() {
		if (typeLabel == null) {
			typeLabel = new JLabel("Tipo:");
		}
		return typeLabel;
	}

	public JTextField getMapTxf() {
		if (mapTxf == null) {
			mapTxf = new JTextField(20);
			mapTxf.setEditable(false);
		}
		return mapTxf;
	}

	public JTextField getTrainingTxf() {
		if (trainingTxf == null) {
			trainingTxf = new JTextField(20);
			trainingTxf.setEditable(false);
		}
		return trainingTxf;
	}

	public JComboBox<ModelBuilder.NNType> getTypeCb() {
		if (typeCb == null) {
			typeCb = new JComboBox<>(ModelBuilder.NNType.values());
			modelBuilder.setType(ModelBuilder.NNType.values()[0]);
			typeCb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelBuilder.setType((ModelBuilder.NNType) typeCb.getSelectedItem());
					updateFinish();
				}
			});
		}
		return typeCb;
	}

	public JButton getFind1() {
		if (find1 == null) {
			find1 = new JButton("Sfoglia");
			ModelBuilderView mbv = this;
			find1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter ff = new FileNameExtensionFilter("SUMO File", "xml");
					fc.setFileFilter(ff);
					fc.showOpenDialog(mbv);
					File f = fc.getSelectedFile();
					modelBuilder.setMap(f);
					if (f != null) {
						mapTxf.setText(f.getAbsolutePath());
					} else {
						mapTxf.setText("");
					}
					updateFinish();
				}
			});
		}
		return find1;
	}

	public JButton getFind2() {
		if (find2 == null) {
			find2 = new JButton("Sfoglia");
			ModelBuilderView mbv = this;
			find2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter ff = new FileNameExtensionFilter("TCL File", "tcl");
					fc.setFileFilter(ff);
					fc.showOpenDialog(mbv);
					File f = fc.getSelectedFile();
					modelBuilder.setTs(f);
					if (f != null) {
						trainingTxf.setText(f.getAbsolutePath());
					} else {
						trainingTxf.setText("");
					}
					updateFinish();
				}
			});
		}
		return find2;
	}

	public JButton getFinish() {
		if (finish == null) {
			finish = new JButton("Finito");
			finish.setEnabled(false);
			finish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (mainView != null) {
						mainView.setModelBuilder(modelBuilder);
					}
				}
			});
		}
		return finish;
	}

	private void updateFinish() {
		if (modelBuilder.isReady()) {
			finish.setEnabled(true);
		} else {
			finish.setEnabled(false);
		}
	}
}
