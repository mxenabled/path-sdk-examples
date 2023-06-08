package path.e10_session_management;

import java.io.IOException;
import java.util.Map;

import com.mx.path.core.common.accessor.PathResponseStatus;
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

@SuppressWarnings("magicnumber")
public class Main {
  public static void main(String... args) throws IOException {
    Logger.log("Example 10 - Session Management");

    /**
     * Load the gateway configuration yaml and use the GatewayConfigurator to create the gateway
     * Note: the result of the configurator is a Hash of gateways. The configurator supports a multi-tenant
     * setup.
     */
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e10_session_management/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the current client
     */
    Gateway gateway = gateways.get("demo");
    Logger.log(gateway.describe());

    /**
     * RequestContext is required for all calls. It is used by many aspects of the Gateway to determine information
     * about the request being made.
     *
     * **ClientID must be set.**
     */
    RequestContext.builder()
        .clientId("demo")
        .build()
        .register(); // Register sets the RequestContext for the current thread.

    /**
     * It's a best practice to establish a new session on authentication before invoking gateway.id().authenticate()
     *
     * This only creates a new session in memory.
     */
    Session.createSession();

    /**
     * Use the gateway to authenticate.
     */
    Logger.log("Authenticating user");
    Authentication authentication = new Authentication();
    authentication.setLogin("testuser");
    authentication.setPassword("password123".toCharArray());

    AccessorResponse<Authentication> authenticationResult = gateway.id().authenticate(authentication);

    /**
     * Session handling is currently required
     */
    String sessionId = null;
    if (authenticationResult.getStatus() == PathResponseStatus.OK) {
      sessionId = Session.current().getId();
      authenticationResult.getResult().withId(sessionId); // this is required if the result is going to be transmitted
      Session.current().save(); // Save the session state to configured SessionStore
      Logger.log(authenticationResult);
    } else {
      System.exit(401);
    }

    /**
     * Authentication complete and session created. Now we will clear the session from the current thread.
     */
    Session.clearSession();

    /**
     * New Interaction within pre-established session
     * <p>
     * Note: Session.current() is bound to ThreadLocal, so care must be taken when spinning off new threads within an accessor
     */
    Session.loadSession(sessionId);
    Logger.log("Session loaded: " + sessionId);

    AccessorResponse<MdxList<Account>> accounts = gateway.accounts().list();
    Logger.log(accounts);

    /**
     * Interaction complete. Save the session in case something has changed.
     */
    Session.current().save();

    /**
     * Clear contexts like this
     */
    Session.clearSession();
    RequestContext.clear();
  }
}
