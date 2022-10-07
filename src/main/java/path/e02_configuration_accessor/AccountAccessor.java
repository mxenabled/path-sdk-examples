package path.e02_configuration_accessor;

import java.math.BigDecimal;

import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;

/**
 * A very simple account accessor that interacts with a static list of accounts
 * <p>
 * Normally, a connection would be made to an external system
 * and the data would be coerced into {@link Account} objects.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class AccountAccessor extends AccountBaseAccessor {

  private static final MdxList<Account> ACCOUNTS;
  static {
    Account checking = new Account();
    checking.setId("ACT-1");
    checking.setName("Your Checking");
    checking.setType("CHECKING");
    checking.setAccountNumber("*9998");
    checking.setBalance(BigDecimal.valueOf(1378.99));
    checking.setAvailableBalance(BigDecimal.valueOf(1358.83));

    Account savings = new Account();
    savings.setId("ACT-2");
    savings.setName("Savings Plus");
    savings.setAccountNumber("*9997");
    savings.setInterestRate(0.011);
    savings.setType("SAVINGS");
    savings.setBalance(BigDecimal.valueOf(9388.73));

    ACCOUNTS = new MdxList<>();
    ACCOUNTS.add(checking);
    ACCOUNTS.add(savings);
  }

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    return new AccessorResponse<MdxList<Account>>()
        .withResult(ACCOUNTS)
        .withStatus(PathResponseStatus.OK);
  }

}
