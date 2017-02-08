package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Storm_Falcon
 *	获取文件名实现类
 */
class GetFileName extends Command {

	/**
	 * 命令样式：get_file_name(STR_FILE, LIST_FILE, INT_POS)
	 * 将 LIST_FILE 中第 INT_POS 个文件的文件名存到 STR_FILE中
	 */
	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs)
			throws Exception {
		String strFile = (String) arrArgs.get(0);
		String strList = (String) arrArgs.get(1);
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> fileList = (ArrayList<ArrayList<String>>) ifExistThenGet(mGlobal, strList);
		String strPos = (String) arrArgs.get(2);
		
		int iPos = Integer.parseInt((String) ifExistThenGet(mGlobal, strPos));
		
		ArrayList<String> fileLine = null;
		try {
			fileLine = fileList.get(iPos);
			String strFileName = fileLine.get(fileLine.size() - 1);
			mGlobal.put(strFile, strFileName);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("获取文件:" + fileLine + "的文件名失败");
		}
		
		return null;
	}

	@Override
	public int getArgsSize() {
		return 3;
	}

}
