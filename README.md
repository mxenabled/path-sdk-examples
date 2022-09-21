Issues: https://github.com/mxenabled/path-sdk/issues

## Path Commandline Example

This project is intended to demonstrate the features of the MX Path SDK.

### Examples:

* Configuration
  * [01 - Basic Configuration and Usage](/src/main/java/path/e01_configuration_basic/README.md)
  * [02 - Accessor Configuration](/src/main/java/path/e02_configuration_accessor/README.md)
  * [02a - Accessor Configuration](/src/main/java/path/e02a_configuration_accessor/README.md)
  * [02b - Accessor Configuration](/src/main/java/path/e02b_configuration_accessor/README.md)
  * [03 - Accessor Scope](/src/main/java/path/e03_configuration_accessor_scope/README.md)
  * [04 - Behavior Configuration](/src/main/java/path/e04_configuration_behavior/README.md)
  * [05 - Facility Configuration](/src/main/java/path/e05_configuration_facilities/README.md)
* Session Management
  * [10 - Basic Session Management](/src/main/java/path/e10_session_management/README.md)
* Messaging
  * [11 - Remote Gateways](/src/main/java/path/e11_remote_gateways/README.md) 
* Connections
  * [12 - Connections](/src/main/java/path/e12_connections/README.md)
* Example Applications
  * [99 - CLI App](/src/main/java/path/e99_app)
  * [100 - Spring App](/src/main/java/path/e100_spring_app/README.md)

### Building

```shell
$ ./gradlew package
```

### Running Examples

_All_
```shell
$ bin/all_examples
```

_Individual_
```shell
$ bin/e01_configuration_basic
```

### Additional Example Ideas:

These examples are yet to be created.

* Contexts
  * Session
  * Request
  * Threading
* Connections
  * External System Calls
  * Request / Response tools
* Events
  * Events
  * Request / Responses
* Behaviors
* Facilities
* Services
