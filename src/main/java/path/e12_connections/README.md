## 12 - Accessor Connections

Connections and accessors are where all the work of connecting and translating to financial systems is done. Accessors
are responsible for the translation and business logic and the connections are what actually make calls into the
financial system. As with accessors, there is a robust configuration platform that allows for highly-customizable
connection setup. The connection classes have a clean, fluent interface to make the calls.

**Important Files:**

* [Main.java](./Main.java)
* [gateway.yml](./gateway.yml)

### Configuration

Each accessor can be provided with 1 or more connections through the gateway.yml and constructor annotations.

A basic configuration example:

```yaml
gateways:
  accounts:
    accessor:
      class: com.bank.accessors.AccountAccessor
      connections:
        bank:
          baseUrl: https://dataservice.bank.com
```

The corresponding accessor code would use an annotated constructor param to pull in the connection name `bank`

```java
package com.bank.accessors;

import com.mx.accessors.AccessorResponse;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.gateway.configuration.annotations.Connection;
import com.bank.connections.BankConnection;
import com.mx.path.gateway.context.Scope;

public class AccountAccessor extends AccountBaseAccessor {
  private BankConnection bankConnection;

  public AccountAccessor(@Connection("bank") BankConnection bankConnection) {
    this.bankConnection = bankConnection;
  }

  @Override
  public AccessorResponse<MdxList<Account>> list() {
    String bankMemberId = Session.current().get(Scope.Session, "bankMemberId"); // read the bank member id from the current session
    bankConnection.getAccounts(bankMemberId);
  }
}
```

The BankConnection class extends one of the available accessor connection types.

```java
import com.mx.path.api.connect.http.HttpAccessorConnection;

public class BankConnection extends HttpAccessorConnection {
  public List<BankAccount> getAccounts(String memberId) {
    // Connect to the bank services
    Response response = request("/mbr/accounts.srv")
        .withQueryParam("memberId", memberId)
        .get();

    // Process result and return ...
  }
}
```

### request(path)

The request() method is where all connections begin. This creates a _configured_ `Request` object that exposes a fluent
interface. In the example above, the Request has a baseUrl of "https://dataservice.bank.com" already configured.
The `request("/mbr/accounts.srv")` sets `/mbr/accounts.srv` as the path. `.withQueryParam("memberId", memberId)` adds a
memberId query string parameter. `get()` executes the request with the GET Http verb.

Example:

```
GET https://dataservice.bank.com/mbr/accounts.srv?memberId=M322314 
```

The Request fluent interface allows the connection code to add headers, a request body, and processing handlers.

#### Common Request Attributes

Many financial system APIs require all requests to have some common configuration. To avoid needing to duplicate the
code needed to add those attributes to the connection, an override can be added to the request() method.

Example:

```java
import com.mx.common.collections.MultiValueMappable;
import com.mx.common.collections.SingleValueMap;
import com.mx.common.collections.SingleValueMap;
import com.mx.path.api.connect.http.HttpAccessorConnection;
import com.mx.path.gateway.context.Scope;
import com.mx.path.model.context.Session;

import java.util.Map;

public class BankConnection extends HttpAccessorConnection {
  public List<BankAccount> getAccounts(String memberId, String apiSessionKey) {
    // Connect to the bank services
    Response response = request("/mbr/accounts.srv", apiSessionKey)
        .withQueryParam("memberId", memberId)
        .get();

    // Process result and return ...
  }

  @Override
  public void request(String path, String apiSessionKey) {
    Request request = super.request(path);
    request.withHeader("clientId", "C989898");
    request.withHeader("apiSessionKey", apiSessionKey);
  }
}
```

In the example above, every request() result will have the provided `apiSessionKey` and `clientId` headers set. Notice
that the clientId uses a static value. This likely needs to be configured and set to a different value for a test
environment vs production. As is the case with Accessors, AccessorConnections can have a bound configuration object that
provides values from the gateway.yml.

#### Configurations

