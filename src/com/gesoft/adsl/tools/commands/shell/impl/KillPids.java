package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;
import com.gesoft.adsl.tools.ssh2.CrtExecutor;

/**
 * @author WU
 *#Method,kill the process in the PIDS_LIST
 */
class KillPids extends Command{

	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs)
			throws Exception {
		String strPids = (String) arrArgs.get(0);
		@SuppressWarnings("unchecked")
		ArrayList<String> listPids = (ArrayList<String>) ifExistThenGet(mGlobal, strPids);

		if(null == listPids){
			throw new CrtException(this.getClass() + " Map doesn't have PIDS_LIST !!!");
		}
			
    	CrtExecutor crt = (CrtExecutor) mGlobal.get("SSH2");
		for (String pid : listPids) {
			crt.run("kill -9 " + pid);
			Thread.sleep(500);
		}
    	
    	return null;
	}

	@Override
	public int getArgsSize() {
		return 1;
	}
}
