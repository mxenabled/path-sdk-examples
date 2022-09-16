package path.e12_connections;

import java.io.IOException;
import java.util.Map;

import com.mx.accessors.AccessorResponse;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

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

    Session.createSession();
    Session.current().setUserId("user1");

    RequestContext.builder().clientId("demo").build().register();

    AccessorResponse<MdxList<Account>> accounts1 = gateway.accounts().list();
    Logger.log(accounts1);

    Logger.log("Interaction complete");
  }
}
