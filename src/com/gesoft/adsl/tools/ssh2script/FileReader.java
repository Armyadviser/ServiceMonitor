package com.gesoft.adsl.tools.ssh2script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

	private BufferedReader mReader = null;
	
	private String strCurrentLine = null;
	
	public boolean open(String strFilePath) {
		File mFile = new File(strFilePath);
		if (!mFile.exists()) {
			return false;
		}
		
		try {
			mReader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(mFile), "GBK"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getLine() throws IOException {
		return strCurrentLine;
	}
	
	public boolean hasNext() throws IOException {
		strCurrentLine = mReader.readLine();
		return strCurrentLine != null;
	}
	
	public void close() {
		try {
			mReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
