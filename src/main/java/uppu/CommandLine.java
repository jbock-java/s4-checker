package uppu;

import net.jbock.Command;
import net.jbock.Parameter;

import java.io.File;

@Command
interface CommandLine {

    @Parameter(index = 0)
    File input();
}
