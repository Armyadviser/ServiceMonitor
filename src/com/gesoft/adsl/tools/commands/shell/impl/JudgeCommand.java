package com.gesoft.adsl.tools.commands.shell.impl;

import com.gesoft.adsl.tools.commands.shell.Command;
import com.gesoft.adsl.tools.ssh2.CrtException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author WU
 * factory mode judges in here.
 */
public class JudgeCommand {

	/**
	 * 保存配置文件的键值
	 */
	private final Map<String, Command> mCmdMap;
	
	private JudgeCommand(){
		mCmdMap = loadProperty();
		
//		Thread m = new Thread() {
//			public void run() {
//				try {
//					Thread.sleep(1000*60);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//				HashMap<String, Command> mapTemp = loadProperty();
//				synchronized (mCmdMap) {
//					mCmdMap = mapTemp;
//				}
//			}
//		};
//		m.start();
	}
	
	/**
	 * 将配置文件加载到Map中
	 */
	private Map<String, Command> loadProperty(){
		URL url = JudgeCommand.class.getResource("/commands.properties");
		Properties pro = new Properties();
		try {
			InputStream inStream = url.openStream();
			pro.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Map<String, Command> mCmdMap = new HashMap<String, Command>();
		for (Entry<Object, Object> entry : pro.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();

			try {
				Command cmd = (Command) Class.forName(value).newInstance();
				mCmdMap.put(key, cmd);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("Can't access class " + value);
			} catch (ClassNotFoundException e) {
				System.err.println("No Class match " + key);
			}
		}
		
		return mCmdMap;
	}
	
	/**
	 * 从Map中取值，反射对应类
	 * @return	oper对应实现类
	 */
	private Command getCommand(String oper) throws CrtException {
		Command cmd;
		synchronized (mCmdMap) {
			cmd = mCmdMap.get(oper);
		}
		
		if (cmd == null) {
			throw new CrtException("Command error，no class：" + oper);
		}
		return cmd;
	}
	
	private static JudgeCommand mCommand = null;
	
	public static synchronized Command getCommandInstance(String oper) throws Exception {
		if (mCommand == null) {
			mCommand = new JudgeCommand();
		}
		return mCommand.getCommand(oper);
	}

}
