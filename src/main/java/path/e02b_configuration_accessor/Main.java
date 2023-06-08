package path.e02b_configuration_accessor;

import java.io.IOException;
import java.util.Map;

import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.model.mdx.model.id.Authentication;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {

  public static void main(String... args) throws IOException {

    Logger.log("Example 02b - Configuring Accessors");

    /**
     * Load the gateway configuration yaml and use the GatewayConfigurator to create the gateway
     * Note: the result of the configurator is a Hash of gateways. The configurator supports a multi-tenant
     * setup.
     */
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e02b_configuration_accessor/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the current client
     */
    Gateway gateway = gateways.get("demo");
    Logger.log(gateway.describe());

    gateway.id().authenticate(new Authentication());

  }

}
