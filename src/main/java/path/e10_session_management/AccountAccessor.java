package path.e10_session_management;

import java.math.BigDecimal;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.context.Scope;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

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
    Logger.log("Getting accounts for user: " + Session.current().get(Scope.Session, "login"));

    return new AccessorResponse<MdxList<Account>>()
        .withResult(ACCOUNTS)
        .withStatus(PathResponseStatus.OK);
  }

}
