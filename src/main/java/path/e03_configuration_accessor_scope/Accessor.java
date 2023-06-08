package path.e03_configuration_accessor_scope;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@MaxScope(AccessorScope.SINGLETON)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
