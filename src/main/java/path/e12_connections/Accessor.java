package path.e12_connections;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@MaxScope(AccessorScope.SINGLETON)
@ChildAccessor(AccountAccessor.class)
public class Accessor extends BaseAccessor {

  public Accessor(AccessorConfiguration configuration) {
    super(configuration);
  }

}
