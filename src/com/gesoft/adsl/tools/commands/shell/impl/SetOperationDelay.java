package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;
import com.gesoft.adsl.tools.ssh2.CrtExecutor;

class SetOperationDelay extends Command {

	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs) throws Exception {
		CrtExecutor crt = (CrtExecutor)mGlobal.get("SSH2");
		if(null == crt){
			throw new CrtException("(Close)The connection has not been created yet!!!");
		}
		
		String sTime = (String) arrArgs.get(0);
		int nTime = Integer.parseInt(sTime);
		crt.setOperateDelay(nTime);
		
		return null;
	}

}
