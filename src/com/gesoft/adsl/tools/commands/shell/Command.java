package com.gesoft.adsl.tools.commands.shell;

import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.ssh2.CrtException;

public abstract class Command {
	/**
	 * 判断参数是否已经在Map中定义过
	 * @return	有：返回对应value	没有：返回原值
	 */
	protected Object ifExistThenGet(Map<String, Object> mGlobal, String strKey) {
		Object objValue = mGlobal.get(strKey);
		if (objValue != null) {
			return objValue;
		}
		return strKey;
	}
	
	/**
	 * 判断方法参数是否合法
	 * @param mGlobal	变量map
	 * @param arrArgs	参数列表
	 */
	void checkParams(Map<String, Object> mGlobal, List<Object> arrArgs) throws CrtException {
		if (mGlobal == null) {
			throw new CrtException(this.getClass() + " The Map is null.");
		}
		
		//检查参数数量
		int listSize = arrArgs.size();
		if (listSize != getArgsSize()) {
			throw new CrtException(this.getClass() + " 参数数量错误\n" +
					"实际参数：" + listSize + " 应有参数：" + getArgsSize());
		}
		
		//检查参数是否有效
		for (Object arrArg : arrArgs) {
			if (arrArg == null) {
				throw new CrtException(this.getClass() + " some params null");
			}
		}
	}
	
	/**
	 * @return	实现方法的参数个数
	 */
	public abstract int getArgsSize(); 
	
	public abstract String runItem(Map<String, Object> mGlobal, List<Object> arrArgs) throws Exception;
}
