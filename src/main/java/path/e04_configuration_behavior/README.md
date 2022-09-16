## 04 - Behavior Configuration

Behaviors can wrap any or all API calls into a gateway. They can be used to provide cross-cutting behaviors (i.e. caching, logging, access control, etc). They can also be used to decorate accessor functionality.

**Important Files:**

* [Main.java](./Main.java)
* [SimpleBehavior.java](./SimpleBehavior.java)
* [gateway.yml](./gateway.yml)

### Behavior Execution

Behaviors can be chained together and execute in order. The code in a behavior decides when the next behavior is to be called. 

### Behavior configuration

_Example:_

```yaml
behavior:
  class: path.e04_configuration_behaviors.LoggingBehavior
  configurations:
    key1: value1
```

This looks similar to the accessor configuration. There is a class and optional configurations block. Like accessors, the behavior class constructor can annotate a parameter with @Configuration to have a POJO bound to the values in the configurations block.

_Example:_

```yaml
behavior:
  class: LoggingBehavior
  configurations:
    logHeader: LOG
```

```java
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.configuration.annotations.Configuration;
import com.mx.path.gateway.configuration.annotations.ConfigurationField;
import lombok.Getter;

public class LoggingBehavior extends GatewayBehavior {
  public static class LoggingBehaviorConfiguration {
    @Getter
    @ConfigurationField("logHeader")
    private String logHeader;
  }
  
  private LoggingBehaviorConfiguration loggingBehaviorConfiguration;

  public LoggingBehavior(ObjectMap configurations, @Configuration LoggingBehaviorConfiguration loggingBehaviorConfiguration) {
    super(configurations);
    this.loggingBehaviorConfiguration = loggingBehaviorConfiguration;
  }
}
```

In this example, a populated instance of LoggingBehaviorConfiguration will be passed to the constructor. The @ClientID annotation is also supported. 

### Root Behaviors

A client can have a set of root behaviors that are 

_Example:_

```yaml
accessor:
  class: path.e03_configuration_accessor_scope.Accessor
  scope: singleton
```
