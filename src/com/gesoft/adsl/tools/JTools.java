package com.gesoft.adsl.tools;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JTools {
	
    public static String getSysTimeStr(String strFormat) {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat(strFormat, Locale.US);
        return formatter.format(now);
    }

    private static DateFormat formatter2 = null;
    public static String getSysTimeStamp() {
    	if (formatter2 == null) {
    		 formatter2 = new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.US);
    	}
    	Date now = new Date();
        return formatter2.format(now);
    }
    
    public static boolean createFile(String strFilePath) {
        int nStartPos = 0;
        int nEndPos1 = strFilePath.indexOf(File.separator, nStartPos);
        int nEndPos2 = strFilePath.indexOf('/', nStartPos);
        int nEndPos = min(nEndPos1, nEndPos2);

        String strTempFilePath = strFilePath.substring(0, nEndPos+1);
        while (createAFile(strTempFilePath)) {
            nStartPos = nEndPos + 1;

            nEndPos1 = strFilePath.indexOf(File.separator, nStartPos);
            nEndPos2 = strFilePath.indexOf('/', nStartPos);
            nEndPos = min(nEndPos1, nEndPos2);

            if (nEndPos == -1)
                return true;

            strTempFilePath = strFilePath.substring(0, nEndPos+1);
        }

        return false;
    }

	private static int min(int n1, int n2) {
		if ((n1==-1) && (n2==-1))
			return -1;
		if (n1==-1)
			return n2;
		if (n2==-1)
			return n1;
		if (n1<n2)
			return n1;
		return n2;
	}

    private static boolean createAFile(String strFilePath) {
        boolean bRes = false;
        try {
			File fp = new File(strFilePath);
			bRes = fp.isDirectory() || fp.mkdir();
		} catch(Exception ignored) {}
        return bRes;
    }

    public static boolean isDigit(String string) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static String getParentPath(String path) {
        return path.substring(0, path.lastIndexOf('/') + 1);
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }
}
