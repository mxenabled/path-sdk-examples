package path.e12_connections;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.Connection;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

import path.e12_connections.bank.BankConnection;
import path.e12_connections.bank.models.BankAccount;

@MaxScope(AccessorScope.SINGLETON)
public class AccountAccessor extends AccountBaseAccessor {

  private BankConnection connection;

  public AccountAccessor(AccessorConfiguration configuration, @Connection("myFakeBank") BankConnection connection) {
    super(configuration);
    this.connection = connection;
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    List<BankAccount> accounts = connection.getAccounts(Session.current().getUserId());
    MdxList<Account> mdxAccounts = accounts.stream().map(bankAccount -> {
      Account mdxAccount = new Account();
      mdxAccount.setId(bankAccount.getId());
      mdxAccount.setAccountNumber(bankAccount.getAccountNumber());
      mdxAccount.setBalance(BigDecimal.valueOf(Double.parseDouble(bankAccount.getCurrentBalance())));

      if (Strings.isNotBlank(bankAccount.getAvailableBalance())) {
        mdxAccount.setAvailableBalance(BigDecimal.valueOf(Double.parseDouble(bankAccount.getAvailableBalance())));
      }

      return mdxAccount;
    }).collect(Collectors.toCollection(MdxList::new));

    return new AccessorResponse<MdxList<Account>>()
        .withResult(mdxAccounts)
        .withStatus(PathResponseStatus.OK);
  }
}
