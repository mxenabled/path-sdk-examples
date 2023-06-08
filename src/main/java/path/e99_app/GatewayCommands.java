package path.e99_app;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;
import com.mx.path.model.mdx.model.id.Authentication;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "gateway", description = "Commands using fully-configured gateway", subcommands = { GatewayAccounts.class })
public class GatewayCommands {

  private Map<String, Gateway> gateways = null;

  @CommandLine.Option(names = { "-g", "--gateway" }, description = "Path to gateway file", defaultValue = "bin/gateway-example-1.yml")
  private String gatewayPath;

  public final Map<String, Gateway> getGateways() {
    if (gateways == null) {
      String gatewayFile = new File(gatewayPath).getAbsolutePath();
      System.out.println("Loading " + gatewayFile);
      String gatewayYaml;
      try {
        gatewayYaml = FileUtils.fileRead(gatewayFile);
      } catch (IOException e) {
        throw new RuntimeException("Unable to load " + gatewayFile, e);
      }
      gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);
    }

    return gateways;
  }

  @CommandLine.Option(names = { "-s", "--session" }, description = "Session key")
  public final void setSessionId(String sessionId) {
    try {
      Session.loadSession(sessionId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Option(names = { "-c", "--client" }, description = "Client id")
  public final void setClientId(String client) {
    RequestContext.builder().clientId(client).build().register();
  }

  public final Gateway getGateway() {
    return getGateways().get(RequestContext.current().getClientId());
  }

  @Command
  public final Integer describe() {
    ObjectMap description = new ObjectMap();
    getGateways().forEach((client, gateway) -> {
      gateway.describe(description.createMap(client));
    });
    Logger.log(description);

    return 0;
  }

  @Command
  public final Integer authenticate(@CommandLine.Parameters(paramLabel = "login") String login, @CommandLine.Parameters(paramLabel = "password") String password) {
    Authentication authentication = new Authentication();
    authentication.setLogin(login);
    authentication.setPassword(password.toCharArray());

    AccessorResponse<Authentication> session = getGateway().id().authenticate(authentication);
    if (session.getStatus() == PathResponseStatus.OK) {
      Session.createSession();
      session.getResult().withId(Session.current().getId());
    }

    Logger.log(session);

    return 0;
  }

}

@Command(name = "accounts", description = "gateway account operations")
class GatewayAccounts {

  @ParentCommand
  private GatewayCommands parent;

  @Command(name = "list")
  public final Integer list() {
    AccessorResponse<MdxList<Account>> accounts = parent.getGateway().accounts().list();
    Logger.log(accounts);

    return 0;
  }

  @Command(name = "get")
  public final Integer get(@CommandLine.Parameters(paramLabel = "accountId") String accountId) {
    AccessorResponse<Account> account = parent.getGateway().accounts().get(accountId);
    Logger.log(account);

    return 0;
  }
}
