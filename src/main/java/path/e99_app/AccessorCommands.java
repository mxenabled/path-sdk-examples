package path.e99_app;

import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;
import com.mx.models.id.Authentication;
import com.mx.path.gateway.configuration.AccessorDescriber;

import path.e01_configuration_basic.Accessor;
import path.lib.Logger;
import picocli.CommandLine;

@SuppressWarnings("visibilitymodifier")
@CommandLine.Command(name = "accessor", description = "Commands using raw accessor", subcommands = { AccessorAccounts.class })
public class AccessorCommands {

  @CommandLine.ParentCommand
  PathApp parent;
  Accessor accessor;

  public AccessorCommands() {
    accessor = new Accessor(AccessorConfiguration.builder().build());
  }

  @CommandLine.Command
  public final Integer describe() {
    AccessorDescriber describer = new AccessorDescriber();
    Logger.log(describer.describeDeep(accessor));

    return 0;
  }

  @CommandLine.Command
  public final Integer authenticate(@CommandLine.Parameters(paramLabel = "login") String login, @CommandLine.Parameters(paramLabel = "password") String password) {

    Authentication authentication = new Authentication();
    authentication.setLogin(login);
    authentication.setPassword(password.toCharArray());

    AccessorResponse<Authentication> session = accessor.id().authenticate(authentication);
    Logger.log(session);

    return 0;
  }
}

@SuppressWarnings("visibilitymodifier")
@CommandLine.Command(name = "accounts")
class AccessorAccounts {

  @CommandLine.ParentCommand
  AccessorCommands parent;

  public final Integer list() {
    AccessorResponse<MdxList<Account>> accounts = parent.accessor.accounts().list();
    Logger.log(accounts);

    return 0;
  }

  public final Integer get(@CommandLine.Parameters(paramLabel = "accountId") String accountId) {
    AccessorResponse<Account> account = parent.accessor.accounts().get(accountId);
    Logger.log(account);

    return 0;
  }
}
