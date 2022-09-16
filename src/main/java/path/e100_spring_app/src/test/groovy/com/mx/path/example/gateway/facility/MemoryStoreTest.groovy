package com.mx.path.example.gateway.facility

import spock.lang.Specification

class MemoryStoreTest extends Specification {
  def "puts and gets a value"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.put("key", "value", 10)
    def value = subject.get("key")

    then:
    value == "value"
    !subject.getStoreRecord("key").getValueRecord().isExpired()
  }

  def "puts and deletes a value"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.put("key", "value", 10)
    subject.delete("key")
    def value = subject.get("key")

    then:
    value == null
  }

  def "puts and expires a value"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.put("key", "value", -10)
    def value = subject.get("key")

    then:
    value == null
  }

  def "puts and gets a set"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.putSet("key", "value", 10)
    def value = subject.getSet("key")

    then:
    value.toList() == ["value"]
  }

  def "puts and checks set has key"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.putSet("key", "value", 10)
    def exists = subject.inSet("key", "value")
    def doesnt = subject.inSet("key", "yolo")

    then:
    exists
    !doesnt
  }

  def "puts and deletes from set"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.putSet("key", "value", 10)
    subject.deleteSet("key", "value")
    def exists = subject.inSet("key", "value")

    then:
    !exists
  }

  def "puts a value if it doesn't exist"() {
    given:
    def subject = new MemoryStore()

    when:
    subject.putIfNotExist("key", "value", 10)
    subject.putIfNotExist("key", "value2", 10)
    def value = subject.get("key")

    then:
    value == "value"
  }
}
