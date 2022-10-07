package com.mx.path.example.gateway.accessor

import static org.mockito.Mockito.*

import com.mx.common.accessors.AccessorConfiguration
import com.mx.common.accessors.AccessorConnectionSettings
import com.mx.common.collections.ObjectMap
import com.mx.models.challenges.Challenge
import com.mx.models.origination.Origination
import com.mx.path.example.gateway.configuration.OriginationConfiguration
import com.mx.path.example.gateway.util.ChallengeValidator
import com.mx.path.model.context.Session
import com.mx.path.originationorchestrator.OriginationOrchestrator
import com.mx.testing.TestUtils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

import spock.lang.Specification

class ExampleOriginationAccessorTest extends Specification {
  ExampleOriginationAccessor subject
  OriginationOrchestrator orchestrator
  ChallengeValidator validator

  def setup() {
    orchestrator = mock(OriginationOrchestrator)
    validator = mock(ChallengeValidator)

    def configurationBuilder = AccessorConfiguration.builder()
    configurationBuilder
        .clientId("clentId")
        .configuration("originationChallenges", new ObjectMap().tap {
          put("basicState", new ObjectMap().tap {
            put("title", "HELLO THERE!")
            put("prompt", "Let's start with some basic info")
          })
          put("credentialState", new ObjectMap().tap {
            put("title", "CREDENTIALS")
            put("prompt", "Enter a secure password")
          })
        })
        .connection("mdx", AccessorConnectionSettings.builder().build())
    subject = spy(new ExampleOriginationAccessor(configurationBuilder.build(), new OriginationConfiguration()))
    subject.setValidator(validator)
    subject.setOrchestrator(orchestrator)
    TestUtils.startFakedSession()
  }

  def cleanup() {
    TestUtils.endFakedSession()
  }

  def "start returns the first challenge"() {
    given:
    doReturn(new Origination().tap {
      setChallenges(new ArrayList<Challenge>().tap {
        add(new Challenge().tap {
          setTitle("HELLO THERE!")
          setPrompt("Let's start with some basic info")
        })
      })
    })
    .when(orchestrator)
    .state(any(String), eq("basicState"))

    when:
    def result = subject.start().getResult()

    then:
    result.challenges.size() == 1
    result.challenges.get(0).title == "HELLO THERE!"
    result.challenges.get(0).prompt == "Let's start with some basic info"
  }

  def "answer throws an exception when the validator fails"() {
    given:
    doReturn(new Origination().tap {
      setChallenges(new ArrayList<Challenge>().tap {
        add(new Challenge().tap {
          setId("basicState")
          setTitle("HELLO THERE!")
          setPrompt("Let's start with some basic info")
        })
      })
    })
    .when(orchestrator)
    .state(any(String), eq("basicState"))
    doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "validator failed")).when(validator).validate(any(), any())

    when:
    subject.answerChallenge("id", "basicState", new Challenge())

    then:
    def e = thrown(ResponseStatusException)
    e.status == HttpStatus.BAD_REQUEST
    e.reason == "validator failed"
  }

  def "answer returns the next challenge"() {
    given:
    doReturn(new Origination().tap {
      setChallenges(new ArrayList<Challenge>().tap {
        add(new Challenge().tap {
          setTitle("CREDENTIALS")
          setPrompt("Enter a secure password")
        })
      })
    })
    .when(orchestrator)
    .nextState(any(String), eq("basicState"))

    doReturn(new Origination().tap {
      setChallenges(new ArrayList<Challenge>().tap {
        add(new Challenge().tap {
          setId("basicState")
          setTitle("HELLO THERE!")
          setPrompt("Let's start with some basic info")
        })
      })
    })
    .when(orchestrator)
    .state(any(String), eq("basicState"))

    when:
    def result = subject.answerChallenge(Session.current().getId(), "basicState", new Challenge()).getResult()

    then:
    result.challenges.size() == 1
    result.challenges.get(0).title == "CREDENTIALS"
    result.challenges.get(0).prompt == "Enter a secure password"
  }
}
