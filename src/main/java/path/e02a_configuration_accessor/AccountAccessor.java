package path.e02a_configuration_accessor;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(TransactionAccessor.class)
@SuppressWarnings("checkstyle:magicnumber")
public class AccountAccessor extends AccountBaseAccessor {

  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
