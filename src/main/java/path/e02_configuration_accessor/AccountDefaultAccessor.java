package path.e02_configuration_accessor;

import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.AccessorSystemException;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;

import path.lib.Logger;

public class AccountDefaultAccessor extends AccountBaseAccessor {

  public AccountDefaultAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    Logger.log("The configured accessor did not override the default. This should not be executed!");
    throw new AccessorSystemException("The configured accessor did not override the default. This should not be executed!");
  }

}
