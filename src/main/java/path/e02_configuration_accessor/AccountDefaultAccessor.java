package path.e02_configuration_accessor;

import com.mx.path.core.common.accessor.BadRequestException;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

import path.lib.Logger;

public class AccountDefaultAccessor extends AccountBaseAccessor {

  public AccountDefaultAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    Logger.log("The configured accessor did not override the default. This should not be executed!");
    throw new BadRequestException("The configured accessor did not override the default. This should not be executed!");
  }
}
