package com.gesoft.adsl.tools.log;

import com.gesoft.adsl.tools.FileHelper;
import com.gesoft.adsl.tools.JTools;

class LocalLog extends Log {
	
	private FileWriter out = null;
	
	private String strPostfix = null;
	
	LocalLog(String strPath, int nType) {
		this.nType = nType;
		init(strPath);
	}
	
	LocalLog(String strPath, int nType, String strPostfix) {
		this.nType = nType;
		this.strPostfix = strPostfix;
		init(strPath);
	}
	
	LocalLog(String strPath, String strFileNameFormat, int nType) {
		this.nType = nType;
		this.strFileNameFormat = strFileNameFormat;
		init(strPath);
	}
	
	private boolean init(String strPath) {
		strFilePath = strPath;
		if (nType == TYPE_NOTCHANGE_FILENAME) {
			strFileName = "";
		} else if (nType == TYPE_AUTOCHANGE_FILENAME) {
			
			if (!strFilePath.endsWith("/") && !strFilePath.endsWith("\\")) {
				strFilePath += "/";
			}
			
			strFileName = JTools.getSysTimeStr(strFileNameFormat);
			strFileName += ".log";
		} 
		
		if (strPostfix == null || strPostfix.length() == 0) {
			out = FileWriter.getFileWriter(strFilePath + strFileName);
		} else {
			out = FileWriter.getFileWriter(strFilePath + strFileName + strPostfix);
		}

		return out != null;
	}
	
	public synchronized void toLogCore(String str) {
		if (nType == TYPE_NOTCHANGE_FILENAME) {
			toLog0(str);
		} else if (nType == TYPE_AUTOCHANGE_FILENAME) {
			toLog1(str);
		}
	}
	
	private void toLog0(String str) {
		if (out == null) {
			return;
		}

		StringBuilder strBuf = new StringBuilder();
		
		if (bShowT) {
			strBuf.append(JTools.getSysTimeStamp());
			strBuf.append("\r\n");
		} 

		strBuf.append(str);
		strBuf.append("\r\n");
		out.write(strBuf.toString());
	}
	
	private void toLog1(String str) {
		if (!bChangeFileName) {
			toLog0(str);
			return;
		}
		
		if (out != null) {
			out.close();
			if (strPostfix != null && strPostfix.length() != 0) {
				FileHelper.renameFile(strFilePath + strFileName + strPostfix, strFilePath + strFileName);
			}
		}
		
		strFileName = JTools.getSysTimeStr(strFileNameFormat);
		strFileName += ".log";
		
		if (strPostfix == null || strPostfix.length() == 0) {
			out = FileWriter.getFileWriter(strFilePath + strFileName);
		} else {
			out = FileWriter.getFileWriter(strFilePath + strFileName + strPostfix);
		}

		bChangeFileName = false;
		
		toLog0(str);
	}
	
	public void closeCore() {
		if (out == null) {
			return;
		}
		
		out.close();
		if (nType == TYPE_AUTOCHANGE_FILENAME) {
			if (strPostfix != null && strPostfix.length() != 0) {
				FileHelper.renameFile(strFilePath + strFileName + strPostfix, strFilePath + strFileName);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Log mLog = Log.getLog("c:\\1\\", 1, ".tmp");
		mLog.setShowT(true);
		
		long nStart = System.currentTimeMillis();
		System.out.println(nStart);
		
		for (int i=0; i<130; i++) {
			String str = "--------------a----start-------------\r\n" +
					"name1=123\r\n" +
					"name1=123\r\n" +
					"name1=123\r\n" +
					"--------------a----end-------------\r\n";
			mLog.toLog(str);
			
			Thread.sleep(1000);
		}
		
		mLog.close();
		System.out.println(System.currentTimeMillis() - nStart);
	}
}
