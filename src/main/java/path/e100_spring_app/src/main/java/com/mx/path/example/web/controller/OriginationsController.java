package com.mx.path.example.web.controller;

import com.mx.models.challenges.Challenge;
import com.mx.models.origination.Origination;
import com.mx.path.model.context.Session;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "{clientId}")
public final class OriginationsController extends GatewayController {
  @RequestMapping(value = "/originations/start", method = RequestMethod.POST)
  public ResponseEntity<Origination> start(@PathVariable("clientId") String clientId) throws Exception {
    Session.deleteCurrent();
    Session.createSession();

    Origination origination = gateway(clientId)
        .originations()
        .start()
        .getResult();
    origination.setId(Session.current().getId());
    return new ResponseEntity<>(origination.wrapped(), headers(), HttpStatus.OK);
  }

  @RequestMapping(value = "/originations/{id}/challenges/{challengeId}", method = RequestMethod.PUT)
  public ResponseEntity<Origination> answerChallenge(
      @PathVariable("clientId") String clientId,
      @PathVariable("id") String id,
      @PathVariable("challengeId") String challengeId,
      @RequestBody Challenge challenge) {
    if (Session.current() == null || !Session.current().getId().equals(id)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid session key");
    }
    Origination origination = gateway(clientId)
        .originations()
        .answerChallenge(id, challengeId, challenge)
        .getResult();
    if (origination == null) {
      // The origination flow has finished so we can send a 204 back.
      return new ResponseEntity<>(headers(), HttpStatus.NO_CONTENT);
    }
    origination.setId(Session.current().getId());
    return new ResponseEntity<>(origination.wrapped(), headers(), HttpStatus.OK);
  }

  private HttpHeaders headers() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("mx-session-key", Session.current().getId());
    return headers;
  }
}
