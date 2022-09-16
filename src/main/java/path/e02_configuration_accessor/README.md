## 02 - Accessor Configuration

This example shows:

* How accessor classes can be configured at each level of the gateway
* How arbitrary configuration can be provided to each accessor
* Accessor selection precedence
* The power and flexibility offered by the Path SDK

Important files:

* [Main.java](./Main.java)
* [gateway.yml](./gateway.yml)
* [IdAccessor.java](./IdAccessor.java)

### Notes

#### Accessor configuration

Each gateway can be provided an accessor from the provided base accessor, or the accessor can be provided in the configuration. Each accessor can be provided a configuration block.

Example:

```yaml
gateways:
  id:
    accessor:
      class: path.e02_configuration_accessor.IdAccessor
      scope: singleton
      configurations:
        key1: value1

  accounts:
    accessor:
      class: path.e02_configuration_accessor.AccountAccessor
      scope: singleton
      configurations:
        key2: value2
```

##### The class convention

You will see the `class` convention used with other components of the Path SDK. 

Notice that it must be a fully-qualified class name. 

##### The configurations convention

You will see the `configurations` block convention used with other components in the Path SDK.

Given the following accessor configuration with `configurations` block:

```yaml
accessor:
  class: path.e02_configuration_accessor.IdAccessor
  scope: singleton
    configurations:
      key1: value1
```

The configuration values can be retrieved within the IdAccess class:

```java
String configurationValue = getConfiguration().getConfigurations().getAsString("key1");
```

#### Accessor Selection

Each time a gateway is called up (e.g. `gateway.accounts()`, `gateway.accounts().transactions()`, `gateway.id()`, etc.) the gateway needs to make a decision about what accessor is going to handle the request. The precedence is as follows:

* Gateway-specific accessor block
* ChildAccessor annotation class
* Accessor provided by root accessor

Consider the following gateway configuration:

```yaml
demo:
  accessor:
    class: Accessor
    scope: singleton
    
  gateways:
    id: {} # <= The Id Accessor is provided by the Accessor.id() implementation 

    accounts:
      accessor:
        class: AccountAccessor # <= Overrides the default provided by Accessor
        scope: singleton
```

`Accessor` provides an implementation for `id()` and `accounts()`. With this configuration, the accessor provided by `id()` will be used, but AccountAccessor will be returned when accessing `gateway.accounts()`, regardless of what the `Accessor.accounts()` provides. 

NOTE: The `id: {}` still needs to be specified in gateways in order for that gateway to be active. It is possible that a root accessor provides child gateways that you don't want to be exposed.

NOTE: __A root accessor is currently required.__ This is true even if all child accessors are overridden.

#### The Flexibility of Path?

Many institutions use a collection of fintech providers to allow their members to interact with their finances.

Consider the following credit union provider list:

* Identity management - Okta
* Core (Accounts, transactions, internal transfers) - Silver jXchange
* Bill Pay - Payveris
* Remote Deposit Capture - Ensenta

The following accessors could be created for each provider:

* Okta
  * `public class OktaIdAccessor extends IdBaseAccessor {...}`
* Silverlake jXchange
  * `public class JXchangeAccountsAccessor extends AccountsBaseAccessor {...}`
  * `public class JXchangeTransactionsAccessor extends TransactionsBaseAccessor {...}`
  * `public class JXchangeTransfersAccessor extends TransfersBaseAccessor {...}`
* Payveris
  * `public class PayverisPaymentsAccessor extends PaymentsBaseAccessor {...}`
* Ensenta
  * `public class EnsentaRemoteDepositAccessor extends RemoteDepositsBaseAccessor {...}`
  
Then we can build _one_ unified interface to ALL of these features:

```yaml
gateways:
  id:
    accessor:
      class: OktaIdAccessor
  accounts:
    accessor:
      class: JXchangeAccountsAccessor
    gateways:
      transactions:
        accessor:
          class: JXchangeTransactionsAccessor
  transfers:
    accessor:
      class: JXchangeTransfersAccessor
  payments:
    accessor:
      class: PayverisPaymentsAccessor
  remote_deposits:
    accessor:
      class: EnsentaRemoteDepositAccessor    
```

Example use:

```java
// List accounts
gateway.accounts().list();

// Transfer money
Transfer transfer = new Transfer();
  // fill in transfer details...
gateway.transfers().create(transfer);

// Pay a bill
Payment payment = new Payment();
  // fill in the payment details...
gateway.payments().create(payment);

// Deposit a check
RemoteDeposit remoteDeposit = new RemoteDeposit();
  // fill in remote deposit details...
gateway.remoteDeposits().create(remoteDeposit);
```

Now say the institution changes their bill pay service to CheckFree

Implement Accessor:

* CheckFree
  * `public class CheckFreePaymentsAccessor extends PaymentsBaseAccessor {...}`

Change configuration:

```yaml
gateways:
  id:
    accessor:
      class: OktaIdAccessor
  accounts:
    accessor:
      class: JXchangeAccountsAccessor
    gateways:
      transactions:
        accessor:
          class: JXchangeTransactionsAccessor
  transfers:
    accessor:
      class: JXchangeTransfersAccessor
  payments:
    accessor:
      class: CheckFreePaymentsAccessor # <= Change here
  remote_deposits:
    accessor:
      class: EnsentaRemoteDepositAccessor    
```

All code using the `gateway.payments()` gateway still functions as before.

```java
mic.drop();
```
