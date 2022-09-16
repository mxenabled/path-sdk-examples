## 01 - Basic Configuration and Usage

This examples shows how a minimal gateway is configured using the configuration system and some basic usage.

**Important Files:**

* [Main.java](./Main.java)
* [gateway.yml](./gateway.yml)

### Notes

Most accessors have some additional dependencies that need to be configured to function. This example is provided to show a very simple configuration. We will build on this in the following examples.

#### Accessors

Accessors provide translation between the MDX models and operations and external systems. _These are the primary functional input to the MX Path system. All other inputs provide support for these._ Each accessor extends a "Base" accessor. The Base Accessors define and organize the MDX models and their operations. The accessors are nested to represent the domain and features each model belongs to.

Consider the following accessor configuration block:

```yaml
accessor:
  class: path.e01_configuration_basic.Accessor
  scope: singleton
```

#### Gateways

A gateway is a "gateway" to an accessor. The nested structure mirrors that of the accessors.

##### Enabling gateways

Consider the following gateway configuration block:

```yaml
gateways:
  id: {}
  accounts:
    gateways:
      transactions: {}
```

This enables the `id` `accounts`, and `accounts.transactions` gateways.

Example usage:

```java
gateway.id().authenticate(authentication);
gateway.accounts().list();
gateway.accounts().transactions().list("account1");
```

Any un-configured gateway or un-implemented operation will result in an AccessorException (status: NOT_IMPLEMENTED) exception.

#### Why the Gateway?

As stated above, the gateway mirrors the accessors. So, why does it exist? 

> "Why can't I just use the accessors directly?"

Subsequent examples will answer this question more fully. Here's a brief explanation -- Accessors are entirely focussed on model and operation translation. They normalize disparate systems to the MX MDX specifications. The gateway provides ancillary support to the operations. Using the gateway features, functionality can be added to a setup without need to modify the accessor code.

The following is a list of functionality that can be added. Some of these are provided by the Path SDK. Others can be added by implementing and configuring simple interfaces.

* Data storage
* Encryption
* Logging
* Access control
* Caching
* Async event reactions
* Async and synch messaging    
