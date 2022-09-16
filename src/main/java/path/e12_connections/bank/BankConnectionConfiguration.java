package path.e12_connections.bank;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class BankConnectionConfiguration {
  @ConfigurationField(required = true)
  private String clientId;
}
