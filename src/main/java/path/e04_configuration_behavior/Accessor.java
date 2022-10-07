package path.e04_configuration_behavior;

import com.mx.accessors.BaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(AccountAccessor.class)
@ChildAccessor(TransferAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
