package path.e12_connections;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
import path.e12_connections.bank.models.BankAccountSubType;
import path.e12_connections.bank.models.BankAccountType;

@MaxScope(AccessorScope.SINGLETON)
public class AccountAccessor extends AccountBaseAccessor {

  private BankConnection connection;

  public AccountAccessor(AccessorConfiguration configuration, @Connection("myFakeBank") BankConnection connection) {
    super(configuration);
    this.connection = connection;
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    String customerId = Session.current().getUserId();

    List<BankAccount> accounts = connection.getAccounts(customerId);
    MdxList<Account> mdxAccounts = mapToMdxAccounts(accounts);

    return new AccessorResponse<MdxList<Account>>()
        .withResult(mdxAccounts)
        .withStatus(PathResponseStatus.OK);
  }

  private MdxList<Account> mapToMdxAccounts(List<BankAccount> accounts) {
    Map<String, BankAccountSubType> accountSubTypeToType = loadBankAccountSubtypes();

    return accounts.stream().map(bankAccount -> {
      Account mdxAccount = new Account();
      mdxAccount.setId(bankAccount.getId());
      mdxAccount.setAccountNumber(bankAccount.getAccountNumber());
      mdxAccount.setBalance(BigDecimal.valueOf(Double.parseDouble(bankAccount.getCurrentBalance())));

      if (Strings.isNotBlank(bankAccount.getAvailableBalance())) {
        mdxAccount.setAvailableBalance(BigDecimal.valueOf(Double.parseDouble(bankAccount.getAvailableBalance())));
      }

      BankAccountSubType accountType = accountSubTypeToType.get(bankAccount.getAccountSubtypeId());
      mdxAccount.setName(accountType.getAccountType().getAccountTypeName() + " - " + accountType.getAccountSubtypeName());

      return mdxAccount;
    }).collect(Collectors.toCollection(MdxList::new));
  }

  /**
   * This is very inefficient. The data retrieved here is mostly static for the client.
   * We will look at a way to improve this by using Facilities in example13.
   *
   * @return map of accountSubtypeId -> accountSubtype
   */
  private Map<String, BankAccountSubType> loadBankAccountSubtypes() {
    // Load all account types
    List<BankAccountType> accountTypes = connection.getAccountTypes();

    // Load all account subtypes, map them to the account_type, and place them in hash by id.
    return accountTypes.stream().flatMap(accountType -> {
      return connection.getAccountSubTypes(accountType.getId()).stream().map(accountSubType -> {
        accountSubType.setAccountType(accountType);
        return accountSubType;
      });
    }).collect(Collectors.toMap(BankAccountSubType::getId, bankAccountSubType -> bankAccountSubType));
  }
}
