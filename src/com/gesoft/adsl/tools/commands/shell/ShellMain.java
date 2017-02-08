package com.gesoft.adsl.tools.commands.shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gesoft.adsl.tools.commands.shell.impl.JudgeCommand;
import com.gesoft.adsl.tools.ssh2.CommandInfo;
import com.gesoft.adsl.tools.ssh2.CrtException;
import com.gesoft.adsl.tools.ssh2script.ScriptParser;

public class ShellMain {
	
	public static int executeScript(String strPath) {
		ArrayList<CommandInfo> commandList = ScriptParser.parse(strPath);
		if (commandList == null) {
			return -1;
		}
		return executeScript(commandList);
	}

	public static int executeScript(List<CommandInfo> commandList) {
		try {
			//开始执行
			Map<String, Object> mGlobal = new HashMap<String, Object>();
			int size = commandList.size();
			for(int i = 0; i < size; i++){
				CommandInfo mCommInfo = commandList.get(i);
				if (mCommInfo == null) {
					continue;
				}
				
				String strCmdName = mCommInfo.strName;
				List<Object> paramList = mCommInfo.arrArgs;
				
				//从工厂获取对应实现类
				Command cmd = JudgeCommand.getCommandInstance(strCmdName);
				if (cmd == null) {
					continue;
				}
				
				//检查参数是否正确
				cmd.checkParams(mGlobal, paramList);
				
				String strMsg = cmd.runItem(mGlobal, paramList);
				if (strMsg == null) {
					continue;
				}
				
				try {
					return Integer.parseInt(strMsg);
				} catch (NumberFormatException e) {
					//出现异常说明执行的是jump方法
					//找到tag方法中标签为strMsg的位置
					boolean flag = false;
					for (int jump = 0; jump < size; jump++){
						CommandInfo ci = commandList.get(jump);
						if (ci == null) {
							continue;
						}
						
						String strName = ci.strName;
						if (strName.equals("tag")) {
							String strTagName = (String) ci.arrArgs.get(0);
							if (strTagName.equals(strMsg)){
								//更改执行位置
								i = jump;
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						continue;
					}
					throw new CrtException("jump方法中tag参数:" + strMsg + "错误");
				}
			}
			return 0;
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(executeScript("D:/test/remote cmd/check_boss1_service.txt"));
		System.out.println(executeScript("D:/test/cmd03.txt"));
	}
	
}

