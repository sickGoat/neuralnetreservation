package network.data;

import java.util.List;

import geographic.interfaces.Parser;
import geographic.interfaces.Parser.NN1Entry;
import geographic.interfaces.Parser.NN2Entry;

/*
 * Classe che mantine  i dati
 * letti dal parser
 */
public class RawData {
	
	
	private final List<Parser.NN1Entry> entriesNN1;
	
	private final List<Parser.NN2Entry> entriesNN2;

	public RawData(List<NN1Entry> entriesNN1, List<NN2Entry> entriesNN2) {
		super();
		this.entriesNN1 = entriesNN1;
		this.entriesNN2 = entriesNN2;
	}

	
	public List<Parser.NN1Entry> getEntriesNN1() {
		return entriesNN1;
	}


	public List<Parser.NN2Entry> getEntriesNN2() {
		return entriesNN2;
	}

	
	
}
