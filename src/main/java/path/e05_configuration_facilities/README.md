## 05 - Facility Configuration

In order for integrations to operate within a deployment, access to infrastructure is necessary. These infrastructure systems include things like storage, message broker, log collection, events, sensitive data encryption, and exception reporting. In order to keep these concerns separate from accessor code, the Path SDK has built an abstraction layer for all external infrastructure concerns. These are referred to as Facilities.

Supported Facilities:

* Encryption
* Caching and Session storage
* Events (local process)
* Messaging (pub/sub)

Future Facilities:

* Exception Reporting

**Important Files:**

* [Main.java](./Main.java)
* [MemoryStore.java](./MemoryStore.java)
* [gateway.yml](./gateway.yml)

### Direct use

Facilities are set up at configuration time and are globally accessible. Facilities can be accessed via the `Facilities` class.

Example:

```java
Facilities.getSessionStore("client1");
```

### Stores

A store is a simple key value pair store. It is intended to be backed by a semi-volatile key-value db like memcached or redis. Unlike other facility types, the `Store` facility type is used for 2 facilities -- SessionStore and CacheStore. These are used the same way but are separated by the lifetime expectation of the data stored in them. 

`CacheStore` data can disappear at any time and should be re-constructable.

`SessionStore` should only be used to store session data with a short expiry. Assuming the session is in a good state, code that needs this data can have a reasonable expectation that it will be there when it is needed. `Session` uses the `SessionStore` internally to keep states between requests. 

Many pre-built behaviors use SessionStore and CacheStore to perform their function. Accessors can use Stores directly, if needed, although it is not recommended.

#### Store Scopes

Session scopes allow key-value data to be written directly to the store while maintaining appropriate key separation.

Example:

```java
    Store sessionStore = Facilities.getSessionStore("client-1");
    Store clientStore = ScopedStore.build(sessionStore, "client");
    clientStore.put("key1", "someValue", 600);
    
    String value = clientStore.get("key1");
```

This example wraps the SessionStore in a client-scoped Store. The value `someValue` will be written for `client-1` and will only retrievable by a client-scoped store configured for the same client.

This is useful if an accessor needs to store a value for a client outside the current session.

There are also `Session`, `CurrentSession`, and `Global` scoped-store wrappers. 

### Encryption Service

The encryption facility provides encrypt/decrypt operations to secure sensitive data before putting it in an insecure store or transmitting it.

`Session` uses the encryption service facility to encrypt the value when using `sPut()`

Example:

```java
    Session.current().sput("key", "value");
    Session.current().get("key"); // will decrypt an encrypted value before returning
````

### Message Broker

The message broker facility is used to perform publish/subscribe style communications. There will be a more detailed example showing how to use the messaging features in Path.

### Event Bus

The event bus is unique in that the Path system provides it. We use the [EventBus](https://github.com/google/guava/tree/master/guava/src/com/google/common/eventbus) framework provided by [Guava](https://github.com/google/guava). The event bus provides a simple publish/subscribe system to be used withing the current process. This allows for the registration of methods that will be invoked when certain events occur. The Path system published many events. Behaviors and Accessors can publish their own events as well. There will be a more detailed example showing how to use the EventBus.
