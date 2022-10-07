package path.e03_configuration_accessor_scope;

import com.mx.accessors.BaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;

@MaxScope(AccessorScope.SINGLETON)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
