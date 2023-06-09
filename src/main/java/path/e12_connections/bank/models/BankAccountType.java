package path.e12_connections.bank.models;

import java.util.List;

import lombok.Data;

@Data
public class BankAccountType {
  private String id;
  private String accountTypeName;
  private String accountTypeAbbreviation;

  @Data
  public static class BankAccountTypes {
    private List<BankAccountType> accountTypes;
  }
}
