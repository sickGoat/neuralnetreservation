package org.unical.neuralnetwork.gui.builder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.unical.neuralnetwork.gui.MainView;
import org.unical.neuralnetwork.logic.ModelBuilder;

import network.interafaces.NetworkBuilder;
import network.interafaces.NetworkManager;
import util.SimulationParameter.SimulationParamBuilder;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class AdvancedModelBuilderView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMap;
	private JTextField txtTraining;
	private JTextField txtSimulation;
	private JButton btnEnd;
	private JComboBox<Double> cmbTrainingThreshould;
	private JComboBox<NetworkBuilder.NetworkType> cmbNetworkType;
	private JComboBox<Double> cmbAlgorithmThreshould;
	private JComboBox<Integer> cmbPredictionStep;
	private JComboBox<Double> cmbCellSide;
	private JComboBox<NetworkManager.PropagationType> cmbPropagationType;
	// variabili mie
	private MainView mv;
	// logica
	private SimulationParamBuilder builder = new SimulationParamBuilder();
	private ModelBuilder modelBuilder = new ModelBuilder();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdvancedModelBuilderView frame = new AdvancedModelBuilderView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AdvancedModelBuilderView(MainView mv) {
		this.mv = mv;
		modelBuilder.setBuilder(builder);
		init();
	}

	public AdvancedModelBuilderView() {
		this(null);
	}

	/**
	 * Create the frame.
	 */
	private void init() {
		setResizable(false);
		if (mv == null)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		AdvancedModelBuilderView view = this;
		setBounds(100, 100, 535, 349);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblMap = new JLabel("File Mappa:");
		GridBagConstraints gbc_lblMap = new GridBagConstraints();
		gbc_lblMap.insets = new Insets(0, 0, 5, 5);
		gbc_lblMap.anchor = GridBagConstraints.WEST;
		gbc_lblMap.gridx = 0;
		gbc_lblMap.gridy = 0;
		contentPane.add(lblMap, gbc_lblMap);

		txtMap = new JTextField();
		txtMap.setEditable(false);
		GridBagConstraints gbc_txtMaptextfield = new GridBagConstraints();
		gbc_txtMaptextfield.insets = new Insets(0, 0, 5, 5);
		gbc_txtMaptextfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMaptextfield.gridx = 1;
		gbc_txtMaptextfield.gridy = 0;
		contentPane.add(txtMap, gbc_txtMaptextfield);
		txtMap.setColumns(10);

		JButton btnSlide1 = new JButton("Sfoglia");
		btnSlide1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter ff = new FileNameExtensionFilter("SUMO xml", "xml");
				fc.setFileFilter(ff);
				fc.showOpenDialog(view);
				File f = fc.getSelectedFile();
				modelBuilder.setMap(f);
				if (f != null) {
					txtMap.setText(f.getAbsolutePath());
				} else {
					txtMap.setText("");
				}
				updateFinish();
			}
		});

		GridBagConstraints gbc_btnSlide1 = new GridBagConstraints();
		gbc_btnSlide1.insets = new Insets(0, 0, 5, 0);
		gbc_btnSlide1.gridx = 2;
		gbc_btnSlide1.gridy = 0;
		contentPane.add(btnSlide1, gbc_btnSlide1);

		JLabel lblTraining = new JLabel("File Training:");
		GridBagConstraints gbc_lblTraining = new GridBagConstraints();
		gbc_lblTraining.anchor = GridBagConstraints.WEST;
		gbc_lblTraining.insets = new Insets(0, 0, 5, 5);
		gbc_lblTraining.gridx = 0;
		gbc_lblTraining.gridy = 1;
		contentPane.add(lblTraining, gbc_lblTraining);

		txtTraining = new JTextField();
		txtTraining.setEditable(false);
		GridBagConstraints gbc_txtTraining = new GridBagConstraints();
		gbc_txtTraining.insets = new Insets(0, 0, 5, 5);
		gbc_txtTraining.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTraining.gridx = 1;
		gbc_txtTraining.gridy = 1;
		contentPane.add(txtTraining, gbc_txtTraining);
		txtTraining.setColumns(10);

		JButton btnSlide2 = new JButton("Sfoglia");
		GridBagConstraints gbc_btnSlide2 = new GridBagConstraints();
		gbc_btnSlide2.insets = new Insets(0, 0, 5, 0);
		gbc_btnSlide2.gridx = 2;
		gbc_btnSlide2.gridy = 1;
		contentPane.add(btnSlide2, gbc_btnSlide2);
		btnSlide2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter ff = new FileNameExtensionFilter("TCL File", "tcl");
				fc.setFileFilter(ff);
				fc.showOpenDialog(view);
				File f = fc.getSelectedFile();
				modelBuilder.setTs(f);
				if (f != null) {
					txtTraining.setText(f.getAbsolutePath());
				} else {
					txtTraining.setText("");
				}
				updateFinish();
			}
		});

		JLabel lblSimulation = new JLabel("File Simulazione:");
		GridBagConstraints gbc_lblSimulation = new GridBagConstraints();
		gbc_lblSimulation.anchor = GridBagConstraints.WEST;
		gbc_lblSimulation.insets = new Insets(0, 0, 5, 5);
		gbc_lblSimulation.gridx = 0;
		gbc_lblSimulation.gridy = 2;
		contentPane.add(lblSimulation, gbc_lblSimulation);

		txtSimulation = new JTextField();
		txtSimulation.setEditable(false);
		GridBagConstraints gbc_txtSimulation = new GridBagConstraints();
		gbc_txtSimulation.insets = new Insets(0, 0, 5, 5);
		gbc_txtSimulation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSimulation.gridx = 1;
		gbc_txtSimulation.gridy = 2;
		contentPane.add(txtSimulation, gbc_txtSimulation);
		txtSimulation.setColumns(10);

		JButton btnSlide3 = new JButton("Sfoglia");
		GridBagConstraints gbc_btnSlide3 = new GridBagConstraints();
		gbc_btnSlide3.insets = new Insets(0, 0, 5, 0);
		gbc_btnSlide3.gridx = 2;
		gbc_btnSlide3.gridy = 2;
		contentPane.add(btnSlide3, gbc_btnSlide3);
		btnSlide3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter ff = new FileNameExtensionFilter("TCL File", "tcl");
				fc.setFileFilter(ff);
				fc.showOpenDialog(view);
				File f = fc.getSelectedFile();
				modelBuilder.setSs(f);
				if (f != null) {
					txtSimulation.setText(f.getAbsolutePath());
				} else {
					txtSimulation.setText("");
				}
				updateFinish();
			}
		});

		JLabel lblTrainingThreshould = new JLabel("Soglia Training:");
		GridBagConstraints gbc_lblTrainingThreshould = new GridBagConstraints();
		gbc_lblTrainingThreshould.anchor = GridBagConstraints.WEST;
		gbc_lblTrainingThreshould.insets = new Insets(0, 0, 5, 5);
		gbc_lblTrainingThreshould.gridx = 0;
		gbc_lblTrainingThreshould.gridy = 3;
		contentPane.add(lblTrainingThreshould, gbc_lblTrainingThreshould);

		Double[] v = new Double[] { 0.01, 0.04, 0.08, 0.1, 0.2 };
		cmbTrainingThreshould = new JComboBox<>(v);
		cmbTrainingThreshould.setSelectedIndex(1);
		GridBagConstraints gbc_cmbTrainingThreshould = new GridBagConstraints();
		gbc_cmbTrainingThreshould.insets = new Insets(0, 0, 5, 5);
		gbc_cmbTrainingThreshould.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbTrainingThreshould.gridx = 1;
		gbc_cmbTrainingThreshould.gridy = 3;
		contentPane.add(cmbTrainingThreshould, gbc_cmbTrainingThreshould);

		JLabel lblNetworkType = new JLabel("Struttura della rete:");
		GridBagConstraints gbc_lblNetworkType = new GridBagConstraints();
		gbc_lblNetworkType.anchor = GridBagConstraints.WEST;
		gbc_lblNetworkType.insets = new Insets(0, 0, 5, 5);
		gbc_lblNetworkType.gridx = 0;
		gbc_lblNetworkType.gridy = 4;
		contentPane.add(lblNetworkType, gbc_lblNetworkType);

		cmbNetworkType = new JComboBox<>(NetworkBuilder.NetworkType.values());
		GridBagConstraints gbc_cmbNetworkType = new GridBagConstraints();
		gbc_cmbNetworkType.insets = new Insets(0, 0, 5, 5);
		gbc_cmbNetworkType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbNetworkType.gridx = 1;
		gbc_cmbNetworkType.gridy = 4;
		contentPane.add(cmbNetworkType, gbc_cmbNetworkType);

		JLabel lblPropagationType = new JLabel("Tipo di propagazione:");
		GridBagConstraints gbc_lblTipoDiPropagazione = new GridBagConstraints();
		gbc_lblTipoDiPropagazione.anchor = GridBagConstraints.EAST;
		gbc_lblTipoDiPropagazione.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipoDiPropagazione.gridx = 0;
		gbc_lblTipoDiPropagazione.gridy = 5;
		contentPane.add(lblPropagationType, gbc_lblTipoDiPropagazione);

		cmbPropagationType = new JComboBox<>(NetworkManager.PropagationType.values());
		GridBagConstraints gbc_cmbPropagationType = new GridBagConstraints();
		gbc_cmbPropagationType.insets = new Insets(0, 0, 5, 5);
		gbc_cmbPropagationType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbPropagationType.gridx = 1;
		gbc_cmbPropagationType.gridy = 5;
		contentPane.add(cmbPropagationType, gbc_cmbPropagationType);

		JLabel lblAlgorithmThreshould = new JLabel("Soglia Algoritmo:");
		GridBagConstraints gbc_lblAlgorithmThreshould = new GridBagConstraints();
		gbc_lblAlgorithmThreshould.anchor = GridBagConstraints.WEST;
		gbc_lblAlgorithmThreshould.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlgorithmThreshould.gridx = 0;
		gbc_lblAlgorithmThreshould.gridy = 6;
		contentPane.add(lblAlgorithmThreshould, gbc_lblAlgorithmThreshould);

		v = new Double[] { 0.15, 0.3, 0.45, 0.6, 0.75 };
		cmbAlgorithmThreshould = new JComboBox<Double>(v);
		cmbAlgorithmThreshould.setSelectedIndex(1);
		GridBagConstraints gbc_cmbAlgorithmThreshould = new GridBagConstraints();
		gbc_cmbAlgorithmThreshould.insets = new Insets(0, 0, 5, 5);
		gbc_cmbAlgorithmThreshould.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbAlgorithmThreshould.gridx = 1;
		gbc_cmbAlgorithmThreshould.gridy = 6;
		contentPane.add(cmbAlgorithmThreshould, gbc_cmbAlgorithmThreshould);

		JLabel lblPredictionStep = new JLabel("Prediction Step:");
		GridBagConstraints gbc_lblPredictionStep = new GridBagConstraints();
		gbc_lblPredictionStep.anchor = GridBagConstraints.WEST;
		gbc_lblPredictionStep.insets = new Insets(0, 0, 5, 5);
		gbc_lblPredictionStep.gridx = 0;
		gbc_lblPredictionStep.gridy = 7;
		contentPane.add(lblPredictionStep, gbc_lblPredictionStep);

		Integer[] vi = new Integer[] { 1, 3, 5, 7, 10 };
		cmbPredictionStep = new JComboBox<Integer>(vi);
		cmbPredictionStep.setSelectedIndex(3);
		GridBagConstraints gbc_cmbPredictionStep = new GridBagConstraints();
		gbc_cmbPredictionStep.insets = new Insets(0, 0, 5, 5);
		gbc_cmbPredictionStep.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbPredictionStep.gridx = 1;
		gbc_cmbPredictionStep.gridy = 7;
		contentPane.add(cmbPredictionStep, gbc_cmbPredictionStep);

		JLabel lblCellSide = new JLabel("Lato della cella:");
		GridBagConstraints gbc_lblCellSide = new GridBagConstraints();
		gbc_lblCellSide.anchor = GridBagConstraints.WEST;
		gbc_lblCellSide.insets = new Insets(0, 0, 5, 5);
		gbc_lblCellSide.gridx = 0;
		gbc_lblCellSide.gridy = 8;
		contentPane.add(lblCellSide, gbc_lblCellSide);

		v = new Double[] { 50.0, 80.0, 120.0, 180.0, 250.0 };
		cmbCellSide = new JComboBox<>(v);
		cmbCellSide.setSelectedIndex(2);
		GridBagConstraints gbc_cmbCellSide = new GridBagConstraints();
		gbc_cmbCellSide.insets = new Insets(0, 0, 5, 5);
		gbc_cmbCellSide.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbCellSide.gridx = 1;
		gbc_cmbCellSide.gridy = 8;
		contentPane.add(cmbCellSide, gbc_cmbCellSide);

		btnEnd = new JButton("Avvia");
		btnEnd.setEnabled(false);
		GridBagConstraints gbc_btnFinito = new GridBagConstraints();
		gbc_btnFinito.gridx = 2;
		gbc_btnFinito.gridy = 9;
		contentPane.add(btnEnd, gbc_btnFinito);
		btnEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				finalizeArguments();
				if (mv != null) {
					mv.setModelBuilder(modelBuilder);
				}
			}
		});
	}

	private void finalizeArguments() {
		builder.isDistributed(cmbNetworkType.getSelectedItem().equals(NetworkBuilder.NetworkType.DISTRIBUITED))
				.isRawData(true).setAlghThreshold((Double)cmbAlgorithmThreshould.getSelectedItem());
		builder.setPropagationType((NetworkManager.PropagationType) cmbPropagationType.getSelectedItem());
		builder.setCellSide((Double)cmbCellSide.getSelectedItem());
		builder.setH((Integer)cmbPredictionStep.getSelectedItem());
		builder.setTrainingThreshold((Double)cmbTrainingThreshould.getSelectedItem());
		
	}

	private void updateFinish() {
		if (modelBuilder.isReady()) {
			btnEnd.setEnabled(true);
		} else {
			btnEnd.setEnabled(false);
		}
	}
}
