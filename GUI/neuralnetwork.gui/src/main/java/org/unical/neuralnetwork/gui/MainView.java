package org.unical.neuralnetwork.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.unical.neuralnetwork.environment.Environment;
import org.unical.neuralnetwork.environment.EnvironmentException;
import org.unical.neuralnetwork.gui.builder.AdvancedModelBuilderView;
import org.unical.neuralnetwork.gui.builder.BuildingProgress;
import org.unical.neuralnetwork.gui.pathselector.PathSelector;
import org.unical.neuralnetwork.gui.pathselector.TableModelAdapter;
import org.unical.neuralnetwork.logic.Model;
import org.unical.neuralnetwork.logic.ModelBuilder;

public class MainView extends JFrame {

	private static final long serialVersionUID = 4127632820374748199L;
	// variabili grafiche
	private JPanel container;
	private JPanel actionsContainer;
	private JTextArea console;
	private JPanel buttonsContainer;
	private JButton btnLoad;
	private JButton btnPredict;
	private JButton btnGridVisibility;
	private JPanel labelPanel;
	private JLabel wasteLabel;
	private JLabel accuracyLabel;
	// variabili mie che fanno bruttissimo
	private Graph map;
	private Viewer mapViewer;
	private ViewPanel mapPanel;
	private Model model;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Environment.bootstrap();
				} catch (EnvironmentException e) {
					e.printStackTrace();
				}
				new MainView();
			}
		});
	}

	public MainView() {
		setBounds(100, 100, 1000, 600);
		// setResizable(false);
		setContentPane(getMyContainer());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		// settiamo l'ambiente
		// TODO
		PrintStream ps = new PrintStream(new CustomOutpuStream(console));
		Environment.setSystemOutput(ps);
	}

	public JPanel getMyContainer() {
		if (container == null) {
			container = new JPanel(new BorderLayout());
			container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			container.add(getActionsContainer(), BorderLayout.EAST);
			container.add(getMapPanel(), BorderLayout.CENTER);
		}
		return container;
	}

	public ViewPanel getMapPanel() {
		if (mapPanel == null) {
			//System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
			map = new MultiGraph("MAP");
			mapViewer = new Viewer(map, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
			//se no non mette il css
			mapViewer.disableAutoLayout();
			mapPanel = mapViewer.addDefaultView(false);
		}
		return mapPanel;
	}

	public JPanel getActionsContainer() {
		if (actionsContainer == null) {
			actionsContainer = new JPanel();
			actionsContainer.setLayout(new BoxLayout(actionsContainer, BoxLayout.Y_AXIS));
			actionsContainer.add(getLabelPanel());
			actionsContainer.add(new JScrollPane(getConsole()));
			actionsContainer.add(getButtonsContainer());
		}
		return actionsContainer;
	}

	public Component getLabelPanel() {
		if(labelPanel == null){
			labelPanel = new JPanel();
			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
			labelPanel.add(getAccuracyLabel());
			labelPanel.add(getWasteLabel());
		}
		return labelPanel;
	}

	public JLabel getWasteLabel() {
		if(wasteLabel == null){
			wasteLabel = new JLabel("Waste:");
		}
		return wasteLabel;
	}

	public JLabel getAccuracyLabel() {
		if(accuracyLabel == null){
			accuracyLabel = new JLabel("Accuracy:");
		}
		return accuracyLabel;
	}

	public JTextArea getConsole() {
		if (console == null) {
			console = new JTextArea();
			console.setEditable(false);
		}
		return console;
	}

	public JPanel getButtonsContainer() {
		if (buttonsContainer == null) {
			buttonsContainer = new JPanel();
			buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.X_AXIS));
			buttonsContainer.add(getBtnLoad());
			buttonsContainer.add(getBtnPredict());
			buttonsContainer.add(getBtnGridVisibility());
		}
		return buttonsContainer;
	}

	public JButton getBtnLoad() {
		if (btnLoad == null) {
			btnLoad = new JButton("Load");
			MainView mv = this;
			btnLoad.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new AdvancedModelBuilderView(mv).setVisible(true);;
				}
			});
		}
		return btnLoad;
	}

	public JButton getBtnPredict() {
		if (btnPredict == null) {
			btnPredict = new JButton("Predict");
			btnPredict.setEnabled(false);
			MainView mv = this;
			btnPredict.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new PathSelector(mv, new TableModelAdapter(model.getResult()));
				}
			});
		}
		return btnPredict;
	}

	public JButton getBtnGridVisibility() {
		if (btnGridVisibility == null) {
			btnGridVisibility = new JButton("Show/Hide");
			btnGridVisibility.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(model != null)
						model.getGridController().showOrHide();
				}
			});
		}
		return btnGridVisibility;
	}
	
	public void setModelBuilder(ModelBuilder mb){
		BuildingProgress bp = new BuildingProgress();
		mb.setViewEnviroment(map, bp, this);
		mb.execute();
	}
	
	public void addAccuracy(double value){
		getAccuracyLabel().setText(String.format("Accuracy: %.2f%%", value));
	}
	
	public void addWaste(double value){
		getWasteLabel().setText(String.format("Waste: %.2f%%", value));
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
		if(model != null){
			btnPredict.setEnabled(true);
		}
	}
	
	public void lightPath(int index){
		model.getGridController().lightPath(model.getResult().getResults().get(index));
	}
}
