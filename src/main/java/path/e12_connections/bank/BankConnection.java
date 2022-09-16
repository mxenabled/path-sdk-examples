package path.e12_connections.bank;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mx.common.configuration.Configuration;
import com.mx.common.connect.ConnectException;
import com.mx.common.http.HttpStatus;
import com.mx.path.api.connect.http.HttpAccessorConnection;
import com.mx.path.api.connect.http.HttpRequest;
import com.mx.path.api.connect.http.HttpResponse;
import com.mx.path.model.context.Session;

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
  public final List<BankAccount> getAccounts(String memberId) {
    HttpResponse response = request("accounts")
        .withQueryStringParam("memberId", memberId)
        .withHeader("cache-ctrl", "NOCACHE")
        .withProcessor((resp) -> gson.fromJson(resp.getBody(), ACCOUNT_LIST_TYPE.getType()))
        .withOnComplete((resp) -> {
          if (resp.getStatus() != HttpStatus.OK) {
            throw new ConnectException("Failed to get accounts", resp.getStatus(), false, null);
          }
        })
        .get();

    return response.getObject();
  }

  @Override
  public final HttpRequest request(String path) {
    HttpRequest httpRequest = super.request(path);
    httpRequest.withHeader("token", Session.current().get(Session.ServiceIdentifier.Session, "bankToken"));
    httpRequest.withHeader("clientId", configuration.getClientId());

    return httpRequest;
  }
}
