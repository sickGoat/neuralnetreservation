package org.unical.neuralnetwork.gui.pathselector;

import javax.swing.table.AbstractTableModel;

import util.PathMatcher;
import util.SimulationResult;

public class TableModelAdapter extends AbstractTableModel {
	
	private static final long serialVersionUID = -5620902520705077233L;
	private SimulationResult result;
	private static final String [] columns = {"Track ID", "Path Reale", "Path Previsione", "Predette", "Non Predette", "Sprecate"};
	
	public TableModelAdapter(SimulationResult result) {
		this.result = result;
	}

	@Override
	public int getRowCount() {
		return result.getResults().size();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PathMatcher p = result.getResults().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return String.format("Track %d", rowIndex);
		case 1:
			return p.getRealPath().size(); 
		case 2:
			return p.getPredicted().size();
		case 3:
			return p.getRightPredicted().size();
		case 4:
			return p.getNotPredicted().size();
		case 5:
			return p.getWaste().size();
		default:
			return null;
		}
	}
}
