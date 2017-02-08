package com.gesoft.adsl.tools.commands.shell;

import com.gesoft.adsl.tools.commands.shell.impl.JudgeCommand;
import com.gesoft.adsl.tools.ssh2.CommandInfo;
import com.gesoft.adsl.tools.ssh2.CrtException;
import com.gesoft.adsl.tools.ssh2script.ScriptParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellMain {

	private int nCurrentPos;

	private int mScriptLength;

	private List<CommandInfo> mCmdList;

	/**
	 * Global parameter pairs.
	 */
	private Map<String, Object> mGlobal;

	public ShellMain(String scriptPath) {
		mCmdList = ScriptParser.parse(scriptPath);
		nCurrentPos = 0;
		mScriptLength = mCmdList.size();
		mGlobal = new HashMap<String, Object>();
	}

	public void setArgument(String name, String value) {
		mGlobal.put(name, value);
	}

	public void removeArgument(String name) {
		mGlobal.remove(name);
	}

	public String getArgument(String name) {
		return (String) mGlobal.get(name);
	}
	
	public String run() {
		try {
			for(nCurrentPos = 0; nCurrentPos < mScriptLength; nCurrentPos++){
				CommandInfo mCommInfo = mCmdList.get(nCurrentPos);
				if (mCommInfo == null) {
					continue;
				}

				String strCmdName = mCommInfo.name;
				List<Object> paramList = mCommInfo.arrArgs;

				//从工厂获取对应实现类
				Command cmd = JudgeCommand.getCommandInstance(strCmdName);
				if (cmd == null) {
					continue;
				}

				//检查参数是否正确
				cmd.checkParams(mGlobal, paramList);

				String msg = cmd.runItem(mGlobal, paramList);
				if (msg == null) {
					continue;
				}

				try {
					return msg;
				} catch (NumberFormatException e) {
					//出现异常说明执行的是jump方法
					//找到tag方法中标签为msg的位置
					if (jumpToTag(msg)) {
						continue;
					}
					throw new CrtException("jump方法中tag参数:" + msg + "错误");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean jumpToTag(String tag) {
		for (int i = 0; i < mScriptLength; i++) {
			CommandInfo ci = mCmdList.get(i);
			if ("tag".equals(ci.name)) {
				String tagName = (String) ci.arrArgs.get(0);
				if (tagName.equals(tag)) {
					nCurrentPos = i;
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		String path = "/home/falcon/test/test.script";
		ShellMain sm = new ShellMain(path);
		sm.setArgument("global_login", "lyelepgzz0120jtt");
		sm.setArgument("global_file", "20170208_fail");
		sm.run();
		System.out.println(sm.getArgument("MSG"));
	}
	
}

