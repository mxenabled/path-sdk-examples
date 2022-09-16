package path.example.accessors

import com.mx.accessors.AccessorConfiguration

import path.e01_configuration_basic.Accessor
import path.e01_configuration_basic.AccountAccessor
import spock.lang.Specification

class AccountAccessorTest extends Specification {

  def configuration
  Accessor subject

  def setup() {
    configuration = AccessorConfiguration.builder().build()
    subject = new Accessor(configuration)
    subject.setAccounts(new AccountAccessor(configuration))
  }

  def "list"() {
    when:
    def list = subject.accounts().list()

    then:
    list.getResult().size() > 0
  }

  def "get"() {
    when:
    def accountId = subject.accounts().list().getResult().get(0).getId()
    def account = subject.accounts().get(accountId)

    then:
    account.getResult().id == accountId
  }
}
