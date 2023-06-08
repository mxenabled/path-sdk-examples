package path.e11_remote_gateways;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@ChildAccessor(AccountAccessor.class)
public final class Accessor extends BaseAccessor {
  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
