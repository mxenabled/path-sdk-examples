package path.e05_configuration_facilities;

import java.io.IOException;
import java.util.Objects;

import com.mx.common.security.EncryptionService;
import com.mx.common.store.Store;
import com.mx.path.gateway.api.GatewayConfigurator;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;
import com.mx.path.model.context.facility.Facilities;
import com.mx.path.model.context.store.ScopedStore;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

@SuppressWarnings("magicnumber")
public class Main {

  public static void main(String... args) throws IOException {
    Logger.log("Example 05 - Facility Configuration and Usage");

    // The static Facilities class is loaded by the configurator
    String gatewayYaml = FileUtils.fileRead("./src/main/java/path/e05_configuration_facilities/gateway.yml");
    new GatewayConfigurator().buildFromYaml(gatewayYaml);

    // ScopedStore.build() requires a request context
    RequestContext.builder().clientId("demo").build().register();

    // Use store facility directly (not recommended)
    Store cacheStore = Facilities.getSessionStore("demo");
    cacheStore.put("key1", "value1", 10);

    // Wrap in scoped store (better)
    Store clientStore = ScopedStore.build(cacheStore, "client");
    clientStore.put("key1", "value2", 10);

    // Note: Getting the same key from each results in different value
    Logger.log("Cache Store Value: key1=" + cacheStore.get("key1"));
    Logger.log("Client Store Value: key1=" + clientStore.get("key1"));

    if (Objects.equals(cacheStore.get("key1"), clientStore.get("key1"))) {
      Logger.log("Values should not be equal!");
      System.exit(1);
    }

    // Use encryption service directly
    EncryptionService encryptionService = Facilities.getEncryptionService("demo");

    String sensitive = "P@$$w0rD";
    String cipherText = encryptionService.encrypt(sensitive);
    String clearText = encryptionService.decrypt(cipherText);

    Logger.log("Sensitive: " + sensitive);
    Logger.log("Encrypted: " + cipherText);
    Logger.log("Decrypted: " + clearText);

    // Session uses encryption service
    Session.createSession();

    // "Secure" Put value to session
    Session.current().sput(Session.ServiceIdentifier.Session, "encrypted", sensitive);

    // Put clear value to session
    Session.current().put(Session.ServiceIdentifier.Session, "clear", sensitive);

    // Look behind the curtains to see what was written to the SessionStore.
    // This would not normally be done! Demonstration purposes only.
    Store sessionStore = Facilities.getSessionStore("demo");
    Logger.log("Value written using sput: " + sessionStore.get(Session.current().getId() + ":Session.encrypted"));
    Logger.log("Value written using put: " + sessionStore.get(Session.current().getId() + ":Session.clear"));

    // Can use get() for both (value will be decrypted if needed)
    Logger.log("Value retrieved using get() for encrypted: " + Session.current().get(Session.ServiceIdentifier.Session, "encrypted"));
    Logger.log("Value retrieved using get() for clear: " + Session.current().get(Session.ServiceIdentifier.Session, "clear"));
  }

}
