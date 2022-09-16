package path.e02b_configuration_accessor;

import java.util.Objects;

import lombok.Getter;

import com.mx.common.configuration.Configuration;
import com.mx.common.connect.AccessorConnectionSettings;

public class SuperAuthConnection extends AccessorConnectionSettings {

  @Getter
  private final ConnectionConfig connectionConfig;

  public SuperAuthConnection(@Configuration ConnectionConfig connectionConfig) {
    this.connectionConfig = connectionConfig;
  }

  @Override
  public final boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(super.hashCode(), connectionConfig);
  }
}
