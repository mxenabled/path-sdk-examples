package path.e04_configuration_behavior;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@ChildAccessor(AccountAccessor.class)
@ChildAccessor(TransferAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
