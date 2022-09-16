package path.e04_configuration_behavior;

import lombok.Getter;

import com.mx.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.common.lang.Strings;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

import path.lib.Logger;

public class LoggingBehavior extends GatewayBehavior {

  public static class LoggingBehaviorConfiguration {
    @Getter
    @ConfigurationField("logHeader")
    private String logHeader;
  }

  private final LoggingBehaviorConfiguration loggingBehaviorConfiguration;

  public LoggingBehavior(ObjectMap configurations, @Configuration LoggingBehaviorConfiguration loggingBehaviorConfiguration) {
    super(configurations);
    this.loggingBehaviorConfiguration = loggingBehaviorConfiguration;
  }

  @Override
  protected final <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {

    // Before Action code
    String header = loggingBehaviorConfiguration.getLogHeader();
    if (Strings.isNotBlank(header)) {
      System.out.println("\n\n --------------------------------------------------------------------------- ");
      System.out.println(" -- " + header);
    }
    System.out.println(" --------------------------------------------------------------------------- ");

    // Continue the call
    AccessorResponse<T> response = callNext(resultType, request, terminatingBehavior);

    // Before Action code
    Logger.log(response);
    System.out.println(" --------------------------------------------------------------------------- ");
    System.out.println(" --------------------------------------------------------------------------- \n\n");

    return response;
  }
}
