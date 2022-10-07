package path.e02b_configuration_accessor;

import com.mx.accessors.BaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(IdAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
