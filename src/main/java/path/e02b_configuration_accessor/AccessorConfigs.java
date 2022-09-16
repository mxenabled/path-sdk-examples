package path.e02b_configuration_accessor;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class AccessorConfigs {

  // Note: configuration will fail if clientSecret is not present.
  @ConfigurationField(required = true)
  private String clientSecret;

  @ConfigurationField("logCalls") // Can optionally override the expected configuration field name.
  private boolean shouldLogCalls;

}
