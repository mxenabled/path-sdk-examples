package path.e10_session_management;

import java.math.BigDecimal;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.model.context.Session;

import path.lib.Logger;

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
    /**
     * Retrieving value from session
     */
    Logger.log("Getting accounts for user: " + Session.current().get(Session.ServiceIdentifier.Session, "login"));

    return new AccessorResponse<MdxList<Account>>()
        .withResult(ACCOUNTS)
        .withStatus(AccessorResponseStatus.OK);
  }

}
