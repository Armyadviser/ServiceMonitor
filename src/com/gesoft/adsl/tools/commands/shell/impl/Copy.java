package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Wu
 * #Method,copy the strReturn to strLog and create a object in the Map.
 */
class Copy extends Command{

	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs)
			throws Exception {
		String strLog = (String) arrArgs.get(0);
		String strReturn = (String) arrArgs.get(1);
		
		String strReturnValue = (String)ifExistThenGet(mGlobal, strReturn); 
		
		if(strReturnValue == null){
			throw new CrtException(this.getClass() + " The " + strReturn + " doesn't created in the Map!!");
		}
		
		mGlobal.put(strLog, strReturnValue);
		mGlobal.remove(strReturn);
		return null;
	}

	@Override
	public int getArgsSize() {
		return 2;
	}
}
