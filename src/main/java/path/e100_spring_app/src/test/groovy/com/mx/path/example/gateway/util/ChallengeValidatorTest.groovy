package com.mx.path.example.gateway.util

import com.mx.models.challenges.Challenge
import com.mx.models.challenges.Question

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

import spock.lang.Specification

class ChallengeValidatorTest extends Specification {
  ChallengeValidator subject

  def setup() {
    subject = new ChallengeValidator()
  }

  def "does nothing when a challenge matches the schema"() {
    given:
    def schema = new Challenge().tap {
      setQuestions(new ArrayList<Question>().tap {
        add(new Question().tap {
          setId("name")
          setPrompt("What is your name?")
        })
      })
    }
    def response = new Challenge().tap {
      setQuestions(new ArrayList<Question>().tap {
        add(new Question().tap {
          setId("name")
          setPrompt("What is your name?")
          setAnswer("Frodo Baggins")
        })
      })
    }

    when:
    subject.validate(schema, response)

    then:
    noExceptionThrown()

    when: "the schema is null"
    subject.validate(null, response)

    then:
    noExceptionThrown()
  }

  def "throws an exception when the response doesn't match the schema"() {
    given:
    def schema = new Challenge().tap {
      setQuestions(new ArrayList<Question>().tap {
        add(new Question().tap {
          setId("name")
          setPrompt("What is your name?")
        })
        add(new Question().tap {
          setId("occupation")
          setPrompt("What is your occupation?")
        })
        add(new Question().tap {
          setId("address")
          setPrompt("What is your address?")
        })
        add(new Question().tap {
          setId("address")
          setPrompt("What is your city?")
        })
      })
    }
    def response = new Challenge().tap {
      setQuestions(new ArrayList<Question>().tap {
        add(new Question().tap {
          setId("name")
          setPrompt("What is your name?")
          setAnswer("Frodo Baggins")
        })
      })
    }

    when:
    subject.validate(schema, response)

    then:
    def e = thrown(ResponseStatusException)
    e.status == HttpStatus.BAD_REQUEST
    e.reason == "'What is your occupation?', 'What is your address?', 'What is your city?' are required."
  }
}
