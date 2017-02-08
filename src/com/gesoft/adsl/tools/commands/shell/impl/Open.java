package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtExecutor;

/**
 * @author Wu
 * #Method class, create a SSH2-connection in GlobalInfo.mGlobla(Map)
 */
class Open extends Command{

	@Override
	public int getArgsSize() {
		return 3;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strHost = (String)arrArgs.get(0);
		String strUserName = (String)arrArgs.get(1);
		String strPwd = (String)arrArgs.get(2);
		
		strHost = (String) ifExistThenGet(mGlobal, strHost);
		strUserName = (String) ifExistThenGet(mGlobal, strUserName);
		strPwd = (String) ifExistThenGet(mGlobal, strPwd);

		CrtExecutor ssh2 = new CrtExecutor();
		ssh2.connect(strHost, strUserName, strPwd);

		mGlobal.put("SSH2", ssh2);
		
		return null;
	}

}
