package path.e04_configuration_behavior;

import java.io.IOException;
import java.util.Map;

import com.mx.models.transfer.options.TransferListOptions;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {
  public static void main(String... args) throws IOException {
    Logger.log("Example 04 - Behavior Configuration");

    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e04_configuration_behavior/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the client
     */
    Gateway gateway = gateways.get("demo");
    //    Logger.log(gateway.describe());

    gateway.accounts().list();
    gateway.transfers().list(new TransferListOptions());

    Logger.log("Interaction complete");
  }
}
