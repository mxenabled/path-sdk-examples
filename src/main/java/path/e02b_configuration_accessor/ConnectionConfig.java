package path.e02b_configuration_accessor;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class ConnectionConfig {
  @ConfigurationField
  private String clientIdentifier;
}
