package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Wu
 *#Method class, create a Object in GlobalInfo.mGlobal(Map)
 */
class New extends Command {

	@Override
	public int getArgsSize() {
		return 2;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strKey = (String)arrArgs.get(0);
		String strValue = (String)arrArgs.get(1);
	
		mGlobal.put(strKey, strValue);
		return null;
	}

}
