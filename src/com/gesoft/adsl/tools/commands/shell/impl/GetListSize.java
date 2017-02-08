package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Storm_Falcon
 *	实现类，获取list大小
 */
class GetListSize extends Command {

	/**
	 * 命令样式：get_list_size(INT_SIZE, LIST_FILE)
	 * 将 LIST_FILE 的大小保存到 INT_SIZE中
	 */
	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs)
			throws Exception {
		String strSize = (String) arrArgs.get(0);
		String strList = (String) arrArgs.get(1);
		@SuppressWarnings("unchecked")
		ArrayList<String> listFile = (ArrayList<String>) ifExistThenGet(mGlobal, strList);
		
		mGlobal.put(strSize, String.valueOf(listFile.size()));
		return null;
	}

	@Override
	public int getArgsSize() {
		return 2;
	}

}
