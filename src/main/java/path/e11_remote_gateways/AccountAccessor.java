package path.e11_remote_gateways;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

import path.lib.Logger;

public final class AccountAccessor extends AccountBaseAccessor {
  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public AccessorResponse<MdxList<Account>> list() {
    Logger.log("AccountAccessor responding to list call");

    Account account1 = new Account();
    account1.setId("1");
    Account account2 = new Account();
    account2.setId("2");

    MdxList<Account> accounts = new MdxList<>();
    accounts.add(account1);
    accounts.add(account2);
    return new AccessorResponse<MdxList<Account>>().withResult(accounts);
  }
}
