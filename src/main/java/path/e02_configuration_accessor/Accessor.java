package path.e02_configuration_accessor;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.model.mdx.accessor.BaseAccessor;
import com.mx.path.model.mdx.accessor.account.AccountBaseAccessor;
import com.mx.path.model.mdx.accessor.id.IdBaseAccessor;

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
