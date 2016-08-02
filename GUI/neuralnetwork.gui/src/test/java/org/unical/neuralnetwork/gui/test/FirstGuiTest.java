package org.unical.neuralnetwork.gui.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.unical.neuralnetwork.converter.test.UtilityTestFile;

public class FirstGuiTest extends JFrame {

	private static final long serialVersionUID = -5681800826129366828L;
	private Graph graph;
	private Viewer graphViewer;
	private ViewPanel graphView;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				FirstGuiTest frame = new FirstGuiTest();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FirstGuiTest() {
		//logical init
		graph = new MultiGraph("Bho");
		try {
			graph.read(UtilityTestFile.DGS_FILE_PATH);
		} catch (ElementNotFoundException | IOException | GraphParseException e) {
			e.printStackTrace();
		}
		graphViewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		graphView = graphViewer.addDefaultView(false);
		//gui init
		setTitle("Graph");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(graphView, BorderLayout.CENTER);
		JButton event = new JButton("Event");
		event.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(graphView.getBounds());
			}
		});
		contentPane.add(event, BorderLayout.EAST);
	}
}
