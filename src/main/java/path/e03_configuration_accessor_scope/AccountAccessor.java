package path.e03_configuration_accessor_scope;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;

/**
 * This accessor returns an empty account list including a header containing the object id to demonstrate
 * accessor scoping.
 */
@SuppressWarnings("checkstyle:magicnumber")
@MaxScope(AccessorScope.SINGLETON)
public class AccountAccessor extends AccountBaseAccessor {

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    return new AccessorResponse<MdxList<Account>>()
        .withResult(new MdxList<>())
        .withStatus(AccessorResponseStatus.OK)
        // Pass this object id back to caller
        .withHeader("accessorId", String.valueOf(System.identityHashCode(this)));
  }

}
