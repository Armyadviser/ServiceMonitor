package com.gesoft.adsl.tools.commands.shell;

/**
 * Created by falcon on 17-2-9.
 *
 */
public class ShellMainTest {
    @org.junit.Test
    public void shouldReturnMsg() throws Exception {
        String path = "/home/falcon/test/test.script";
        ShellMain sm = new ShellMain(path);
        sm.setArgument("global_login", "lyelepgzz0120jtt");
        sm.setArgument("global_file", "20170208_fail");
        sm.run();
        System.out.println(sm.getArgument("MSG"));
    }
}
