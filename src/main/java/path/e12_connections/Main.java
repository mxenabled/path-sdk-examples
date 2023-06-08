package path.e12_connections;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.gateway.context.Scope;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {
  public static void main(String... args) throws IOException {
    Logger.log("Example 12 - Building Connections");

    /**
     * Load the gateway configuration yaml and use the GatewayConfigurator to create the gateway
     * Note: the result of the configurator is a Hash of gateways.
     * The configurator supports a multi-tenant setup.
     */
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e12_connections/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the client
     */
    Gateway gateway = gateways.get("demo");

    RequestContext.builder().clientId("demo").build().register();

    Session.createSession();
    Session.current().setUserId("user1");
    Session.current().put(Scope.Session, "bankToken", UUID.randomUUID().toString()); // Simulate previous login and store token

    RequestContext.builder().clientId("demo").build().register();

    AccessorResponse<MdxList<Account>> accounts1 = gateway.accounts().list();
    Logger.log(accounts1);

    Logger.log("Interaction complete");
  }
}
