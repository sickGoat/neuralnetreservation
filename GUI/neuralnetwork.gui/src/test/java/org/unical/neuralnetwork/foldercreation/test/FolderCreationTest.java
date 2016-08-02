package org.unical.neuralnetwork.foldercreation.test;

import java.io.File;
import org.junit.Test;

public class FolderCreationTest {

	@Test
	public void creationIfNotExist() {
		String homepath = System.getProperty("user.home");
		File appFolder = new File(homepath + "/NeuralNetwork");
		makeFolder(homepath, appFolder);
		File plainFolder = new File(appFolder.getAbsolutePath() + "/PlainModel");
		makeFolder(appFolder.getAbsolutePath(), plainFolder);
		File dgsFolder = new File(appFolder.getAbsolutePath() + "/DGSModel");
		makeFolder(appFolder.getAbsolutePath(), dgsFolder);
		File logFolder = new File(appFolder.getAbsolutePath() + "/Log");
		makeFolder(appFolder.getAbsolutePath(), logFolder);
		File netConvertFile = new File("/opt/local/bin/netconvert");
		if(!netConvertFile.exists()){
			//dopo vediamo
		}
	}

	private void makeFolder(String path, File folder) {
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
			System.out.println(String.format("Directory creata in:%s", folder.getAbsolutePath()));
		}
	}
}
