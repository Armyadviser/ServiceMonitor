package com.gesoft.adsl.monitor;

import com.gesoft.adsl.tools.JTools;
import com.gesoft.adsl.tools.commands.shell.ShellMain;
import com.gesoft.adsl.tools.http.HttpRequest;
import com.gesoft.adsl.tools.log.Log;
import com.gesoft.adsl.tools.mail.MailSender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Storm_Falcon
 * 根据配置文件检查某些服务运行状态
 */
public class ServiceMonitor {

	private ServiceMonitorReader mMonitor = null;
	private Log mSysLog = null;
	
	/**
	 * 保存每个被监控服务的线程句柄
	 */
	private HashMap<String, Thread> mServiceThreadInstance = null;
	
	public ServiceMonitor(String iniFilePath) throws Exception {
		//获取配置文件信息实例
		mMonitor = ServiceMonitorReader.getServiceMonitorReader(iniFilePath);
		mServiceThreadInstance = GlobalServiceThreadInstance.getServiceThreadMap();
		
		mSysLog = mMonitor.getSysLog();
	}

	public void start() {
		//获取时间戳，作为监控开始时间
		long lStart = System.currentTimeMillis();
		while (true) {
			//日志，检查开始
			mSysLog.toLog(JTools.getSysTimeStr("yyyy-MM-dd HH:mm:ss") + " Check Start.");
			String strNow = JTools.getSysTimeStr("HH:mm");
			long lNow = System.currentTimeMillis();
			//服务从开始到现在运行了多少分钟
			int iPassed = (int) ((lNow - lStart) / 60 / 1000);
			
			//遍历Service
			for (int i = 1; ; i++) {
				String strSection = "Service" + i;
				String strServiceName = mMonitor.getName(strSection);
				if (strServiceName == null) {
					break;
				}
				
				//#enable=false
				boolean isEnable = mMonitor.isEnable(strSection);
				if (!isEnable) {
					continue;
				}
				
				//是否进行服务监察的标志
				boolean bExecute;
				//比对系统时间和服务监察时间
				String time = mMonitor.getCheckTime(strSection);
				try {
					//此处为每过几分钟检查一次
					int iTmp = Integer.parseInt(time);
					bExecute = iPassed % iTmp == 0;
				} catch (NumberFormatException e) {
					//此处为固定时间检查
					bExecute = strNow.equals(time);
				}
					
				//如果不符合检查时间或者上次检查还未完成，则不进行检查
				if (!bExecute || mServiceThreadInstance.get(strServiceName) != null) {
					mSysLog.toLog(strServiceName + " not run.");
					continue;
				}

				//开始检查
				startCheck(strSection);
				mSysLog.toLog(strServiceName + " run.");
			}

			//日志，检查结束
			mSysLog.toLog(JTools.getSysTimeStr("yyyy-MM-dd HH:mm:ss") + " Check Over.\r\n");
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException ignored) {}
		}
	}
	
	/**
	 * 启动线程检查服务运行状态
	 * @param strSection 要检查的服务
	 */
	private void startCheck(final String strSection) {
		Thread thread = new Thread() {
			public void run(){
				mServiceThreadInstance.put(strSection, this);
				
				//本次检查日志
				StringBuffer sbLog = new StringBuffer();
				
				int nRepeat = mMonitor.getRepeatTime(strSection);
				for (int currentTime = 1; currentTime <= nRepeat; currentTime++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignored) {}

					
					//在日志中生成开始时间
					long lStart = System.currentTimeMillis();
					sbLog.append("-----").append(JTools.getSysTimeStr("yyyy-MM-dd HH:mm:ss "))
							.append(strSection).append(" Start");
					if (currentTime == 1) {
						sbLog.append("-----\n");
					} else {
						sbLog.append("Retry:").append(currentTime).append("-----\n");
					}
					//执行脚本，获得返回值
					ShellMain sm = new ShellMain(
							mMonitor.getBasePath() + mMonitor.getScriptPath(strSection));
					sm.run();
					int nExitCode = sm.getExitCode();
					
					//判断是否发送短信或邮件
					boolean bSend = ifSend(strSection, nExitCode, currentTime, nRepeat);
					
					//根据返回值从信息列表中获取对应意义
					String strMessage = mMonitor.getMessage(nExitCode);
					sbLog.append("ExitCode:").append(nExitCode)
							.append(",Check Result：").append(strMessage).append("\n");
					
					if (bSend) {
						String strNow = JTools.getSysTimeStr("HH:mm");
						String msgContent = strNow + " 执行:" + mMonitor.getName(strSection) + "," + strMessage;
						//发送短信或邮件
						String[] action = mMonitor.getAction(strSection);
						for (String anAction : action) {
							if (anAction.equals("SendMsg")) {
								sendMessage(strSection, msgContent, sbLog);
							} else if (anAction.equals("SendEmail")) {
								sendEmail(strSection, msgContent, sbLog);
							} else {
								sbLog.append(mMonitor.getName(strSection))
										.append(": action error :").append(anAction).append("\n");
							}
						}
					}
					
					//生成日志结束时间
					long lNow = System.currentTimeMillis();
					long lPassed = (lNow - lStart) / 1000;
					sbLog.append("-----").append(JTools.getSysTimeStr("yyyy-MM-dd HH:mm:ss "))
							.append(strSection).append(" End ").append(lPassed).append("s-----\n");
					
					//正常运行，不进行下一次尝试
					if (nExitCode == 0) {
						break;
					}
				}
				mSysLog.toLog(sbLog.toString());
				mServiceThreadInstance.remove(strSection);
			}
		};
		thread.start();
	}
	
	/**
	 * 判断是否发送短信或邮件
	 * 此处有多种条件：
	 * 如果是每天定时类型，则发送条件需满足
	 * 		(返回值为0 || 重试多次都不为0) && condition字段中含有返回值
	 * 如果是健康性检查，则发送条件需满足
	 * 		(重试多次都不为0 && condition字段中含有返回值)
	 * @param iExitCode 执行返回值
	 * @param currentTime 当前是第几次尝试
	 * @param nRepeat 最大尝试次数
	 * @return 是否发送
	 */
	private boolean ifSend(String strSection, int iExitCode, int currentTime, int nRepeat) {

		//condition字段中是否含有返回值
		boolean bContains = false;
		String strExitCode = iExitCode + "";
		String[] condition = mMonitor.getCondition(strSection);
		for (String aCondition : condition) {
			if (strExitCode.equals(aCondition)) {
				bContains = true;
				break;
			}
		}

		//是否是最后一次尝试
		boolean bLastTry = currentTime == nRepeat;

		//检查类型，true：每天定时，false：健康性
		boolean bType = mMonitor.getCheckTime(strSection).contains(":");

		return bType && (iExitCode == 0 || bLastTry) && bContains || !bType && bLastTry && bContains;

	}
	
	/**
	 * http://202.96.74.23:8080/sms/send?DestTermID=[Phone]&MsgContent=s%20[Message]
	 * 
	 * 发送短信
	 * @param strSection 服务
	 * @param msgContent 发送内容
	 * @param sbLog 日志
	 */
	private void sendMessage(String strSection, String msgContent, StringBuffer sbLog) {
		//生成发送信息具体内容
		String encodingMsg = "";
		try {
			encodingMsg = URLEncoder.encode(msgContent, "GBK");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String[] phone = mMonitor.getPhone(strSection);
		for (String aPhone : phone) {
			String url = mMonitor.getSMSUrl(strSection).replace("[Phone]", aPhone);
			url = url.replace("[Message]", encodingMsg);

			String strReturn;
			try {
				strReturn = HttpRequest.sendGet(url);
			} catch (IOException e) {
				e.printStackTrace();
				mSysLog.toLog("SendMessage,Error," + e.getMessage());
				return;
			}
			sbLog.append("SendMessage,").append(aPhone).append(",Content:")
					.append(msgContent).append(",").append(strReturn).append("\n");
		}
	}
	
	
	/**
	 * 发送邮件
	 * @param strSection 服务
	 * @param msgContent 发送内容
	 * @param sbLog 日志
	 */
	private void sendEmail(String strSection, String msgContent, StringBuffer sbLog) {
		final String emailHost = "382599200@qq.com";
		final String emailPwd = "Js3011102102";
		
		MailSender sender = new MailSender(emailHost, emailPwd);
		String[] email = mMonitor.getEamil(strSection);
		try {
			sender.send(email, "服务情报", msgContent);
		} catch (Exception e) {
			e.printStackTrace();
			mSysLog.toLog("SendEmail,Error," + e.getMessage());
			return;
		}
		sbLog.append("SendEmail,").append(Arrays.deepToString(email)).append(",Content:")
				.append(msgContent).append("\n");
	}

	/**
	 * @param args ini全局变量文件路径
	 * "D:/test/config01 service_monitor.txt"
	 */
	public static void main(String[] args) {
		ServiceMonitor sm = null;
		try {
			sm = new ServiceMonitor(args[0]);
//			sm = new ServiceMonitor("E:/Document/service monitor/config01 service_monitor.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sm != null) {
			sm.start();
		}
	}
}
