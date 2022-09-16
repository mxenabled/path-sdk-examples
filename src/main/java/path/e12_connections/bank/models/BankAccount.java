package path.e12_connections.bank.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccount {
  private String id;
  private String desc;
  private String t;
  private double bal;
}
