package path.e04_configuration_behavior;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

@SuppressWarnings("checkstyle:magicnumber")
public class TimingBehavior extends GatewayBehavior {

  public TimingBehavior(ObjectMap configurations) {
    super(configurations);
  }

  @Override
  protected final <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {

    // Setup to time the call
    long start = System.nanoTime();

    // Make the call
    AccessorResponse<T> response = callNext(resultType, request, terminatingBehavior);

    // Calculate the duration and record in response header
    long duration = System.nanoTime() - start;
    response.withHeader("duration", String.valueOf(duration / 1000000));

    return response;
  }

}
