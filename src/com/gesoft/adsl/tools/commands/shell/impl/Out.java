package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Storm_Falcon
 *	输出实现类
 */
class Out extends Command {

	@Override
	public int getArgsSize() {
		return 1;
	}
	
	/**
	 * 命令样式：out(LIST_FILE)
	 * 将list内容输出
	 * 命令样式：out(STR_FILE)
	 * 将字符串内容输出
	 */
	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strKey = (String) arrArgs.get(0);
		
		Object obj = ifExistThenGet(mGlobal, strKey);
		
		System.out.println(obj);
		
		return null;
	}

}
