package path.e02a_configuration_accessor;

import com.mx.accessors.id.IdBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.models.id.Authentication;

import path.lib.Logger;

public class IdAccessor extends IdBaseAccessor {

  public IdAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<Authentication> authenticate(Authentication authentication) {

    /**
     * Retrieve arbitrary value from the configurations block
     */
    String configurationValue = getConfiguration().getConfigurations().getAsString("key1");
    Logger.log("key1 = " + configurationValue);

    Authentication session = new Authentication()
        .withUserId("user-123");

    return new AccessorResponse<Authentication>()
        .withResult(session)
        .withStatus(PathResponseStatus.OK);
  }

}
