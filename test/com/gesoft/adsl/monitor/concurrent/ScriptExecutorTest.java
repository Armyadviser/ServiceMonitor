package com.gesoft.adsl.monitor.concurrent;

import org.junit.Test;

import java.util.List;

/**
 * Created by falcon on 17-2-9.
 *
 */
public class ScriptExecutorTest {
    @Test
    public void allTest() throws Exception {
        ScriptExecutor executor = new ScriptExecutor("/home/falcon/test/test.script");
        executor.setTimeout(30);
        executor.setArgument("global_login", "ly07156588b");
        executor.setArgument("global_file", "20170209_fail");
        executor.execute();
        List<String> exeResults = executor.getExeResults();
        System.out.println(exeResults);
    }
}
