package network.data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.util.kmeans.Centroid;

import util.SideConverter;

public class NN2MLDataPair implements MLDataPair {
	
	private double[] input;
	
	private double[] ideal;
	
	protected NN2MLDataPair(double[] input,double[] output) {
		this.input = Arrays.copyOf(input, input.length);
		this.ideal = Arrays.copyOf(output, output.length);
	}
	
	@Override
	public Centroid<MLDataPair> createCentroid() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MLData getIdeal() {
		return new BasicMLData(this.ideal);
	}

	@Override
	public double[] getIdealArray() {
		return this.ideal;
	}

	@Override
	public MLData getInput() {
		return new BasicMLData(this.input);
	}

	@Override
	public double[] getInputArray() {
		return this.input;
	}

	@Override
	public double getSignificance() {
		return 1.0;
	}

	@Override
	public boolean isSupervised() {
		return true;
	}

	@Override
	public void setIdealArray(double[] arg0) {
		this.ideal = Arrays.copyOf(arg0, arg0.length);
	}

	@Override
	public void setInputArray(double[] arg0) {
		this.input = Arrays.copyOf(arg0, arg0.length);
	}

	@Override
	public void setSignificance(double arg0) {

	}
	
	
	/**
	 * Questo metodo viene chiamato
	 * quando eseguiamo la statistica
	 * sui dati
	 * @param d
	 * @param output
	 * @param directionNumber
	 * @return
	 */
	protected static NN2MLDataPair createPair(int d,double[] output,int directionNumber){
		return new NN2MLDataPair(new SideConverter(directionNumber).convert(d), output);
	}
	
	
	/**
	 * Questo metodo prende le due direzioni
	 * ingresso/uscita e si calcola il vettore
	 * corrispondete.
	 * Lo utilizziamo quando non effettuiamo
	 * statistica sui dati
	 * @param inDir
	 * @param outDir
	 * @param directionNumber
	 * @return
	 */
	public static NN2MLDataPair createPair(int inDir,int outDir,int directionNumber){
		SideConverter converter = new SideConverter(directionNumber);
		return new NN2MLDataPair(converter.convert(inDir), converter.convert(outDir));
	}
	
	
	/**
	 * Questo metodo serve quando eseguiamo statistica
	 * sui dati. 
	 * @param matrix :ogni riga indica il lato di ingresso e contiene la statistica
	 * 				  ad esso associata
	 * @param directionNumber
	 * @return
	 */
	public static List<MLDataPair> createListPair(double[][] matrix,int directionNumber){
		List<MLDataPair> list = new LinkedList<>();
		double[] ideal = new double[matrix[0].length];
		for(int i = 0 ; i < matrix.length ; i++){
			for( int j = 0 ; j < matrix[0].length ; j++ ){
				ideal[j] = matrix[i][j];
			}
			list.add(NN2MLDataPair.createPair(i, ideal,directionNumber));
		}
		return list;
	}
	

}
