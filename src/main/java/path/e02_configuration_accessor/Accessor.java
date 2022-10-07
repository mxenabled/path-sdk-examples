package path.e02_configuration_accessor;

import com.mx.accessors.BaseAccessor;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.accessors.id.IdBaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;

public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final IdBaseAccessor id() {
    return new IdAccessor(getConfiguration());
  }

  @Override
  public final AccountBaseAccessor accounts() {
    // This provides the "default" accessor. This will be overridden in the gateway.yml
    return new AccountDefaultAccessor(getConfiguration());
  }

}
