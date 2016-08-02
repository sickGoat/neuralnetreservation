package org.unical.neuralnetwork.converter.test;

import java.io.File;

import org.junit.Test;

public class FileNameTest {
	
	@Test
	public void testingName(){
		File f = new File(UtilityTestFile.DGS_FILE_PATH);
		System.out.println(f.getName());
		System.out.println(f.getName().endsWith(".dgs"));
		System.out.println(f.getName().substring(0, f.getName().indexOf('.')));
	}

}
