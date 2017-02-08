package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Storm_Falcon
 *	实现类，将字符串解析成二维list
 */
class ParseLsTral extends Command {

	@Override
	public int getArgsSize() {
		return 2;
	}

	/**
	 * 命令样式：parse_ls_tral(LIST_FILE, STR_R_LOG)
	 * 将STR_R_LOG的值解析成List保存到LIST_FILE中
	 */
	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strListFile = (String) arrArgs.get(0);//key
		String strLog = (String) arrArgs.get(1);
		
		strLog = (String) ifExistThenGet(mGlobal, strLog);
		
		ArrayList<ArrayList<String>> fileList = new ArrayList<ArrayList<String>>();
		
		//得到每一行
		String[] fileLine = strLog.split("\n");
		for (String line : fileLine){
			//去掉两个以上空格，保留一个
			line = line.replaceAll("\\s+", " ");
			String[] item = line.split(" ");
			
			//去掉非文件信息
			if (item.length < 7){
				continue;
			}
			ArrayList<String> fileContent = new ArrayList<String>();
			Collections.addAll(fileContent, item);
			fileList.add(fileContent);
		}
		
		mGlobal.put(strListFile, fileList);
		
		return null;
	}

}
