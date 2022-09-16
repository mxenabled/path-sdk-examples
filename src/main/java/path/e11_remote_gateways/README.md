## 11 - Remote Messaging & Gateways

Occasionally, the need may arise to communicate with another application that is also running the Path SDK. In order
to facilitate and standardize this communication, several utilities have been provided.

**Important Files**

* [Main.java](./Main.java)
* [ThreadMessageBroker.java](./ThreadMessageBroker.java)
* [requester_gateway.yml](./requester_gateway.yml)
* [responder_gateway.yml](./responder_gateway.yml)

### Calling Remote Gateways

A `RemoteAccessor` class is automatically generated and provided based on the API specified in `BaseAccessor`. This class
handles transforming requests into message payloads and transmitting them over the configured `MessageBroker`.

#### Requesting a list of accounts

```java
// This class is thread-safe and can be reused.
RemoteAccessor remoteAccessor = new RemoteAccessor(); 

// A request is made through the configured `MessageBroker`. A registered responder will handle the request. 
AccessorResponse<MdxList<Account>> response = remoteAccessor.accounts().list();

// Once you receive a response, you can process the accounts in whatever manner you need.
MdxList<Account> accounts = response.getResult(); 
```

#### Creating a recurring transfer

```java
// This class is thread-safe and can be reused.
RemoteAccessor remoteAccessor = new RemoteAccessor(); 

// Sub-accessors are accessed the same way sub-gateways are accessed.
AccessorResponse<RecurringTransfer> response = remoteAccessor.transfers().recurring().create(new RecurringTransfer());
```

### Configuring & Exposing Remote Gateways

Gateways are, by default, **not** exposed to remote requests. In order to configure a gateway to respond to remote requests,
you must explicitly configure _each operation_ you want exposed in the `gateway.yml`.

Consider the following configuration:

```yaml
bigBank:
  gateways:
    accounts:
      remotes:
        list: {}
```

This configuration exposes the `list` operation on the `accounts` gateway for the client `bigBank`. This means that
requests made via `remoteAccessor.accounts().list()` for the client `bigBank` will be responded to via this gateway.

This is all you need to do in order to enable a gateway to respond to remote requests. The service-to-service communication
will happen over the configured `MessageBroker`.


### Security

By default, a remote gateway will first verify that an active session exists and is in the authenticated state. If a session is
not present, or is not in an authenticated state, the request will be rejected and an error will be returned to the caller.

In some cases, you may want to expose a gateway without requiring an authenticated session (i.e. `LocationGateway#search`).
If you wish to disable the authenticated session requirement, you can provide the following syntax _for each operation_.

```yaml
bigBank:
  gateways:
    locations:
      remotes:
        get:
          requireSession: false # Disables the authenticated session requirement for `LocationGateway#get`
        search:
          requireSession: false # Disables the authenticated session requirement for `LocationGateway#search`
```