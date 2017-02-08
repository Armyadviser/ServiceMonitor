package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;

/**
 * @author Wu
 * #Method class, send a cmd of exit with a exit-code.
 * @author Wu
 *	exit the main-method
 */
class Exit extends Command{

	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {

		return (String) arrArgs.get(0);
	}

}
