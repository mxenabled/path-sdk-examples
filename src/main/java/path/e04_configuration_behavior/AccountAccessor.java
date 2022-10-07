package path.e04_configuration_behavior;

import java.util.Random;

import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.models.MdxList;
import com.mx.models.account.Account;

@SuppressWarnings("checkstyle:magicnumber")
public class AccountAccessor extends AccountBaseAccessor {
  private static final Random RAND = new Random();

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Account>> list() {
    // Simulate call
    try {
      Thread.sleep(1000 + RAND.nextInt(500));
    } catch (InterruptedException ignore) {
    }

    return new AccessorResponse<MdxList<Account>>()
        .withResult(new MdxList<>())
        .withStatus(PathResponseStatus.OK);
  }

}
