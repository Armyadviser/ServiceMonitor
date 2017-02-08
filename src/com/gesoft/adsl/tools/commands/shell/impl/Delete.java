package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Wu
 * #Method class, remove a object from the GlobalInfo.mGlobal(Map).
 */
class Delete extends Command{

	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs) throws Exception {
		String strParameter = (String) arrArgs.get(0);
		//If the object exist in the map, then remove it!
		
		if(!mGlobal.containsKey(strParameter)){
			throw new CrtException(this.getClass() + " The " + strParameter + " doesn't exsit in the map!!");
		}
		
		mGlobal.remove(strParameter);

		return null;
	}

	@Override
	public int getArgsSize() {
		return 1;
	}

}
