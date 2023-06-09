package path.e12_connections.bank;

import lombok.Data;

import com.mx.path.core.common.configuration.ConfigurationField;

@Data
public class BankConnectionConfiguration {
  @ConfigurationField(required = true, secret = true)
  private String apiKey;
}
