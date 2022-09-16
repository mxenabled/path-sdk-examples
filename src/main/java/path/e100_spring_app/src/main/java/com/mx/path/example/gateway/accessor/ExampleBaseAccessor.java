package com.mx.path.example.gateway.accessor;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.BaseAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(ExampleOriginationAccessor.class)
public final class ExampleBaseAccessor extends BaseAccessor {
  public ExampleBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
