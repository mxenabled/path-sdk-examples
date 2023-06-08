package path.e01_configuration_basic;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.id.IdBaseAccessor;
import com.mx.path.model.mdx.model.id.Authentication;

public class IdAccessor extends IdBaseAccessor {

  public IdAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<Authentication> authenticate(Authentication authentication) {
    Authentication session = new Authentication()
        .withUserId("user-123");

    return new AccessorResponse<Authentication>()
        .withResult(session)
        .withStatus(PathResponseStatus.OK);
  }

}
