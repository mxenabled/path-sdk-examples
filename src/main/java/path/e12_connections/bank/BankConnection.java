package path.e12_connections.bank;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.connect.http.HttpAccessorConnection;
import com.mx.path.connect.http.HttpRequest;
import com.mx.path.connect.http.HttpResponse;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.connect.UpstreamErrorException;
import com.mx.path.core.common.http.HttpStatus;

import path.e12_connections.bank.models.BankAccount;

@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class BankConnection extends HttpAccessorConnection {

  private transient Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private final BankConnectionConfiguration configuration;

  public BankConnection(@Configuration BankConnectionConfiguration connectionConfiguration) {
    this.configuration = connectionConfiguration;
  }

  @SuppressWarnings("unchecked")
  public final List<BankAccount> getAccounts(String customerId) {
    HttpResponse response = request("customers/" + customerId + "/accounts")
        .withProcessor((resp) -> {
          BankAccount.BankAccounts accounts = gson.fromJson(resp.getBody(), BankAccount.BankAccounts.class);
          return accounts.getAccounts();
        })
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get();

    return response.getObject();
  }

  @Override
  public final HttpRequest request(String path) {
    HttpRequest httpRequest = super.request(path);
    httpRequest.withHeader("API-KEY", configuration.getApiKey()); // Adding API key header to all requests.

    return httpRequest;
  }
}
