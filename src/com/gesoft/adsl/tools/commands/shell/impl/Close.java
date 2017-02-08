package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;
import com.gesoft.adsl.tools.ssh2.CrtExecutor;

/**
 * @author Wu
 * #Method class, close the connection of SSH2.
 */
class Close extends Command {
	public int getArgsSize() {
		return 0;
	}
	
	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs) throws Exception {
		CrtExecutor crt = (CrtExecutor)mGlobal.get("SSH2");
		if(null == crt){
			throw new CrtException("(Close)The connection has not been created yet!!!");
		}
		
		crt.close();
		return null;
	}
}
