package path.e01_configuration_basic;

import java.io.IOException;
import java.util.Map;

import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;
import com.mx.path.model.mdx.model.id.Authentication;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {
  public static void main(String... args) throws IOException {
    Logger.log("Example 1 - Basic Configuration and Usage");

    /**
     * Load the gateway configuration yaml and use the GatewayConfigurator to create the gateway
     * Note: the result of the configurator is a Hash of gateways.
     * The configurator supports a multi-tenant setup.
     */
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e01_configuration_basic/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the client
     */
    Gateway gateway = gateways.get("demo");
    Logger.log(gateway.describe());

    /**
     * Now we can use the gateway to interact with disparate systems
     */
    Logger.log("Authenticating user");
    Authentication authentication = new Authentication();
    authentication.setLogin("testuser");
    authentication.setPassword("password123".toCharArray());

    AccessorResponse<Authentication> authenticationResult = gateway.id().authenticate(authentication);
    Logger.log(authenticationResult);

    Logger.log("Listing user's accounts");
    AccessorResponse<MdxList<Account>> accounts = gateway.accounts().list();
    Logger.log(accounts);

    String accountId = accounts.getResult().get(0).getId();
    Logger.log("Get user account " + accountId);
    AccessorResponse<Account> account = gateway.accounts().get(accountId);
    Logger.log(account);

    Logger.log("Interaction complete");
  }
}
