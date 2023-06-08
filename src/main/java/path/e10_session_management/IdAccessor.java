package path.e10_session_management;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.context.Scope;
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

    /**
     * NOTE: Storing a value to the session. A session is established at this point.
     */
    Session.current().put(Scope.Session, "login", authentication.getLogin());

    return new AccessorResponse<Authentication>()
        .withResult(session)
        .withStatus(PathResponseStatus.OK);
  }
}
