package path.e01_configuration_basic;

import com.mx.accessors.BaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(AccountAccessor.class)
@ChildAccessor(IdAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
