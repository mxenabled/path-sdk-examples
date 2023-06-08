package path.e04_configuration_behavior;

import java.util.Random;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.account.Account;

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
