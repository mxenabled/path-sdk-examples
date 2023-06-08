package path.e12_connections.bank;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
  private static final TypeToken<List<BankAccount>> ACCOUNT_LIST_TYPE = new TypeToken<List<BankAccount>>() {
  };

  private transient Gson gson = new Gson();
  private final BankConnectionConfiguration configuration;

  public BankConnection(@Configuration BankConnectionConfiguration connectionConfiguration) {
    this.configuration = connectionConfiguration;
  }

  @SuppressWarnings("unchecked")
  public final List<BankAccount> getAccounts(String memberId, String token) {
    HttpResponse response = request("accounts", token)
        .withQueryStringParam("memberId", memberId)
        .withHeader("cache-ctrl", "NOCACHE")
        .withProcessor((resp) -> gson.fromJson(resp.getBody(), ACCOUNT_LIST_TYPE.getType()))
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new UpstreamErrorException("Failed to get accounts", resp.getStatus(), PathResponseStatus.UNAVAILABLE);
          }
        })
        .get();

    return response.getObject();
  }

  public final HttpRequest request(String path, String token) {
    HttpRequest httpRequest = super.request(path);
    httpRequest.withHeader("token", token);
    httpRequest.withHeader("clientId", configuration.getClientId());

    System.out.println("CLIENT_ID: " + configuration.getClientId());

    return httpRequest;
  }
}
