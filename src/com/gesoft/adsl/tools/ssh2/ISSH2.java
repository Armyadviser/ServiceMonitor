package com.gesoft.adsl.tools.ssh2;

public interface ISSH2 {
	
	/**
	 * 连接服务器
	 * @return	登录信息
	 * @throws Exception	登陆异常
	 */
	String connect(String host, String username, String password) throws Exception;
	
	/**
	 * 关闭
	 * @throws Exception	关闭异常
	 */
	void close() throws Exception;
	
	/**
	 * 执行立即返回型命令
	 * @return	执行结果
	 * @throws Exception	命令错误
	 */
	String run(String strCommand) throws Exception;
	
	/**
	 * 执行耗时型、查找型等命令
	 * @param strValue	查找内容
	 * @param strTimeOut	超时时间，单位：秒
	 * @return 查找结果
	 */
	String run(String strCommand, String strValue, String strTimeOut) throws Exception;
}
