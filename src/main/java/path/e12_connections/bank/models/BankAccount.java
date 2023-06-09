package path.e12_connections.bank.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccount {
  private String id;
  private String accountNumber;
  private String accountStatus;
  private String currentBalance;
  private String availableBalance;

  @Data
  public static class BankAccounts {
    private List<BankAccount> accounts;
  }
}
