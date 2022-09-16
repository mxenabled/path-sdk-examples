package path.e02b_configuration_accessor;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.id.IdBaseAccessor;
import com.mx.common.configuration.Configuration;
import com.mx.common.connect.AccessorConnectionSettings;
import com.mx.models.id.Authentication;
import com.mx.path.gateway.configuration.annotations.Connection;

import path.lib.Logger;

public class IdAccessor extends IdBaseAccessor {

  private final AccessorConfigs configs;
  private final SuperAuthConnection superAuthConnection;

  public IdAccessor(
      AccessorConfiguration configuration,
      @Configuration AccessorConfigs configs,
      @Connection("superAuth") SuperAuthConnection superAuthConnection) {
    super(configuration);
    this.configs = configs;
    this.superAuthConnection = superAuthConnection;
  }

  @Override
  public final AccessorResponse<Authentication> authenticate(Authentication authentication) {
    /**
     * Retrieve arbitrary value from the configurations block
     */
    Logger.log("clientSecret = " + configs.getClientSecret());
    Logger.log("logCalls = " + configs.isShouldLogCalls());
    Logger.log("clientIdentifier = " + superAuthConnection.getConnectionConfig().getClientIdentifier());

    AccessorConnectionSettings connection = getConfiguration().getConnections().getConnection("superAuth");
    Logger.log("baseUrl from AccessorConnectionSettings = " + connection.getBaseUrl());

    Authentication session = new Authentication().withUserId("user1").withId("session1");

    return AccessorResponse.<Authentication>builder().result(session).status(AccessorResponseStatus.OK).build();
  }

}
