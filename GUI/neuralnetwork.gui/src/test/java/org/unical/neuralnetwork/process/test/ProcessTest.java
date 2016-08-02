package org.unical.neuralnetwork.process.test;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

public class ProcessTest {
	@Test
	public void testProcess() throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder("/opt/local/bin/netconvert", "--help");
		Process process = pb.start();
		Scanner error = new Scanner(process.getErrorStream());
		Scanner output = new Scanner(process.getInputStream());
		int state = process.waitFor();
		if(state == 0){
			while(output.hasNextLine()){
				System.out.println(output.nextLine());
			}
		}else{
			while(error.hasNextLine()){
				System.out.println(error.nextLine());
			}
		}
		error.close();
		output.close();
	}
	
}
