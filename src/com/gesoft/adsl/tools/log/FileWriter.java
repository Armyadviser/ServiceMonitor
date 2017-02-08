package com.gesoft.adsl.tools.log;

import com.gesoft.adsl.tools.JTools;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
class FileWriter {

	private final Object objLock = new Object();
	
	private int nCount = 0;
	
	private FileOutputStream out = null; 
	
	private BufferedOutputStream bufOut = null;
	
	private String strFilePath = null;
	
	FileWriter(String strFilePath) throws FileNotFoundException {
		this.strFilePath = strFilePath;
		out = new FileOutputStream(strFilePath, true);
		bufOut = new BufferedOutputStream(out);
	} 
	
	public void write(String strData) {
		try {
			bufOut.write(strData.getBytes());

			bufOut.flush();
		} catch (IOException ignored) {
		}
	}
	
	public void addRefer() {
		synchronized(objLock) {
			nCount++;
		}
	}
	
	public void close() {
		synchronized(objLock) {
			nCount--;
			if (nCount == 0) {
				try {
					bufOut.close();
					out.close();
					mData.remove(strFilePath);
				} catch (Exception ignored) {
				}
			}
		}
	}
	
	private static Map mData = new HashMap();
	
	public static synchronized FileWriter getFileWriter(String strFilePath) {
		
		boolean bSucc = JTools.createFile(strFilePath);
		if (!bSucc) {
			return null;
		}

		FileWriter mFileWriter = (FileWriter)mData.get(strFilePath);
		if (mFileWriter == null) {
			try {
				mFileWriter = new FileWriter(strFilePath);
			} catch (FileNotFoundException e) {
				return null;
			}
			mData.put(strFilePath, mFileWriter);
		}
		
		mFileWriter.addRefer();
		
		return mFileWriter;
	}
}
