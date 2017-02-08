package com.gesoft.adsl.tools.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CrtExecutor implements ISSH2 {
	
	/**
	 * Connection
	 */
	private Connection conn = null;
	
	/**
	 * 编码方式
	 */
	private String encode = "GBK";
	
	/**
	 * Session
	 */
	private Session session = null;
	
	/**
	 * 睡眠时间，单位：毫秒
	 */
	private int nSleep = 1500;
	
	/**
	 * session输出流stdout
	 */
	private InputStream stdOut;
	
	/**
	 * session输出流stderr
	 */
	private InputStream stdErr;
	
	/**
	 * session输入流,命令输出流
	 */
	private OutputStream stdIn;
	
	/**
	 * 主机名
	 */
	private String strHost = null;
	
	/**
	 * 设置操作延迟时间
	 */
	public void setOperateDelay(int nMilliSecond) {
		this.nSleep = nMilliSecond;
	}
	
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	/**
	 * 自带密码参数是否为空检查
	 * @return 自带密码参数其中一项为空时，返回false。
	 */
	private boolean isPasswordEmpty(String strHost, String strUserName, String strPassWord) {
		return strHost == null || strUserName == null || strPassWord == null
				|| strHost.length() == 0 || strUserName.length() == 0 || strPassWord.length() == 0;

	}
	/* (non-Javadoc)
	 * @see com.ge.utils.ISSH2#connect()
	 */
	@Override
	public String connect(String strHost, String strUserName, String strPassWord) throws CrtException {
		this.strHost = strHost;
		try {
			
			conn = new Connection(strHost);
			conn.connect();
			
			if (isPasswordEmpty(strHost, strUserName, strPassWord)){
				throw new CrtException(strHost + ", 登录信息中某项信息为空.");
			}
			
			boolean isAuthenticated = conn.authenticateWithPassword(strUserName, strPassWord);
			if (!isAuthenticated) {
				throw new CrtException(strHost + ", 用户名或密码错误.");
			}
			
			//开始会话，读取登录信息
			session = conn.openSession();
			if (session == null) {
				throw new CrtException(strHost + ", 创建session失败.");
			}
			
			//获取输入输出流
			stdOut = session.getStdout();
			stdErr = session.getStderr();
			stdIn = session.getStdin();
			if (stdOut == null || stdErr == null || stdIn == null){
				throw new CrtException(strHost + ", 获取流失败.");
			}
			
			//开始会话
			session.requestDumbPTY();
			session.startShell();
			
			Thread.sleep(nSleep);
			
			return readStream();
		} catch (IOException e) {
			throw new CrtException(strHost + ", Connect:IOException.");
		} catch (InterruptedException e) {
			throw new CrtException(strHost + ", Connect:InterruptedException.");
		}
	}

	/* (non-Javadoc)
	 * @see com.ge.utils.ISSH2#close()
	 */
	@Override
	public void close() throws CrtException {
		try {
			stdIn.close();
			stdOut.close();
			stdErr.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new CrtException(strHost + ", 流关闭异常.");
		}
		
		session.close();
		conn.close();
	}
	private String readStream() throws IOException{
		String str1 = readStream(stdOut);
		String str2 = readStream(stdErr);
		
		//连接两个流的读取结果
		return str1 + str2;
	}
	private String readStream(String strValue, String strTimeOut) throws IOException {
		//超时时间
		long lTimeOut = Long.parseLong(strTimeOut);
		//开始读取时间
		long lStart = System.currentTimeMillis();
		
		String strResult = "";
		while (true) {
			//判断是否超时，在未找到时发生
			long lPassed = System.currentTimeMillis() - lStart;
			if (lPassed / 1000 > lTimeOut){
				stdIn.write(3);
				stdIn.flush();
				
				break;
//				throw new CrtTimeOutException();
			}
			
			String strTemp = readStream();
			strResult += strTemp;
			
			//查找结果集
			//找到了，结束命令
			if (strResult.contains(strValue)){
				stdIn.write(3);
				stdIn.flush();
				break;
			}
		}
		
		return strResult;
	}
	
	private String readStream(InputStream in) throws IOException{
		int length = in.available();
		if (length == 0) {
			return "";
		}
		
		byte[] buff = new byte[length];
		in.read(buff);
		return new String(buff, encode);
	}

	/**
	 * 执行命令strCommand
	 */
	@Override
	public String run(String strCommand) throws CrtException {
		String strResult = null;
		try {
			strCommand += "\n";
			stdIn.write(strCommand.getBytes());
			stdIn.flush();
			Thread.sleep(nSleep);
			
			strResult = readStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw new CrtException(strHost + ", 执行" + strCommand + "时出错");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return strResult;
	}
	
	/**
	 * 执行命令strCommand
	 * 在结果中查找关键字strValue
	 * 超时时间为:strTimeOut(秒)
	 */
	@Override
	public String run(String strCommand, String strValue, String strTimeOut)
			throws CrtException {
		String strResult = null;
		try {
			strCommand += "\n";
			stdIn.write(strCommand.getBytes());
			stdIn.flush();
			Thread.sleep(nSleep);
		
			strResult = readStream(strValue, strTimeOut);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CrtException(strHost + ", 执行" + strCommand + "时出错.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return strResult;
	}

	
	public static void main(String[] args) throws Exception {
		String msg;
		CrtExecutor executor = new CrtExecutor();
		executor.connect("202.96.74.21", "pin74", "zcgx$pin74");
		executor.run("cd gwp");
		msg = executor.run("head 20170207_fail");
		System.out.print(msg);
		
		executor.close();
	}
}
