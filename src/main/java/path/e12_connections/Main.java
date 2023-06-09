package path.e12_connections;

import java.io.IOException;
import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.core.context.facility.Facilities;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.gateway.event.AfterUpstreamRequestEvent;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {

  /**
   * This is an example of an event subscriber that listens for AfterUpstreamRequest events and logs the request
   * to the console.
   */
  public static class RequestLogListener {
    @Subscribe
    public final void afterRequest(AfterUpstreamRequestEvent event) {
      System.out.println("\nREQUEST --------------------------------------");
      System.out.println("  URI: [" + event.getRequest().getMethod() + "] " + event.getRequest().getUri());
      System.out.println("  Status: " + event.getResponse().getStatus());
      System.out.println("  Body:" + event.getResponse().getBody());
      System.out.println("----------------------------------------------");
    }
  }

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
    Facilities.getEventBus("demo").register(new RequestLogListener());

    RequestContext.builder().clientId("demo").build().register();

    Session.createSession();
    Session.current().setUserId("3415ff69-008c-4323-bd79-fbf188f68397");

    AccessorResponse<MdxList<Account>> accounts1 = gateway.accounts().list();
    Logger.log(accounts1);

    Logger.log("Interaction complete");
  }
}
