package com.mx.path.example.gateway.accessor;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AccessLevel;
import lombok.Setter;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.context.Session;
import com.mx.path.example.gateway.configuration.OriginationConfiguration;
import com.mx.path.example.gateway.util.ChallengeValidator;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.origination.OriginationBaseAccessor;
import com.mx.path.model.mdx.model.challenges.Challenge;
import com.mx.path.model.mdx.model.origination.Origination;
import com.mx.path.originationorchestrator.OriginationOrchestrator;

public final class ExampleOriginationAccessor extends OriginationBaseAccessor {
  private final OriginationConfiguration configuration;
  @Setter(AccessLevel.PACKAGE)
  private OriginationOrchestrator orchestrator;
  @Setter(AccessLevel.PACKAGE)
  private ChallengeValidator validator;

  public ExampleOriginationAccessor(AccessorConfiguration configuration, @Configuration OriginationConfiguration originationConfiguration) {
    super(configuration);
    this.configuration = originationConfiguration;
    orchestrator = new OriginationOrchestrator(configuration);
    validator = new ChallengeValidator();
  }

  @Override
  public AccessorResponse<Origination> start() {
    simulateIO();

    String firstState = getChallengesIterator()
        .next()
        .getKey();

    return new AccessorResponse<Origination>().withResult(orchestrator.state(Session.current().getId(), firstState));
  }

  @Override
  public AccessorResponse<Origination> answerChallenge(String id, String challengeId, Challenge challenge) {
    simulateIO();

    // First, we make sure that the provided challenge is valid for the given challengeId.
    validator.validate(getChallengeSchemaForId(challengeId), challenge);
    Origination nextState = orchestrator.nextState(id, challengeId);
    if (nextState == null) {
      return new AccessorResponse<Origination>().withStatus(PathResponseStatus.NO_CONTENT);
    }
    return new AccessorResponse<Origination>().withResult(nextState);
  }

  /**
   * Returns an iterator over the origination challenges. Can be used to traverse the configuration or determine
   * which origination state should come next.
   * @return
   */
  private Iterator<Map.Entry<String, Object>> getChallengesIterator() {
    return getConfiguration()
        .getConfigurations()
        .getMap("originationChallenges")
        .entrySet()
        .iterator();
  }

  /**
   * Returns a Challenge constructed from the associated configuration.
   * @param challengeId
   * @return
   */
  private Challenge getChallengeSchemaForId(String challengeId) {
    return orchestrator.state(Session.current().getId(), challengeId)
        .getChallenges()
        .stream()
        .filter(challenge -> challenge.getId().equals(challengeId))
        .findFirst()
        .orElse(null);
  }

  /**
   * To make the accessor more realistic, we can simulate waiting on IO by sleeping for a given amount of milliseconds
   * bounded by a range defined in the gateway.yaml.
   */
  private void simulateIO() {
    try {
      int delayMillis = ThreadLocalRandom
          .current()
          .nextInt(configuration.getMinRequestDelayMillis(), configuration.getMaxRequestDelayMillis() + 1);
      Thread.sleep(delayMillis);
    } catch (Exception ignored) {
    }
  }
}
