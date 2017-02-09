package com.gesoft.adsl.tools.ssh2script;

import com.gesoft.adsl.tools.ssh2.CommandInfo;
import org.junit.Test;

import java.util.List;

/**
 * Created by falcon on 17-2-9.
 *
 */
public class ScriptParserTest {
    @Test
    public void relativePath() throws Exception {
        String script = "/home/falcon/SVNProjects/Csr/src/operatefile/more1.txt";
        List<CommandInfo> cmds = ScriptParser.parse(script);
        System.out.println(cmds);
    }
}
