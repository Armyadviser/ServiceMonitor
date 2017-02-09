package com.gesoft.adsl.monitor.concurrent;

import com.gesoft.adsl.tools.commands.shell.ShellMain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by falcon on 17-2-9.
 * A thread task to execute one script
 */
class ScriptTask implements Runnable, Callable<String> {

    private ShellMain shellMain;

    private Map<String, String> parameterMap;

    private boolean isRunning;

    private String msgReturn;

    public ScriptTask(String scriptPath) {
        shellMain = new ShellMain(scriptPath);
        parameterMap = new HashMap<String, String>();
    }

    public void setParameter(Map<String, String> args) {
        parameterMap.putAll(args);
    }

    public void setParameter(String name, String value) {
        parameterMap.put(name, value);
    }

    private void setArgument() {
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            shellMain.setArgument(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void run() {
        execute();
    }

    @Override
    public String call() throws Exception {
        return execute();
    }

    public String execute() {
        isRunning = true;
        setArgument();
        shellMain.run();
        isRunning = false;
        msgReturn = shellMain.getArgument("MSG_RETURN");
        return msgReturn;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getMsgReturn() {
        return msgReturn;
    }
}
