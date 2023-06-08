package path.e02a_configuration_accessor;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;

@ChildAccessor(TransactionAccessor.class)
@SuppressWarnings("checkstyle:magicnumber")
public class AccountAccessor extends AccountBaseAccessor {

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
