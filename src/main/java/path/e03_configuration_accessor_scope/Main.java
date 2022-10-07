package path.e03_configuration_accessor_scope;

import java.io.IOException;
import java.util.Map;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;
import com.mx.models.transfer.Transfer;
import com.mx.models.transfer.options.TransferListOptions;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;

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
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e03_configuration_accessor_scope/gateway.yml");
    Map<String, Gateway> gateways = new GatewayConfigurator().buildFromYaml(gatewayYaml);

    /**
     * Pick the gateway for the client
     */
    Gateway gateway = gateways.get("demo");
    Logger.log(gateway.describe());

    Logger.log("Accounts should be singleton scoped");
    AccessorResponse<MdxList<Account>> accounts1 = gateway.accounts().list();
    AccessorResponse<MdxList<Account>> accounts2 = gateway.accounts().list();
    String accounts1AccessorId = accounts1.getHeaders().get("accessorId");
    String accounts2AccessorId = accounts2.getHeaders().get("accessorId");
    Logger.log("Account call 1: " + accounts1AccessorId);
    Logger.log("Account call 2: " + accounts2AccessorId);

    if (!accounts1AccessorId.equals(accounts2AccessorId)) {
      Logger.log("Accessor IDs don't match!");
      System.exit(500);
    }

    Logger.log("Transfers should be prototype scoped");
    AccessorResponse<MdxList<Transfer>> transfers1 = gateway.transfers().list(new TransferListOptions());
    AccessorResponse<MdxList<Transfer>> transfers2 = gateway.transfers().list(new TransferListOptions());
    String transfers1AccessorId = transfers1.getHeaders().get("accessorId");
    String transfers2AccessorId = transfers2.getHeaders().get("accessorId");
    Logger.log("Transfer call 1: " + transfers1AccessorId);
    Logger.log("Transfer call 2: " + transfers2AccessorId);

    if (transfers1AccessorId.equals(transfers2AccessorId)) {
      Logger.log("Accessor IDs match!");
      System.exit(500);
    }

    Logger.log("Interaction complete");
  }
}
