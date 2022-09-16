## 02 - Accessor Configuration

This example shows:


Important files:

* [Main.java](./Main.java)
* [gateway.yml](./gateway.yml)
* [IdAccessor.java](./Accessor.java)
* [IdAccessor.java](./IdAccessor.java)
* [IdAccessor.java](./AccessorConfigs.java)

### Notes

#### Accessor configuration

In the previous example, we demonstrated the use of the accessor configurations block. The values in the configurations block can be directly accessed via the AccessorConfiguration object provided to the accessor's constructor. There is an alternative, preferred way to pass and utilize the values in the configurations block.

The `@Configuration` annotation can mark a constructor parameter whose type is POJO that matches the configurations block names. This offers several advantages over the previous method. -- First, it allows us to code to static property names, letting us lean on the compiler to make field name changes. Second, it allows us to perform configuration-time validations on the provided configurations. Third, it allows us to expose a dynamic description of the accessor's configuration options. 

Example:

_Accessor with configuration block_

```yaml
gateways:
  id:
    accessor:
      class: path.path.e02a_configuration_accessor.IdAccessor
      scope: singleton
      configurations:
        key1: value1
```

_Configuration POJO_

```java
import com.mx.path.gateway.configuration.annotations.ConfigurationField;
import lombok.Data;

@Data
public class IdAccessorConfiguration {
  
  @ConfigurationField(value = "key1", required = true)
  private String key1;
  
}
```

_Accessor Definition_

```java
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.id.IdBaseAccessor;
import com.mx.path.gateway.configuration.annotations.Configuration;

public class IdAccessor extends IdBaseAccessor {
  private IdAccessorConfiguration idAccessorConfiguration;
  
  public IdAccessor(AccessorConfiguration configuration, @Configuration IdAccessorConfiguration idAccessorConfiguration) {
    super(configuration);
    this.idAccessorConfiguration = idAccessorConfiguration;
  }
}
```

The configuration values can be retrieved within the IdAccess class:

```java
String configurationValue = idAccessorConfiguration.getKey1();
```

_Notes:_

The fields inside the configuration POJO class must be annotated with `@ConfigurationField` otherwise they will be ignored and not populated. The fields can be values (String, int, etc.), configuration objects, arrays, or arrays of configuration objects. All of the same rules apply to sub-configuration objects.

### Accessor Connections

An Accessor Connection is a Java representation of an external API. These connections are represented in the configuration.

```yaml
accessor:
  class: path.e02b_configuration_accessor.Accessor
  configurations:
    clientSecret: $ecr3t
    logCalls: true
  connections:
    superAuth:
      baseUrl: http://api.bank1.com/v1
      
      configurations:
        clientIdentifier: client1
```

There can be more than one connection.

```yaml
accessor:
  class: path.e02b_configuration_accessor.Accessor
  configurations:
    clientSecret: $ecr3t
    logCalls: true
  connections:
    superAuth:
      baseUrl: http://api.bank1.com/v1
      configurations:
        clientIdentifier: client1
    rsa:
      baseUrl: http://adaptiveauthentication.rsa.com/
      configurations:
        clientId: client1
```

_Note the presence of the configurations block for each connection._

#### Using Connections

The connection configurations can be acessed via the AccessorConfiguration like this:

```java
public class IdAccessor extends IdBaseAccessor {

  public IdAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<Authentication> authenticate(Authentication authentication) {
    AccessorConnection connection = getConfiguration().getConnections().getConnection("superAuth");
    String baseUrl = connection.getBaseUrl();
    String clientIdentifier = connection.getConfigurations().getAsString("clientIdentifier");
    
    // do something
    
    return AccessorResponse.<Authentication>builder().result(session).status(AccessorResponseStatus.OK).build();
  }
}
```

As seen above with AccessorConfigurations, there is a better way to bind the configuration to the code.

First, the Connections should inherit from AccessorConnection. Then, the `@Connection` annotation can mark an accessor constructor parameter as a connection. 

Example:

```java
import com.mx.accessors.AccessorConnection;
import com.mx.models.id.Authentication;
import com.mx.path.gateway.configuration.annotations.Configuration;
import com.mx.path.gateway.configuration.annotations.ConfigurationField;
import lombok.Data;

@Data
public class SuperAuthConnectionConfiguration {
  @ConfigurationField("clientIdentifier")
  private String clientIdentifier;
}

public class SuperAuthConnection extends AccessorConnection {
  private SuperAuthConnectionConfiguration superAuthConnectionConfiguration;

  public SuperAuthConnection(@Configuration SuperAuthConnectionConfiguration superAuthConnectionConfiguration) {
    this.superAuthConnectionConfiguration = superAuthConnectionConfiguration;
  }

  public AuthResult authenticate(...) {
    String clientIdentifier = superAuthConnectionConfiguration.getClientIdentifier();
    String baseUrl = getBaseUrl();

    // do request

    return authResult;
  }
}

public class IdAccessor extends IdBaseAccessor {

  private SuperAuthConnection superAuthConnection;

  public IdAccessor(AccessorConfiguration configuration, @Connection("superAuth") SuperAuthConnection superAuthConnection) {
    super(configuration);
    this.superAuthConnection = superAuthConnection;
  }

  @Override
  public final AccessorResponse<Authentication> authenticate(Authentication authentication) {
    // Allow the connection class do the connection and give us the results
    AuthResult result = superAuthConnection.authenticate(authentication.getLogin(), new String(authentication.getPassword()));
    
    // Accessor interprets the results and translates results into MDX model(s)
    Authentication session = new Authentication();
    session.setUserId(result.getUserIdentifier);

    // Build and return result
    return AccessorResponse.<Authentication>builder().result(session).status(AccessorResponseStatus.OK).build();
  }
}
```

_Note that the AccessorConnection constructor accepts an annotated configuration POJO. This is a pattern we will see again._

#### Style and architecture

It is best practice to encapsulate all external connection activity in one or more AccessorConnection class(es). The API for connections class should closely match the API being integrated. By keeping endpoint names, input and output closely aligned makes the code easy to understand.

The Accessor's responsibilities should be to perform the _interpretation_ and _translation_ only.
