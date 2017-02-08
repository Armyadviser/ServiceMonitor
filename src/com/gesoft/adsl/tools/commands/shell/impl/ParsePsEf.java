package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Wu
 * #Method, convert STR_RETURN to LIST_PIDS and put LIST_PIDS in GlobalInfo.mGlobal(Map).
 */
class ParsePsEf extends Command{

	@Override
	public int getArgsSize() {
		return 2;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		String strListPids = (String) arrArgs.get(0);
		String strReturn = (String) arrArgs.get(1);
		
		strReturn = (String) ifExistThenGet(mGlobal, strReturn);
		
		if (strReturn == null) {
			throw new CrtException(this.getClass() + " Map doesn't have STR_RETURN !!!");
		}
		
		ArrayList<String> listPids = new ArrayList<String>();
		
		//得到每一行
		String[] processLine = strReturn.split("\n");
		
		if (processLine.length == 0) {
			mGlobal.put(strListPids, listPids);
	    	return null;
		}
		
		for (String line : processLine) {
			//去掉两个以上空格，保留一个
			line = line.replaceAll("\\s+", " ");
			String[] items = line.split(" ");
			
			//第一个纯数字字符串是进程pid
			if (items.length >= 4) {
				for (String item : items) {
					if (item.matches("[0-9]+")) {
						listPids.add(item);
						break;
					}
				}
			}
    	}
		
    	mGlobal.put(strListPids, listPids);
    	return null;
	}
}
