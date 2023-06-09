package path.e02_configuration_accessor;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.id.IdBaseAccessor;
import com.mx.path.model.mdx.model.id.Authentication;

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
