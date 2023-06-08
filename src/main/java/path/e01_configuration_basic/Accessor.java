package path.e01_configuration_basic;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@ChildAccessor(AccountAccessor.class)
@ChildAccessor(IdAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
