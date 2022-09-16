package path.e04_configuration_behavior;

import java.util.Random;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.models.MdxList;
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
        .withStatus(AccessorResponseStatus.OK);
  }

}
