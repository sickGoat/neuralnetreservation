package org.unical.neuralnetwork.gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class CustomOutpuStream extends OutputStream {

	private JTextArea textArea;
	
	public CustomOutpuStream(JTextArea jta) {
		super();
		textArea = jta;
	}
	
	@Override
	public void write(int b) throws IOException {
		textArea.append(String.valueOf((char)b));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
