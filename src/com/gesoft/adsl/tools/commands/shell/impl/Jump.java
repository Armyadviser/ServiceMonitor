package com.gesoft.adsl.tools.commands.shell.impl;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

/**
 * @author Storm_Falcon
 *	跳转命令实现类
 */
class Jump extends Command {
	
	/**
	 * 跳转命令
	 * 命令样式：jump(login, in, STR_RETURN, SUCC1)
	 * 如果login 在 STR_RETURN中则跳转到SUCC1处
	 * 命令样式：jump(LIST_PIDS, ==, 0, end)
	 * 如果LIST_PIDS == 0则跳转到end处
	 * @throws CrtException 跳转目标错误
	 * @return tag名称
	 * */
	@Override
	public String runItem(Map<String, Object> mGlobal, List<Object> arrArgs) throws CrtException {
		//要查找的字符串
		String strSearch = (String) arrArgs.get(0);
		//查找类型：in, not in, ==, !=
		String strType = (String) arrArgs.get(1);
		//上一个方法返回字符串
		String strReturn = (String) arrArgs.get(2);
		//跳转目标位置 tag
		String strTarget = (String) arrArgs.get(3);
		
		strSearch = (String) ifExistThenGet(mGlobal, strSearch);
		strReturn = (String) ifExistThenGet(mGlobal, strReturn);
		
		//flag为true时将会跳转，为false不会跳转
		boolean flag = false;
		if (strType.equals("in")) {
			int loc = strReturn.indexOf(strSearch);
			flag = loc != -1;
		} else if (strType.equals("not in")) {
			int loc = strReturn.indexOf(strSearch);
			flag = loc == -1;
		} else if (strType.equals("==")) {
			flag = strSearch.equals(strReturn);
		} else if (strType.equals("!=")) {
			flag = !strSearch.equals(strReturn);
		}
		
		if (flag) {
			return strTarget;
		}
		return null;
	}

	@Override
	public int getArgsSize() {
		return 4;
	}

}
