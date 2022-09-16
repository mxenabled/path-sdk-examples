package path.e12_connections;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.Connection;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.context.Session;

import path.e12_connections.bank.BankConnection;
import path.e12_connections.bank.models.BankAccount;

@MaxScope(AccessorScope.SINGLETON)
public class AccountAccessor extends AccountBaseAccessor {

  private BankConnection connection;

  public AccountAccessor(AccessorConfiguration configuration, @Connection("bank") BankConnection connection) {
    super(configuration);
    this.connection = connection;
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    List<BankAccount> accounts = connection.getAccounts(Session.current().getUserId());
    MdxList<Account> mdxAccounts = accounts.stream().map(bankAccount -> {
      Account mdxAccount = new Account();
      mdxAccount.setId(bankAccount.getId());
      mdxAccount.setName(bankAccount.getDesc());
      mdxAccount.setAccountNumber(bankAccount.getId());
      mdxAccount.setBalance(BigDecimal.valueOf(bankAccount.getBal()));
      mdxAccount.setType(mapType(bankAccount.getT()));

      return mdxAccount;
    }).collect(Collectors.toCollection(MdxList::new));

    return new AccessorResponse<MdxList<Account>>()
        .withResult(mdxAccounts)
        .withStatus(AccessorResponseStatus.OK);
  }

  private String mapType(String t) {
    if (t.equals("CHK")) {
      return "CHECKING";
    } else if (t.equals("SAV")) {
      return "SAVINGS";
    } else {
      return "UNKNOWN";
    }
  }

}
