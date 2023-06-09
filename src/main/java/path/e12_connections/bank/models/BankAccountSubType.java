package path.e12_connections.bank.models;

import java.util.List;

import lombok.Data;

@Data
public class BankAccountSubType {
  private String id;
  private String accountTypeId;
  private String accountSubtypeName;
  private String accountSubtypeAbbreviation;

  private BankAccountType accountType;

  @Data
  public static class BankAccountSubTypes {
    private List<BankAccountSubType> accountSubtypes;
  }
}
