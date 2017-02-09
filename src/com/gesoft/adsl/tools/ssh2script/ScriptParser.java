package com.gesoft.adsl.tools.ssh2script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gesoft.adsl.tools.JTools;
import com.gesoft.adsl.tools.ssh2.CommandInfo;

/**
 * @author Storm_Falcon
 *	脚本解析工具类
 *	提供各种字符串操作
 */
public class ScriptParser {

	private static String parentPath;
	
	/**
	 * 检查命令中括号是否匹配
	 * @return	true：匹配 false：不匹配
	 */
	private static boolean checkBrackets(String strInfo) {
		int left = strInfo.indexOf('(');
		int right = strInfo.indexOf(')');
		
		if (left > right) {
			return false;
		}
		if (left == -1 && right == -1) {
			return true;
		}
		return true;
	}
	
	/**
	 * 解析脚本文件
	 * @param strPath	文件绝对路径
	 * @return	根据各条命令生成list
	 */
	public static List<CommandInfo> parse(String strPath) {
		if (strPath.contains("/")) {
			parentPath = JTools.getParentPath(strPath);
		}
		String fileName = JTools.getFileName(strPath);
		FileReader reader = new FileReader();
		boolean bOpen = reader.open(parentPath + fileName);
		if (!bOpen) {
			System.err.println("文件打开失败：" + parentPath + fileName);
			return Collections.emptyList();
		}

		try {
			List<CommandInfo> commandList = new ArrayList<CommandInfo>();
			while (reader.hasNext()) {
				String strCmd = reader.getLine();
				
				//空行
				if (strCmd.length() == 0) {
					continue;
				}
				//注释
				if (strCmd.charAt(0) == '#'){
					continue;
				}
				//跳转其他文件
				if (strCmd.charAt(0) == '~') {
					String strOtherPath = strCmd.substring(1, strCmd.length());
					List<CommandInfo> otherList = parse(strOtherPath);
					if (otherList != null) {
						commandList.addAll(otherList);
					}
					continue;
				}
				
				if (!checkBrackets(strCmd)) {
					throw new Exception("命令：" + strCmd + "括号不匹配");
				}
				
				CommandInfo ci = CommandInfo.parse(strCmd);
				if (ci == null) {
					throw new Exception("命令:" + strCmd + "错误");
				}
				
				commandList.add(ci);
			}

			return commandList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}

		return Collections.emptyList();
	}
	
	public static String getCenterContent(String src, String prefix, String suffix) {
		int nBegin = src.indexOf(prefix) + prefix.length();
		int nEnd = src.indexOf(suffix, nBegin + 1);
		return src.substring(nBegin, nEnd);
	}
}
