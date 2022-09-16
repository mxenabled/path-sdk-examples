package path.e11_remote_gateways;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.BaseAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(AccountAccessor.class)
public final class Accessor extends BaseAccessor {
  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
