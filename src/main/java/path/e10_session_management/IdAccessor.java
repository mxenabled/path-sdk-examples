package path.e10_session_management;

import com.mx.accessors.id.IdBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.models.id.Authentication;
import com.mx.path.model.context.Session;

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
    Session.current().put(Session.ServiceIdentifier.Session, "login", authentication.getLogin());

    return new AccessorResponse<Authentication>()
        .withResult(session)
        .withStatus(PathResponseStatus.OK);
  }
}
