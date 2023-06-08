package path.e11_remote_gateways;

import java.io.IOException;
import java.util.Map;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.core.context.tracing.CustomTracer;
import com.mx.path.gateway.accessor.remote.RemoteAccessor;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;

import org.apache.commons.io.FileUtils;

import path.lib.Logger;

import io.opentracing.mock.MockTracer;

public class Main {
  public static void main(String... args) throws IOException, InterruptedException {
    Logger.log("Example 11 - Remote Gateways");

    // You can imagine that this thread represents an isolated application running the Path SDK.
    Thread responderThread = new Thread(() -> {
      Logger.log("Starting responder service on thread " + Thread.currentThread().getId());

      String gatewayYml = null;
      try {
        gatewayYml = FileUtils.fileRead("./src/main/java/path/e11_remote_gateways/responder_gateway.yml");
      } catch (IOException e) {
        e.printStackTrace();
      }

      // The gateway.yml contains the configuration needed to configure:
      // 1. The MessageBroker that will be used to broker requests
      // 2. The Accessor that communicates with an upstream system and translates the data to MDX
      // 3. The Gateway(s) that will be exposed, as well as any operations that will be exposed remotely.
      Map<String, Gateway> gatewayMap = new GatewayConfigurator().buildFromYaml(gatewayYml);

      // Calling `registerRemotes()` will register any remote gateways defined the `gateway.yml` with the configured
      // MessageBroker.
      gatewayMap.get("demo").registerRemotes();
    });

    // You can imagine that this thread represents a different isolated application running the Path SDK.
    Thread requesterThread = new Thread(() -> {
      Logger.log("Starting requester service on thread " + Thread.currentThread().getId());

      // Requests made over the configured `MessageBroker` will expect a tracer to be configured in order to provide
      // tracing headers.
      CustomTracer.setTracer(new MockTracer());

      // An active, authenticated session will be required by the responder.
      Session.createSession();
      Session.current().setSessionState(Session.SessionState.AUTHENTICATED);

      // The `clientId` contained in the `RequestContext` will be used to route the request to associated responder.
      // Since our responder has been configured for the `demo` client, we need to set the clientID to `demo` here.
      // Generally, the clientId will be dictated by the request URL or some other signal based on the application context.
      RequestContext.builder().clientId("demo").build().register();

      String gatewayYaml = null;
      try {
        gatewayYaml = FileUtils.fileRead("./src/main/java/path/e11_remote_gateways/requester_gateway.yml");
      } catch (IOException e) {
        e.printStackTrace();
      }
      // Although we are not exposing any gateways or accessors, we need to configure the `MessageBroker` that `RemoteAccessor`
      // will use.
      new GatewayConfigurator().buildFromYaml(gatewayYaml);

      Logger.log("Requesting a list of accounts via RemoteAccessor");

      // The `RemoteAccessor` class will handle making remote requests over the configured `MessageBroker` for the current
      // client. In this case we are requesting a list of `accounts` over the `ThreadMessageBroker` for the `demo` client.
      RemoteAccessor remoteAccessor = new RemoteAccessor();
      Logger.log(remoteAccessor.accounts().list().getResult());
    });

    // Start the responder "service" and wait for the responders to be registered.
    responderThread.start();
    responderThread.join();

    // Start the requester "service" and wait for the remote request to finish.
    requesterThread.start();
    requesterThread.join();
  }
}
