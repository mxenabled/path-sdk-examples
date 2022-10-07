package path.e01_configuration_basic;

import java.math.BigDecimal;
import java.util.Objects;

import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.accessors.ResourceNotFoundException;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;

import path.lib.Logger;

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
    checking.setId("ACCT-1");
    checking.setName("Advantage Checking");
    checking.setType("CHECKING");
    checking.setAccountNumber("*9900");
    checking.setBalance(BigDecimal.valueOf(1425.99));
    checking.setAvailableBalance(BigDecimal.valueOf(1415.83));

    Account savings = new Account();
    savings.setId("ACCT-2");
    savings.setName("Super Savings");
    savings.setAccountNumber("*3223");
    savings.setInterestRate(0.012);
    savings.setType("SAVINGS");
    savings.setBalance(BigDecimal.valueOf(16424.73));

    ACCOUNTS = new MdxList<>();
    ACCOUNTS.add(checking);
    ACCOUNTS.add(savings);
  }

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<Account> get(String id) {
    simulateLongCall();
    return AccessorResponse.<Account>builder().result(ACCOUNTS.stream()
        .filter(account -> Objects.equals(id, account.getId()))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Account not found (" + id + ")", "The requested account does not exist")))
        .build();
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    Logger.log("Activating list() on " + getClass().getCanonicalName() + " (" + System.identityHashCode(this) + ")");

    simulateLongCall();
    return AccessorResponse.<MdxList<Account>>builder()
        .result(ACCOUNTS)
        .status(PathResponseStatus.OK)
        .build();
  }

  private void simulateLongCall() {
    try {
      Thread.sleep(1000); // simulate long call
    } catch (InterruptedException ignored) {
    }
  }

}
