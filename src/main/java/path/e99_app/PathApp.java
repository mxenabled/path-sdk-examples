package path.e99_app;

import com.mx.path.core.context.Session;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "path-example", subcommands = { AccessorCommands.class, GatewayCommands.class }, mixinStandardHelpOptions = true, version = "demo-cmd 0.0.1", description = "Demo Path on the command line")
public class PathApp {

  public static void main(String... args) {
    int exitCode = new CommandLine(new PathApp()).execute(args);

    if (Session.current() != null) {
      Session.current().save();
    }

    System.exit(exitCode);

  }
}
