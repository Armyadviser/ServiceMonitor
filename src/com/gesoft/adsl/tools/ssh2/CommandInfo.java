package com.gesoft.adsl.tools.ssh2;

import java.util.ArrayList;

public class CommandInfo {

	/**
	 * 方法名
	 */
	public String strName = null;
	
	/**
	 * 参数列表
	 */
	public ArrayList<Object> arrArgs = new ArrayList<Object>();
	
	public String toString() {
		return null;
	}
	
	/**
	 * 命令反序列化，将strCmd解析
	 */
	public static CommandInfo parse(String strCmd) {
		try {
			char flag = strCmd.charAt(0);
			if (flag == '@') {//系统方法
				CommandInfo ci = new CommandInfo();
				ci.strName = "@";
				strCmd = strCmd.substring(1, strCmd.length());
				ci.arrArgs.add(strCmd);
				return ci;
			}
			
			if (flag == '$') {//自定义方法
				CommandInfo ci = new CommandInfo();
				
				//去掉开头标识符
				strCmd = strCmd.substring(1, strCmd.length());
				
				//获取命令名
				ci.strName = strCmd.substring(0, strCmd.indexOf('('));
				
				//获取参数列表
				String strParam = strCmd.substring(strCmd.indexOf('(') + 1, strCmd.indexOf(')'));
				String[] params = strParam.split(",");
				for (String param : params) {
					//有些命令无参数
					if (param.length() == 0) {
						continue;
					}
					ci.arrArgs.add(param.trim());
				}
				
				return ci;
			} 
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println(strCmd);
			e.printStackTrace();
		}
		return null;
	}
}
