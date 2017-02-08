package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Storm_Falcon
 *	标签命令实现类，跳转命令的目的地
 */
class Tag extends Command {

	@Override
	public int getArgsSize() {
		return 1;
	}

	/**
	 * 命令样式tag(end)
	 * end为标签名
	 * 什么都不做，执行下一条语句
	 * @throws CrtException 参数错误
	 */
	@Override
	public String runItem(Map<String, Object> mGlobal,
			List<Object> arrArgs) throws Exception {
		return null;
	}
	
	
}
