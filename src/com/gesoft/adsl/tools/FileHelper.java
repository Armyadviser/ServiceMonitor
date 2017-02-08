package com.gesoft.adsl.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Java文件相关操作工具类
 * @author Administrator
 *
 */
public class FileHelper {
	
	private static final int INITIAL_SIZE = 1024;
	
	private FileHelper() {}
	
	/**
	 * 获取文件列表(一级)
	 * @return 文件名
	 */
	public static String[] ls (String strPath) {
		if (StringHelper.isEmpty(strPath)) {
			return null;
		}
		
		try {
			return new File(strPath).list();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取文件列表(一级)
	 */
	public static String[] ls (String strPath, FilenameFilter fileFilter) {
		if (StringHelper.isEmpty(strPath)) {
			return null;
		}
		
		try {
			File mFile = new File(strPath);
			return mFile.list(fileFilter);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 检查文件全路径中去除文件名的夹是否存在,如不存在则创建
	 * @param filePath 文件或文件夹的全路径
	 */
	public static boolean mkdirs(String filePath) {
		if (StringHelper.isEmpty(filePath)) {
			return false;
		}

		String folderPath;

		if (filePath.endsWith(File.separator)) {
			folderPath = filePath;
		} else {
			int endPoint = filePath.lastIndexOf(File.separator);
			if (endPoint == -1) {
				return false;
			}

			folderPath = filePath.substring(0, endPoint);
		}

		File file = new File(folderPath);
		return !file.exists() && file.mkdirs();

	}
	
	/**
	 * 文件备份
	 * @param strOldFileName 待拷贝文件全路径，包括文件名
	 * @param strFileBackUpName 拷贝后文件全路径，包括文件名
	 */
	public static boolean cp(String strOldFileName, String strFileBackUpName){
		if (StringHelper.isEmpty(strOldFileName)) {
			return false;
		}
		if (StringHelper.isEmpty(strFileBackUpName)) {
			return false;
		}
		
        mkdirs(strFileBackUpName);
        
		try {
			
	        InputStream in = new FileInputStream(strOldFileName);
	        
	        OutputStream out = new FileOutputStream(strFileBackUpName);
	    
	        byte[] buf = new byte[INITIAL_SIZE];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        
	        in.close();
	        out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	/**
	 * 拷贝指定目录下的多个文件到指定目录,只拷贝文件
	 */
	public static boolean fileCopy(String strOldFilePath, String strNewFilePath) {
		if (StringHelper.isEmpty(strOldFilePath)) {
			return false;
		}
		
		if (StringHelper.isEmpty(strNewFilePath)) {
			return false;
		}
		
		mkdirs(strNewFilePath);
        
		String[] strFileNameList = FileHelper.ls(strOldFilePath);
		if (strFileNameList == null) {
			return false;
		}
		
		String strOldFilePathTemp = strOldFilePath;
		if (!strOldFilePathTemp.endsWith(File.separator)) {
			strOldFilePathTemp += File.separator;
		}
		
		String strNewFilePathTemp = strNewFilePath;
		if (!strNewFilePathTemp.endsWith(File.separator)) {
			strNewFilePathTemp += File.separator;
		}

		for (String aStrFileNameList : strFileNameList) {

			String strOldFileName = strOldFilePathTemp + aStrFileNameList;
			if (new File(strOldFileName).isDirectory()) {
				continue;
			}

			String strFileBackUpName = strNewFilePathTemp + aStrFileNameList;

			FileHelper.cp(strOldFileName, strFileBackUpName);

			System.out.println(strOldFileName + " -> " + strFileBackUpName);
		}
		
		return true;
	}

	/**
	 * 删除文件
	 */
	public static boolean rm(String fileName) {
		if (StringHelper.isEmpty(fileName)) {
			return false;
		}
		
		File fp = new File(fileName);
		if (!fp.isFile()) {
			return false;
		}

		try {
			return new File(fileName).delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 删除文件夹
	 * @param folderPath 文件夹完整路径
	 */
	public static boolean delFolder(String folderPath) {
		if (StringHelper.isEmpty(folderPath)) {
			return false;
		}
		
		File fp = new File(folderPath);
		if (!fp.isDirectory()) {
			return false;
		}

	    try {
	    	File[] fps = fp.listFiles();
			if (fps != null) {
				for (File fpTemp : fps) {
                    if (fpTemp.isFile()) {
                        fpTemp.delete();
                        continue;
                    }

                    if (fpTemp.isDirectory()) {
                        delFolder(fpTemp.getAbsolutePath());
                    }
                }
			}

			fp.delete();
	    } catch (Exception e) {
	    	e.printStackTrace(); 
	    	return false;
	    }
	    
	    return true;
	}
	
	/**
	* 文件重命名 
	* @param oldFileName 原来的文件名
	* @param newFileName 新文件名
	*/
	public static boolean renameFile(String oldFileName, String newFileName) {
		if (StringHelper.isEmpty(oldFileName)) {
			return false;
		}
		if (StringHelper.isEmpty(newFileName)) {
			return false;
		}

		if (oldFileName.equals(newFileName)) {
			return false;
		}

		File oldFile = new File(oldFileName);
		if (!oldFile.exists() || oldFile.isDirectory()) {
			return false;
		}

		File newFile = new File(newFileName);
		return !(newFile.exists() || newFile.isDirectory()) && oldFile.renameTo(newFile);

	}
	
	/**
	 * 获取文件名
	 */
	public static String getName(String strFilePath) {
		if (StringHelper.isEmpty(strFilePath)) {
			return null;
		}
		
		File fp = new File(strFilePath);
        if (!fp.exists() || fp.isDirectory()) { 
            return null;
        }
        
        return fp.getName();
	}
}
