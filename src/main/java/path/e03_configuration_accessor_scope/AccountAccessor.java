package path.e03_configuration_accessor_scope;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

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
        .withStatus(PathResponseStatus.OK)
        // Pass this object id back to caller
        .withHeader("accessorId", String.valueOf(System.identityHashCode(this)));
  }

}
