package com.mx.path.example.gateway.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.mx.path.core.common.lang.Strings;
import com.mx.path.model.mdx.model.challenges.Challenge;
import com.mx.path.model.mdx.model.challenges.Question;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * A trivial challenge validator. Takes in a challenge schema and a "filled out" challenge and ensures all fields have
 * an answer. It does not validate answer types (i.e. EMAIL, PHONE, etc.)
 */
public final class ChallengeValidator {
  public void validate(Challenge schema, Challenge challenge) {
    if (schema == null) {
      return;
    }

    HashMap<String, Question> questionIdToQuestion = new HashMap<>();
    challenge.getQuestions().forEach(question -> questionIdToQuestion.put(question.getId(), question));

    List<Question> invalidQuestions = new ArrayList<>();
    for (Question questionSchema : schema.getQuestions()) {
      if (!questionIdToQuestion.containsKey(questionSchema.getId())) {
        invalidQuestions.add(questionSchema);
        continue;
      }

      Question question = questionIdToQuestion.get(questionSchema.getId());
      if (Strings.isBlank(question.getAnswer())) {
        invalidQuestions.add(questionSchema);
      }
    }

    if (invalidQuestions.size() > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, buildErrorMessage(invalidQuestions));
    }
  }

  private String buildErrorMessage(List<Question> invalidQuestions) {
    List<String> prompts = invalidQuestions
        .stream()
        .map(Question::getPrompt)
        .map(prompt -> "'" + prompt + "'")
        .collect(Collectors.toList());

    if (prompts.size() == 1) {
      return prompts.get(0) + " is required.";
    }

    String joined = String.join(", ", prompts);
    return joined + " are required.";
  }
}
