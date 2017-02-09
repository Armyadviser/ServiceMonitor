package com.gesoft.adsl.monitor.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by falcon on 17-2-9.
 * Execute a list of scripts with thread pool.
 */
public class ScriptExecutor {

    private List<ScriptTask> tasks;

    private ExecutorService executorPool;

    /**
     * second.
     * default value is 10s.
     */
    private int timeout;

    private List<String> exeResults;

    public ScriptExecutor(String...scripts) {
        tasks = new ArrayList<ScriptTask>();
        for (String script : scripts) {
            tasks.add(new ScriptTask(script));
        }
        executorPool = Executors.newFixedThreadPool(scripts.length);
        timeout = 10;
    }

    public void setArgument(String name, String value) {
        for (ScriptTask task : tasks) {
            task.setParameter(name, value);
        }
    }

    public void setArguments(Map<String, String> args) {
        for (ScriptTask task : tasks) {
            task.setParameter(args);
        }
    }

    /**
     * Set executor pool's timeout in second.
     * @param timeout executor pool's timeout in second.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void execute() {
        try {
            executorPool.invokeAll(tasks, timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorPool.shutdown();

        saveResults(tasks);
    }

    private void saveResults(List<ScriptTask> tasks) {
        exeResults = new ArrayList<String>();
        for (ScriptTask task : tasks) {
            exeResults.add(task.getMsgReturn());
        }
    }

    public List<String> getExeResults() {
        return exeResults;
    }
}
