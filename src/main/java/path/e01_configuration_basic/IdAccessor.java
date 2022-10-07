package path.e01_configuration_basic;

import com.mx.accessors.id.IdBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.models.id.Authentication;

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
