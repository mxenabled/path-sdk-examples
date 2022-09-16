## 03 - Accessor Scope Configuration

Once a gateway has been constructed, it is intended to be used to respond to API calls for the life of the process. It is thread safe and does not store state between calls. Accessor scope indicates the instance retention policy for an accessor. 

**Important Files:**

* [Main.java](./Main.java)
* [gateway.yml](./gateway.yml)
  
### Scopes

* `singleton` (preferred) one instance handles all gateway requests
* `request` (not supported yet) a single instance will be given everytime a gateway getter is called on the same thread and within the scope of a request
* `prototype` a new instance will be given everytime a gateway getter is called

#### singleton

An accessor that is built to be thread-safe and does not store request-level state is a candidate to be used as a singleton. The benefit of using the singleton scope is that the accessors do not need to be reconstructed with every request.

#### prototype

An accessor that needs to be torn down and rebuilt with every request should be configured with `prototype` scope. The benefit of using the prototype scope is that each call is guaranteed a clean slate (assuming no static classes are causing side effects). It is intended to be used for troubleshooting, backward compatibility, or in special cases.

Example:

```yaml
accessor:
  class: path.e03_configuration_accessor_scope.Accessor
  scope: singleton
```

### MaxScope

The `MaxScope` annotation allows the developer of an accessor to set the maximum scope that the accessor is compatible with. For example, if an accessor is developed with internal request state its scope must be `prototype` so that the scope is clean for every time the accessor is used and it won't be used across threads. The developer can set the MaxScope to `AccessorScope.PROTOTYPE`. If the accessor is ever configured with a scope of singleton, the configuration will fail.

_Example:_

```java
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;

@MaxScope(AccessorScope.SINGLETON)
public class AccountAccessor extends AccountBaseAccessor {
  public AccountAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
```

Scope precedence:

* Singleton - Highest
* Request - Medium
* Prototype - Lowest

