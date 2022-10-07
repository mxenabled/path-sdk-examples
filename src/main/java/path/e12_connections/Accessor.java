package path.e12_connections;

import com.mx.accessors.BaseAccessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.gateway.configuration.annotations.MaxScope;

@MaxScope(AccessorScope.SINGLETON)
@ChildAccessor(AccountAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
