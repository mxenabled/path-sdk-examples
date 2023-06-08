package com.mx.path.example.gateway.accessor;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.model.mdx.accessor.BaseAccessor;

@ChildAccessor(ExampleOriginationAccessor.class)
public final class ExampleBaseAccessor extends BaseAccessor {
  public ExampleBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
