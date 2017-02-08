package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Wu
 * #Method, get the variable in map increased by 1.
 */
class Increase extends Command{

	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs)
			throws Exception {
		String strPosKey = (String) arrArgs.get(0);
		String strPosValue = (String) mGlobal.get(strPosKey);
		if (strPosValue == null) {
			throw new CrtException(this.getClass() + " The " + strPosKey + " has not been created yet!!!");
		}
		
		int iPos = Integer.parseInt(strPosValue);
		
		iPos++;
		
		mGlobal.put(strPosKey, String.valueOf(iPos));
		return null;
	}

	@Override
	public int getArgsSize() {
		return 1;
	}
}
