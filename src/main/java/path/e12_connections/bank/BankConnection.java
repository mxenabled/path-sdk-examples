package path.e12_connections.bank;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.connect.http.HttpAccessorConnection;
import com.mx.path.connect.http.HttpRequest;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.connect.UpstreamErrorException;
import com.mx.path.core.common.http.HttpStatus;

import path.e12_connections.bank.models.BankAccount;
import path.e12_connections.bank.models.BankAccountSubType;
import path.e12_connections.bank.models.BankAccountType;

@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class BankConnection extends HttpAccessorConnection {

  private transient Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private final BankConnectionConfiguration configuration;

  public BankConnection(@Configuration BankConnectionConfiguration connectionConfiguration) {
    this.configuration = connectionConfiguration;
  }

  public final List<BankAccount> getAccounts(String customerId) {
    return request("customers/" + customerId + "/accounts")
        .withProcessor((resp) -> {
          BankAccount.BankAccounts accounts = gson.fromJson(resp.getBody(), BankAccount.BankAccounts.class);
          return accounts.getAccounts();
        })
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get()
        .throwException()
        .getObject();
  }

  public final BankAccount getAccount(String customerId, String accountId) {
    return request("customers/" + customerId + "/accounts/" + accountId)
        .withProcessor((resp) -> gson.fromJson(resp.getBody(), BankAccount.class))
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get()
        .throwException()
        .getObject();
  }

  public final List<BankAccountType> getAccountTypes() {
    return request("administration/account_types")
        .withProcessor((resp) -> {
          BankAccountType.BankAccountTypes accounts = gson.fromJson(resp.getBody(), BankAccountType.BankAccountTypes.class);
          return accounts.getAccountTypes();
        })
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get()
        .throwException()
        .getObject();
  }

  public final List<BankAccountSubType> getAccountSubTypes(String accountTypeId) {
    return request("administration/account_types/" + accountTypeId + "/account_subtypes")
        .withProcessor((resp) -> {
          BankAccountSubType.BankAccountSubTypes accounts = gson.fromJson(resp.getBody(), BankAccountSubType.BankAccountSubTypes.class);
          return accounts.getAccountSubtypes();
        })
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get()
        .throwException()
        .getObject();
  }

  @Override
  public final HttpRequest request(String path) {
    HttpRequest httpRequest = super.request(path);
    httpRequest.withHeader("API-KEY", configuration.getApiKey()); // Adding API key header to all requests.

    return httpRequest;
  }
}
