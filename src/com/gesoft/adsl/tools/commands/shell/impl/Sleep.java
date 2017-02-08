package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;


/**
 * @author Wu
 * #Method class, send a cmd of sleeping some time. 
 */
class Sleep extends Command{

	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strTime = (String) arrArgs.get(0);
		strTime = strTime.toLowerCase();
		int iTime = 0;
		
		if(strTime.endsWith("s")){
			strTime = strTime.replace("s", "");
			iTime = Integer.parseInt(strTime);
			iTime *= 1000;
		}
		else
		if(strTime.endsWith("m")){
			strTime = strTime.replace("m", "");
			iTime = Integer.parseInt(strTime);
			iTime *= 6000;
		}
		
		Thread.sleep(iTime);
		
    	return null;
	}
}
