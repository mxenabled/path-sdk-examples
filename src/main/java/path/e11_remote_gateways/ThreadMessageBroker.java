package path.e11_remote_gateways;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import lombok.NonNull;

import com.mx.common.collections.ObjectMap;
import com.mx.common.messaging.EventListener;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageResponder;
import com.mx.common.messaging.MessageStatus;
import com.mx.path.model.context.RequestContext;

import path.lib.Logger;

/**
 * This is an **example** facility that allows disparate threads to register a MessageResponder or EventListener
 * for a given channel.
 *
 * As a note, the responder will execute on the same thread as the requester, so this is not a
 * solution for communicating from thread to thread. It simply allows multiple threads to register responders that can
 * be used by another thread.
 */
public final class ThreadMessageBroker implements MessageBroker {
  private static final Map<String, List<MessageResponder>> RESPONDER_MAP = new ConcurrentHashMap<>();
  private static final Map<String, List<EventListener>> LISTENER_MAP = new ConcurrentHashMap<>();
  private static final Object RESPONDER_MUTEX = new Object();
  private static final Object LISTENER_MUTEX = new Object();

  @Getter
  private final ObjectMap configurations;

  public ThreadMessageBroker(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public String request(String channel, String payload) {
    if (!RESPONDER_MAP.containsKey(channel) || RESPONDER_MAP.get(channel).size() == 0) {
      throw new MessageError("No responders registered for " + channel, MessageStatus.NO_RESPONDER, null);
    }
    String response;
    RequestContext requestContext = RequestContext.current();
    try {
      Logger.log("ThreadMessageBroker requesting response on channel " + channel);
      MessageResponder responder = Objects.requireNonNull(randomItem(RESPONDER_MAP.get(channel)));
      response = responder.respond(channel, payload);
      Logger.log("ThreadMessageBroker received a response on channel " + channel);
    } finally {
      requestContext.register();
    }
    return response;
  }

  @Override
  public void publish(String channel, String payload) {
    if (!LISTENER_MAP.containsKey(channel)) {
      throw new MessageError("No listeners registered for " + channel, MessageStatus.NO_RESPONDER, null);
    }
    for (EventListener listener : LISTENER_MAP.get(channel)) {
      listener.receive(channel, payload);
    }
  }

  @Override
  public void registerResponder(String channel, @NonNull MessageResponder responder) {
    Logger.log("ThreadMessageBroker registering responder on " + channel);
    synchronized (RESPONDER_MUTEX) {
      if (!RESPONDER_MAP.containsKey(channel)) {
        RESPONDER_MAP.put(channel, new ArrayList<>());
      }
      RESPONDER_MAP.get(channel).add(responder);
    }
  }

  @Override
  public void registerListener(String channel, @NonNull EventListener listener) {
    synchronized (LISTENER_MUTEX) {
      if (!LISTENER_MAP.containsKey(channel)) {
        LISTENER_MAP.put(channel, new ArrayList<>());
      }
      LISTENER_MAP.get(channel).add(listener);
    }
  }

  private <T> T randomItem(List<T> list) {
    if (list.size() == 0) {
      return null;
    }
    int index = ThreadLocalRandom.current().nextInt(list.size());
    return list.get(index);
  }
}
