package com.mx.path.example.web.controller

import static org.mockito.Mockito.*

import com.mx.accessors.AccessorResponse
import com.mx.models.challenges.Challenge
import com.mx.models.origination.Origination
import com.mx.path.gateway.api.Gateway
import com.mx.path.gateway.api.origination.OriginationGateway
import com.mx.path.model.context.Session
import com.mx.testing.TestUtils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

import spock.lang.Specification

class OriginationsControllerTest extends Specification {
  OriginationsController subject
  Gateway gateway
  OriginationGateway originationGateway

  def setup() {
    originationGateway = spy(OriginationGateway.builder().build())
    gateway = spy(Gateway.builder().originations(originationGateway).build())
    subject = spy(new OriginationsController())
    TestUtils.startFakedSession()
  }

  def cleanup() {
    TestUtils.endFakedSession()
  }

  def "start interacts with gateway"() {
    given:
    def origination = new Origination()
    doReturn(gateway).when(subject).gateway(any(String))

    when:
    doReturn(new AccessorResponse<Origination>().withResult(origination)).when(originationGateway).start()
    def response = subject.start("clientId")

    then:
    verify(originationGateway).start() || true
    HttpStatus.OK == response.getStatusCode()
  }

  def "answerChallenge interacts with gateway"() {
    given:
    Session.current().setId("sessionId")
    doReturn(gateway).when(subject).gateway(any(String))

    when:
    doReturn(new AccessorResponse<Origination>()).when(originationGateway).answerChallenge(any(), any(), any())
    def response = subject.answerChallenge("clientId", "sessionId","challengeId", new Challenge())

    then:
    verify(originationGateway).answerChallenge(any(), any(), any()) || true
    HttpStatus.NO_CONTENT == response.getStatusCode()
  }

  def "answerChallenge throws an exception when sessionId and originationId don't match"() {
    given:
    Session.current().setId("sessionId")
    doReturn(gateway).when(subject).gateway(any(String))

    when:
    doReturn(new AccessorResponse<Origination>()).when(originationGateway).answerChallenge(any(), any(), any())
    def response = subject.answerChallenge("clientId", "session2Id","challengeId", new Challenge())

    then:
    def e = thrown(ResponseStatusException)
    e.status == HttpStatus.BAD_REQUEST
    e.reason == "Invalid session key"
    verify(originationGateway, never()).answerChallenge(any(), any(), any()) || true
  }
}