Connection configurations are provided in the gateway.yml. In the following example, a connection with then `bank` is
attached to the `accounts` gateway.

```yaml
gateways:
  accounts:
    accessor:
      class: com.bank.accessors.AccountAccessor
      connections:
        bank:
          baseUrl: https://dataservice.bank.com
          certificateAlias: bankCert
          keystorePath: ./keystore.jks
          keystorePassword: p@$$w0rd
          skipHostNameVerify: false
```

The request that is returned from the request() will be pre-configured with the following values that were provided in gateway.yml:

- baseUrl
- certificateAlias
- keystorePath
- keystorePassword
- skipHostNameVerify

Most connections will have their own configuration needs. Values like api keys or client flags may be required in all requests.
These can be provided in the `configurations` block.

```yaml
gateways:
  accounts:
    accessor:
      class: com.bank.accessors.AccountAccessor
      connections:
        bank:
          baseUrl: https://dataservice.bank.com
          configurations:
            apiKey: 12345
```

There are 2 ways to get these values in the connection implementation:

- Read directly using `getConfigurations().get("")`
- Bind configurations to a configuration object using constructor annotation (preferred)


###### Reading directly:

The configuration values are available inside the connection class by calling getConfigurations() and reading the values directly. While this approach works, it has some significant disadvantages, mostly concerning discoverability and validation. The configurator has no way of knowing what values are required or what types they should be. 

Example:

**gateway.yml**

```yaml
gateways:
  accounts:
    accessor:
      class: com.bank.accessors.AccountAccessor
      connections:
        bank:
          baseUrl: https://dataservice.bank.com
          configurations:
            apiKey: 12345
```

**ConnectionImplementation.class**

```java
import com.google.gson.Gson;
import com.mx.path.api.connect.http.HttpAccessorConnection;

public class ConnectionImplementation extends HttpAccessorConnection {
  public BankAccount getAccount(String accountId, String sessionKey) {
    // Read the apiKey directly
    String apiKey = getConfigurations().getAsString("apiKey");
    
    return request("accountApi")
        .withHeader("apiKey", apiKey)
        .withQueryStringParam("id", accountId)
        .withProcessor((request, response) -> {
          new Gson().fromJson(response.getBody(), BankAccount.class)
        })
        .get()
        .getObject();
  }
}
```

The above example reads the `apiKey` value from the configurations object directly. In order to know that the configuration is needed and what type it should be you need to inspect the code. Not ideal.

###### Configuration binding (preferred method)

This mechanism is identical to the configuration bindings used by accessor configurations.

Example:

**gateway.yml**

```yaml
gateways:
  accounts:
    accessor:
      class: com.bank.accessors.AccountAccessor
      connections:
        bank:
          baseUrl: https://dataservice.bank.com
          configurations:
            apiKey: 12345
```

**ConnectionImplementation.class**

```java
import com.google.gson.Gson;
import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.path.api.connect.http.HttpAccessorConnection;

public class ConnectionImplementation extends HttpAccessorConnection {

  public static class ConnectionImplementationConfiguration {
    @ConfigurationField(required = true)
    private String apiKey;
    
    public String getApiKey() {
      return apiKey;
    }
  }
  
  private final ConnectionImplementationConfiguration config;
  
  public ConnectionImplementation(@Configuration ConnectionImplementationConfiguration config) {
    this.config = config;
  }

  public BankAccount getAccount(String accountId, String sessionKey) {
    // Read the apiKey from bound configuration object
    String apiKey = config.getApiKey();

    return request("accountApi")
        .withHeader("apiKey", apiKey)
        .withQueryStringParam("id", accountId)
        .withProcessor((request, response) -> {
          new Gson().fromJson(response.getBody(), BankAccount.class)
        })
        .get()
        .getObject();
  }
}
```

In this example, we read the `apiKey` value from the bound configuration object. The advantage of using this approach is that we are able to define the data that can be provided in the configuration for this connection. We can define the key value types and validation rules. This make discoverability much cleaner.
